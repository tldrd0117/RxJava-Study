package com.example.iseongjae.rxjavatest;

import android.os.Handler;
import android.support.annotation.MainThread;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Observable;

/**
 * Created by iseongjae on 2017. 7. 16..
 */

public class JavaTest {
    @Test
    public void test(){
        String path = "path";
        fileProcess(path, new NewRunnable() {
            @Override
            public void run(String val) {
                auth(val, new NewRunnable() {
                    @Override
                    public void run(String val) {
                        issue(val, new NewRunnable() {
                            @Override
                            public void run(String val) {
                                System.out.println(val);
                            }
                        });
                    }
                });
            }
        });

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        ExecutorService singleService = Executors.newSingleThreadExecutor();

        List<String> paths = Arrays.asList("path1", "path2", "path3", "path4", "path5");

        for( String path2 : paths ) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                    fileProcess(path2, new NewRunnable() {
                        @Override
                        public void run(String val) {
                            singleService.submit(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("main : " + Thread.currentThread().getName());
                                }
                            });
                            auth(val, new NewRunnable() {
                                @Override
                                public void run(String val) {
                                    singleService.submit(new Runnable() {
                                        @Override
                                        public void run() {
                                            System.out.println("main : " + Thread.currentThread().getName());
                                        }
                                    });
                                    issue(val, new NewRunnable() {
                                        @Override
                                        public void run(String val) {
                                            singleService.submit(new Runnable() {
                                                @Override
                                                public void run() {
                                                    System.out.println("main : " + Thread.currentThread().getName());
                                                }
                                            });
                                            System.out.println(val);
                                        }
                                    });
                                }
                            });

                        }
                    });
                }
            });

        }


    }

    public String fileProcess(String path, NewRunnable completion){
        //task
        String result = path+":fileProcess";
        completion.run(result);
        return result;
    }

    public String auth(String file, NewRunnable completion){
        boolean auth = true;
        if(auth) {
            //task
            String result = file+":auth";
            completion.run(result);
            return result;
        }
        else {
            completion.run(":AuthProcessFail");
            return "AuthProcessFail";
        }
    }

    public String issue(String auth, NewRunnable completion){
        boolean issue = true;
        if(issue){
            //task
            String result = auth + ":issue";
            completion.run(result);
            return result;
        }
        else {
            String result = auth + ":issueProcessFail";
            return "";
        }
    }

    public interface NewRunnable{
        void run(String val);
    }
}
