package de.verdox.mccreativelab.behavior;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PlayerDataBehavior extends Behaviour {

    @Nullable
    default CompletableFuture<JsonElement> loadAdvancementsAsync(@NotNull UUID playerUUID) {
        return null;
    }

    @Nullable
    default CompletableFuture<Void> saveAdvancementsAsync(@NotNull UUID playerUUID, JsonElement jsonElement) {
        return null;
    }

    @Nullable
    default CompletableFuture<ByteArrayInputStream> loadNBTDataAsync(@NotNull UUID playerUUID) {
        return null;
    }

    @Nullable
    default CompletableFuture<Void> saveNBTDataAsync(@NotNull UUID playerUUID, ByteArrayOutputStream nbtData) {
        return null;
    }

    @Nullable
    default CompletableFuture<JsonElement> loadStatsAsync(@NotNull UUID playerUUID) {
        return null;
    }

    @Nullable
    default CompletableFuture<Void> saveStatsAsync(@NotNull UUID playerUUID, JsonElement jsonElement) {
        return null;
    }
}
