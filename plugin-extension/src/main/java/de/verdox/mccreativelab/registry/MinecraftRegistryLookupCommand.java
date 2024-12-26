package de.verdox.mccreativelab.registry;

import de.verdox.mccreativelab.wrapper.registry.MCCRegistry;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class MinecraftRegistryLookupCommand<T> extends AbstractRegistryLookUpCommand<T, MCCRegistry<T>> {
    public MinecraftRegistryLookupCommand(@NotNull String name, String subCommand, MCCRegistry<T> registry, BiConsumer<Player, T> consumeEntry) {
        super(name, subCommand, registry, consumeEntry);
    }

    public MinecraftRegistryLookupCommand(@NotNull String name, MCCRegistry<T> registry, BiConsumer<Player, T> consumeEntry) {
        super(name, registry, consumeEntry);
    }

    @Override
    protected Stream<String> streamRegistryKeys() {
        return registry.keySet().stream().map(Key::asString);
    }

    @Override
    protected boolean contains(String key) {
        return registry.containsKey(Key.key(key));
    }

    @Override
    protected T getValueFromRegistry(String key) {
        return registry.get(Key.key(key));
    }
}
