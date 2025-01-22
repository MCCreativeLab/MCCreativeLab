package de.verdox.mccreativelab.container;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface CustomInventory {

    @NotNull Slot[] getSlots();
    @NotNull ItemStack quickMoveStack(@NotNull Player player, int slot);

    interface Slot {
        default void onQuickCraft(@NotNull ItemStack stack, int amount) {
        }

        default void onSwapCraft(int amount) {
        }

        default void onTake(@NotNull Player player, @NotNull ItemStack stack) {
        }

        default boolean mayPlace(@NotNull ItemStack stack) {
            return true;
        }

        default int getMaxStackSize() {
            return 64;
        }

        default int getMaxStackSize(@NotNull ItemStack stack) {
            return Math.min(getMaxStackSize(), stack.getMaxStackSize());
        }

        default boolean isActive() {
            return true;
        }

        default boolean mayPickUp(@NotNull Player player){
            return true;
        }
    }
}
