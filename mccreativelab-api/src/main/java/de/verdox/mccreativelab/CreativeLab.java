package de.verdox.mccreativelab;

import de.verdox.mccreativelab.behavior.MCCWorldHook;
import de.verdox.mccreativelab.container.CustomInventory;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public interface CreativeLab {
    /**
     * Returns the {@link de.verdox.mccreativelab.data.DataPackInterceptor}
     * @return the data pack interceptor
     */
    @NotNull de.verdox.mccreativelab.data.DataPackInterceptor getDataPackInterceptor();

    /**
     * Returns the {@link de.verdox.mccreativelab.data.VanillaRegistryManipulator}
     * @return the vanilla registry manipulator
     */
    @NotNull de.verdox.mccreativelab.data.VanillaRegistryManipulator getRegistryManipulator();

    /**
     * Returns the {@link @NotNull de.verdox.mccreativelab.advancement.AdvancementBuilder}
     * @return the advancement builder
     */
    @NotNull de.verdox.mccreativelab.advancement.AdvancementBuilder createAdvancement();

    @NotNull Inventory openCustomContainerMenu(@NotNull CustomInventory customInventory, @NotNull org.bukkit.entity.Player player, @NotNull net.kyori.adventure.text.Component title);

    /**
     * Sets the world hook of a particular world
     * @param world the world
     * @param mccWorldHook the world hook
     */
    void setWorldHook(@NotNull org.bukkit.World world, @NotNull de.verdox.mccreativelab.behaviour.MCCWorldHook mccWorldHook);

    /**
     * Gets the world hook of a particular world
     * @param world the world
     * @return the world hook of the world if available else null
     */
    @NotNull MCCWorldHook getWorldHook(@NotNull org.bukkit.World world);

    /**
     * Places a block from a specified {@link org.bukkit.inventory.ItemStack} at {@link org.bukkit.Location}.
     * This method will NOT call a {@link org.bukkit.event.block.BlockPlaceEvent} but will call a {@link org.bukkit.event.block.BlockCanBuildEvent}.
     *
     * @param stack The item
     * @param location the location
     * @return the result of the action
     */
    @NotNull InteractionResult placeBlockFromItemWithoutPlayer(@NotNull org.bukkit.inventory.ItemStack stack, @NotNull org.bukkit.Location location);

}
