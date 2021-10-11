package com;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TestColdAndHotObservable {
  @Test
  void testColdObservable() throws InterruptedException {
    Observable<Long> ob = Observable.interval(1, TimeUnit.SECONDS);
    ob.subscribe(x -> log.info("Observable 1: {}", x.toString()));
    Thread.sleep(5000);
    ob.subscribe(x -> log.info("Observable 2: {}", x.toString()));
    Thread.sleep(5000);
  }

  @Test
  void testHotObservableUsingConnectable() throws InterruptedException {
    ConnectableObservable<Long> ob = Observable.interval(1, TimeUnit.SECONDS).publish();
    ob.connect();
    ob.subscribe(x -> log.info("Observable 1: {}", x.toString()));
    Thread.sleep(5000);
    ob.subscribe(x -> log.info("Observable 2: {}", x.toString()));
    Thread.sleep(5000);
  }

  @Test
  void testAnotherColdAndHotObservable() {
    List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
    Observable<String> coldObservable = Observable.fromIterable(list);
    coldObservable.subscribe(log::info);
    list.add("d");
    coldObservable.subscribe(log::info);

    List<String> listN = new ArrayList<>(Arrays.asList("1", "2", "3"));
    ConnectableObservable<String> hotObservable = Observable.fromIterable(listN).publish();
    hotObservable.subscribe(log::info);
    hotObservable.connect();
    listN.add("4");
    hotObservable.publish();
    hotObservable.subscribe(log::info);
    hotObservable.connect();
  }
}
