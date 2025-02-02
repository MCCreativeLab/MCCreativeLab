package de.verdox.mccreativelab.behaviour.entity;

import de.verdox.mccreativelab.behavior.entity.AnimalEntityBehaviour;
import de.verdox.mccreativelab.behaviour.BehaviourUtil;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import org.bukkit.entity.Animals;

public interface BehaviorAnimal extends BehaviorPathfinderMob {
    default boolean mcc$isFood(ItemStack stack) {
        var behaviour = BehaviorEntity.getBehaviour(asAnimal(), org.bukkit.entity.Animals.class, AnimalEntityBehaviour.class);
        return BehaviourUtil.evaluateBoolean(behaviour, entityBehavior -> entityBehavior.isFood((Animals) asAnimal().getBukkitLivingEntity(), stack.asBukkitMirror()), () -> asAnimal().isFood(stack));
    }

    default boolean mcc$canMate(Animal otherAnimal) {
        var behaviour = BehaviorEntity.getBehaviour(asAnimal(), org.bukkit.entity.Animals.class, AnimalEntityBehaviour.class);
        return BehaviourUtil.evaluateBoolean(behaviour, entityBehavior -> entityBehavior.canMate((Animals) asAnimal().getBukkitLivingEntity(), (Animals) otherAnimal.getBukkitLivingEntity()), () -> asAnimal().canMate(otherAnimal));
    }

    default Animal asAnimal() {
        return (Animal) asEntity();
    }
}
