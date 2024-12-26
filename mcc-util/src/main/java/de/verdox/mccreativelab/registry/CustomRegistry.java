package de.verdox.mccreativelab.registry;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public abstract class CustomRegistry<T> implements Iterable<Map.Entry<Key, T>> {
    private final AtomicInteger idCounter = new AtomicInteger();
    private final Map<Key, T> registry = new HashMap<>();
    private final Map<T, Key> dataToKeyMapping = new HashMap<>();
    private final Map<Key, Integer> keyToId = new HashMap<>();
    private final Map<Integer, Key> idToKey = new HashMap<>();
    private boolean freeze;

    protected <S extends T> Reference<S> register(Key namespacedKey, S data) {
        if (freeze)
            throw new IllegalStateException("Registry already frozen");
        int id = idCounter.getAndIncrement();
        checkForDuplicates(namespacedKey, data);
        registry.put(namespacedKey, data);
        keyToId.put(namespacedKey, id);
        idToKey.put(id, namespacedKey);
        dataToKeyMapping.put(data, namespacedKey);

        return (Reference<S>) Reference.create(this, namespacedKey);
    }

    public void freeze() {
        onFreeze();
    }

    public int getSize() {
        return registry.size();
    }

    protected void clear() {
        this.freeze = false;
        idCounter.set(0);
        registry.clear();
        dataToKeyMapping.clear();
        keyToId.clear();
        idToKey.clear();
    }

    protected void onFreeze() {
    }

    public boolean isEmpty() {
        return registry.isEmpty();
    }

    public Iterator<T> values() {
        return registry.values().iterator();
    }

    public Iterator<Reference<T>> referenceIterator() {
        return new Iterator<>() {
            private final Iterator<T> iterator = values();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Reference<T> next() {
                T value = iterator.next();
                return Reference.create(CustomRegistry.this, getKey(value));
            }
        };
    }

    @NotNull
    @Override
    public Iterator<Map.Entry<Key, T>> iterator() {
        return registry.entrySet().iterator();
    }

    @Override
    public Spliterator<Map.Entry<Key, T>> spliterator() {
        return registry.entrySet().spliterator();
    }

    public Iterator<Key> keys() {
        return registry.keySet().iterator();
    }

    public Stream<Key> streamKeys() {
        return registry.keySet().stream();
    }

    public Stream<T> streamValues() {
        return registry.values().stream();
    }

    @Nullable
    public Key getKey(T data) {
        return dataToKeyMapping.get(data);
    }

    public boolean contains(Key namespacedKey) {
        return registry.containsKey(namespacedKey);
    }

    @Nullable
    public Key getKey(int id) {
        return idToKey.getOrDefault(id, null);
    }

    public int getId(Key namespacedKey) {
        return keyToId.getOrDefault(namespacedKey, -1);
    }

    @Nullable
    public T get(Key namespacedKey) {
        return registry.get(namespacedKey);
    }

    public Reference<T> getAsReference(Key namespacedKey) {
        return Reference.create(this, namespacedKey);
    }

    public Reference<T> getAsReference(T value) {
        return getAsReference(getKey(value));
    }

    public @Nullable T get(int id) {
        Key key = getKey(id);
        if (key == null)
            return null;
        return registry.get(key);
    }

    protected void checkForDuplicates(Key namespacedKey, T data) {
        if (registry.containsKey(namespacedKey))
            throw new IllegalStateException(data.getClass()
                .getSimpleName() + " with key " + namespacedKey.toString() + " already registered");
        if (dataToKeyMapping.containsKey(data))
            throw new IllegalStateException(data.getClass()
                .getSimpleName() + " already registered with key " + dataToKeyMapping
                .get(data).toString());
    }

}
