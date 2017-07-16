package com.example.iseongjae.rxjavatest;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by iseongjae on 2017. 7. 15..
 */

public class RxJavaTestLambda {
    public void print( String s ){
        System.out.println( s );
    }
    @Test
    public void observable() throws Exception {
        String hello = "hello";
         Observable.just(hello).subscribe(
                (v) -> System.out.println(v),
                (Throwable e) -> e.printStackTrace(),
                ()-> {}
                );

        Observable.create((e -> {
            e.onNext("hello1");
            e.onNext("hello2");
            e.onNext("hello3");
            e.onComplete();
        })).subscribe(
                (System.out::println),
                (Throwable::printStackTrace),
                (this::completable),
                (disposable1 -> {})
        );

    }

    @Test
    public void flowable() throws Exception {
        String hello = "hello";
        Flowable.just(hello).subscribe(
                (System.out::println)
        );
    }

    @Test
    public void single() throws Exception {
        String hello = "hello";
        Single.just(hello).subscribe(
                (System.out::println)
        );
    }

    @Test
    public void maybe() throws Exception {
        String hello = "hello";
        Maybe.just(hello).subscribe(
                (System.out::println)
        );
    }

    @Test
    public void completable() throws Exception {
        String hello = "hello";
        Completable.fromRunnable(()->{})
                .subscribe(
                        ()->{print("complete");}
                );
    }

    @Test
    public void observableBuffer(){
        String[] strings = new String[]{"apple", "banana", "crop", "domain", "file"};
        Observable.fromIterable(Arrays.asList(strings))
                .buffer(2)
                .subscribe(
                        (value) ->{value.forEach(System.out::println);}
                );
    }

    @Test
    public void observableGroupBy(){
        String[] strings = new String[]{"apple", "banana", "crop", "domain", "file"};
        Observable.fromIterable(Arrays.asList(strings))
                .groupBy(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) throws Exception {
                        return s+"key";
                    }
                }).subscribe(new Consumer<GroupedObservable<String, String>>() {
            @Override
            public void accept(@NonNull final GroupedObservable<String, String> objectStringGroupedObservable) throws Exception {
                objectStringGroupedObservable.subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String value) throws Exception {
                        print(objectStringGroupedObservable.getKey());
                        print(value);
                    }
                });
            }
        });

    }

    @Test
    public void observableAll(){
        //ean test(@NonNull String s) throws Exception {
//        return s.length() < 7
        String[] strings = new String[]{"apple", "banana", "crop", "domain", "file"};
        Observable.fromIterable(Arrays.asList(strings))
                .all(s -> s.length() < 7)
                .subscribe( s -> print("result : " + s) );

    }

    @Test
    public void observableReduce(){
        String[] strings = new String[]{"apple", "banana", "crop", "domain", "file"};
        Observable.fromIterable(Arrays.asList(strings))
                .reduce(new BiFunction<String, String, String>() {
                    @Override
                    public String apply(@NonNull String s, @NonNull String s2) throws Exception {
                        return s+s2;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        print("result : "+s);

                    }
                });
    }

    @Test
    public void observableDebounce(){
        String[] strings = new String[]{"apple", "banana", "crop", "domain", "file"};
        Observable.fromIterable(Arrays.asList(strings))
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull String s) throws Exception {
                        return Observable.just(s);
                    }
                })
                .debounce(1, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        print(s);
                    }
                });
    }

    @Test
    public void observableMuliti(){
        String[] strings = new String[]{"apple", "banana", "crop", "domain", "file"};
        ConnectableObservable<String> obs = Observable.fromArray(strings)
                .concatMap( (s) -> {
                            System.out.println(s);
                            return Observable.just(s);
                            })
                .publish();

        obs.subscribe((s)->{System.out.println("connect 1 : " + s);});
        obs.subscribe((s)->{System.out.println("connect 2 : " + s);});
        obs.connect();

    }

    @Test
    public void test(){
        Observable.just("path")
                .concatMap( (String s) ->
                     fileProcess(s).switchIfEmpty(Observable.just("fileProcessFail").observeOn(Schedulers.io()))
                )
                .concatMap( (String s) ->
                     auth(s).switchIfEmpty(Observable.just("AuthProcessFail").observeOn(Schedulers.io()))
                 )
                .concatMap( (String s) ->
                    issue(s).switchIfEmpty(Observable.just("issueProcessFail").observeOn(Schedulers.io()))
                 )
                .subscribe(
                        (System.out::println),
                        (Throwable::printStackTrace),
                        () ->{},
                        (disposable -> {})
                );


        List<String> paths = Arrays.asList("path1", "path2", "path3", "path4", "path5");
        Observable.fromIterable(paths)
                .concatMap( (String s) -> {
                    System.out.println("fileProcess:             "+Thread.currentThread().getName());
                    return fileProcess(s).switchIfEmpty(Observable.just("fileProcessFail").observeOn(Schedulers.io()));
                } )
                .concatMap( (String s) -> {
                    System.out.println("auth:             "+Thread.currentThread().getName());
                    return auth(s).switchIfEmpty(Observable.just("AuthProcessFail").observeOn(Schedulers.io()));
                } )
                .concatMap( (String s) -> {
                    System.out.println("issue:             "+Thread.currentThread().getName());
                    return issue(s).switchIfEmpty(Observable.just("issueProcessFail").observeOn(Schedulers.io()));
                } )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(
                        (System.out::println),
                        (Throwable::printStackTrace),
                        () ->{},
                        (disposable -> {})
                );
    }

    public Observable<String> fileProcess(String path){
        return Observable.just(path+":fileProcess")
                .observeOn(Schedulers.single())
                .concatMap( (String s) -> {
                    System.out.println(Thread.currentThread().getName());
                    return Observable.just(s);
                })
                .observeOn(Schedulers.io());
    }

    public Observable<String> auth(String file){
        boolean auth = true;
        if(auth)
            return Observable.just(file+":auth")
                    .observeOn(Schedulers.single())
                    .concatMap( (String s) -> {
                        System.out.println(Thread.currentThread().getName());
                        return Observable.just(s);
                    })
                    .observeOn(Schedulers.io());

        else
            return Observable.empty();
    }

    public Observable<String> issue(String auth){
        boolean issue = true;
        if(issue)
            return Observable.just(auth+":issue")
                    .observeOn(Schedulers.single())
                    .concatMap( (String s) -> {
                        System.out.println(Thread.currentThread().getName());
                        return Observable.just(s);
                    })
                    .observeOn(Schedulers.io());

        else
            return Observable.empty();
    }

}
