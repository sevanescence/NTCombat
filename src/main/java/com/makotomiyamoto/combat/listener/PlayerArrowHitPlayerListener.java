package com.makotomiyamoto.combat.listener;

import com.google.gson.Gson;
import com.makotomiyamoto.combat.CombatSystem;
import com.makotomiyamoto.combat.entity.CombatEntity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public class PlayerArrowHitPlayerListener implements Listener {

    CombatSystem system;

    public PlayerArrowHitPlayerListener(CombatSystem system) {
        this.system = system;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow) || !(event.getEntity() instanceof Player)) {
            return;
        }
        String owner = (String) event.getDamager().getMetadata("owner").get(0).value();
        if (owner == null) {
            return;
        }
        Player attacker = Bukkit.getPlayer(UUID.fromString(owner));
        if (attacker == null) {
            return;
        }
        CombatEntity attackerInstance = CombatEntity.getPlayer(system, attacker);
        CombatEntity defender = CombatEntity.getPlayer(system, (Player) event.getEntity());
        double damage = attackerInstance.getRangedDamageRoll();
        int armor = defender.getArmor();
        event.setDamage(damage / armor);
        System.out.println("[]====================[ DEBUG FOR PVP ]====================[]");
        System.out.println("ATTACKER => " + new Gson().toJson(attackerInstance));
        System.out.println("DEFENDER => " + new Gson().toJson(defender));
        attacker.sendMessage(ChatColor.GREEN + "You dealt " + event.getDamage() + " damage!");
        event.getEntity().sendMessage(ChatColor.RED + "You have been dealt " + event.getDamage() + " damage!");
        System.out.println("[]====================[ END OF DEBUG! ]====================[]");

    }

}
