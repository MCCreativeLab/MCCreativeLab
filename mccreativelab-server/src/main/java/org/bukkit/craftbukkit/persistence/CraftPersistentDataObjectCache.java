package org.bukkit.craftbukkit.persistence;

import com.google.common.base.Preconditions;
import net.minecraft.nbt.Tag;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataObject;
import org.bukkit.persistence.PersistentDataObjectCache;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class CraftPersistentDataObjectCache implements PersistentDataObjectCache {
    private static final Set<CraftPersistentDataContainer> weakReferences = Collections.newSetFromMap(new WeakHashMap<>());

    public static int saveAllPersistentDataObjects(){
        synchronized (weakReferences){
            int counter = 0;
            for (CraftPersistentDataContainer weakReference : weakReferences) {
                CraftPersistentDataObjectCache craftPersistentDataObjectCache = (CraftPersistentDataObjectCache) weakReference.getPersistentDataObjectCache();
                craftPersistentDataObjectCache.saveObjectsToTags(false);
                craftPersistentDataObjectCache.persistentDataObjectMap.clear();
                counter++;
            }
            return counter;
        }
    }

    private final CraftPersistentDataContainer parentContainer;
    private final CraftPersistentDataTypeRegistry registry;
    private final Map<String, Tag> tags;
    final Map<NamespacedKey, PersistentDataObject> persistentDataObjectMap = new ConcurrentHashMap<>();
    CraftPersistentDataObjectCache(CraftPersistentDataContainer parentContainer, CraftPersistentDataTypeRegistry registry, Map<String, Tag> tags){
        this.parentContainer = parentContainer;
        this.registry = registry;
        this.tags = tags;
    }
    @Override
    public <T extends PersistentDataObject> @NotNull T loadOrSupplyPersistentDataObject(@NotNull NamespacedKey key, java.util.function.Supplier<T> newObjectSupplier) {
        Preconditions.checkArgument(key != null, "The NamespacedKey key cannot be null");
        Preconditions.checkArgument(newObjectSupplier != null, "The provided newObjectSupplier cannot be null");
        if (persistentDataObjectMap.containsKey(key))
            return (T) persistentDataObjectMap.get(key);

        T newObject = newObjectSupplier.get();
        Preconditions.checkArgument(newObject != null, "The newObject supplied cannot be null");

        readObjectFromTags(key, newObject);
        persistentDataObjectMap.put(key, newObject);
        markParentAsDirty();
        synchronized (weakReferences){
            weakReferences.add(parentContainer);
        }
        return newObject;
    }

    @Override
    public <T extends PersistentDataObject> @Nullable T loadPersistentDataObject(@NotNull NamespacedKey key, @NotNull Class<? extends T> type) {
        Preconditions.checkArgument(key != null, "The NamespacedKey key cannot be null");
        Preconditions.checkArgument(type != null, "The provided type cannot be null");
        if (persistentDataObjectMap.containsKey(key))
            return (T) persistentDataObjectMap.get(key);
        return null;
    }

    @Override
    public void removePersistentDataObject(@NotNull NamespacedKey key, boolean serializeBeforeRemoval) {
        Preconditions.checkArgument(key != null, "The NamespacedKey key cannot be null");
        org.bukkit.persistence.PersistentDataObject oldValue = persistentDataObjectMap.remove(key);
        if(serializeBeforeRemoval && oldValue != null)
            saveObjectToTags(key, oldValue, true);
        else {
            markParentAsDirty();
        }
        if(persistentDataObjectMap.isEmpty())
            synchronized (weakReferences){
                weakReferences.remove(this);
            }
    }

    void saveObjectsToTags(boolean removeWhenEmpty) {
        for (NamespacedKey namespacedKey : Set.copyOf(persistentDataObjectMap.keySet())) {
            org.bukkit.persistence.PersistentDataObject persistentDataObject = persistentDataObjectMap.get(namespacedKey);
            saveObjectToTags(namespacedKey, persistentDataObject, removeWhenEmpty);
        }
    }

    void saveObjectToTags(@NotNull NamespacedKey key, @NotNull org.bukkit.persistence.PersistentDataObject persistentDataObject, boolean removeWhenEmpty){
        try{
            PersistentDataContainer serialized = persistentDataObject.serialize(parentContainer.getAdapterContext());
            if(!serialized.isEmpty())
                tags.put(key.toString(), registry.wrap(PersistentDataType.TAG_CONTAINER, PersistentDataType.TAG_CONTAINER.toPrimitive(serialized, parentContainer.getAdapterContext())));
            else if(removeWhenEmpty) {
                tags.remove(key.toString());
            }
            markParentAsDirty();
        }
        catch (Throwable e){
            org.bukkit.Bukkit.getLogger().log(Level.WARNING, "An error occured while saving the persistent data object "+ persistentDataObject +" with the key "+key.asString()+" ", e);
        }
    }

    void readObjectFromTags(@NotNull NamespacedKey key, @NotNull org.bukkit.persistence.PersistentDataObject persistentDataObject){
        try{
            if (parentContainer.has(key, PersistentDataType.TAG_CONTAINER)) {
                PersistentDataContainer persistentDataContainer = parentContainer.get(key, PersistentDataType.TAG_CONTAINER);
                if (persistentDataContainer != null)
                    persistentDataObject.deSerialize(persistentDataContainer);
            }
        } catch (Throwable e){
            org.bukkit.Bukkit.getLogger().log(Level.WARNING, "An error occured while loading the persistent data object "+ persistentDataObject +" with the key "+key.asString()+" ", e);
        }
    }

    void serializeObjectAtKeyBeforeLookup(NamespacedKey key){
        if(persistentDataObjectMap.containsKey(key))
            saveObjectToTags(key, persistentDataObjectMap.get(key), true);
    }

    void markParentAsDirty(){
        if(parentContainer instanceof DirtyCraftPersistentDataContainer dirtyCraftPersistentDataContainer)
            dirtyCraftPersistentDataContainer.dirty(true);
    }
}
