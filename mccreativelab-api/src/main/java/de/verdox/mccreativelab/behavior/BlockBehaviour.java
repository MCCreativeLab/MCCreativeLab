package de.verdox.mccreativelab.behavior;

import de.verdox.mccreativelab.InteractionResult;
import de.verdox.mccreativelab.ItemInteractionResult;
import de.verdox.mccreativelab.MultiCustomBehaviour;
import de.verdox.mccreativelab.random.VanillaRandomSource;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BlockBehaviour extends Behaviour {
    MultiCustomBehaviour<Material, BlockBehaviour> BLOCK_BEHAVIOUR = new MultiCustomBehaviour<>(BlockBehaviour.class, new BlockBehaviour() {}, "MCCLab - BlockBehaviour");

    /**
     * This method is called to get the explosion resistance of a block
     *
     * @param block     The block
     * @param blockData The blockData of the block
     * @return float result
     */
    @NotNull
    default BehaviourResult.Object<Float> getExplosionResistance(@NotNull Block block, @NotNull BlockData blockData) {
        return BehaviourResult.Object.DEFAULT_INSTANCE;
    }

    /**
     * This method is called everytime the block receives a block update due to any reason.
     * Normally this happens when a block next to this block was changed. If you want to implement any custom behaviour on environmental changes use this method.
     *
     * @param location           The location of the block
     * @param blockData          The blockData of the block
     * @param direction          The direction of the Blockupdate
     * @param neighbourBlockData The neighbour blockdata that issued the block update
     * @param neighbourLocation  The neighbour location
     * @return The blockdata that results from this update.
     */
    @NotNull
    default BehaviourResult.Object<BlockData> blockUpdate(@NotNull Location location, @NotNull BlockData blockData, @NotNull BlockFace direction, @NotNull BlockData neighbourBlockData, @NotNull Location neighbourLocation) {
        return result(blockData, BehaviourResult.Object.Type.USE_VANILLA);
    }

    /**
     * This method is called everytime the block recognizes a neighbour block update
     * Normally this happens when a block next to this block was changed. If you want to implement any custom behaviour on environmental changes use this method.
     *
     * @param block           The block recognizing the neighbour update
     * @param sourceBlock     The neighbour block receiving the block update
     * @param notify          The Notify flag
     */
    @NotNull
    default BehaviourResult.Void onNeighbourBlockUpdate(@NotNull Block block, @NotNull Block sourceBlock, boolean notify) {
        return BehaviourResult.Void.DEFAULT_INSTANCE;
    }

    /**
     * This method is called whenever an entity steps on a block
     *
     * @param block     The block
     * @param blockData The blockData of the block
     * @param entity    The Entity stepping on the block
     * @return void result
     */
    @NotNull
    default BehaviourResult.Void stepOn(@NotNull Block block, @NotNull BlockData blockData, @NotNull Entity entity) {
        return BehaviourResult.Void.DEFAULT_INSTANCE;
    }

    /**
     * This method is called every time the server software tries to randomly tick a block.
     *
     * @param block               The randomly ticked block
     * @param vanillaRandomSource The vanilla random source
     */
    @NotNull
    default BehaviourResult.Void randomTick(@NotNull Block block, @NotNull VanillaRandomSource vanillaRandomSource) {
        return BehaviourResult.Void.DEFAULT_INSTANCE;
    }

    /**
     * This method is called to check if a blockdata is randomly ticking regardless of it being placed in a world right now.
     *
     * @param blockData The block data of the block
     * @return A boolean result
     */
    @NotNull
    default BehaviourResult.Bool isBlockDataRandomlyTicking(@NotNull BlockData blockData) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * This method is called to check if a block in a world is randomly ticking
     *
     * @param block     The block
     * @param blockData The block data of the block
     * @return A boolean result
     */
    @NotNull
    default BehaviourResult.Bool isBlockRandomlyTicking(@NotNull Block block, @NotNull BlockData blockData) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * This callback is run after the server placed a block due to any reason. This method is NOT called when a player places block!
     *
     * @param location     The location
     * @param newBlockData The new block data
     * @param oldBlockData The old block data
     * @param notify       Whether this notifies the world
     * @return callback
     */
    @NotNull
    default BehaviourResult.Callback onPlace(@NotNull Location location, @NotNull BlockData newBlockData, @NotNull BlockData oldBlockData, boolean notify, boolean isProcessingBlockPlaceEvent) {
        return BehaviourResult.Callback.DEFAULT_INSTANCE;
    }

    /**
     * This callback is run after a player successfully placed a block. If the Block Place Action was cancelled by an event this function is not called.
     *
     * @param player   The player
     * @param location The location
     * @return callback
     */
    @NotNull
    default BehaviourResult.Callback onPlayerPlace(@NotNull Player player, @NotNull ItemStack stackUsedToPlaceBlock, @NotNull Location location, @NotNull BlockData thePlacedState) {
        return BehaviourResult.Callback.DEFAULT_INSTANCE;
    }

    /**
     * This callback is run after a player breaks a block
     *
     * @param player      The player
     * @param location    The location
     * @param brokenState The broken block state
     * @return callback
     */
    @NotNull
    default BehaviourResult.Callback onPlayerBreak(@NotNull Player player, @NotNull Location location, @NotNull BlockData brokenState) {
        return BehaviourResult.Callback.DEFAULT_INSTANCE;
    }


    /**
     * This callback is run after the server removes a block due to any reason. This does not include a player breaking a block.
     *
     * @param location     The location
     * @param newBlockData The new block data
     * @param oldBlockData The old block data
     * @param moved        Whether the block was moved
     * @return callback
     */
    @NotNull
    default BehaviourResult.Callback onRemove(@NotNull Location location, @NotNull BlockData newBlockData, @NotNull BlockData oldBlockData, boolean moved) {
        return BehaviourResult.Callback.DEFAULT_INSTANCE;
    }

    /**
     *
     * @param location - The block location
     * @param drop - True if the block dropped items
     * @param destroyingEntity - The Entity that destroyed the block
     * @param maxUpdateDepth - The chunk update depth
     * @return a callback object
     */
    @NotNull
    default BehaviourResult.Callback onDestroy(@NotNull Location location, boolean drop, @Nullable Entity destroyingEntity, int maxUpdateDepth) {
        return BehaviourResult.Callback.DEFAULT_INSTANCE;
    }

    /**
     * This method is run when a player interacts with a block in any way. By overriding this method, various bukkit events, paper code, and fixed won't be called.
     *
     * @param block          The block
     * @param player         The player
     * @param hand           The Hand used to interact
     * @param rayTraceResult The interaction info
     * @return The result of this interaction
     */
    @NotNull
    default BehaviourResult.Object<ItemInteractionResult> use(@NotNull Block block, @NotNull Player player, @NotNull EquipmentSlot hand, @NotNull RayTraceResult rayTraceResult) {
        return BehaviourResult.Object.DEFAULT_INSTANCE;
    }

    /**
     * This callback is run after a player interacted with this block. This method is not run on blocks that call bukkit events! Only on those that do not define any specific onUse Behaviour
     *
     * @param block          The block
     * @param player         The player
     * @param hand           The Hand used to interact
     * @param rayTraceResult The interaction info
     * @return callback
     */
    @NotNull
    default BehaviourResult.Callback onUseCallback(@NotNull Block block, @NotNull Player player, @NotNull EquipmentSlot hand, @NotNull RayTraceResult rayTraceResult, @NotNull InteractionResult interactionResult) {
        return BehaviourResult.Callback.DEFAULT_INSTANCE;
    }

    /**
     * This callback is run after a block was successfully moved by a piston
     *
     * @return callback
     */
    @NotNull
    default BehaviourResult.Callback onPistonMoveBlock(@NotNull BlockData blockDataMoved, @NotNull Location positionBeforeMove, @NotNull Location positionAfterMove, @NotNull Block piston, @NotNull Vector moveDirection) {
        return BehaviourResult.Callback.DEFAULT_INSTANCE;
    }

    /**
     * This method is called every time the server software ticks a block.
     * <p>
     * Some blocks may not be ticked in vanilla by default. For example Stone blocks don't receive ticks.
     * Please use the blockUpdate method to implement any custom logic or to call this method.
     *
     * @param block               The ticked block
     * @param vanillaRandomSource The vanilla random source
     */
    @NotNull
    default BehaviourResult.Void tick(@NotNull Block block, @NotNull VanillaRandomSource vanillaRandomSource) {
        return BehaviourResult.Void.DEFAULT_INSTANCE;
    }

    /**
     * This method is called to check if a block can survive in the current environment
     * <p>
     * Some blocks may not use this method by default.
     * Please use the blockUpdate method to implement any custom logic or to call this method.
     *
     * @param block The block
     * @param world The world
     * @return A boolean result
     */
    @NotNull
    default BehaviourResult.Bool canSurvive(@NotNull Block block, @NotNull World world) {
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }

    /**
     * This method is called every time a user left-clicks a block.
     * <p>
     *
     * @param block  The clicked block
     * @param player The player
     */
    @NotNull
    default BehaviourResult.Void attack(@NotNull Block block, @NotNull Player player) {
        return BehaviourResult.Void.DEFAULT_INSTANCE;
    }

    /**
     * Called when a player uses bonemeal on a block.
     * @param block The block that is fertilized
     * @param stack The item used to fertilize. The Minecraft server will call this only for bonemeal.
     * @return True if the action was a success
     */
    @NotNull
    default BehaviourResult.Bool fertilizeAction(@NotNull Block block, @NotNull ItemStack stack){
        return BehaviourResult.Bool.DEFAULT_INSTANCE;
    }
}

