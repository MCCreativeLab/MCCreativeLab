package de.verdox.mccreativelab.advancement;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public interface AdvancementRewardBuilder {
    @NotNull AdvancementRewardBuilder setExperience(int experience);

    @NotNull AdvancementRewardBuilder withLootTable(@NotNull NamespacedKey namespacedKey);

    @NotNull AdvancementRewardBuilder withRecipe(@NotNull NamespacedKey namespacedKey);
}
