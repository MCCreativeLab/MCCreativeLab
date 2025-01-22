package de.verdox.mccreativelab.events;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

public class ChunkDataSaveEvent extends ChunkDataEvent {
    private static final HandlerList handlers = new HandlerList();

    private final boolean unloaded;

    public ChunkDataSaveEvent(@NotNull World world, @NotNull Chunk chunk, @NotNull ChunkDataEvent.ChunkPos chunkPos, @NotNull PersistentDataContainer persistentDataContainer, boolean unloaded) {
        super(world, chunk, chunkPos, persistentDataContainer);
        this.unloaded = unloaded;
    }

    public boolean isUnloaded() {
        return this.unloaded;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }
}
