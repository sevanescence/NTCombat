package com.makotomiyamoto.combat.listener;

import com.google.gson.Gson;
import com.makotomiyamoto.combat.CombatSystem;
import com.makotomiyamoto.combat.data.ArrowMeta;
import com.makotomiyamoto.combat.entity.CombatEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.metadata.MetadataValue;

public final class PlayerFireBowListener implements Listener {

    CombatSystem system;

    public PlayerFireBowListener(CombatSystem system) {
        this.system = system;
    }

    @EventHandler
    public void onFire(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        String uuid = event.getEntity().getUniqueId().toString();
        event.getProjectile().setMetadata("owner", new ArrowMeta(system, uuid));
        //System.out.println(event.getProjectile().getMetadata("owner").get(0).value());
        //System.out.println(new Gson().toJson(CombatEntity.getPlayer(system, (Player) event.getEntity())));
    }

}
