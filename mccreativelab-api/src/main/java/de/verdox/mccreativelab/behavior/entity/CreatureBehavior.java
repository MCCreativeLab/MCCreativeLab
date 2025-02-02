package de.verdox.mccreativelab.behavior.entity;

import de.verdox.mccreativelab.behavior.BehaviourResult;
import org.bukkit.entity.Creature;

public interface CreatureBehavior<T extends Creature> extends MobBehaviour<T> {
    /**
     * Is called to determine the leash following speed for a given entity
     * @param entity the entity
     * @return the leash following speed
     */
    default BehaviourResult.Object<Double> followLeashSpeed(Creature entity) {
        return BehaviourResult.Object.DEFAULT_INSTANCE;
    }
}
