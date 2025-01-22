package de.verdox.mccreativelab.events;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

public abstract class ChunkDataEvent extends Event {

    private final World world;

    private final Chunk chunk;

    private final ChunkPos chunkPos;

    private final PersistentDataContainer persistentDataContainer;

    public ChunkDataEvent(@NotNull World world, @NotNull Chunk chunk, @NotNull ChunkPos chunkPos, @NotNull PersistentDataContainer persistentDataContainer) {
        super(!Bukkit.isPrimaryThread());
        this.world = world;
        this.chunk = chunk;
        this.chunkPos = chunkPos;
        this.persistentDataContainer = persistentDataContainer;
    }

    @NotNull
    public ChunkPos getChunkPos() {
        return this.chunkPos;
    }

    @NotNull
    public Chunk getChunk() {
        return this.chunk;
    }

    @NotNull
    public World getWorld() {
        return this.world;
    }

    @NotNull
    public PersistentDataContainer getPersistentDataContainer() {
        return this.persistentDataContainer;
    }


    public record ChunkPos(int x, int z) {

    }
}
