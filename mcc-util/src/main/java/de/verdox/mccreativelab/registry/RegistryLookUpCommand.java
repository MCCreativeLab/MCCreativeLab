package de.verdox.mccreativelab.registry;

import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class RegistryLookUpCommand<T> extends AbstractRegistryLookUpCommand<T, CustomRegistry<T>> {

    public RegistryLookUpCommand(@NotNull String name, String subCommand, CustomRegistry<T> registry, BiConsumer<Player, T> consumeEntry) {
        super(name, subCommand, registry, consumeEntry);
    }

    public RegistryLookUpCommand(@NotNull String name, CustomRegistry<T> registry, BiConsumer<Player, T> consumeEntry) {
        super(name, registry, consumeEntry);
    }

    @Override
    protected Stream<String> streamRegistryKeys() {
        return registry.streamKeys().map(Key::asString);
    }

    @Override
    protected boolean contains(String key) {
        NamespacedKey namespacedKey = NamespacedKey.fromString(key);
        return registry.contains(namespacedKey);
    }

    @Override
    protected T getValueFromRegistry(String key) {
        NamespacedKey namespacedKey = NamespacedKey.fromString(key);
        return registry.get(namespacedKey);
    }
}
