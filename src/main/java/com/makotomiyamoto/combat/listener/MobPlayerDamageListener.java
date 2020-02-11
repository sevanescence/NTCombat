package com.makotomiyamoto.combat.listener;

import com.google.gson.GsonBuilder;
import com.makotomiyamoto.combat.CombatSystem;
import com.makotomiyamoto.combat.entity.CombatEntity;
import com.makotomiyamoto.combat.roll.Roll;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.text.DecimalFormat;

public final class MobPlayerDamageListener implements Listener {

    CombatSystem system;

    public MobPlayerDamageListener(CombatSystem system) {
        this.system = system;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if ((event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }
        if (event.getDamager() instanceof Arrow || event.getDamager() instanceof Firework) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity)) {
            System.out.println("entity beating you is not a livingentity. wtf?");
            return;
        }
        System.out.println("[]====================[ MOB SHITS ON PLAYER EVENT ]====================[]");
        CombatEntity player = CombatEntity.getPlayer(system, (Player) event.getEntity()),
        entity = CombatEntity.findMob(system, event.getDamager().getCustomName());
        System.out.println("PLAYER => " + new GsonBuilder().create().toJson(player));
        System.out.println("ENTITY => " + new GsonBuilder().create().toJson(entity));
        int armor = player.getArmor();
        double damage = entity.getDamageRoll();
        if (Roll.percentChance(player.getDodge())) {
            System.out.println("Player successfully dodged the attack!");
            event.getEntity().sendMessage(ChatColor.GREEN + "Dodged!");
            event.setCancelled(true);
        }
        if (Roll.percentChance(entity.getPenetration())) {
            armor /= 2;
            System.out.println("Mob penetrated and ignored half of the player's armor!");
            event.getEntity().sendMessage(ChatColor.RED + "Penetration! Armor effectiveness cut in half.");
        }
        if (Roll.percentChance(entity.getCriticalChance())) {
            damage *= (1 + entity.getCriticalDamage());
            System.out.println("Mob deals a critical strike on the player!");
            event.getEntity().sendMessage(ChatColor.RED + "Opponent dealt critical strike!");
        }
        if (Roll.percentChance(player.getBlock())) {
            damage /= 2;
            System.out.println("Player successfully blocks mob's attack, damage cut in half!");
            event.getEntity().sendMessage(ChatColor.GREEN + "Blocked!");
        }
        if (Roll.percentChance(player.getParry())) {
            damage /= 2;
            System.out.println("Player successfully parries mob's attack! Damage cut in half!");
            event.getEntity().sendMessage(ChatColor.GREEN + "Parried!");
        }
        event.setDamage(damage);
        System.out.println("Final damage: " + new DecimalFormat("##.00").format(damage));
        LivingEntity t = (LivingEntity) event.getEntity();
        System.out.println("Player's remaining health: " + new DecimalFormat("##.00").format(t.getHealth() - damage));
        System.out.println("[]====================[ END OF MOB ON PLAYER SHIT ]====================[]");
    }

}
