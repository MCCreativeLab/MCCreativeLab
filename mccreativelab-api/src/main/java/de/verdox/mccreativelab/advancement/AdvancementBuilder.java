package de.verdox.mccreativelab.advancement;

import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface AdvancementBuilder {
    @NotNull AdvancementBuilder withParent(@NotNull NamespacedKey namespacedKey);
    @NotNull AdvancementBuilder withDisplay(@NotNull Consumer<AdvancementDisplayBuilder> craftAdvancementDisplayBuilder);
    @NotNull AdvancementBuilder withRewards(@NotNull Consumer<AdvancementRewardBuilder> craftAdvancementRewardBuilder);
    @NotNull AdvancementBuilder withRequirements(@NotNull Consumer<AdvancementRequirementsBuilder> craftAdvancementRewardBuilder);
    @NotNull Advancement addToBukkit(@NotNull NamespacedKey namespacedKey);
}
