package de.verdox.mccreativelab.advancement;

import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CraftAdvancementRewardBuilder implements AdvancementRewardBuilder{
    int experience;
    final List<NamespacedKey> lootTables = new ArrayList<>();
    final List<NamespacedKey> recipes = new ArrayList<>();

    public @NotNull CraftAdvancementRewardBuilder setExperience(int experience) {
        this.experience = experience;
        return this;
    }

    public @NotNull CraftAdvancementRewardBuilder withLootTable(@NotNull NamespacedKey namespacedKey){
        this.lootTables.add(namespacedKey);
        return this;
    }

    public @NotNull CraftAdvancementRewardBuilder withRecipe(@NotNull NamespacedKey namespacedKey){
        this.recipes.add(namespacedKey);
        return this;
    }

    public AdvancementRewards build(){
        return new AdvancementRewards(
            this.experience,
            net.minecraft.core.HolderSet.direct(lootTables.stream().map(key -> net.minecraft.server.MinecraftServer.getServer().reloadableRegistries().lookup().lookupOrThrow(Registries.LOOT_TABLE).getOrThrow(ResourceKey.create(Registries.LOOT_TABLE, CraftNamespacedKey.toMinecraft(key)))).toList()),
            recipes.stream().map(key -> ResourceKey.create(Registries.RECIPE, CraftNamespacedKey.toMinecraft(key))).toList(),
            Optional.empty()
            );
    }
}
