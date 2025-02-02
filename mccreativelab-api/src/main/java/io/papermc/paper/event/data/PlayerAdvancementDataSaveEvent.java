package io.papermc.paper.event.data;

import com.google.gson.JsonElement;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerAdvancementDataSaveEvent extends PlayerDataEvent implements Cancellable {
    private final JsonElement serializedAdvancements;
    private boolean cancelled;

    public PlayerAdvancementDataSaveEvent(@NotNull UUID playerUUID, @NotNull JsonElement serializedAdvancements) {
        super(playerUUID);
        this.serializedAdvancements = serializedAdvancements;
    }

    public PlayerAdvancementDataSaveEvent(boolean isAsync, @NotNull UUID playerUUID, @NotNull JsonElement serializedAdvancements) {
        super(isAsync, playerUUID);
        this.serializedAdvancements = serializedAdvancements;
    }

    public JsonElement getSerializedAdvancements() {
        return serializedAdvancements;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return null;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
