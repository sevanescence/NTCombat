package com.makotomiyamoto.combat.listener;

import com.google.gson.Gson;
import com.makotomiyamoto.combat.CombatSystem;
import com.makotomiyamoto.combat.entity.CombatEntity;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public final class MobArrowHitPlayerListener implements Listener {

    CombatSystem system;

    public MobArrowHitPlayerListener(CombatSystem system) {
        this.system = system;
    }

    @SuppressWarnings("ConstantConditions")
    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow) || !(event.getEntity() instanceof Player)) {
            return;
        }
        if (!(event.getDamager().getMetadata("owner").get(0).value() instanceof LivingEntity)) {
            return;
        }
        System.out.println("[]====================[ ENTITY ARROW EVENT ]====================[]");
        LivingEntity entity = (LivingEntity) event.getDamager().getMetadata("owner").get(0).value();
        String name = entity.getCustomName();
        CombatEntity mobInstance;
        if (name == null) {
            mobInstance = CombatEntity.findDefaults(system, entity);
        } else {
            mobInstance = CombatEntity.findMob(system, name);
        }
        CombatEntity player = CombatEntity.getPlayer(system, (Player) event.getEntity());
        System.out.println("ATTACKER => " + new Gson().toJson(mobInstance));
        System.out.println("DEFENDER => " + new Gson().toJson(player));
        double damage = mobInstance.getRangedDamageRoll();
        int armor = player.getArmor();
        event.setDamage(damage / armor);
        System.out.println("[]====================[ END OF THE DEBUG ! ]====================[]");
    }

}
