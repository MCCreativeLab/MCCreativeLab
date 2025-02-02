package de.verdox.mccreativelab.behavior.entity;

import de.verdox.mccreativelab.behavior.Behaviour;
import de.verdox.mccreativelab.behavior.BehaviourResult;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface EntityBehaviour<T extends Entity> extends Behaviour {
    /**
     * Gets if an {@link Entity} is fire immune
     * @param entity - The entity
     * @return - true if it is fire immune
     */
    @NotNull
    default BehaviourResult.Bool fireImmune(@NotNull T entity) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * Called on every tick of an {@link Entity}
     * @param entity - The entity
     * @return - nothing
     */
    @NotNull
    default BehaviourResult.Callback onTick(@NotNull T entity) {
        return BehaviourResult.Callback.DEFAULT_INSTANCE;
    }

    /**
     * Is called when an {@link Entity} is loaded
     * @param entity - The entity
     * @return - nothing
     */
    @NotNull
    default BehaviourResult.Callback readAdditionalSaveData(@NotNull T entity, @NotNull PersistentDataContainer persistentDataContainer) {
        return done();
    }

    /**
     * Is called when an {@link Entity} is saved
     * @param entity - The entity
     * @return - nothing
     */
    @NotNull
    default BehaviourResult.Callback addAdditionalSaveData(@NotNull T entity, @NotNull PersistentDataContainer persistentDataContainer) {
        return done();
    }

    /**
     * Is called to check if a given entity collides another entity
     * @param entity the entity
     * @param other the other entity
     * @return if it can collide
     */
    default BehaviourResult.Bool canCollideWith(T entity, T other) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * Is called to check if a given entity accepts passengers
     * @param entity the entity
     * @return if it accepts passengers
     */
    default BehaviourResult.Bool couldAcceptPassenger(T entity) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * Is called to check if a given entity is attackable
     * @param entity the entity
     * @return if it is attackable
     */
    default BehaviourResult.Bool isAttackable(T entity) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * Returns the max fall distance of the given entity
     * @param entity the entity
     * @return the max fall distance
     */
    default BehaviourResult.Object<Integer> getMaxFallDistance(T entity) {
        return BehaviourResult.Object.DEFAULT_INSTANCE;
    }

    /**
     * Is called when the entity is under the world
     * @param entity the entity
     * @return void
     */
    @NotNull
    default BehaviourResult.Void onBelowWorld(Entity entity) {
        return BehaviourResult.Void.DEFAULT_INSTANCE;
    }

    /**
     * Gets if an {@link Entity} can be leashed
     * @param entity - The entity
     * @return - if it can be leashed
     */
    @NotNull
    default BehaviourResult.Bool canBeLeashed(@NotNull T entity) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }
}
