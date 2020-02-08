package com.makotomiyamoto.combat.listener;

import com.google.gson.Gson;
import com.makotomiyamoto.combat.CombatSystem;
import com.makotomiyamoto.combat.entity.CombatEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public class PlayerArrowHitMobListener implements Listener {

    CombatSystem system;

    public PlayerArrowHitMobListener(CombatSystem system) {
        this.system = system;
    }

    @SuppressWarnings("ConstantConditions")
    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        if (event.getEntity() instanceof Player) {
            return;
        }
        System.out.println("[]====================[ ARROW HIT MOB EVENT ]====================[]");
        System.out.println(event.getDamager().getMetadata("owner").get(0).value());
        Player player = Bukkit.getPlayer(UUID.fromString((String) event.getDamager().getMetadata("owner").get(0).value()));
        CombatEntity playerInstance = CombatEntity.getPlayer(system, player);
        String name = event.getEntity().getCustomName();
        CombatEntity target;
        if (name == null) {
            target = CombatEntity.findDefaults(system, event.getEntity());
        } else {
            target = CombatEntity.findMob(system, event.getEntity().getCustomName());
        }
        System.out.println("PLAYER => " + new Gson().toJson(playerInstance));
        System.out.println("ENTITY => " + new Gson().toJson(target));
        double damage = playerInstance.getRangedDamageRoll();
        int armor = target.getArmor();
        damage /= armor;
        event.setDamage(damage);
        System.out.println("Final damage: " + damage);
        System.out.println("Remaining health of target mob: " + (((LivingEntity) event.getEntity()).getHealth() - damage));
        System.out.println("[]====================[ ARROW EVENT DONE :D ]====================[]");
    }

}
