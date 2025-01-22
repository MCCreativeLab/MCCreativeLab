package de.verdox.mccreativelab.ai.builder;

import de.verdox.mccreativelab.ai.behavior.AIBehavior;
import de.verdox.mccreativelab.ai.behavior.ControlledBehavior;
import de.verdox.mccreativelab.ai.behavior.CustomAIBehavior;
import de.verdox.mccreativelab.ai.behavior.OneShotBehavior;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface WeightedBehaviorsBuilder<E extends LivingEntity> {
    @NotNull WeightedBehaviorsBuilder<E> withBehaviour(int priority, @NotNull ControlledBehavior<? super E> aiBehavior);

    @NotNull WeightedBehaviorsBuilder<E> withBehaviour(int priority, @NotNull AIBehavior<? super E> customAiBehavior);

    @NotNull WeightedBehaviorsBuilder<E> withBehaviour(int priority, @NotNull CustomAIBehavior<? super E> customAiBehaviour);

    @NotNull WeightedBehaviorsBuilder<E> withBehaviour(int priority, @NotNull OneShotBehavior<? super E> aiBehavior);

    @NotNull WeightedBehaviorsBuilder<E> withBehaviour(int priority, @NotNull Function<BehaviorFactory, ControlledBehavior<? super E>> behaviourCreator);
}
