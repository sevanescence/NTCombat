# NTCombat
New repo because intellij decided to screw up the previous repo.

# Changelog
#### 1.0.1
Redo of the addFromItemStack() function which handles
adding player stats from equipped items.
##### bugs during 1.0.1 development
> Damage return was screwed.
>
> Cause:
> > com.makotomiyamoto.combat.entity.CombatEntity line 194: damage
> was redefined rather than added on the base value. This mistake affected
> every damage stat, and has since been resolved.