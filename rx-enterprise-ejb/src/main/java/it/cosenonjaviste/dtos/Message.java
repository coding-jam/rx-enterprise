package it.cosenonjaviste.dtos;

import javax.validation.constraints.NotNull;

/**
 * Created by pizzo on 25/05/15.
 */
public class Message {

    @NotNull
    private String message;

    private String sessionId;

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
