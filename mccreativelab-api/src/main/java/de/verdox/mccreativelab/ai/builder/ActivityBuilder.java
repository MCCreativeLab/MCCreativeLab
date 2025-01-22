package de.verdox.mccreativelab.ai.builder;

import de.verdox.mccreativelab.ai.MemoryStatus;
import de.verdox.mccreativelab.ai.behavior.AIBehavior;
import de.verdox.mccreativelab.ai.behavior.ControlledBehavior;
import de.verdox.mccreativelab.ai.behavior.CustomAIBehavior;
import de.verdox.mccreativelab.ai.behavior.OneShotBehavior;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.memory.MemoryKey;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface ActivityBuilder<E extends LivingEntity> {

    @NotNull ActivityBuilder<E> withRequiredMemory(@NotNull MemoryKey<?> requiredMemoryKey, @NotNull MemoryStatus memoryStatus);

    @NotNull ActivityBuilder<E> withForgettingMemoriesWhenStopped(@NotNull MemoryKey<?> forgettingMemoryKey);

    @NotNull ActivityBuilder<E> withBehaviour(int priority, @NotNull ControlledBehavior<? super E> controlledBehavior);

    @NotNull ActivityBuilder<E> withBehaviour(int priority, @NotNull AIBehavior<? super E> aiBehavior);

    @NotNull ActivityBuilder<E> withBehaviour(int priority, @NotNull CustomAIBehavior<? super E> customAIBehavior);

    @NotNull ActivityBuilder<E> withBehaviour(int priority, @NotNull OneShotBehavior<? super E> oneShotBehavior);

    @NotNull ActivityBuilder<E> withBehaviour(int priority, @NotNull Function<BehaviorFactory, ControlledBehavior<? super E>> behaviourCreator);
}
