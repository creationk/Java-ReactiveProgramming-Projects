package com;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TestCreateObservable {
  @Test
  void testCreateObservable() {

    Observable<String> subject =
        Observable.create(
            emitter -> {
              // body of the subscribe method
              emitter.onNext("Hello");
              emitter.onNext("World");
              emitter.onComplete();
            });
    Disposable observer = subject.subscribe(log::info);
    observer.dispose();
  }

  @Test
  void testObservableUsingJust() {
    Observable<String> observable = Observable.just("a", "b", "c");
    observable.subscribe(log::info);
  }

  @Test
  void testObservableUsingFromIterable() {
    Observable<String> observable = Observable.fromIterable(Arrays.asList("a", "b", "c"));
    observable.subscribe(log::info);
  }

  @Test
  void testObservableUsingRange() {
    Observable<Integer> observable = Observable.range(1, 4);
    observable.subscribe(consumer -> log.info(String.valueOf(consumer)));

    Observable<Long> observableLong = Observable.rangeLong(1L, 4);
    observable.subscribe(consumer -> log.info(String.valueOf(consumer)));
  }

  @Test
  void testObservableUsingInterval() throws InterruptedException {
    @NonNull Observable<Long> observable = Observable.interval(4, TimeUnit.MILLISECONDS);
    observable.subscribe(consumer -> log.info(consumer.toString()));
    Thread.sleep(100);
    // 100/4 = 25
  }

  @Test
  void testObservableDefer() {
    // TODO
  }

  @Test
  void testSingleObservable() {
    Observable<Integer> observable = Observable.fromIterable(Arrays.asList(1, 6, 8, 9, 0));
    @NonNull Single<Integer> singleObservable = observable.first(-99);

    SingleObserver<Integer> observer =
        new SingleObserver<Integer>() {

          @Override
          public void onSubscribe(@NonNull Disposable d) {
            log.info("Subscribed");
          }

          @Override
          public void onSuccess(@NonNull Integer integer) {
            log.info("On next = complete");
          }

          @Override
          public void onError(@NonNull Throwable e) {
            log.info("On error");
          }
        };
    singleObservable.subscribe(observer);

    @NonNull Single<Integer> so = Single.just(3);
    so.subscribe(onSuccess -> log.info(onSuccess.toString()));
  }

  @Test
  void testMaybeObservable() {
    // for 0 or 1 emission
    Observable<Integer> multipleEmissions = Observable.fromIterable(Collections.emptyList());
    Maybe<Integer> maybeObservable = Maybe.fromObservable(multipleEmissions);

    maybeObservable.subscribe(
        x -> log.info(x.toString())); // prints firstElement or nothing if nothing is emitted
  }

  @Test
  void testObservableFromCallable() {
    // Observable<Integer> obs = Observable.just(1/0); //exception not processed by the observer
    Observable<Integer> obsCallable = Observable.fromCallable(() -> 1 / 0);
    obsCallable.subscribe(
        x -> log.info(x.toString()),
        e -> log.info(e.getMessage())); // exception processed by the observer
  }

  @Test
  void testCompletableObservable() {
    Completable observable = Completable.complete();
    observable.subscribe(() -> log.info("Complete"));

    Completable observableRunnable = Completable.fromRunnable(() -> log.info("Running"));
    observableRunnable.subscribe(() -> log.info("Complete"));
  }
}
