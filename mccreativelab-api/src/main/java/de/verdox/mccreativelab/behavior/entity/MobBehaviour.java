package de.verdox.mccreativelab.behavior.entity;

import de.verdox.mccreativelab.InteractionResult;
import de.verdox.mccreativelab.behavior.BehaviourResult;
import org.bukkit.Material;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface MobBehaviour <T extends Mob> extends LivingEntityBehaviour<T> {
    /**
     * Is called when a {@link Player} interacts with a {@link Mob}
     * @param entity - The Entity
     * @param player - The Player
     * @param hand - The Interaction Hand
     * @return - An Interaction result
     */
    @NotNull
    default BehaviourResult.Object<InteractionResult> mobInteract(@NotNull T entity, @NotNull Player player, @NotNull EquipmentSlot hand){
        return BehaviourResult.Object.DEFAULT_INSTANCE;
    }

    /**
     * Gets if a {@link Mob} can fire projectile weapons
     * @param entity - The entity
     * @param weapon - The weapon material
     * @return - true if it can
     */
    @NotNull
    default BehaviourResult.Bool canFireProjectileWeapon(@NotNull T entity, @NotNull Material weapon) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * Called when an entity ate something
     * @param entity - The Entity
     * @return - nothing
     */
    @NotNull
    default BehaviourResult.Void ate(@NotNull T entity) {
        return BehaviourResult.Void.DEFAULT_INSTANCE;
    }

    /**
     * Gets if a {@link Mob} can hold an {@link ItemStack}
     * @param entity - The entity
     * @param stack - The ItemStack
     * @return - true if it can
     */
    @NotNull
    default BehaviourResult.Bool canHoldItem(@NotNull T entity, @NotNull ItemStack stack) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * Gets if a {@link Mob} can pickup an {@link ItemStack}
     * @param entity - The entity
     * @param stack - The ItemStack
     * @return - true if it can
     */
    @NotNull
    default BehaviourResult.Bool wantsToPickUp(@NotNull T entity, @NotNull ItemStack stack) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * Gets if a {@link Mob} should be removed when away a particular distance from players
     * @param entity - The entity
     * @param distanceSquared - The distance
     * @return - true if it should be removed
     */
    @NotNull
    default BehaviourResult.Bool removeWhenFarAway(@NotNull T entity, double distanceSquared) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * Gets if a {@link Mob} can be leashed by a {@link Player}
     * @param entity - The entity
     * @param player - The Player
     * @return - true if it can
     */
    @NotNull
    default BehaviourResult.Bool canBeLeashed(@NotNull T entity, @NotNull Player player) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }
}
