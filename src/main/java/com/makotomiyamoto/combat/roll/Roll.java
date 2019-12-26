package com.makotomiyamoto.combat.roll;

import java.util.Random;

public abstract class Roll {
    public static boolean percentChance(double chance) {
        return new Random().nextFloat() <= chance;
    }
    public static int damageRoll(Integer[] damageRange) {
        return (int) (new Random().nextFloat() * (damageRange[1]-damageRange[0]) + 1);
    }
    public static double tryCritical(double damage, double chance, double multiplier) {
        if (percentChance(chance)) {
            return damage * multiplier;
        }
        return damage;
    }
}
