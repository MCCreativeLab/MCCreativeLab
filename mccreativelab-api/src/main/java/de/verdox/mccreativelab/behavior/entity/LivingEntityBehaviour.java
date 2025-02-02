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
     * Is called when an {@link LivingEntity} picks up an {@link Item}
     * @param entity - The Entity
     * @param item - The picked up Item
     * @return - nothing
     */
    @NotNull
    default BehaviourResult.Void onItemPickup(@NotNull T entity, @NotNull Item item) {
        return BehaviourResult.Void.DEFAULT_INSTANCE;
    }

    /**
     * Gets if an {@link LivingEntity} can attack an {@link Entity} with a particular {@link EntityType}
     * @param entityType - The entity type
     * @return - true if it can
     */
    @NotNull
    default BehaviourResult.Bool canAttackType(@NotNull EntityType entityType) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * Gets if an {@link LivingEntity} can attack an {@link Entity}
     * @param other - The entity
     * @return - true if it can
     */
    @NotNull
    default BehaviourResult.Bool canAttack(LivingEntity entity, LivingEntity other) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * Checks if an entity can be name tagged
     * @param entity the entity
     * @return if it can be name tagged
     */
    @NotNull
    default BehaviourResult.Bool canBeNameTagged(LivingEntity entity) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * Checks if an entity can pick up loot
     * @param entity the entity
     * @return if it can pick up loot
     */
    @NotNull
    default BehaviourResult.Bool canPickUpLoot(LivingEntity entity) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }
}
