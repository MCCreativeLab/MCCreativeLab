package de.verdox.mccreativelab.registry;

import net.kyori.adventure.key.Key;

public class OpenRegistry<T> extends CustomRegistry<T> {
    private final String namespace;

    public OpenRegistry(String namespace){
        this.namespace = namespace;
    }

    public <S extends T> Reference<S> register(String key, S data) {
        return this.register(Key.key(namespace, key), data);
    }

    public <S extends T> Reference<S> register(Key namespacedKey, S data) {
        return super.register(namespacedKey, data);
    }

    @Override
    public void clear() {
        super.clear();
    }
}
