package de.verdox.mccreativelab.behaviour;

import de.verdox.mccreativelab.behavior.ItemBehaviour;
import de.verdox.mccreativelab.recipe.CustomItemData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.function.Supplier;

public class ItemBehaviourUtil implements BehaviourUtil {
    public static BlockState placeBlockAction(Player player, BlockPos pos, Level world, ItemStack stack, BlockState state, Supplier<BlockState> vanillaLogic){
        return evaluate(getBehaviour(stack),
            itemBehaviour -> itemBehaviour.placeBlockAction(stack.getBukkitStack(), ((CraftPlayer) player.getBukkitEntity()), new Location(world.getWorld(), pos.getX(), pos.getY(), pos.getZ()), Converter.BlockData.INSTANCE.nmsToBukkitValue(state)),
            vanillaLogic,
            Converter.BlockData.INSTANCE
        );
    }

    public static InteractionResult useOn(ItemStack stack, UseOnContext context) {
        BlockFace blockFace = toBukkitBlockFace(context.getClickedFace());
        Vector vector = new Vector(context.getClickedPos().getX(), context.getClickedPos().getY(), context
            .getClickedPos().getZ());
        Block block = context.getLevel().getWorld().getBlockAt(new Location(context.getLevel()
                                                                                   .getWorld(), vector.getX(), vector.getY(), vector.getZ()));
        RayTraceResult rayTraceResult = new RayTraceResult(new Vector(context.getClickedPos().getX(), context
            .getClickedPos().getY(), context.getClickedPos().getZ()), block, blockFace);
        return evaluate(getBehaviour(stack), itemBehaviour -> itemBehaviour.useOn(stack.asBukkitMirror(), ((org.bukkit.entity.Player) context.getPlayer().getBukkitEntity()), context
                .getHand()
                .equals(InteractionHand.MAIN_HAND) ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND, rayTraceResult),
            () -> stack.getItem().useOn(context)
            , Converter.InteractionResult.INSTANCE
        );
    }

    public static boolean isCorrectToolForDrops(ItemStack stack, BlockState blockState) {
        return evaluateBoolean(getBehaviour(stack),
            itemBehaviour -> itemBehaviour.isCorrectToolForDrops(stack.asBukkitMirror(), blockState.createCraftBlockData()),
            () -> stack.getItem().isCorrectToolForDrops(stack, blockState)
        );
    }

    public static void mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, Player miner) {
        evaluateVoid(getBehaviour(stack),
            itemBehaviour -> itemBehaviour.mineBlock(stack.asBukkitMirror(), world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), (CraftPlayer) miner.getBukkitEntity()),
            () -> {});
    }

    public static InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity livingEntity, InteractionHand interactionHand) {
        return evaluate(getBehaviour(stack),
            itemBehaviour -> itemBehaviour.interactLivingEntity(stack.asBukkitMirror(), (CraftPlayer) player.getBukkitEntity(), livingEntity.getBukkitLivingEntity(),
            interactionHand.equals(InteractionHand.MAIN_HAND) ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND),
            () -> stack.getItem().interactLivingEntity(stack, player, livingEntity, interactionHand)
            , Converter.InteractionResult.INSTANCE);
    }

    public static void onCraftedBy(ItemStack stack, Level world, Player player) {
        evaluateVoid(getBehaviour(stack),
            itemBehaviour -> itemBehaviour.onCraftedBy(stack.asBukkitMirror(), (CraftPlayer) player.getBukkitEntity()),
            () -> {});
    }

    public static void onDestroyed(ItemStack stack, ItemEntity entity) {
        evaluateVoid(getBehaviour(stack),
            itemBehaviour -> itemBehaviour.onDestroyed(stack.asBukkitMirror(), (CraftItem) entity.getBukkitEntity()),
            () -> stack.getItem().onDestroyed(entity));
    }

    public static boolean canFitInsideContainerItems(ItemStack stack) {
        return evaluateBoolean(getBehaviour(stack),
            itemBehaviour -> itemBehaviour.canFitInsideContainerItems(stack.asBukkitMirror()),
            () -> stack.getItem().canFitInsideContainerItems());
    }

    public static boolean canDrop(ItemStack stack) {
        return evaluateBoolean(getBehaviour(stack),
            itemBehaviour -> itemBehaviour.canDropOnDeath(stack.asBukkitMirror()),
            () -> true);
    }

    public static ItemBehaviour getBehaviour(ItemStack stack) {
        if(stack.itemBehaviour != null)
            return stack.itemBehaviour;
        CustomItemData customItemData = CustomItemData.fromItemStack(stack.getBukkitStack());
        if (ItemBehaviour.ITEM_BEHAVIOUR.isImplemented(customItemData))
            return ItemBehaviour.ITEM_BEHAVIOUR.getBehaviour(customItemData);
        return null;
    }
    private static BlockFace toBukkitBlockFace(Direction direction) {
        return switch (direction) {
            case DOWN -> BlockFace.DOWN;
            case UP -> BlockFace.UP;
            case NORTH -> BlockFace.NORTH;
            case SOUTH -> BlockFace.SOUTH;
            case WEST -> BlockFace.WEST;
            case EAST -> BlockFace.EAST;
        };
    }

}
