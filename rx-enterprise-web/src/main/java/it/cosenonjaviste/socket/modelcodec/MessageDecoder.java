package it.cosenonjaviste.socket.modelcodec;

import it.cosenonjaviste.dtos.Message;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by pizzo on 02/06/15.
 */
public class MessageDecoder implements Decoder.Text<Message> {

    private static final Logger LOGGER = Logger.getLogger(MessageDecoder.class.getName());

    @Override
    public Message decode(String s) throws DecodeException {
        try(JsonReader reader = Json.createReader(new StringReader(s))) {
            JsonObject jsonObject = reader.readObject();

            Message message = new Message();
            message.setMessage(jsonObject.getString("message"));
            return message;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new DecodeException(s, e.getMessage(), e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        try(JsonReader reader = Json.createReader(new StringReader(s))) {
            reader.readObject();
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
