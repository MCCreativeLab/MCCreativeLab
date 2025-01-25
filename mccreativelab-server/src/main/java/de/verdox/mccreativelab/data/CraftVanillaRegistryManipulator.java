package de.verdox.mccreativelab.data;

import com.google.common.collect.ImmutableSet;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.sound.Sound;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.PoiType;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftPoiType;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.Villager;
import org.bukkit.entity.memory.MemoryKey;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CraftVanillaRegistryManipulator implements VanillaRegistryManipulator {
    public static List<Runnable> CUSTOM_BOOTSTRAPPERS = new LinkedList<>();

    @Override
    public @NotNull RegistryEntryReference<Attribute> createAttribute(@NotNull NamespacedKey namespacedKey, double defaultValue, double min, double max) {
        if (namespacedKey.namespace().equals(NamespacedKey.MINECRAFT_NAMESPACE))
            throw new IllegalStateException("Cannot create attributes in minecraft namespace");
        CUSTOM_BOOTSTRAPPERS.add(() -> {
            ResourceLocation resourceLocation = CraftNamespacedKey.toMinecraft(namespacedKey);
            var key = "attribute.name." + resourceLocation.getPath();
            Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, resourceLocation, new RangedAttribute(key, defaultValue, min, max).setSyncable(false));

        });
        return new RegistryEntryReference<>(namespacedKey, () -> Attribute.getAttribute(namespacedKey));
    }

    @Override
    public <T> @NotNull RegistryEntryReference<MemoryKey<T>> createMemoryKey(@NotNull NamespacedKey namespacedKey, @NotNull Class<? extends T> storedType) {
        if (namespacedKey.namespace().equals(NamespacedKey.MINECRAFT_NAMESPACE))
            throw new IllegalStateException("Cannot create MemoryKeys in minecraft namespace");
        CUSTOM_BOOTSTRAPPERS.add(() -> {
            ResourceLocation resourceLocation = CraftNamespacedKey.toMinecraft(namespacedKey);
            Registry.register(BuiltInRegistries.MEMORY_MODULE_TYPE, resourceLocation, new MemoryModuleType(Optional.empty()));
        });
        return (RegistryEntryReference<MemoryKey<T>>) new RegistryEntryReference<>(namespacedKey, () -> (T) MemoryKey.getByKey(namespacedKey));
    }

    @Override
    public @NotNull RegistryEntryReference<PoiType> createPoiType(@NotNull NamespacedKey namespacedKey, @NotNull Set<BlockData> states, int ticketCount, int searchDistance) {
        if (namespacedKey.namespace().equals(NamespacedKey.MINECRAFT_NAMESPACE))
            throw new IllegalStateException("Cannot create PoiTypes in minecraft namespace");
        CUSTOM_BOOTSTRAPPERS.add(() -> {
            ResourceKey<net.minecraft.world.entity.ai.village.poi.PoiType> resourceKey = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, CraftNamespacedKey.toMinecraft(namespacedKey));

            PoiTypes.register(BuiltInRegistries.POINT_OF_INTEREST_TYPE, resourceKey, states.stream()
                .map(blockData -> ((CraftBlockData) blockData).getState())
                .collect(Collectors.toSet()), ticketCount, searchDistance);

        });
        return new RegistryEntryReference<>(namespacedKey, () -> PoiType.getPoiType(namespacedKey));
    }

    @Override
    public @NotNull RegistryEntryReference<Villager.Profession> createProfession(@NotNull NamespacedKey namespacedKey, @NotNull Predicate<PoiType> heldWorkStation, @NotNull Predicate<PoiType> acquirableWorkstation, @NotNull Set<Material> gatherableItems, @NotNull Set<Material> secondaryJobSites, Sound.@NotNull Type workSound) {
        if (namespacedKey.namespace().equals(NamespacedKey.MINECRAFT_NAMESPACE))
            throw new IllegalStateException("Cannot create VillagerProfessions in minecraft namespace");
        CUSTOM_BOOTSTRAPPERS.add(() -> {
            Predicate<Holder<net.minecraft.world.entity.ai.village.poi.PoiType>> heldWorkstationNms = poiTypeHolder -> heldWorkStation.test(CraftPoiType.minecraftToBukkit(poiTypeHolder.value()));
            Predicate<Holder<net.minecraft.world.entity.ai.village.poi.PoiType>> acquirableWorkstationNms = poiTypeHolder -> acquirableWorkstation.test(CraftPoiType.minecraftToBukkit(poiTypeHolder.value()));
            ImmutableSet<Item> items = ImmutableSet.copyOf(gatherableItems.stream().map(CraftMagicNumbers::getItem).collect(Collectors.toSet()));
            ImmutableSet<Block> jobBlocks = ImmutableSet.copyOf(secondaryJobSites.stream().map(CraftMagicNumbers::getBlock).collect(Collectors.toSet()));
            @Nullable SoundEvent soundEvent = workSound == null ? null : BuiltInRegistries.SOUND_EVENT.get(PaperAdventure.asVanilla(workSound.key()));
            VillagerProfession villagerProfession = new VillagerProfession(namespacedKey.getKey(), heldWorkstationNms, acquirableWorkstationNms, items, jobBlocks, soundEvent);
            Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, CraftNamespacedKey.toMinecraft(namespacedKey), villagerProfession);
        });
        return new RegistryEntryReference<>(namespacedKey, () -> org.bukkit.Registry.VILLAGER_PROFESSION.get(namespacedKey));
    }
}
