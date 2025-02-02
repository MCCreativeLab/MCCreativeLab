package de.verdox.mccreativelab.behaviour.entity;

import de.verdox.mccreativelab.behavior.entity.CreatureBehavior;
import de.verdox.mccreativelab.behaviour.BehaviourUtil;
import net.minecraft.world.entity.PathfinderMob;

public interface BehaviorPathfinderMob extends BehaviorMob {
    default double mcc$followLeashSpeed() {
        var behaviour = BehaviorEntity.getBehaviour(asPathfinderMob(), org.bukkit.entity.Creature.class, CreatureBehavior.class);
        return BehaviourUtil.evaluateDouble(behaviour, entityBehavior -> entityBehavior.followLeashSpeed((org.bukkit.entity.Creature) asPathfinderMob().getBukkitLivingEntity()), () -> asPathfinderMob().followLeashSpeed());
    }

    default PathfinderMob asPathfinderMob(){
        return (PathfinderMob) asEntity();
    }
}
