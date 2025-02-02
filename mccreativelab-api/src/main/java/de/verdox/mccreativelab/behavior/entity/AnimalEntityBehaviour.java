package de.verdox.mccreativelab.behavior.entity;

import de.verdox.mccreativelab.behavior.BehaviourResult;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Animals;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface AnimalEntityBehaviour<T extends Animals> extends AgeableEntityBehaviour<T> {
    /**
     * Gets if an {@link Animals} can mate with another {@link Animals}
     * @param entity - The first animal
     * @param other - The other animal
     * @return - true if it can
     */
    @NotNull
    default BehaviourResult.Bool canMate(@NotNull T entity, @NotNull Animals other) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * Gets if an {@link org.bukkit.entity.Entity} would eat an {@link ItemStack}
     * @param entity - The Entity
     * @param stack - The ItemStack
     * @return - True if it can
     */
    @NotNull
    default BehaviourResult.Bool isFood(@NotNull T entity, @NotNull ItemStack stack) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }
}
