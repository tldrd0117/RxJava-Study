package com.example.iseongjae.rxjavatest

import org.junit.Test

import java.util.Arrays
import java.util.concurrent.TimeUnit

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.*
import io.reactivex.functions.Function
import io.reactivex.observables.ConnectableObservable
import io.reactivex.observables.GroupedObservable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable

/**
 * Created by iseongjae on 2017. 7. 15..
 */

class RxJavaTestKotlin {

    @Test
    @Throws(Exception::class)
    fun observable() {
        val hello = "hello"
        Observable.just(hello).subscribe(
                { v -> System.out.print(v) },
                { e: Throwable -> e.printStackTrace() }
        ) { }

        Observable.create<Any> { e ->
            e.onNext("hello1")
            e.onNext("hello2")
            e.onNext("hello3")
            e.onComplete()
        }.subscribeBy(
            onNext={ println(it) },
            onError={ it.printStackTrace() },
            onComplete={ this.completable() }

        )

    }

    @Test
    @Throws(Exception::class)
    fun flowable() {
        val hello = "hello"
        Flowable.just(hello).subscribe { println(it) }
    }

    @Test
    @Throws(Exception::class)
    fun single() {
        val hello = "hello"
        Single.just(hello).subscribe(
                Consumer<String> { println(it) }
        )
    }

    @Test
    @Throws(Exception::class)
    fun maybe() {
        val hello = "hello"
        Maybe.just(hello).subscribe { println(it) }
    }

    @Test
    @Throws(Exception::class)
    fun completable() {
        val hello = "hello"
        Completable.fromRunnable { }
                .subscribe { print("complete") }
    }

    @Test
    fun observableBuffer() {
        val strings = arrayOf("apple", "banana", "crop", "domain", "file")
        Observable.fromIterable(Arrays.asList(*strings))
                .buffer(2)
                .subscribe { value -> value.forEach({s -> println(s)}) };
    }

    @Test
    fun observableGroupBy() {
        val strings = arrayOf("apple", "banana", "crop", "domain", "file")
        Observable.fromIterable(Arrays.asList(*strings))
                .groupBy { s -> s + "key" }.subscribe { objectStringGroupedObservable ->
            objectStringGroupedObservable.subscribe { value ->
                print(objectStringGroupedObservable.getKey())
                print(value)
            }
        }

    }

    @Test
    fun observableAll() {
//        val strings = arrayOf("apple", "banana", "crop", "domain", "file")
//        Observable.fromIterable(Arrays.asList(*strings))
//                .all { s -> s.length < 7 }.subscribe(object : Consumer<Boolean> {
//            @Throws(Exception::class)
//            override fun accept(@NonNull aBoolean: Boolean?) {
//                print("result : " + aBoolean!!)
//            }
//        })

    }

    @Test
    fun observableReduce() {
        val strings = arrayOf("apple", "banana", "crop", "domain", "file")
        Observable.fromIterable(Arrays.asList(*strings))
                .reduce { s, s2 -> s + s2 }
                .subscribe { s -> print("result : " + s) }
    }

    @Test
    fun observableDebounce() {
        val strings = arrayOf("apple", "banana", "crop", "domain", "file")
        strings.toObservable().flatMap { s -> Observable.just(s) }
                .debounce ( 1, TimeUnit.MICROSECONDS )
                .subscribeBy(onNext={ s-> print(s)})
    }

    @Test
    fun observableMuliti() {
        val strings = arrayOf("apple", "banana", "crop", "domain", "file")
        val obs = Observable.fromArray(*strings)
                .concatMap { s ->
                    println(s)
                    Observable.just(s)
                }
                .publish()

        obs.subscribe { s -> println("connect 1 : " + s) }
        obs.subscribe { s -> println("connect 2 : " + s) }
        obs.connect()

    }

    @Test
    fun test() {
        Observable.just("path")
                .concatMap { s -> fileProcess(s).switchIfEmpty(Observable.just("fileProcessFail")) }
                .concatMap { s -> auth(s).switchIfEmpty(Observable.just("AuthProcessFail")) }
                .concatMap { s -> issue(s).switchIfEmpty(Observable.just("issueProcessFail")) }
                .subscribeBy (
                        onNext={ println(it) },
                        onError={ it.printStackTrace() },
                        onComplete =  {  }
                )

    }

    fun fileProcess(path: String): Observable<String> {
        return Observable.just(path + ":fileProcess")
    }

    fun auth(file: String): Observable<String> {
        val auth = true
        if (auth)
            return Observable.just(file + ":auth")
        else
            return Observable.empty<String>()
    }

    fun issue(auth: String): Observable<String> {
        val issue = true
        if (issue)
            return Observable.just(auth + ":issue")
        else
            return Observable.empty<String>()
    }

}
