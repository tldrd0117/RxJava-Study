package com.example.iseongjae.rxjavatest;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by iseongjae on 2017. 7. 16..
 */

public class JavaTest {
    @Test
    public void test(){
        String path = "/path/";
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

        List<String> paths = new ArrayList<>();
        paths.add("path1");
        paths.add("path2");
        paths.add("path3");
        paths.add("path4");
        paths.add("path5");

        for( String path2 : paths ) {
            fileProcess(path2, new NewRunnable() {
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
