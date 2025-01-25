package de.verdox.mccreativelab.ai.builder;

import org.bukkit.EntityActivity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class CraftAIFactory implements AIFactory {

    private static final BehaviorFactory BEHAVIOR_FACTORY = new CraftBehaviorFactory();
    private static final GoalFactory GOAL_FACTORY = new CraftGoalFactory();
    @Override
    public @NotNull BehaviorFactory getBehaviorFactory() {
        return BEHAVIOR_FACTORY;
    }

    @Override
    public @NotNull GoalFactory getGoalFactory() {
        return GOAL_FACTORY;
    }

    @Override
    public <E extends LivingEntity> @NotNull ActivityBuilder<E> createActivityBuilder(@NotNull Class<? extends E> type, @NotNull EntityActivity entityActivity) {
        return new CraftActivityBuilder<>(entityActivity);
    }

    @Override
    public <E extends LivingEntity> @NotNull WeightedBehaviorsBuilder<E> createWeightedBehaviorsBuilder(@NotNull Class<? extends E> type) {
        return new CraftWeightedBehaviorsBuilder<>();
    }


}
