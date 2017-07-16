package com.example.iseongjae.rxjavatest;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iseongjae on 2017. 7. 16..
 */

public class LamdaTest {

    @Test
    public void test(){
        String path = "/path/";

        fileProcess(path, (val -> auth(val, (val1 -> issue(val1, System.out::println)))));

        List<String> paths = new ArrayList<>();
        paths.add("path1");
        paths.add("path2");
        paths.add("path3");
        paths.add("path4");
        paths.add("path5");

        paths.forEach( (s) -> fileProcess(s, (val -> auth(val, (val1 -> issue(val1, System.out::println))))));



    }

    public String fileProcess(String path, JavaTest.NewRunnable completion){
        //task
        String result = path+":fileProcess";
        completion.run(result);
        return result;
    }

    public String auth(String file, JavaTest.NewRunnable completion){
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

    public String issue(String auth, JavaTest.NewRunnable completion){
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
