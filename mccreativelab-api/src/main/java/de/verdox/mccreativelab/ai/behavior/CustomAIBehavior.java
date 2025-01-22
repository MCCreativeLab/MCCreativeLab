package de.verdox.mccreativelab.ai.behavior;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a player based implementation of a behaviour
 * @param <E> - The entity type
 */
public interface CustomAIBehavior<E extends LivingEntity> extends AIBehavior<E> {

    void start(@NotNull World world, @NotNull E entity, long time);
    void tick(@NotNull World world, @NotNull E entity, long time);

    void stop(@NotNull World world, @NotNull E entity, long time);

    boolean canStillUse(@NotNull World world, @NotNull E entity, long time);

    boolean checkExtraStartConditions(@NotNull World world, @NotNull E entity, long time);
    @NotNull Class<? extends E> getEntityType();

    enum Status {
        STOPPED,
        RUNNING,
    }
}
