package com;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TestReactiveJava {
  @Test
  void testSimpleObservable() {

    Observable<String> subject =
        Observable.create(
            emitter -> {
              try {
                emitter.onNext("Hello");
                emitter.onNext("World");
                emitter.onComplete();
              } catch (Exception ex) {
                log.error(ex.getMessage());
              }
            });
    Disposable observer = subject.subscribe(log::info);
    observer.dispose();
  }

  @Test
  void testObservableWithException() {
    Observable<String> subject =
        Observable.create(
            emitter -> {
              try {
                emitter.onNext("1");
                emitter.onNext("2");
                throw new Exception("Custom error occurred");
              } catch (Exception ex) {
                emitter.onError(ex);
              }
            });

    Disposable o1 = subject.subscribe(log::info, throwable -> log.info(throwable.getMessage()));
    o1.dispose();
  }

  @Test
  void testObservableObserverComplete() {
    Observable<String> observable =
        Observable.create(
            emitter -> {
              try {
                emitter.onNext("a");
                emitter.onNext("b");
                emitter.onComplete();
              } catch (Exception ex) {
                emitter.onError(ex);
              }
            });
    Observer<String> observer =
        new Observer<String>() {
          @Override
          public void onSubscribe(@NonNull Disposable d) {
            log.info("Subscribed");
          }

          @Override
          public void onNext(@NonNull String s) {
            log.info("Next value: {}", s);
          }

          @Override
          public void onError(@NonNull Throwable e) {
            log.info("Error: {}", e.getMessage());
          }

          @Override
          public void onComplete() {
            log.info("Completed");
          }
        };
    observable.subscribe(observer);
  }

  @Test
  void testObservableObserverOnError() {
    Observable<String> observable =
        Observable.create(
            emitter -> {
              try {
                emitter.onNext("a");
                emitter.onNext("b");
                throw new Exception("Exception thrown");
              } catch (Exception ex) {
                emitter.onError(ex);
              }
            });
    Observer<String> observer =
        new Observer<String>() {
          @Override
          public void onSubscribe(@NonNull Disposable d) {
            log.info("Subscribed");
          }

          @Override
          public void onNext(@NonNull String s) {
            log.info("Next value: {}", s);
          }

          @Override
          public void onError(@NonNull Throwable e) {
            log.info("Error: {}", e.getMessage());
          }

          @Override
          public void onComplete() {
            log.info("Completed");
          }
        };
    observable.subscribe(observer);
  }

  @Test
  void disposableObservableObserverComplete() {
    Observable<String> observable =
        Observable.create(
            emitter -> {
              try {
                emitter.onNext("a");
                emitter.onNext("b");
                emitter.onComplete();
              } catch (Exception ex) {
                emitter.onError(ex);
              }
            });
    DisposableObserver<String> observer =
        new DisposableObserver<String>() {
          @Override
          public void onNext(@NonNull String s) {
            log.info("Next value: {}", s);
          }

          @Override
          public void onError(@NonNull Throwable e) {
            log.info("Error: {}", e.getMessage());
          }

          @Override
          public void onComplete() {
            log.info("Completed");
          }
        };
    observable.subscribe(observer);
    observer.dispose();
  }
}
