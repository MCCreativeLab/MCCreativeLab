package de.verdox.mccreativelab.behavior;

import de.verdox.mccreativelab.InteractionResult;
import de.verdox.mccreativelab.MultiCustomBehaviour;
import de.verdox.mccreativelab.recipe.CustomItemData;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;

public interface ItemBehaviour extends Behaviour {
    MultiCustomBehaviour<CustomItemData, ItemBehaviour> ITEM_BEHAVIOUR = new MultiCustomBehaviour<>(ItemBehaviour.class, new ItemBehaviour() {
    }, "MCCLab - ItemBehaviour");

    /**
     * Called when an {@link ItemStack} is used to mine a {@link Block} by a {@link Player}
     * @param stack - The ItemStack
     * @param block - The block
     * @param miner - The miner
     * @return - If the block was mined successfully
     */
    @NotNull
    default BehaviourResult.Bool mineBlock(ItemStack stack, Block block, Player miner) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * Is called to determine if an {@link ItemStack} is the correct tool to drop loot from {@link BlockData}
     * @param stack - The item stack
     * @param blockData - The block data
     * @return - True when it is the correct tool
     */
    @NotNull
    default BehaviourResult.Bool isCorrectToolForDrops(ItemStack stack, BlockData blockData) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * Called when an amount of {@link ItemStack} is crafted by a {@link Player}
     * @param stack - The ItemStack
     * @param player - The crafter
     * @param amount - The amount crafted
     * @return - Nothing
     */
    @NotNull
    default BehaviourResult.Void onCraftedBy(ItemStack stack, Player player, int amount) {
        return BehaviourResult.Void.DEFAULT_INSTANCE;
    }

    /**
     * Called when an {@link Item} is destroyed in the world
     * @param stack - The ItemStack of the item
     * @param item - The Item entity
     * @return - nothing
     */
    @NotNull
    default BehaviourResult.Void onDestroyed(ItemStack stack, Item item) {
        return BehaviourResult.Void.DEFAULT_INSTANCE;
    }

    /**
     * Gets if the {@link ItemStack} can drop from an inventory on death
     * @param stack - The ItemStack
     * @return - true if it can drop
     */
    @NotNull
    default BehaviourResult.Bool canDropOnDeath(ItemStack stack) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * Called when a {@link Player} interacts with a {@link LivingEntity} with an {@link ItemStack}
     * @param stack - The ItemStack
     * @param player - The Player
     * @param livingEntity - The LivingEntity
     * @param equipmentSlot - The hand in which the item is carried
     * @return - An InteractionResult
     */
    @NotNull
    default BehaviourResult.Object<InteractionResult> interactLivingEntity(ItemStack stack, Player player, LivingEntity livingEntity, EquipmentSlot equipmentSlot) {
        return BehaviourResult.Object.DEFAULT_INSTANCE;
    }

    /**
     * Called when a {@link Player} uses an {@link ItemStack} on a {@link Block}
     * @param stack - The ItemStack
     * @param player - The Player
     * @param rayTraceResult - The hit block information
     * @param equipmentSlot - The hand in which the item is carried
     * @return - An InteractionResult
     */
    @NotNull
    default BehaviourResult.Object<InteractionResult> useOn(ItemStack stack, Player player, EquipmentSlot equipmentSlot, RayTraceResult rayTraceResult) {
        return BehaviourResult.Object.DEFAULT_INSTANCE;
    }

    /**
     * Gets if the {@link ItemStack} fits inside container items like Shulker Boxes or Bundles
     * @param stack - The ItemStack
     * @return - true if it fits
     */
    @NotNull
    default BehaviourResult.Bool canFitInsideContainerItems(ItemStack stack) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * Called when a player attempts to place this item. The BlockData returned is the block that is placed by this action.
     * @param stack - The Item stack
     * @param player - The player
     * @param clickedPosition - The clicked position
     * @param vanillaBlockData - The block data vanilla would place
     * @return - the Blockdata that will be placed
     */
    @NotNull
    default BehaviourResult.Object<BlockData> placeBlockAction(ItemStack stack, Player player, Location clickedPosition, BlockData vanillaBlockData){
        return BehaviourResult.Object.DEFAULT_INSTANCE;
    }
}
