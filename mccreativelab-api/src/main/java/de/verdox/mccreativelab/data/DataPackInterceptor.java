package de.verdox.mccreativelab.data;

import com.google.gson.JsonObject;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public interface DataPackInterceptor {
    @NotNull DataPackInterceptor exclude(@NotNull String pathContains);

    @NotNull DataPackInterceptor exclude(@NotNull PackAssetType packAssetType);

    @NotNull DataPackInterceptor exclude(@NotNull PackAssetType packAssetType, @NotNull Key key);

    @NotNull DataPackInterceptor onInstall(@NotNull Consumer<DataPackAsset> installCallback);

    /**
     * Modifies a data pack element loaded into the server. The provided function modifies the asset and returns true if the asset should be loaded.
     *
     * @param packAssetType - The Pack Asset Type
     * @param key      - The Key
     * @param modifier - The modification function
     * @return         - The interceptor
     */
    @NotNull DataPackInterceptor modify(@NotNull PackAssetType packAssetType, @NotNull Key key, @NotNull Function<DataPackAsset, Boolean> modifier);

    /**
     * Modifies a data pack element loaded into the server. The provided function modifies the asset and returns true if the asset should be loaded.
     *
     * @param modifier - The modification function
     * @return         - The interceptor
     */
    @NotNull DataPackInterceptor modify(@NotNull Function<DataPackAsset, Boolean> modifier);

    record DataPackAsset(@NotNull PackAssetType packAssetType, @NotNull Key key, @NotNull JsonObject jsonObject) {}

    record PackAssetType(@NotNull String parentFolder) {
        public static final PackAssetType ADVANCEMENT = new PackAssetType("advancement");
        public static final PackAssetType CHAT_TYPE = new PackAssetType("chat_type");
        public static final PackAssetType DAMAGE_TYPE = new PackAssetType("damage_type");
        public static final PackAssetType DIMENSION_TYPE = new PackAssetType("dimension_type");
        public static final PackAssetType LOOT_TABLE = new PackAssetType("loot_table");
        public static final PackAssetType RECIPE = new PackAssetType("recipe");
        public static final PackAssetType STRUCTURES = new PackAssetType("structures");
        public static final PackAssetType TAGS = new PackAssetType("tags");
        public static final PackAssetType TRIM_MATERIAL = new PackAssetType("trim_material");
        public static final PackAssetType TRIM_PATTERN = new PackAssetType("trim_pattern");
        public static final PackAssetType WORLDGEN = new PackAssetType("worldgen");

        @NotNull
        public String getParentFolder() {
            return parentFolder;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PackAssetType that = (PackAssetType) o;
            return Objects.equals(parentFolder, that.parentFolder);
        }

        @Override
        public int hashCode() {
            return Objects.hash(parentFolder);
        }
    }
}
