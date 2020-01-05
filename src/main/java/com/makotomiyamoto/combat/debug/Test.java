package com.makotomiyamoto.combat.debug;

import com.makotomiyamoto.combat.data.CombatAttribute;

import java.util.Random;

public class Test {
    public static void main(String[] args) {
        System.out.println(new Random().nextFloat()*100);
        System.out.println("doing probability thing");
        float[] list = new float[500];
        for (int i = 0; i < list.length; i++) {
            list[i] = new Random().nextFloat();
        }
        System.out.println("checking list for one percent occurrences...");
        int numberOfOnePercent = 0;
        for (float i : list) {
            if (i <= .015) {
                numberOfOnePercent++;
            }
        }
        System.out.println("1 percent chance occurred " + numberOfOnePercent + " times.");
        // unrelated test
        for (int i = 3; i < 5; i++) {
            System.out.println(i);
        }
        // regex test
        String hello = "5 Hello";
        System.out.println(hello);
        hello = hello.replaceAll("[0-9] ", "");
        System.out.println(hello);
        // unrelaetd test
        Integer[] damage = new Integer[2];
        String damageLine = "2-5 Damage".replaceAll("[^0-9-]", "");
        String[] range = damageLine.split("-");
        damage[0] = Integer.parseInt(range[0]);
        damage[1] = Integer.parseInt(range[1]);
        for (int i : damage) {
            System.out.println(i);
        }
        // threading test
        Thread thread = new Thread(() -> {
            System.out.println("I'm the first thread!");
            System.out.println("First thread done!");
        });
        Thread thread1 = new Thread(() -> {
            System.out.println("I'm the second thread!");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread 2 done!");
        });
        thread1.start();
        thread.start();
        try {
            thread1.join();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("done!");
    }
}
