package com.makotomiyamoto.combat.listener;

import com.makotomiyamoto.combat.CombatSystem;
import com.makotomiyamoto.combat.data.ArrowMeta;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public final class EntityFireBowListener implements Listener {

    CombatSystem system;

    public EntityFireBowListener(CombatSystem system) {
        this.system = system;
    }

    @EventHandler
    public void onFire(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            return;
        }
        ArrowMeta<LivingEntity> meta = new ArrowMeta<>(system, event.getEntity());
        event.getProjectile().setMetadata("owner", meta);
        //System.out.println(event.getProjectile().getMetadata("owner").get(0).value());
        //System.out.println(new Gson().toJson(CombatEntity.findDefaults(system, event.getEntity())));
    }

}
