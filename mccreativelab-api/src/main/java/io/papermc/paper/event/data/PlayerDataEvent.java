package io.papermc.paper.event.data;

import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class PlayerDataEvent extends Event {
    private final UUID playerUUID;

    public PlayerDataEvent(@NotNull UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public PlayerDataEvent(boolean isAsync, @NotNull UUID playerUUID) {
        super(isAsync);
        this.playerUUID = playerUUID;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }
}
