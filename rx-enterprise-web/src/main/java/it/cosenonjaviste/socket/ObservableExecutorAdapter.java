package it.cosenonjaviste.socket;

import rx.Observable;
import rx.schedulers.Schedulers;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by pizzo on 14/06/15.
 */
@ApplicationScoped
public class ObservableExecutorAdapter {

    @Resource
    private ManagedExecutorService executorService;

    public <O> Observable<O> executeAsync(Supplier<O> function) {

        return (Observable<O>) Observable.create(subscriber -> {
            try {
                subscriber.onNext(function.get());
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }).subscribeOn(Schedulers.from(executorService));
    }
}
