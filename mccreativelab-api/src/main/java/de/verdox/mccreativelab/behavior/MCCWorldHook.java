package de.verdox.mccreativelab.behavior;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Used to implement additional logic into a World
 */
public abstract class MCCWorldHook {
    private final Map<Block, BlockContext> contexts = new ConcurrentHashMap<>();

    /**
     * Used to store additional context for a block action
     *
     * @param block the block to store the action in
     */
    public void setBlockContext(@NotNull Block block, @Nullable BlockContext blockContext){
        if(blockContext == null)
            contexts.remove(block);
        else
            contexts.put(block, blockContext);
    }

    /**
     * Consumes the block action at a particular block
     * @param block the block
     * @return the block action if one is available at the block
     */
    public @Nullable BlockContext getBlockContext(@NotNull Block block){return contexts.getOrDefault(block, null);}

    /**
     * Is called everytime the server changes a block.
     * @param block The block
     * @param oldBlockData the block data before the change
     * @param newBlockData the block data after the change
     */
    public void onBlockChange(@NotNull Block block, @NotNull BlockData oldBlockData, @NotNull BlockData newBlockData){

    }

    public void onBlockDrawLoot(@NotNull Block block, @Nullable Entity entity, @Nullable ItemStack tool, boolean dropExperience, @NotNull List<ItemStack> itemsThatWillBeDropped){}

    public interface BlockContext {
        @NotNull Block block();
    }

}
