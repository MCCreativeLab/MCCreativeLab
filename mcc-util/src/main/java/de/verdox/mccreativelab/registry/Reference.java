package de.verdox.mccreativelab.registry;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.NamespacedKey;

import java.util.Objects;

public interface Reference<T> extends Keyed {
    Key getKey();
    CustomRegistry<T> getRegistry();
    T unwrapValue();
    static <T> Reference<T> create(CustomRegistry<T> registry, Key namespacedKey){
        Objects.requireNonNull(registry);
        Objects.requireNonNull(namespacedKey);
        return new ReferenceImpl<>(registry, namespacedKey);
    }
}
