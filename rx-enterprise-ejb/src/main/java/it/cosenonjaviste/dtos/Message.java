package it.cosenonjaviste.dtos;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by pizzo on 25/05/15.
 */
@XmlRootElement
public class Message {

    public enum Status {OK, ERROR}

    @NotNull
    private String message;

    private String sessionId;

    private Status status = Status.OK;

    public Message() {}

    public Message(String message, String sessionId) {
        this.message = message;
        this.sessionId = sessionId;
    }

    public Message(String message, String sessionId, Status status) {
        this.message = message;
        this.sessionId = sessionId;
        this.status = status;
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

    public Status getStatus() {
        return status;
    }
}
