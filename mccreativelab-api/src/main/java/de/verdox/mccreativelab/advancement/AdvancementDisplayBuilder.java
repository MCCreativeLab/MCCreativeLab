package de.verdox.mccreativelab.advancement;

import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface AdvancementDisplayBuilder {
    @NotNull AdvancementDisplayBuilder setFrame(@NotNull AdvancementDisplay.Frame frame);

    @NotNull AdvancementDisplayBuilder setTitle(@NotNull Component title);

    @NotNull AdvancementDisplayBuilder setDescription(@NotNull Component description);

    @NotNull AdvancementDisplayBuilder setIcon(@NotNull ItemStack icon);

    @NotNull AdvancementDisplayBuilder setShowToast(boolean showToast);

    @NotNull AdvancementDisplayBuilder setAnnounceToChat(boolean announceToChat);

    @NotNull AdvancementDisplayBuilder setHidden(boolean hidden);

    @NotNull AdvancementDisplayBuilder setBackground(@NotNull NamespacedKey background);

    @NotNull AdvancementDisplayBuilder setX(float x);

    @NotNull AdvancementDisplayBuilder setY(float y);
}
