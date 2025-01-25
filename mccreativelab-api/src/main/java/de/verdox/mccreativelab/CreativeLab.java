package de.verdox.mccreativelab;

import de.verdox.mccreativelab.advancement.AdvancementBuilder;
import de.verdox.mccreativelab.behavior.MCCWorldHook;
import de.verdox.mccreativelab.container.CustomInventory;
import de.verdox.mccreativelab.data.DataPackInterceptor;
import de.verdox.mccreativelab.data.VanillaRegistryManipulator;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface CreativeLab {
    /**
     * Returns the {@link DataPackInterceptor}
     * @return the data pack interceptor
     */
    @NotNull DataPackInterceptor getDataPackInterceptor();

    /**
     * Returns the {@link VanillaRegistryManipulator}
     * @return the vanilla registry manipulator
     */
    @NotNull VanillaRegistryManipulator getRegistryManipulator();

    /**
     * Returns the {@link @NotNull AdvancementBuilder}
     * @return the advancement builder
     */
    @NotNull AdvancementBuilder createAdvancement();

    @NotNull Inventory openCustomContainerMenu(@NotNull CustomInventory customInventory, @NotNull Player player, @NotNull net.kyori.adventure.text.Component title);

    /**
     * Sets the world hook of a particular world
     * @param world the world
     * @param mccWorldHook the world hook
     */
    void setWorldHook(@NotNull org.bukkit.World world, @NotNull MCCWorldHook mccWorldHook);

    /**
     * Gets the world hook of a particular world
     * @param world the world
     * @return the world hook of the world if available else null
     */
    @NotNull MCCWorldHook getWorldHook(@NotNull org.bukkit.World world);

    /**
     * Places a block from a specified {@link ItemStack} at {@link org.bukkit.Location}.
     * This method will NOT call a {@link org.bukkit.event.block.BlockPlaceEvent} but will call a {@link org.bukkit.event.block.BlockCanBuildEvent}.
     *
     * @param stack The item
     * @param location the location
     * @return the result of the action
     */
    @NotNull InteractionResult placeBlockFromItemWithoutPlayer(@NotNull ItemStack stack, @NotNull Location location);

}
