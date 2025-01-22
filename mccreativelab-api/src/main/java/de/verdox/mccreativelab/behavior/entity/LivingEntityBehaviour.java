package de.verdox.mccreativelab.behavior.entity;

import de.verdox.mccreativelab.behavior.BehaviourResult;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public interface LivingEntityBehaviour<T extends LivingEntity> extends EntityBehaviour<T> {
    /**
     * Gets if an {@link LivingEntity} is sensitive to water
     * @param entity - The entity
     * @return - true if it is
     */
    @NotNull
    default BehaviourResult.Bool isSensitiveToWater(@NotNull T entity) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * Gets the water-damage of an {@link LivingEntity} applied when it is sensitive to water
     * @param entity - The entity
     * @return - The water damage
     */
    @NotNull
    default BehaviourResult.Object<Float> waterDamage(@NotNull T entity) {
        return BehaviourResult.Object.DEFAULT_INSTANCE;
    }

    /**
     * Is called when an {@link LivingEntity} picks up an {@link Item}
     * @param entity - The Entity
     * @param item - The picked up Item
     * @return - nothing
     */
    @NotNull
    default BehaviourResult.Void onItemPickup(@NotNull T entity, @NotNull Item item){
        return BehaviourResult.Void.DEFAULT_INSTANCE;
    }

    /**
     * Gets if an {@link LivingEntity} can disable shields
     * @param entity - The entity
     * @return - true if it can
     */
    @NotNull
    default BehaviourResult.Bool canDisableShield(@NotNull T entity) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * Gets if an {@link LivingEntity} can attack an {@link Entity} with a particular {@link EntityType}
     * @param entity - The entity
     * @return - true if it can
     */
    @NotNull
    default BehaviourResult.Bool canAttackType(@NotNull T entity, @NotNull EntityType entityType) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }
}
