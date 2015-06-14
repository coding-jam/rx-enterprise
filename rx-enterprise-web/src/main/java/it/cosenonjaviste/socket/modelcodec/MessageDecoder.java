package it.cosenonjaviste.socket.modelcodec;

import it.cosenonjaviste.dtos.Message;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by pizzo on 02/06/15.
 */
public class MessageDecoder implements Decoder.Text<Message> {

    private static final Logger LOGGER = Logger.getLogger(MessageDecoder.class.getName());

    @Override
    public Message decode(String s) throws DecodeException {

        return tryDecode(s, jsonObject -> {
            Message message = new Message();
            message.setMessage(jsonObject.getString("message"));
            return message;
        });

    }

    @Override
    public boolean willDecode(String s) {
//        return false;
        try {
            return tryDecode(s, jsonObject -> {
                return true;
            });
        } catch (DecodeException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    private <T> T tryDecode(String input, Function<JsonObject, T> decode) throws DecodeException {

        try(JsonReader reader = Json.createReader(new StringReader(input))) {
            JsonObject jsonObject = reader.readObject();
            return decode.apply(jsonObject);
        }
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
