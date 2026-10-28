package de.verdox.mccreativelab;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainerFactory;
import org.bukkit.support.RegistryHelper;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

@VanillaFeature
class ChunkSectionCopyTest {
    @Test
    void contextualSectionCanBeRecountedAndCopiedWhenPaperOmitsCoordinates() {
        var factory = PalettedContainerFactory.create(RegistryHelper.registryAccess());
        var section = new LevelChunkSection(factory.createForBlockStates(), factory.createForBiomes());
        section.setBlockState(1, 2, 3, Blocks.STONE.defaultBlockState());
        section.setBlockState(4, 5, 6, Blocks.DIRT.defaultBlockState());
        assertDoesNotThrow(() -> section.init(mock(Level.class), new ChunkPos(2, -3), -4));
        var copy = assertDoesNotThrow(section::copy);
        assertSame(Blocks.STONE.defaultBlockState(), copy.getBlockState(1, 2, 3));
        assertSame(Blocks.DIRT.defaultBlockState(), copy.getBlockState(4, 5, 6));
        assertFalse(copy.isRandomlyTickingBlocks());
        assertEquals(0, copy.moonrise$getTickingBlockList().size());
    }

    @Test
    void snapshotRetainsTheTickingPositionListWithoutReevaluatingWorldCallbacks() {
        var factory = PalettedContainerFactory.create(RegistryHelper.registryAccess());
        var section = new LevelChunkSection(factory.createForBlockStates(), factory.createForBiomes());
        section.setBlockState(1, 2, 3, Blocks.WHEAT.defaultBlockState());
        section.init(mock(Level.class), new ChunkPos(0, 0), 0);
        var copy = section.copy();
        assertEquals(1, section.moonrise$getTickingBlockList().size());
        assertEquals(1, copy.moonrise$getTickingBlockList().size());
        assertEquals(section.moonrise$getTickingBlockList().getRaw(0), copy.moonrise$getTickingBlockList().getRaw(0));
        copy.setBlockState(1, 2, 3, Blocks.AIR.defaultBlockState());
        assertEquals(1, section.moonrise$getTickingBlockList().size());
        assertEquals(0, copy.moonrise$getTickingBlockList().size());
    }
}
