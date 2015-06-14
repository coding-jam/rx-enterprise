package it.cosenonjaviste.socket;

import it.cosenonjaviste.dtos.Message;
import it.cosenonjaviste.service.EchoMessageService;
import it.cosenonjaviste.service.LongTimeProcessingService;
import it.cosenonjaviste.socket.modelcodec.MessageDecoder;
import it.cosenonjaviste.socket.modelcodec.MessageEncoder;
import rx.functions.Action1;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by pizzo on 26/05/15.
 *
 * Aggiunta una dipendenza nel pom del progetto web (jboss-websocket-api_1.0_spec)
 */
@ServerEndpoint(value = "/socket/rx-echo-message", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class RxEchoMessageWebSocket {

    private static final Logger LOGGER = Logger.getLogger(RxEchoMessageWebSocket.class.getName());

    @Inject
    private EchoMessageService service;

    @Inject
    private ObservableExecutorAdapter executorAdapter;

    @OnOpen
    public void onOpen(Session session) {
        LOGGER.info(logWithDetails("Session " + session.getId() + " connected"));
    }

    @OnMessage
    public Message onMessage(Message msg, Session session) throws InterruptedException {
        msg.setSessionId(session.getId());

        executorAdapter.executeAsync(() -> {
            try {
                return service.echoSync(msg);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }).subscribe(notifyEcho(session), notifyError(session));

        String response = "Message '" + msg.getMessage() + "' received";
        LOGGER.info(logWithDetails(response));
        return new Message(response, session.getId());
    }

    private Action1<Throwable> notifyError(Session session) {
        return error -> {
            try {
                onError(error, session);
            } catch (EncodeException| IOException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                throw new RuntimeException(e);
            }
        };
    }

    private Action1<Message> notifyEcho(Session session) {
        return echo -> {
            if (session.isOpen()) {
                LOGGER.info(logWithDetails("Sending response to session " + session.getId()));
                try {
                    session.getBasicRemote().sendObject(echo);
                } catch (IOException | EncodeException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            } else {
                LOGGER.severe("Session " + session.getId() + " already closed!");
            }
        };
    }

    @OnClose
    public void onClose(CloseReason reason, Session session) {
        LOGGER.info(logWithDetails("Session " + session.getId() + " disconnected"));
        LOGGER.info("Close code: " + reason.getCloseCode() + "; close phrase: " + reason.getReasonPhrase());
    }

    @OnError
    public void onError(Throwable error, Session session) throws IOException, EncodeException {
        LOGGER.log(Level.SEVERE, error.getMessage(), error);
        if (session.isOpen()) {
            session.getBasicRemote().sendObject(new Message(error.getMessage(), session.getId(), Message.Status.ERROR));
        }
    }

    private String logWithDetails(String message) {
        return message + " on thread " + Thread.currentThread().getId() + " and bean hash " + this.hashCode();
    }
}
