package de.verdox.mccreativelab.worldgen;

import de.verdox.mccreativelab.behavior.WorldGenerationBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.BlockVector;

public class WorldGenerationCallbackUtil {
    public static void executeWorldGenCallbacks(WorldGenerationBehaviour.FeatureType featureType, WorldGenLevel worldGenLevel, BlockPos pos, BlockState state) {
        var foundChunk = worldGenLevel.getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()), ChunkStatus.EMPTY, false);
        executeWorldGenCallbacks(featureType, worldGenLevel, pos, state, foundChunk);
    }
    public static void executeWorldGenCallbacks(WorldGenerationBehaviour.FeatureType featureType, WorldGenLevel worldGenLevel, BlockPos pos, BlockState state, ChunkAccess chunkAccess) {
        if(!WorldGenerationBehaviour.WORLD_GENERATION_BEHAVIOUR.isImplemented())
            return;
        BlockData blockData = state.createCraftBlockData();
        BlockVector blockPosition = new BlockVector(pos.getX(), pos.getY(), pos.getZ());
        WorldGenerationBehaviour.WORLD_GENERATION_BEHAVIOUR.getBehaviour().featureBlockGenerationCallback(featureType, blockPosition, chunkAccess, blockData);
    }
}
