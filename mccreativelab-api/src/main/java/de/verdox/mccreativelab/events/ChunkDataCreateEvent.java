package de.verdox.mccreativelab.events;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

public class ChunkDataCreateEvent extends ChunkDataEvent {
    private static final HandlerList handlers = new HandlerList();

    public ChunkDataCreateEvent(@NotNull World world, @NotNull Chunk chunk, @NotNull ChunkDataEvent.ChunkPos chunkPos, @NotNull PersistentDataContainer persistentDataContainer) {
        super(world, chunk, chunkPos, persistentDataContainer);
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
