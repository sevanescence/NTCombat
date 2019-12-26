package com.makotomiyamoto.combat.debug;

import java.util.Random;

public class Test {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(new Random().nextFloat() <= 0.74);
        }
    }
}
