package com;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.ResourceObserver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TestDisposable {

  @Test
  void testDisposableLambda() {
    Disposable d = Observable.just("1", "2").subscribe(log::info);
    d.dispose();
  }

  @Test
  void testCustomHandleDisposeWithObserver() {
    @NonNull Observable<String> observable = Observable.just("1", "2");
    Observer<String> observer =
        new Observer<String>() {
          private Disposable disposable;

          @Override
          public void onSubscribe(@NonNull Disposable disposable) {
            this.disposable = disposable;
          }

          @Override
          public void onNext(@NonNull String s) {}

          @Override
          public void onError(@NonNull Throwable e) {}

          @Override
          public void onComplete() {
            log.info("Disposing in onCompleteMethod");
            disposable.dispose();
          }
        };
    observable.subscribe(observer);
  }

  @Test
  void testAutoHandleDisposeWithResourceObserver() {
    Observable<String> observable = Observable.just("2", "7", "3");
    ResourceObserver<String> resourceObserver =
        new ResourceObserver<String>() {
          @Override
          public void onNext(@NonNull String s) {}

          @Override
          public void onError(@NonNull Throwable e) {}

          @Override
          public void onComplete() {}
        };
    @NonNull ResourceObserver<String> disposable = observable.subscribeWith(resourceObserver);
    // use disposable to make it disposed or wait for jvm to do it
  }

  @Test
  void testCompositeDisposable() throws InterruptedException {
    Observable<String> observable =
        Observable.interval(250, TimeUnit.MILLISECONDS).map(x -> x.toString());
    Disposable d1 = observable.subscribe(log::info);
    Disposable d2 = observable.subscribe(log::info);
    Thread.sleep(1000); // 1000/250 = 4

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    compositeDisposable.addAll(d1, d2);
    compositeDisposable.dispose();
    Thread.sleep(2000);
    log.info("d1: {}, d2: {}", d1, d2);
  }
}
