package it.cosenonjaviste.service;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

/**
 * Created by pizzo on 20/05/15.
 */
@Stateless
public class LongTimeProcessingService {

    @Inject
    private Event<String> echoComplete;

    @Asynchronous
    public void echo(String message) throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        echoComplete.fire("Echo " + message);
    }
}
