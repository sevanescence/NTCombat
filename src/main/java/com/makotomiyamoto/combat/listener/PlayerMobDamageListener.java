package com.makotomiyamoto.combat.listener;

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
        if (!(event.getDamager() instanceof Player && !(event.getEntity() instanceof Player))) {
            return;
        }
        String mobName = event.getEntity().getCustomName();
        CombatEntity target;
        if (mobName == null) {
            target = CombatEntity.findDefaults(system, event.getEntity());
        } else {
            target = CombatEntity.findMob(system, mobName);
        }
    }

}
