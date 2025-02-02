package io.papermc.paper.io;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.Strictness;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.mojang.logging.LogUtils;
import io.papermc.paper.event.data.PlayerAdvancementDataLoadEvent;
import io.papermc.paper.event.data.PlayerAdvancementDataSaveEvent;
import net.minecraft.FileUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.world.level.storage.LevelResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataLoaderHelper {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Map<UUID, LoadedData> cache = new ConcurrentHashMap<>();

    public static void load(@NotNull UUID uuid) {
        loadAdvancements(uuid);
        loadStats(uuid);
        loadNBTData(uuid);
    }

    protected static void loadAdvancements(@NotNull UUID uuid) {
        Path playerSavePath = MinecraftServer.getServer().getWorldPath(LevelResource.PLAYER_ADVANCEMENTS_DIR).resolve(uuid + ".json");

        PlayerAdvancementDataLoadEvent event = new PlayerAdvancementDataLoadEvent(true, uuid);
        event.callEvent();

        if (event.getSerializedAdvancements() != null) {
            data(uuid).loadedAdvancements = event.getSerializedAdvancements();
        } else if (Files.isRegularFile(playerSavePath)) {
            try (JsonReader jsonReader = new JsonReader(Files.newBufferedReader(playerSavePath, StandardCharsets.UTF_8))) {
                jsonReader.setStrictness(Strictness.LEGACY_STRICT);
                data(uuid).loadedAdvancements = Streams.parse(jsonReader);
            } catch (JsonIOException | IOException var7) {
                LOGGER.error("Couldn't access player advancements in {}", playerSavePath, var7);
            } catch (JsonParseException var8) {
                LOGGER.error("Couldn't parse player advancements in {}", playerSavePath, var8);
            }
        }
    }

    public static void saveAdvancements(@NotNull UUID uuid, JsonElement serializedAdvancements) {
        PlayerAdvancementDataSaveEvent event = new PlayerAdvancementDataSaveEvent(uuid, serializedAdvancements);
        if(event.callEvent()){
            Path playerSavePath = MinecraftServer.getServer().getWorldPath(LevelResource.PLAYER_ADVANCEMENTS_DIR).resolve(uuid + ".json");
            try {
                FileUtil.createDirectoriesSafe(playerSavePath.getParent());

                try (Writer bufferedWriter = Files.newBufferedWriter(playerSavePath, StandardCharsets.UTF_8)) {
                    PlayerAdvancements.GSON.toJson(serializedAdvancements, PlayerAdvancements.GSON.newJsonWriter(bufferedWriter));
                }
            } catch (JsonIOException | IOException var7) {
                LOGGER.error("Couldn't save player advancements to {}", playerSavePath, var7);
            }
        }
    }

    protected static void loadStats(@NotNull UUID uuid) {
        //TODO Event
    }

    public static void saveStats(@NotNull UUID uuid, JsonElement serializedAdvancements) {
        //TODO Event
    }

    protected static void loadNBTData(@NotNull UUID playerUUID) {
        //TODO Event
    }

    public static void saveNBTData(@NotNull UUID playerUUID, ByteArrayOutputStream nbtData) {
        //TODO Event
    }

    public static void clear(@NotNull UUID playerUUID) {
        cache.remove(playerUUID);
    }

    public static LoadedData data(@NotNull UUID playerUUID) {
        return cache.computeIfAbsent(playerUUID, uuid -> new LoadedData());
    }

    public static class LoadedData {
        @Nullable
        private JsonElement loadedAdvancements;
        @Nullable
        private JsonElement loadedStats;
        @Nullable
        private ByteArrayInputStream loadedEntityData;

        public JsonElement consumeLoadedAdvancements() {
            try {
                return loadedAdvancements;
            } finally {
                loadedAdvancements = null;
            }
        }

        public JsonElement consumeLoadedStats() {
            try {
                return loadedStats;
            } finally {
                loadedStats = null;
            }
        }

        public ByteArrayInputStream consumeLoadedEntityData() {
            try {
                return loadedEntityData;
            } finally {
                loadedEntityData = null;
            }
        }
    }
}
