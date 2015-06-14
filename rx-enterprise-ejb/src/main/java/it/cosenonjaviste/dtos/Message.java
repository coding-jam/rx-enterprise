package it.cosenonjaviste.dtos;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by pizzo on 25/05/15.
 */
@XmlRootElement
public class Message {

    @NotNull
    private String message;

    private String sessionId;

    public Message() {}

    public Message(String message, String sessionId) {
        this.message = message;
        this.sessionId = sessionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
