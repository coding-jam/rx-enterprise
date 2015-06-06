package it.cosenonjaviste.socket.modelcodec;

import it.cosenonjaviste.dtos.Message;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Created by pizzo on 02/06/15.
 *
 * Aggiunta dipendenza JSR-353
 */
public class MessageEncoder implements Encoder.Text<Message> {

    @Override
    public String encode(Message message) throws EncodeException {
        return Json.createObjectBuilder()
                .add("message", message.getMessage())
                .add("sessionId", message.getSessionId())
                .build().toString();
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
