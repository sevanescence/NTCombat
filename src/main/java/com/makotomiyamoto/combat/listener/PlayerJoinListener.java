package com.makotomiyamoto.combat.listener;

import com.makotomiyamoto.combat.CombatSystem;
import com.makotomiyamoto.combat.entity.CombatEntity;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public final class PlayerJoinListener implements Listener {

    private CombatSystem system;

    public PlayerJoinListener(CombatSystem system) {
        this.system = system;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String s = File.separator;
        String path = system.getDataFolder().getPath() + s + "player_data" + s
                + event.getPlayer().getUniqueId().toString();
        File file = new File(path);
        try {
            if (file.createNewFile()) {
                System.out.println(path + " created for " + event.getPlayer().getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String uuid = event.getPlayer().getUniqueId().toString();
        // create a default update checker and a StatsAlreadyWritten exception checker.
        new BukkitRunnable() {
            @Override
            public void run() {
                event.getPlayer().sendMessage(ChatColor.RED + "Setting player defaults...");
                @SuppressWarnings("unused")
                CombatEntity combatPlayer = new CombatEntity(system, uuid, true);
                System.out.println(uuid + ".json updated!");
                event.getPlayer().sendMessage(ChatColor.GREEN + "Player defaults set!");
            }
        }.runTaskAsynchronously(system);
    }

}
