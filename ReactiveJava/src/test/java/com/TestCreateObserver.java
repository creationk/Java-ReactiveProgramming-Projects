package com;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

@Slf4j
public class TestCreateObserver {
  @Test
  void testShorthandObserverWithLambda() {
    Observable<Integer> source = Observable.just(3, 8, 2, 1, 8, 0);
    source.subscribe(x -> log.info(x.toString()));
    source.subscribe(
        x -> log.info(x.toString()), e -> log.error(e.getMessage()), () -> log.info("Complete"));
  }

  @Test
  void testObserverWithIntermediateOperations() {
    Observable<String> source = Observable.just("Alpha", "Beta", "Gamma");
    source
        .map(String::length)
        .filter(x -> x > 4)
        .subscribe(
            x -> log.info(x.toString()),
            e -> log.error(e.getMessage()),
            () -> log.info("Complete"));
  }

  @Test
  void testCreateDisposableObserver() {
    Observable<String> observable = Observable.fromIterable(Arrays.asList("3", "4", "5", "7", "9"));
    @NonNull
    Disposable disposableObserver =
        observable.subscribe(log::info, Throwable::printStackTrace, () -> log.info("Complete"));
    disposableObserver.dispose();
  }

  @Test
  void testEmptyObservable() {
    Observable<String> emptyObservable = Observable.empty();
    emptyObservable.subscribe(log::info);
  }

  @Test
  void testNeverObservable() {
    Observable<String> neverObserver = Observable.never();
    neverObserver.subscribe(log::info, e -> log.error(e.getMessage()), () -> log.info("Completed"));
  }

  @Test
  void testErrorObservable() {
    Observable<String> errorObservable = Observable.error(new Exception("Dummy"));
    errorObservable.subscribe(
        log::info, e -> log.error(e.getMessage()), () -> log.info("Complete"));
  }
}
