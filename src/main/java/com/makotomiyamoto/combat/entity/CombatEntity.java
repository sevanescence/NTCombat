package com.makotomiyamoto.combat.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.makotomiyamoto.combat.CombatSystem;
import com.makotomiyamoto.combat.data.CombatAttribute;
import com.makotomiyamoto.combat.roll.Roll;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;
import java.util.HashMap;

public class CombatEntity {
    private double
            damage,
            rangedDamage;
    private int armor;
    private double
            critical,
            criticalDamage,
            block,
            parry,
            dodge,
            penetration;
    private HashMap<CombatAttribute, Integer> attributes;
    public CombatEntity() { }
    public CombatEntity(FileConfiguration mob) {
        this.damage = mob.getDouble("damage");
        this.rangedDamage = mob.getDouble("rangedDamage");
        this.armor = mob.getInt("armor");
        this.critical = mob.getDouble("critical");
        this.criticalDamage = mob.getDouble("criticalDamage");
        this.block = mob.getDouble("block");
        this.parry = mob.getDouble("parry");
        this.dodge = mob.getDouble("dodge");
        this.penetration = mob.getDouble("penetration");
        this.attributes = new HashMap<CombatAttribute, Integer>();
        ConfigurationSection attributes = mob.getConfigurationSection("attributes");
        if (attributes == null) {
            this.attributes.put(CombatAttribute.STRENGTH, 0);
            this.attributes.put(CombatAttribute.VITALITY, 0);
            this.attributes.put(CombatAttribute.AGILITY, 0);
            this.attributes.put(CombatAttribute.TENACITY, 0);
            this.attributes.put(CombatAttribute.INTELLECT, 0);
            this.attributes.put(CombatAttribute.SPIRIT, 0);
            return;
        }
        for (String attribute : attributes.getKeys(false)) {
            this.attributes.put(CombatAttribute.valueOf(attribute), attributes.getInt(attribute));
        }
    }
    public static CombatEntity parseFromJson(CombatSystem system, String name) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(new JsonReader(new FileReader(new File(system.getDataFolder().getPath()
                    + File.separator + "mob_data" + File.separator + name))), CombatEntity.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new CombatEntity();
        }
    }
    public void save(CombatSystem system, String name) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);
        File file = new File(system.getDataFolder().getPath() + File.separator
                + "mob_data" + File.separator + name);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public double getDamageRoll() {
        Integer[] range = new Integer[] {
                (int) damage + attributes.get(CombatAttribute.STRENGTH),
                (int)(damage-(int) damage) + attributes.get(CombatAttribute.STRENGTH)
        };
        return Roll.damageRoll(range);
    }
    public double getRangedDamageRoll() {
        Integer[] range = new Integer[] {
                (int) rangedDamage + attributes.get(CombatAttribute.AGILITY),
                (int)(rangedDamage-(int) rangedDamage) + attributes.get(CombatAttribute.AGILITY)
        };
        return Roll.damageRoll(range);
    }
    public int getArmor() {
        return armor + attributes.get(CombatAttribute.VITALITY);
    }
    public double getCritical() {
        return critical + (0.0015 * attributes.get(CombatAttribute.TENACITY));
    }
    public double getCriticalDamage() {
        return criticalDamage + (0.01 * attributes.get(CombatAttribute.STRENGTH));
    }
    public double getBlock() {
        return block + (0.0015 * attributes.get(CombatAttribute.AGILITY));
    }
    public double getParry() {
        return parry + (0.0015 * attributes.get(CombatAttribute.AGILITY));
    }
    public double getDodge() {
        return dodge + (0.0015 * attributes.get(CombatAttribute.AGILITY));
    }
    public double getPenetration() {
        return penetration + (0.0015 * attributes.get(CombatAttribute.TENACITY));
    }
}
