package de.verdox.mccreativelab.registry;

import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record ReferenceImpl<T>(CustomRegistry<T> registry, Key namespacedKey) implements Reference<T>{
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReferenceImpl<?> reference = (ReferenceImpl<?>) o;
        return Objects.equals(registry, reference.registry) && Objects.equals(namespacedKey, reference.namespacedKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registry, namespacedKey);
    }

    @Override
    public Key getKey() {
        return namespacedKey;
    }

    @Override
    public CustomRegistry<T> getRegistry() {
        return registry;
    }

    @Override
    public T unwrapValue() {
        return registry.get(namespacedKey);
    }

    @Override
    public @NotNull Key key() {
        return getKey();
    }
}
