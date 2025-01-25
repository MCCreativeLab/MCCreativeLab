package de.verdox.mccreativelab;

import de.verdox.mccreativelab.behavior.MCCWorldHook;
import de.verdox.mccreativelab.behaviour.BehaviourUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class CreativeLabImpl implements CreativeLab {

    private final de.verdox.mccreativelab.data.DataPackInterceptor dataPackInterceptor = new CraftDataPackInterceptor();
    @Override
    public de.verdox.mccreativelab.data.@NotNull DataPackInterceptor getDataPackInterceptor() {
        return dataPackInterceptor;
    }

    private final de.verdox.mccreativelab.data.VanillaRegistryManipulator vanillaRegistryManipulator = new de.verdox.mccreativelab.data.CraftVanillaRegistryManipulator();
    @Override
    public de.verdox.mccreativelab.data.@NotNull VanillaRegistryManipulator getRegistryManipulator() {
        return vanillaRegistryManipulator;
    }

    @Override
    public void setWorldHook(org.bukkit.@NotNull World world, @NotNull MCCWorldHook mccWorldHook) {
        Objects.requireNonNull(mccWorldHook, "mccWorldHook cannot be null");
        ((org.bukkit.craftbukkit.CraftWorld) world).getHandle().mccWorldHook = mccWorldHook;
    }

    @Override
    public @NotNull MCCWorldHook getWorldHook(@NotNull org.bukkit.World world) {
        return ((org.bukkit.craftbukkit.CraftWorld) world).getHandle().mccWorldHook;
    }

    @Override
    public de.verdox.mccreativelab.advancement.@NotNull AdvancementBuilder createAdvancement() {
        return new de.verdox.mccreativelab.advancement.CraftAdvancementBuilder();
    }

    @Override
    public org.bukkit.inventory.@NotNull Inventory openCustomContainerMenu(@NotNull de.verdox.mccreativelab.container.CustomInventory customInventory, @NotNull org.bukkit.entity.Player player, @NotNull net.kyori.adventure.text.Component title) {
        return de.verdox.mccreativelab.container.CustomContainerMenu.openToPlayer(customInventory, player, title);
    }

    @Override
    public @NotNull InteractionResult placeBlockFromItemWithoutPlayer(@NotNull ItemStack stack, @NotNull Location location) {
        if (!(org.bukkit.craftbukkit.inventory.CraftItemStack.asNMSCopy(stack).getItem() instanceof net.minecraft.world.item.BlockItem blockItem))
            throw new IllegalArgumentException();

        net.minecraft.world.level.Level level = ((org.bukkit.craftbukkit.CraftWorld) location.getWorld()).getHandle();
        BlockHitResult blockHitResult = new BlockHitResult(new Vec3(location.getBlockX(), location.getBlockY(), location.getBlockZ()), Direction.UP, new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()), true);

        return BehaviourUtil.Converter.InteractionResult.INSTANCE.nmsToBukkitValue(blockItem.placeItemWithoutPlayer(new BlockPlaceContext(level, null, InteractionHand.MAIN_HAND, org.bukkit.craftbukkit.inventory.CraftItemStack.asNMSCopy(stack), blockHitResult)));
    }
}
