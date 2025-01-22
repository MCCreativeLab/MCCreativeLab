package de.verdox.mccreativelab;

import org.jetbrains.annotations.NotNull;

public final class MCCreativeLab{
    private static CreativeLab creativeLab;
    public static void setCreativeLab(@NotNull CreativeLab creativeLab) {
        if(MCCreativeLab.creativeLab != null)
            throw new UnsupportedOperationException("Cannot redefine singleton CreativeLab");

        MCCreativeLab.creativeLab = creativeLab;
    }
    @NotNull public static de.verdox.mccreativelab.data.DataPackInterceptor getDataPackInterceptor(){
        return creativeLab.getDataPackInterceptor();
    }

    @NotNull public static de.verdox.mccreativelab.data.VanillaRegistryManipulator getRegistryManipulator(){
        return creativeLab.getRegistryManipulator();
    }

    @NotNull public static de.verdox.mccreativelab.advancement.AdvancementBuilder createAdvancement(){
        return creativeLab.createAdvancement();
    }

    public static void setWorldHook(@NotNull org.bukkit.World world, @NotNull de.verdox.mccreativelab.behaviour.MCCWorldHook mccWorldHook){
        creativeLab.setWorldHook(world, mccWorldHook);
    }

    @NotNull
    public static de.verdox.mccreativelab.behaviour.MCCWorldHook getWorldHook(@NotNull org.bukkit.World world){
        return creativeLab.getWorldHook(world);
    }

    @NotNull
    public static InteractionResult placeBlockFromItemWithoutPlayer(@NotNull org.bukkit.inventory.ItemStack stack, @NotNull org.bukkit.Location location){
        return creativeLab.placeBlockFromItemWithoutPlayer(stack, location);
    }
}
