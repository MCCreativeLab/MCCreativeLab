package de.verdox.mccreativelab.ai.behavior;

import de.verdox.mccreativelab.ai.MemoryStatus;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.memory.MemoryKey;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class PaperCustomAIBehavior<E extends LivingEntity> implements CustomAIBehavior<E> {
    private final Class<? extends E> entityType;
    private final Map<MemoryKey<?>, MemoryStatus> requiredMemoryState;
    private final int minRunTime;
    private final int maxRunTime;

    public PaperCustomAIBehavior(@NotNull Class<? extends E> entityType, @NotNull Map<MemoryKey<?>, @NotNull MemoryStatus> requiredMemoryState, int minRunTime, int maxRunTime){
        this.entityType = entityType;
        this.requiredMemoryState = requiredMemoryState;
        this.minRunTime = minRunTime;
        this.maxRunTime = maxRunTime;
    }

    public PaperCustomAIBehavior(@NotNull Class<? extends E> entityType, @NotNull Map<MemoryKey<?>, MemoryStatus> requiredMemoryState) {
        this(entityType, requiredMemoryState, 60);
    }

    public PaperCustomAIBehavior(@NotNull Class<? extends E> entityType, Map<MemoryKey<?>, @NotNull MemoryStatus> requiredMemoryState, int runTime) {
        this(entityType, requiredMemoryState, runTime, runTime);
    }

    @Override
    public final int getMinDuration() {
        return minRunTime;
    }

    @Override
    public final int getMaxDuration() {
        return maxRunTime;
    }

    @Override
    public final @NotNull Map<MemoryKey<?>, MemoryStatus> getRequiredMemoryStates() {
        return new HashMap<>(requiredMemoryState);
    }

    @Override
    public final @NotNull Class<? extends E> getEntityType() {
        return entityType;
    }
}
