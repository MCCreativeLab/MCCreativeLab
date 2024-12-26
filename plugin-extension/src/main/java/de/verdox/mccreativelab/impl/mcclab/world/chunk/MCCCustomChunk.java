package de.verdox.mccreativelab.impl.mcclab.world.chunk;

import de.verdox.mccreativelab.conversion.converter.MCCConverter;
import de.verdox.mccreativelab.impl.vanilla.world.chunk.NMSChunk;
import de.verdox.mccreativelab.world.block.FakeBlockStorage;
import de.verdox.mccreativelab.wrapper.block.MCCBlockState;
import de.verdox.mccreativelab.wrapper.block.MCCBlockType;
import de.verdox.mccreativelab.wrapper.platform.MCCHandle;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.craftbukkit.CraftChunk;
import org.jetbrains.annotations.NotNull;

public class MCCCustomChunk extends NMSChunk {
    public static final MCCConverter<LevelChunk, MCCCustomChunk> CONVERTER = converter(MCCCustomChunk.class, LevelChunk.class, MCCCustomChunk::new, MCCHandle::getHandle);

    public MCCCustomChunk(LevelChunk handle) {
        super(handle);
    }

    @Override
    public MCCBlockType getBlockTypeAt(int localX, int localY, int localZ) {
        //TODO: First check fake block storage, else proceed with nms logic
        return super.getBlockTypeAt(localX, localY, localZ);
    }

    @Override
    public void setBlock(@NotNull MCCBlockType mccBlockType, int localX, int localY, int localZ) {
        //TODO: First check fake block storage, else proceed with nms logic
        super.setBlock(mccBlockType, localX, localY, localZ);
    }

    @Override
    public void setBlock(@NotNull MCCBlockState mccBlockState, int localX, int localY, int localZ) {
        //TODO: First check fake block storage, else proceed with nms logic
        super.setBlock(mccBlockState, localX, localY, localZ);
    }

    @Override
    public MCCBlockState getBlockDataAt(int localX, int localY, int localZ) {
        //TODO: First check fake block storage, else proceed with nms logic
        return super.getBlockDataAt(localX, localY, localZ);
    }
}
