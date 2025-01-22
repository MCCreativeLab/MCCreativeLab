package de.verdox.mccreativelab.data;

import net.kyori.adventure.sound.Sound;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
//import org.bukkit.PoiType;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Villager;
import org.bukkit.entity.memory.MemoryKey;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface VanillaRegistryManipulator {
    @NotNull RegistryEntryReference<Attribute> createAttribute(@NotNull NamespacedKey namespacedKey, double defaultValue, double min, double max);

    @NotNull <T> RegistryEntryReference<MemoryKey<T>> createMemoryKey(@NotNull NamespacedKey namespacedKey, @NotNull Class<? extends T> storedType);

    //@NotNull RegistryEntryReference<PoiType> createPoiType(@NotNull NamespacedKey namespacedKey, @NotNull Set<BlockData> states, int ticketCount, int searchDistance);

    @NotNull RegistryEntryReference<Villager.Profession> createProfession(@NotNull NamespacedKey namespacedKey, @NotNull Predicate<PoiType> heldWorkStation, @NotNull Predicate<PoiType> acquirableWorkstation, @NotNull Set<Material> gatherableItems, @NotNull Set<Material> secondaryJobSites, @NotNull Sound.Type workSound);

    record RegistryEntryReference<T>(@NotNull NamespacedKey namespacedKey, @NotNull Supplier<T> dataSupplier){}
}
