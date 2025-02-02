package de.verdox.mccreativelab.behavior;

import de.verdox.mccreativelab.MultiCustomBehaviour;
import de.verdox.mccreativelab.random.VanillaRandomSource;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


import java.util.function.BiConsumer;

public interface BlockBehaviour extends Behaviour {
    MultiCustomBehaviour<BlockType, BlockBehaviour> BLOCK_BEHAVIOUR = new MultiCustomBehaviour<>(BlockBehaviour.class, new BlockBehaviour() {}, "MCCLab - BlockBehaviour");
    MultiCustomBehaviour<BlockData, BlockBehaviour> BLOCK_BEHAVIOUR_SPECIFIC = new MultiCustomBehaviour<>(BlockBehaviour.class, new BlockBehaviour() {}, "MCCLab - BlockBehaviour");

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
     * @param movedByPiston       Whether this notifies the world
     * @return callback
     */
    @NotNull
    default BehaviourResult.Void onPlace(@NotNull Location location, @NotNull BlockData newBlockData, @NotNull BlockData oldBlockData, boolean movedByPiston) {
        return BehaviourResult.Void.DEFAULT_INSTANCE;
    }


    /**
     * This callback is run after the server removes a block due to any reason. This does not include a player breaking a block.
     *
     * @param location     The location
     * @param newBlockData The new block data
     * @param oldBlockData The old block data
     * @param movedByPiston        Whether the block was moved
     * @return callback
     */
    @NotNull
    default BehaviourResult.Void onRemove(@NotNull Location location, @NotNull BlockData newBlockData, @NotNull BlockData oldBlockData, boolean movedByPiston) {
        return BehaviourResult.Void.DEFAULT_INSTANCE;
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
     * @return A boolean result
     */
    @NotNull
    default BehaviourResult.Bool canSurvive(@NotNull Block block) {
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
     * This function is run when a block is hit by an explosion
     * @param block the block
     * @param explosionResult the result of the explosion
     * @param dropConsumer the code that is run on the loot that is dropped because of the explosion
     * @return void
     */
    default BehaviourResult.Void onExplosionHit(Block block, ExplosionResult explosionResult, BiConsumer<ItemStack, Location> dropConsumer) {
        return BehaviourResult.Void.DEFAULT_INSTANCE;
    }

    /**
     * This function is run when a block was destroyed by an explosion
     * @param block the block
     * @param explosionResult the result of the explosion
     * @return void
     */
    default BehaviourResult.Void wasExploded(Block block, ExplosionResult explosionResult) {
        return BehaviourResult.Void.DEFAULT_INSTANCE;
    }

    /**
     * This function is run when an entity falls on a block from a certain distance
     * @param block the block
     * @param blockData the data of the block
     * @param fallingEntity  the entity falling
     * @param fallDistance the falling distance
     * @return void
     */
    default BehaviourResult.Void fallOn(Block block, BlockData blockData, Entity fallingEntity, float fallDistance) {
        return BehaviourResult.Void.DEFAULT_INSTANCE;
    }

    /**
     * This function is called when a block gets in contact with precipitation.
     * @param block the block
     * @param blockData the data of the block
     * @param precipitation the precipitation
     * @return void
     */
    default BehaviourResult.Void handlePrecipitation(Block block, BlockData blockData, Precipitation precipitation) {
        return BehaviourResult.Void.DEFAULT_INSTANCE;
    }

    /**
     * This function is called after a block was broken and the server predicts that something should be spawned from that
     * @param block the block
     * @param toolUsedToBreakBlock the item stack used to break the block
     * @param dropExperience whether to drop experience
     * @return void
     */
    default BehaviourResult.Void spawnAfterBreak(Block block, ItemStack toolUsedToBreakBlock, boolean dropExperience) {
        return BehaviourResult.Void.DEFAULT_INSTANCE;
    }

    /**
     * This function is called when a block with a specific data is called by a projectile
     * @param block the block
     * @param projectile the projectile
     * @return void
     */
    default BehaviourResult.Void onProjectileHit(Block block, Projectile projectile) {
        return BehaviourResult.Void.DEFAULT_INSTANCE;
    }
}

