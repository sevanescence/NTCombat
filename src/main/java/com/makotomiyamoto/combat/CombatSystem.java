package com.makotomiyamoto.combat;

import com.makotomiyamoto.combat.command.CombatCommand;
import com.makotomiyamoto.combat.entity.CombatEntity;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CombatSystem extends JavaPlugin {

    @Override
    public void onEnable() {
        if (new File(this.getDataFolder().getPath() + File.separator + "mob_data").mkdirs()) {
            System.out.println(this.getDataFolder().getPath() + File.separator + "mob_data created!");
        }
        System.out.println("Parsing custom mobs...");
        try {
            loadCustomMobs();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        //noinspection ConstantConditions dw this is an IntelliJ thing
        this.getCommand("ntc").setExecutor(new CombatCommand(this));
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
            mob = YamlConfiguration.loadConfiguration(file);
            CombatEntity entity = new CombatEntity(mob);
            entity.save(this, file.getName());
            System.out.println(file.getName() + " saved.");
        }
    }

}
