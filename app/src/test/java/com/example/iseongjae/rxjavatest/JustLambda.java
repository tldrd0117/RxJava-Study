package com.example.iseongjae.rxjavatest;

import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observables.GroupedObservable;

/**
 * Created by iseongjae on 2017. 7. 15..
 */

public class JustLambda {
    public void print( String s ){
        System.out.println( s );
    }

    @Test
    public void observable() throws Exception {
        String hello = "hello";
        Observable.just(hello).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {
                print(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                e.onNext("hello1");
                e.onNext("hello2");
                e.onNext("hello3");
                e.onComplete();
            }
        }).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Object o) {
                print((String)o);

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                print("complete");

            }
        });
    }

    @Test
    public void flowable() throws Exception {
        String hello = "hello";
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(0);
            }

            @Override
            public void onNext(String s) {
                print(s);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        };
        Flowable.just(hello).subscribe(subscriber);
    }

    @Test
    public void single() throws Exception {
        String hello = "hello";
        Single.just(hello).subscribe(new SingleObserver<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull String s) {
                print(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    @Test
    public void maybe() throws Exception {
        String hello = "hello";
        Maybe.just(hello).subscribe(new MaybeObserver<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull String s) {
                print(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Test
    public void completable() throws Exception {
        String hello = "hello";
        Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {

            }
        }).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                print("complete");
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    @Test
    public void observableBuffer(){
        String[] strings = new String[]{"apple", "banana", "crop", "domain", "file"};
        Observable.fromIterable(Arrays.asList(strings))
                .buffer(2)
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<String> strings) {
                        print("stringSize : " + strings.size());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
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
        String[] strings = new String[]{"apple", "banana", "crop", "domain", "file"};
        Observable.fromIterable(Arrays.asList(strings))
                .all(new Predicate<String>() {
                    @Override
                    public boolean test(@NonNull String s) throws Exception {
                        return s.length() < 7;
                    }
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                print("result : "+aBoolean);
            }
        });

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
                .concatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull String s) throws Exception {
                        System.out.println("org1 : "+s);
                        return Observable.just(s);
                    }
                })
                .publish();

        obs.subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                System.out.println("connect 1 : " + s);
            }
        });
        obs.subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                System.out.println("connect 2 : " + s);
            }
        });

        obs.connect();

    }
}
