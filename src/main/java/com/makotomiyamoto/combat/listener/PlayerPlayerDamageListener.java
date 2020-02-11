package com.makotomiyamoto.combat.listener;

import com.makotomiyamoto.combat.CombatSystem;
import com.makotomiyamoto.combat.entity.CombatEntity;
import com.makotomiyamoto.combat.roll.Roll;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerPlayerDamageListener implements Listener {

    CombatSystem system;

    public PlayerPlayerDamageListener(CombatSystem system) {
        this.system = system;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }
        CombatEntity attacker = CombatEntity.getPlayer(system, (Player) event.getDamager());
        CombatEntity defender = CombatEntity.getPlayer(system, (Player) event.getEntity());
        double damage = attacker.getDamageRoll();
        int armor = defender.getArmor();
        if (Roll.percentChance(defender.getDodge())) {
            event.getDamager().sendMessage(ChatColor.RED + "Attacked dodged!");
            event.getEntity().sendMessage(ChatColor.GREEN + "Dodged!");
            event.setCancelled(true);
            return;
        }
        if (Roll.percentChance(attacker.getPenetration())) {
            armor /= 2;
            event.getDamager().sendMessage(ChatColor.GREEN + "Penetrated! 50% of the opponent's armor ignored.");
            event.getEntity().sendMessage(ChatColor.RED + "Penetrated! 50% of your armor is ignored.");
        }
        if (Roll.percentChance(attacker.getCriticalChance())) {
            damage *= (1 + attacker.getCriticalDamage());
            event.getDamager().sendMessage(ChatColor.GREEN + "Critical hit!");
            event.getEntity().sendMessage(ChatColor.RED + "Critical blow!");
        }
        // block parry
        if (Roll.percentChance(defender.getBlock())) {
            damage /= 2;
            event.getDamager().sendMessage(ChatColor.RED + "Attack blocked! 50% less damage dealt.");
            event.getEntity().sendMessage(ChatColor.GREEN + "Attack blocked! 50% less damage taken.");
        }
        if (Roll.percentChance(defender.getParry())) {
            damage /= 2;
            event.getDamager().sendMessage(ChatColor.RED + "Attack parried! 50% less damage dealt.");
            event.getEntity().sendMessage(ChatColor.GREEN + "Attack parried! 50% less damage taken.");
        }
        event.setDamage(damage);
    }

}
