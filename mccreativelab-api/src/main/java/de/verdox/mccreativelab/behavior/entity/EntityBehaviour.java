package de.verdox.mccreativelab.behavior.entity;

import de.verdox.mccreativelab.behavior.Behaviour;
import de.verdox.mccreativelab.behavior.BehaviourResult;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
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
     * Gets if an {@link Entity} ignores a particular explosion
     * @param entity - The entity
     * @return - true if it ignores the explosion
     */
    @NotNull
    default BehaviourResult.Bool ignoreExplosion(@NotNull T entity, @NotNull Location explosionLocation, float radius, @Nullable Entity source, boolean explosionHasFire, @NotNull Map<Player, Vector> hitPlayers, @NotNull List<Location> hitBlocks) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * Called on every tick of an {@link Entity}
     * @param entity - The entity
     * @return - nothing
     */
    @NotNull
    default BehaviourResult.Callback onTick(@NotNull T entity){
        return BehaviourResult.Callback.DEFAULT_INSTANCE;
    }

    /**
     * Gets if an {@link Entity} can change dimensions
     * @param entity - The entity
     * @return - true if it can change dimensions
     */
    @NotNull
    default BehaviourResult.Bool canChangeDimensions(@NotNull T entity) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * Is called when an {@link Entity} is loaded
     * @param entity - The entity
     * @return - nothing
     */
    @NotNull
    default BehaviourResult.Callback readAdditionalSaveData(@NotNull T entity, @NotNull PersistentDataContainer persistentDataContainer){
        return done();
    }

    /**
     * Is called when an {@link Entity} is saved
     * @param entity - The entity
     * @return - nothing
     */
    @NotNull
    default BehaviourResult.Callback addAdditionalSaveData(@NotNull T entity, @NotNull PersistentDataContainer persistentDataContainer){
        return done();
    }
}
