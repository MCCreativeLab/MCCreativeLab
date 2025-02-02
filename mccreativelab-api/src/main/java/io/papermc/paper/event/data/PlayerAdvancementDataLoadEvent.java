package io.papermc.paper.event.data;

import com.google.gson.JsonElement;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerAdvancementDataLoadEvent extends PlayerDataEvent {
    private JsonElement serializedAdvancements;

    public PlayerAdvancementDataLoadEvent(@NotNull UUID playerUUID) {
        super(playerUUID);
    }

    public PlayerAdvancementDataLoadEvent(boolean isAsync, @NotNull UUID playerUUID) {
        super(isAsync, playerUUID);
    }

    public JsonElement getSerializedAdvancements() {
        return serializedAdvancements;
    }

    public void setSerializedAdvancements(JsonElement serializedAdvancements) {
        this.serializedAdvancements = serializedAdvancements;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return null;
    }
}
