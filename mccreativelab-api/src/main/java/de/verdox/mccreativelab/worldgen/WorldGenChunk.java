package de.verdox.mccreativelab.worldgen;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;

public interface WorldGenChunk extends PersistentDataHolder {
    /**
     * Gets the X-coordinate of this chunk
     *
     * @return X-coordinate
     */
    int getX();
    /**
     * Gets the Z-coordinate of this chunk
     *
     * @return Z-coordinate
     */
    int getZ();
    // Paper start
    /**
     * @return The Chunks X and Z coordinates packed into a long
     */
    default long getChunkKey() {
        return getChunkKey(getX(), getZ());
    }
    /**
     * @param loc Location to get chunk key
     * @return Location's chunk coordinates packed into a long
     */
    static long getChunkKey(@NotNull Location loc) {
        return getChunkKey((int) Math.floor(loc.getX()) >> 4, (int) Math.floor(loc.getZ()) >> 4);
    }
    /**
     * @param x X Coordinate
     * @param z Z Coordinate
     * @return Chunk coordinates packed into a long
     */
    static long getChunkKey(int x, int z) {
        return (long) x & 0xffffffffL | ((long) z & 0xffffffffL) << 32;
    }
    // Paper end
    /**
     * Gets the world containing this chunk
     *
     * @return Parent World
     */
    @NotNull World getWorld();
}
