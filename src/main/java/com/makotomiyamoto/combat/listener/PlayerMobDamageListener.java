package com.makotomiyamoto.combat.listener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.makotomiyamoto.combat.CombatSystem;
import com.makotomiyamoto.combat.entity.CombatEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerMobDamageListener implements Listener {

    private CombatSystem system;

    public PlayerMobDamageListener(CombatSystem system) {
        this.system = system;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        // TODO (after this class) PlayerRangedMobDamageListener
        if (!(event.getDamager() instanceof Player && !(event.getEntity() instanceof Player))) {
            return;
        }
        System.out.println("PlayerMobDamageEvent fired");
        String mobName = event.getEntity().getCustomName();
        CombatEntity target;
        if (mobName == null) {
            target = CombatEntity.findDefaults(system, event.getEntity());
            System.out.println("Target does not have a name.");
        } else {
            target = CombatEntity.findMob(system, mobName);
            System.out.println("Target has name: " + mobName);
        }
        System.out.println(new GsonBuilder().create().toJson(target));
        CombatEntity player = CombatEntity.getPlayer(system, (Player)event.getDamager());
        System.out.println(new GsonBuilder().create().toJson(player));
    }

}
