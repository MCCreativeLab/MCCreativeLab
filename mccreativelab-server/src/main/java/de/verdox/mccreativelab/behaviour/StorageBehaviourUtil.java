package de.verdox.mccreativelab.behaviour;

import com.google.gson.JsonElement;
import de.verdox.mccreativelab.behavior.StorageBehaviour;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.UUID;
import java.util.function.Supplier;

public class StorageBehaviourUtil implements BehaviourUtil {
    public static JsonElement loadPlayerAdvancements(ServerPlayer serverPlayer, Supplier<JsonElement> vanillaLogic) {
        return evaluate(StorageBehaviour.STORAGE_BEHAVIOUR, storageBehaviourCustomBehaviour -> storageBehaviourCustomBehaviour.getBehaviour().loadPlayerAdvancements(serverPlayer.getBukkitEntity()), vanillaLogic, Converter.DummyConverter.getInstance(JsonElement.class));
    }

    public static Path savePlayerAdvancements(ServerPlayer serverPlayer, JsonElement serializedAdvancements, Supplier<Path> vanillaLogic) {
        return evaluate(StorageBehaviour.STORAGE_BEHAVIOUR, storageBehaviourCustomBehaviour -> storageBehaviourCustomBehaviour.getBehaviour().savePlayerAdvancements(serverPlayer.getBukkitEntity(), serializedAdvancements), vanillaLogic, Converter.DummyConverter.getInstance(Path.class));
    }

    public static JsonElement loadPlayerStats(UUID playerUUID, Supplier<JsonElement> vanillaLogic) {
        return evaluate(StorageBehaviour.STORAGE_BEHAVIOUR, storageBehaviourCustomBehaviour -> storageBehaviourCustomBehaviour.getBehaviour().loadPlayerStats(playerUUID), vanillaLogic, Converter.DummyConverter.getInstance(JsonElement.class));
    }

    public static Path savePlayerStats(UUID playerUUID, JsonElement serializedAdvancements, Supplier<Path> vanillaLogic) {
        return evaluate(StorageBehaviour.STORAGE_BEHAVIOUR, storageBehaviourCustomBehaviour -> storageBehaviourCustomBehaviour.getBehaviour().savePlayerStats(playerUUID, serializedAdvancements), vanillaLogic, Converter.DummyConverter.getInstance(Path.class));
    }

    public static InputStream loadPlayerNBTData(@NotNull UUID playerUUID, @Nullable Player player, Supplier<InputStream> vanillaLogic){
        return evaluate(StorageBehaviour.STORAGE_BEHAVIOUR, storageBehaviourCustomBehaviour -> storageBehaviourCustomBehaviour.getBehaviour().loadPlayerNBTData(playerUUID, player), vanillaLogic, Converter.DummyConverter.getInstance(InputStream.class));
    }

    public static OutputStream savePlayerNBTData(@NotNull Player player, Supplier<OutputStream> vanillaLogic){
        return evaluate(StorageBehaviour.STORAGE_BEHAVIOUR, storageBehaviourCustomBehaviour -> storageBehaviourCustomBehaviour.getBehaviour().savePlayerNBTData(player), vanillaLogic, Converter.DummyConverter.getInstance(OutputStream.class));
    }
}
