package de.verdox.mccreativelab.ai.builder;

//import org.bukkit.EntityActivity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public interface AIFactory {
    @NotNull BehaviorFactory getBehaviorFactory();
    @NotNull GoalFactory getGoalFactory();
    @NotNull <E extends LivingEntity> ActivityBuilder<E> createActivityBuilder(@NotNull Class<? extends E> type, @NotNull EntityActivity entityActivity);
    @NotNull <E extends LivingEntity> WeightedBehaviorsBuilder<E> createWeightedBehaviorsBuilder(@NotNull Class<? extends E> type);
}
