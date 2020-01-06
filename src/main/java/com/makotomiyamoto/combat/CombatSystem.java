package com.makotomiyamoto.combat;

import com.makotomiyamoto.combat.command.CombatCommand;
import com.makotomiyamoto.combat.entity.CombatEntity;
import com.makotomiyamoto.combat.listener.PlayerJoinListener;
import com.makotomiyamoto.combat.listener.PlayerMobDamageListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CombatSystem extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        if (new File(this.getDataFolder().getPath() + File.separator + "mob_data").mkdirs()) {
            System.out.println(this.getDataFolder().getPath() + File.separator + "mob_data created!");
        }
        if (new File(this.getDataFolder() + File.separator + "player_data").mkdirs()) {
            System.out.println(this.getDataFolder().getPath() + File.separator + "player_data created!");
        }
        System.out.println("Parsing custom mobs...");
        try {
            loadCustomMobs();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        //noinspection ConstantConditions (dw this is an IntelliJ thing)
        this.getCommand("ntc").setExecutor(new CombatCommand(this));
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerMobDamageListener(this), this);
    }

    public void loadCustomMobs() throws NullPointerException {
        String s = File.separator;
        File dir = new File(this.getDataFolder().getPath() + s + "custom_mobs");
        if (dir.mkdirs()) {
            System.out.println("custom_mobs directory created.");
        }
        File[] customMobs = dir.listFiles();
        assert customMobs != null;
        if (customMobs.length < 1) {
            System.out.println("No mobs in folder.");
            System.err.println("Having no default mob folders will render unmarked mobs instantly killable.");
            return;
        }
        FileConfiguration mob;
        for (File file : customMobs) {
            try {
                mob = YamlConfiguration.loadConfiguration(file);
                CombatEntity entity = new CombatEntity(mob);
                entity.save(this, file.getName().replaceAll(".yml", ""));
                System.out.println(file.getName().replaceAll("yml", "json") + " saved.");
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                System.err.println("To put it simply, you have a typo in one of your attributes in "
                        + file.getPath());
            }
        }
    }

}
