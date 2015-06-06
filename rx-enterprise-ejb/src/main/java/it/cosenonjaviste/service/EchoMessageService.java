package it.cosenonjaviste.service;

import it.cosenonjaviste.dtos.Message;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by pizzo on 20/05/15.
 */
@Stateless
public class EchoMessageService {

    @Inject
    private Event<Message> echoComplete;

    @Asynchronous
    public void echo(Message message) throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        message.setMessage("Echo " + message.getMessage());
        echoComplete.fire(message);
    }
}
