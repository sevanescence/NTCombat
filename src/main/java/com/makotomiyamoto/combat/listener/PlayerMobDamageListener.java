package com.makotomiyamoto.combat.listener;

import com.google.gson.GsonBuilder;
import com.makotomiyamoto.combat.CombatSystem;
import com.makotomiyamoto.combat.entity.CombatEntity;
import com.makotomiyamoto.combat.roll.Roll;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.text.DecimalFormat;

public class PlayerMobDamageListener implements Listener {

    private CombatSystem system;

    public PlayerMobDamageListener(CombatSystem system) {
        this.system = system;
    }
    
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        // TODO (after this class) PlayerRangedMobDamageListener
        // TODO ignore shield blocking in player combat
        // TODO fix null error when smacking a bitch with a shield
        // TODO overall just fucking fix the garbage lore-checking crap
        if (!(event.getDamager() instanceof Player && !(event.getEntity() instanceof Player))) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity)) {
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
        int mobArmor = target.getArmor();
        // TODO in v1.0.3, put all of the rolls in the CombatEntity class
        //  i.e. target.tryArmor(event.getDamager(), event.getEntity(), (CombatEntity) player)
        if (Roll.percentChance(target.getDodge())) {
            event.setCancelled(true);
            // play dodge sound effect for both mob and player
            // knock mob to the side half a block. make sure to check for ledges.
            System.out.println("Dodge success!");
            event.getDamager().sendMessage(ChatColor.RED + "Dodged!");
            return;
        }
        if (Roll.percentChance(target.getPenetration())) {
            mobArmor /= 2;
            // play penetration effect for both mob and player
            System.out.println("Penetration success! Armor cut in half.");
        }
        DecimalFormat format = new DecimalFormat("##.00");
        event.getDamager().sendMessage(ChatColor.GREEN + "Hit!");
        System.out.println("mobArmor = " + mobArmor);
        double playerDamage = player.getDamageRoll();
        System.out.println("Initial playerDamage = " + format.format(playerDamage));
        playerDamage /= mobArmor;
        System.out.println("Post-armor damage: " + format.format(playerDamage));
        if (Roll.percentChance(player.getCriticalChance())) {
            playerDamage *= (1 + player.getCriticalDamage());
            System.out.println("Critical successful! Damage: " + format.format(playerDamage));
        }
        if (Roll.percentChance(target.getBlock())) {
            playerDamage /= 2;
            System.out.println("Block successful! Damage: " + format.format(playerDamage));
        }
        if (Roll.percentChance(target.getParry())) {
            playerDamage /= 2;
            System.out.println("Parry successful! Damage: " + format.format(playerDamage));
        }
        System.out.println("Final damage: " + format.format(playerDamage));
        event.setDamage(playerDamage);
        LivingEntity t = (LivingEntity) event.getEntity();
        System.out.println("Target's remaining health: " + format.format(t.getHealth() - playerDamage));
    }

}
