package de.verdox.mccreativelab.data;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public interface FakeEnum {
    @NotNull
    static <T extends Keyed> T valueOf(@NotNull String name, @NotNull Registry<T> storage){
        return storage.get(NamespacedKey.minecraft(name.replace(".","_").toLowerCase(Locale.ROOT)));
    }

    @NotNull
    static <T extends Keyed> List<T> values(@NotNull Registry<T> storage){
        return storage.stream().toList();
    }

    @NotNull
    static String restoreEnumNameSchemeFromKey(@NotNull NamespacedKey namespacedKey){
        return namespacedKey.value().replace(".","_").toUpperCase(Locale.ROOT);
    }
}
