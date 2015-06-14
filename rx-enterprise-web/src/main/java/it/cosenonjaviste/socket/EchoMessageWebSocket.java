package it.cosenonjaviste.socket;

import it.cosenonjaviste.dtos.Message;
import it.cosenonjaviste.service.EchoMessageService;
import it.cosenonjaviste.socket.modelcodec.MessageDecoder;
import it.cosenonjaviste.socket.modelcodec.MessageEncoder;

import javax.enterprise.context.ApplicationScoped;
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
@ServerEndpoint(value = "/socket/echo-message", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class EchoMessageWebSocket {

    private static final Logger LOGGER = Logger.getLogger(EchoMessageWebSocket.class.getName());

    @Inject
    private SessionHolder sessionHolder;

    @Inject
    private EchoMessageService messageService;

    @OnOpen
    public void onOpen(Session session) {
        LOGGER.info(logWithDetails("Session " + session.getId() + " connected"));
        sessionHolder.add(session);
    }

    @OnMessage
    public Message onMessage(Message msg, Session session) throws InterruptedException {
        msg.setSessionId(session.getId());

        messageService.echo(msg);

        String response = "Message '" + msg.getMessage() + "' received";
        LOGGER.info(logWithDetails(response));
        return new Message(response, session.getId());
    }

    @OnClose
    public void onClose(CloseReason reason, Session session) {
        LOGGER.info(logWithDetails("Session " + session.getId() + " disconnected"));
        LOGGER.info("Close code: " + reason.getCloseCode() + "; close phrase: " + reason.getReasonPhrase());
        sessionHolder.remove(session);
    }

    @OnError
    public void onError(Throwable error, Session session) throws IOException, EncodeException {
        LOGGER.log(Level.SEVERE, error.getMessage(), error);
        if (session.isOpen()) {
            session.getBasicRemote().sendObject(new Message(error.getMessage(), session.getId()));
        }
    }

    public void onEchoMessage(@Observes Message message) throws IOException, EncodeException {
        Optional<Session> session = sessionHolder.get(message.getSessionId());
        if (session.isPresent()) {
            LOGGER.info(logWithDetails("Sending response to session " + session.get().getId()));
            session.get().getBasicRemote().sendObject(message);
        } else {
            LOGGER.severe("Session " + message.getSessionId() + " not present or already closed!");
        }
    }

    private String logWithDetails(String message) {
        return message + " on thread " + Thread.currentThread().getId() + " and bean hash " + this.hashCode();
    }
}
