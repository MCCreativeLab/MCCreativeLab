package de.verdox.mccreativelab.behaviour.storage;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.mojang.datafixers.DataFixer;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.minecraft.FileUtil;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.storage.PlayerDataStorage;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public class BehaviorBasedPlayerAdvancements extends PlayerAdvancements {
    private static final Logger LOGGER = LogUtils.getLogger();

    public BehaviorBasedPlayerAdvancements(DataFixer dataFixer, PlayerList playerList, ServerAdvancementManager manager, Path playerSavePath, ServerPlayer player) {
        super(dataFixer, playerList, manager, playerSavePath, player);
    }

    @Override
    public void save() {
        if (org.spigotmc.SpigotConfig.disableAdvancementSaving) return; // Spigot
        // MCCreativeLab start - Add StorageBehaviour
        JsonElement jsonElement = this.codec.encodeStart(JsonOps.INSTANCE, this.asData()).getOrThrow();
        try {
            FileUtil.createDirectoriesSafe(this.playerSavePath.getParent());
            try (Writer bufferedWriter = Files.newBufferedWriter(this.playerSavePath, StandardCharsets.UTF_8)) {
                GSON.toJson(jsonElement, GSON.newJsonWriter(bufferedWriter));
            }
        } catch (JsonIOException | IOException var7) {
            LOGGER.error("Couldn't save player advancements to {}", pathToSave, ioexception);
        }
    }

    @Override
    protected void load(ServerAdvancementManager manager) {
        JsonElement jsonelement = de.verdox.mccreativelab.behaviour.StorageBehaviourUtil.loadPlayerAdvancements(player, () -> {
            if (Files.isRegularFile(this.playerSavePath, new LinkOption[0])) {
                try {
                    JsonReader jsonreader = new JsonReader(Files.newBufferedReader(this.playerSavePath, StandardCharsets.UTF_8));

                    try {
                        jsonreader.setLenient(false);
                        return Streams.parse(jsonreader);
                    } catch (Throwable throwable) {
                        try {
                            jsonreader.close();
                        } catch (Throwable throwable1) {
                            throwable.addSuppressed(throwable1);
                        }

                        throw throwable;
                    }
                    finally {
                        jsonreader.close();
                    }
                } catch (JsonIOException | IOException ioexception) {
                    LOGGER.error("Couldn't access player advancements in {}", this.playerSavePath, ioexception);
                } catch (JsonParseException jsonparseexception) {
                    LOGGER.error("Couldn't parse player advancements in {}", this.playerSavePath, jsonparseexception);
                }
            }
            return null;
        });
        if(jsonelement == null)
            return;

        PlayerAdvancements.Data data = this.codec.parse(JsonOps.INSTANCE, jsonelement).getOrThrow(JsonParseException::new);
        this.applyFrom(manager, data);

        this.checkForAutomaticTriggers(manager);
        this.registerListeners(manager);
    }
}
