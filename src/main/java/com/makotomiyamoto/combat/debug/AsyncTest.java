package com.makotomiyamoto.combat.debug;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.concurrent.atomic.AtomicReference;

public class AsyncTest {
    private static class User {
        private String username;
        private long id;
    }
    private static class UserScanThread extends Thread {
        @Override
        public void run() {

        }
    }
    public static void main(String[] args) throws InterruptedException {
        //AtomicReference<User> john = null, sarah = null, makoto = null;
        AtomicReference<User> john = new AtomicReference<>();
        String path = "users" + File.separator;
        Thread johnThread = new Thread(() -> {
            Gson gson = new Gson();
            JsonReader reader;
            try {
                reader = new JsonReader(new FileReader(new File(path + "john.json")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                reader = null;
            }
            assert reader != null;
            john.set(gson.fromJson(reader, User.class));
        });
        johnThread.start();
        johnThread.join();
        System.out.println(john.get().username);
    }
}
