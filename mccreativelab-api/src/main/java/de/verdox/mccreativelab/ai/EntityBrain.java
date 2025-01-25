package de.verdox.mccreativelab.ai;

import de.verdox.mccreativelab.ai.builder.ActivityBuilder;
import de.verdox.mccreativelab.ai.builder.BehaviorFactory;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.memory.MemoryKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public interface EntityBrain<E extends LivingEntity> {
/*
    @NotNull BehaviorFactory getBehaviorFactory();

    //@NotNull ActivityBuilder<E> createActivityBuilder(@NotNull EntityActivity entityActivity);

    void tick(@NotNull World world, @NotNull E entity);

    <U> void addMemoryToBrain(@NotNull MemoryKey<U> memoryKey);

    <U> void eraseMemory(@NotNull MemoryKey<U> memoryKey);

    <U> void setMemory(@NotNull MemoryKey<U> type, @Nullable U value);

    <U> void setMemoryWithExpiry(@NotNull MemoryKey<U> type, @Nullable U value, long expiry);

    @NotNull <U> Optional<U> getMemory(@NotNull MemoryKey<U> type);

    <U> long getTimeUntilExpiry(@NotNull MemoryKey<U> type);

    <U> boolean isMemoryValue(@NotNull MemoryKey<U> type, @NotNull U value);

    boolean checkMemory(@NotNull MemoryKey<?> type, @NotNull MemoryStatus state);

    @NotNull EntitySchedule getSchedule();

    void setSchedule(@NotNull EntitySchedule schedule);

    void setCoreActivities(@NotNull Set<EntityActivity> coreActivities);

    void useDefaultActivity();

    @NotNull Optional<EntityActivity> getActiveNonCoreActivity();

    void setActiveActivityIfPossible(@NotNull EntityActivity activity);

    void updateActivityFromSchedule(long timeOfDay, long time);

    void setActiveActivityToFirstValid(@NotNull List<EntityActivity> activities);

    void setDefaultActivity(@NotNull EntityActivity activity);

    void addActivity(@NotNull ActivityBuilder<E> activityBuilder, boolean replaceCompleteActivity, boolean replaceActivityRequirements, boolean replaceForgettingMemories);

    default void addActivity(@NotNull ActivityBuilder<E> activityBuilder, boolean replaceCompleteActivity, boolean replaceActivityRequirements) {
        addActivity(activityBuilder, replaceCompleteActivity, replaceActivityRequirements, false);
    }

    default void addActivity(@NotNull ActivityBuilder<E> activityBuilder, boolean replaceCompleteActivity) {
        addActivity(activityBuilder, replaceCompleteActivity, false, false);
    }

    default void addActivity(@NotNull ActivityBuilder<E> activityBuilder) {
        addActivity(activityBuilder, true, false, false);
    }

    void addActivity(@NotNull EntityActivity activity, @NotNull Consumer<ActivityBuilder<E>> activityBuilder, boolean replaceCompleteActivity, boolean replaceActivityRequirements, boolean replaceForgettingMemories);

    default void addActivity(@NotNull EntityActivity activity, @NotNull Consumer<ActivityBuilder<E>> activityBuilder, boolean replaceCompleteActivity, boolean replaceActivityRequirements) {
        addActivity(activity, activityBuilder, replaceCompleteActivity, replaceActivityRequirements, false);
    }

    default void addActivity(@NotNull EntityActivity activity, @NotNull Consumer<ActivityBuilder<E>> activityBuilder, boolean replaceCompleteActivity) {
        addActivity(activity, activityBuilder, replaceCompleteActivity, false, false);
    }

    default void addActivity(@NotNull EntityActivity activity, @NotNull Consumer<ActivityBuilder<E>> activityBuilder) {
        addActivity(activity, activityBuilder, true, false, false);
    }

    boolean isActive(@NotNull EntityActivity activity);

    @NotNull EntityBrain<E> copyWithoutBehaviours();

    void stopAll(@NotNull World world, @NotNull E entity);
*/

}
