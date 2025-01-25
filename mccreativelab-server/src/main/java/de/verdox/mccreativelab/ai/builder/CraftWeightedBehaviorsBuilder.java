package de.verdox.mccreativelab.ai.builder;

import com.mojang.datafixers.util.Pair;
import de.verdox.mccreativelab.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class CraftWeightedBehaviorsBuilder<E extends LivingEntity> implements WeightedBehaviorsBuilder<E> {
    public final List<Pair<Integer, BehaviorControl<? extends net.minecraft.world.entity.LivingEntity>>> behaviours = new LinkedList<>();
    @Override
    public @NotNull WeightedBehaviorsBuilder<E> withBehaviour(int priority, @NotNull ControlledBehavior<? super E> aiBehavior) {
        if (aiBehavior instanceof CustomAIBehavior<?> customAiBehaviour1)
            return withBehaviour(priority, (CustomAIBehavior<E>) customAiBehaviour1);
        else if (aiBehavior instanceof OneShotBehavior<?> oneShotBehavior)
            return withBehaviour(priority, (OneShotBehavior<? super E>) oneShotBehavior);
        if (aiBehavior instanceof AIBehavior<?> aiBehaviour)
            return withBehaviour(priority, (AIBehavior<? super E>) aiBehaviour);
        if (aiBehavior instanceof CraftControlledBehavior<?, ?> craftControlledBehavior) {
            this.behaviours.add(Pair.of(priority, craftControlledBehavior.getHandle()));
            return this;
        } else throw new IllegalArgumentException("Unknown ControlledBehaviour implementation " + aiBehavior.getClass()
                                                                                                            .getName());
    }

    @Override
    public @NotNull WeightedBehaviorsBuilder<E> withBehaviour(int priority, @NotNull AIBehavior<? super E> customAiBehavior) {
        if (customAiBehavior instanceof CraftAIBehavior<?> craftAIBehavior)
            this.behaviours.add(Pair.of(priority, craftAIBehavior.getHandle()));
        else
            throw new IllegalArgumentException("You may not use your own implementation of AIBehavior. Consider using CustomAIBehaviour");
        return this;
    }

    @Override
    public @NotNull WeightedBehaviorsBuilder<E> withBehaviour(int priority, @NotNull CustomAIBehavior<? super E> customAiBehaviour) {
        Behavior<net.minecraft.world.entity.LivingEntity> behavior = new WrappedPaperAIBehavior<>(customAiBehaviour);
        this.behaviours.add(Pair.of(priority, behavior));
        return this;
    }

    @Override
    public @NotNull WeightedBehaviorsBuilder<E> withBehaviour(int priority, @NotNull OneShotBehavior<? super E> aiBehavior) {
        if (aiBehavior instanceof CraftOneShotBehavior<?> craftOneShotBehaviour)
            this.behaviours.add(Pair.of(priority, craftOneShotBehaviour.getHandle()));
        else
            throw new IllegalArgumentException("You may not use your own implementation of OneShotBehavior. Consider using CustomAIBehaviour");
        return this;
    }

    @Override
    public @NotNull WeightedBehaviorsBuilder<E> withBehaviour(int priority, @NotNull Function<BehaviorFactory, ControlledBehavior<? super E>> behaviourCreator) {
        return withBehaviour(priority, behaviourCreator.apply(Bukkit.getAIFactory().getBehaviorFactory()));
    }
}
