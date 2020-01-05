package com.makotomiyamoto.combat.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.makotomiyamoto.combat.CombatSystem;
import com.makotomiyamoto.combat.data.CombatAttribute;
import com.makotomiyamoto.combat.roll.Roll;
import com.sun.istack.internal.Nullable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class CombatEntity {
    private Integer[]
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
    public CombatEntity() {
        damage = new Integer[2];
        rangedDamage = new Integer[2];
        armor = 0;
        critical = 0;
        criticalDamage = 0;
        block = 0;
        parry = 0;
        dodge = 0;
        penetration = 0;
        attributes = new HashMap<>();
    }
    public CombatEntity(FileConfiguration mob) throws IllegalArgumentException {
        this.damage = mob.getIntegerList("damage").toArray(new Integer[0]);
        this.rangedDamage = mob.getIntegerList("rangedDamage").toArray(new Integer[0]);
        this.armor = mob.getInt("armor");
        this.critical = mob.getDouble("critical");
        this.criticalDamage = mob.getDouble("criticalDamage");
        this.block = mob.getDouble("block");
        this.parry = mob.getDouble("parry");
        this.dodge = mob.getDouble("dodge");
        this.penetration = mob.getDouble("penetration");
        this.attributes = new HashMap<>();
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
            this.attributes.put(CombatAttribute.valueOf(attribute.toUpperCase()), attributes.getInt(attribute));
        }
    }
    // new player constructor
    public CombatEntity(CombatSystem system, String uuid, boolean saveNow) {
        attributes = new HashMap<>();
        FileConfiguration config = system.getConfig();
        ConfigurationSection defaults = config.getConfigurationSection("player-defaults");
        assert defaults != null;
        this.damage = defaults.getIntegerList("damage").toArray(new Integer[0]);
        this.rangedDamage = defaults.getIntegerList("rangedDamage").toArray(new Integer[0]);
        this.armor = defaults.getInt("armor");
        this.critical = defaults.getDouble("critical");
        this.criticalDamage = defaults.getDouble("criticalDamage");
        this.block = defaults.getDouble("block");
        this.parry = defaults.getDouble("parry");
        this.dodge = defaults.getDouble("dodge");
        this.penetration = defaults.getDouble("penetration");
        this.attributes.put(CombatAttribute.STRENGTH, defaults.getInt("attributes.strength"));
        this.attributes.put(CombatAttribute.VITALITY, defaults.getInt("attributes.vitality"));
        this.attributes.put(CombatAttribute.AGILITY, defaults.getInt("attributes.agility"));
        this.attributes.put(CombatAttribute.TENACITY, defaults.getInt("attributes.tenacity"));
        this.attributes.put(CombatAttribute.INTELLECT, defaults.getInt("attributes.intellect"));
        this.attributes.put(CombatAttribute.SPIRIT, defaults.getInt("attributes.spirit"));
        if (saveNow) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(this);
            File file = new File(system.getDataFolder().getPath() + File.separator
                    + "player_data" + File.separator + uuid + ".json");
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(json);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static CombatEntity findDefaults(CombatSystem system, Entity entity) {
        String path = system.getDataFolder().getPath() + File.separator
                + "mob_data" + File.separator + "default_" + entity.getType().toString() + ".json";
        File file = new File(path);
        try {
            JsonReader reader = new JsonReader(new FileReader(file));
            Gson gson = new Gson();
            return gson.fromJson(reader, CombatEntity.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("To put it simply, you forgot to set the default for the "
                    + entity.getType().toString().toLowerCase() + ".");
            return new CombatEntity();
        }
    }
    public static CombatEntity findMob(CombatSystem system, String name) {
        name = name.replace(" ", "") + ".json";
        String path = system.getDataFolder().getPath() + File.separator
                + "mob_data" + File.separator + name;
        File file = new File(path);
        try {
            JsonReader reader = new JsonReader(new FileReader(file));
            Gson gson = new Gson();
            return gson.fromJson(reader, CombatEntity.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("To put it simply, your mob doesn't exist.");
            return new CombatEntity();
        }
    }



    public static CombatEntity getPlayer(CombatSystem system, Player player) {
        Gson gson = new Gson();
        String s = File.separator;
        String path = system.getDataFolder() + s + "player_data" + s + player.getUniqueId().toString();
        File file = new File(path);
        try {
            CombatEntity combatPlayer = gson.fromJson(new JsonReader(new FileReader(file)), CombatEntity.class);
            ItemStack[] armor = player.getInventory().getArmorContents();
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            ItemStack offHand = player.getInventory().getItemInOffHand();
            for (ItemStack item : armor) {
                combatPlayer.addFromItemStack(item);
            }
            combatPlayer.addFromItemStack(mainHand);
            combatPlayer.addFromItemStack(offHand);
            return combatPlayer;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new CombatEntity(system, player.getUniqueId().toString(), true);
        }
    }
    public void addFromItemStack(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }
        List<String> lore = meta.getLore();
        if (lore == null || lore.size() < 6) {
            return;
        }
        String mainLine = lore.get(3);
        if (mainLine.contains("Armor")) {
            armor += Integer.parseInt(mainLine.replaceAll("[^0-9]", ""));
        } else {
            String[] range = mainLine.replaceAll("§r", "")
                    .replaceAll("[^0-9-]", "").split("-");
            damage[0] += Integer.parseInt(range[0]);
            damage[1] += Integer.parseInt(range[1]);
        }
        // TODO add attributes here (DO NOT CHANGE STATS, THAT IS DONE IN THEIR PUBLIC FETCHERS)
        for (String line : lore) {
            if (line.contains(" Strength") || line.contains(" Vitality") || line.contains(" Agility")
                    || line.contains(" Tenacity") || line.contains(" Intellect") || line.contains(" Spirit")) {
                String[] split = line.split(" ");
                int lvl = Integer.parseInt(split[0].replaceAll("§r§[A-z,0-9]", ""));
                CombatAttribute attribute = CombatAttribute.valueOf(split[1].toUpperCase());
                attributes.replace(attribute, attributes.get(attribute)+lvl);
            }
        }
    }



    public void savePlayer(CombatSystem system, String uuid) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String s = File.separator;
        String path = system.getDataFolder().getPath() + s + "player_data"
                + s + uuid + ".json";
        String json = gson.toJson(this);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path)));
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static CombatEntity parseFromJson(CombatSystem system, String path) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(new JsonReader(new FileReader(new File(system.getDataFolder().getPath()
                    + File.separator + path))), CombatEntity.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new CombatEntity();
        }
    }
    public void save(CombatSystem system, String name) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);
        File file = new File(system.getDataFolder().getPath() + File.separator
                + "mob_data" + File.separator + name + ".json");
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
                damage[0] + attributes.get(CombatAttribute.STRENGTH),
                damage[1] + attributes.get(CombatAttribute.STRENGTH)
        };
        return Roll.damageRoll(range);
    }
    public double getRangedDamageRoll() {
        Integer[] range = new Integer[] {
                rangedDamage[0] + attributes.get(CombatAttribute.AGILITY),
                rangedDamage[1] + attributes.get(CombatAttribute.AGILITY)
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
        return dodge + (0.0005 * attributes.get(CombatAttribute.AGILITY));
    }
    public double getPenetration() {
        return penetration + (0.0015 * attributes.get(CombatAttribute.TENACITY));
    }
}
