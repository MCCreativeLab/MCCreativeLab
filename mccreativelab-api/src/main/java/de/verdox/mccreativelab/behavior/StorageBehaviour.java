package de.verdox.mccreativelab.behavior;

import com.google.gson.JsonElement;
import de.verdox.mccreativelab.CustomBehaviour;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.UUID;

/**
 * Changes behaviour of the saving / loading mechanics of the server
 */
public interface StorageBehaviour extends Behaviour {
    CustomBehaviour<StorageBehaviour> STORAGE_BEHAVIOUR = new CustomBehaviour<>(StorageBehaviour.class, new StorageBehaviour() {
    }, "MCCLab - StorageBehaviour");

    /**
     * Called when the server tries to load player advancements
     *
     * @param player the player
     * @return The advancements in json loaded
     */
    @NotNull
    default BehaviourResult.Object<JsonElement> loadPlayerAdvancements(@NotNull Player player) {
        return BehaviourResult.Object.DEFAULT_INSTANCE;
    }

    /**
     * Called when the server tries to save player advancements
     * If the behaviour returns a null result the server won't save the advancements at all
     *
     * @param player                 the player
     * @param serializedAdvancements the advancements serialized into json
     * @return the path the server should save the json to
     */
    @NotNull
    default BehaviourResult.Object<Path> savePlayerAdvancements(@NotNull Player player, @NotNull JsonElement serializedAdvancements) {
        return BehaviourResult.Object.DEFAULT_INSTANCE;
    }

    /**
     * Called when the server tries to load player stats
     *
     * @param playerUUID the player uuid
     * @return The stats in json loaded
     */
    @NotNull
    default BehaviourResult.Object<JsonElement> loadPlayerStats(@NotNull UUID playerUUID) {
        return BehaviourResult.Object.DEFAULT_INSTANCE;
    }

    /**
     * Called when the server tries to save player stats
     * If the behaviour returns a null result the server won't save the stats at all
     *
     * @param playerUUID             the player uuid
     * @param serializedAdvancements the stats serialized into json
     * @return the path the server should save the json to
     */
    @NotNull
    default BehaviourResult.Object<Path> savePlayerStats(@NotNull UUID playerUUID, @NotNull JsonElement serializedAdvancements) {
        return BehaviourResult.Object.DEFAULT_INSTANCE;
    }

    /**
     * Called when the server tries to load player nbt data
     *
     * @param playerUUID the player uuid
     * @param player     the player if available at the time of loading the data
     * @return the InputStream where the data will be serialized into
     */
    @NotNull
    default BehaviourResult.Object<InputStream> loadPlayerNBTData(@NotNull UUID playerUUID, @Nullable Player player) {
        return BehaviourResult.Object.DEFAULT_INSTANCE;
    }

    /**
     * Called when the server tries to save player nbt data
     *
     * @param player the player
     * @return the OutputStream where the data will be deserialized from
     */
    @NotNull
    default BehaviourResult.Object<OutputStream> savePlayerNBTData(@NotNull Player player) {
        return BehaviourResult.Object.DEFAULT_INSTANCE;
    }
}
