package de.verdox.mccreativelab.advancement;

import io.papermc.paper.advancement.AdvancementDisplay;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CraftAdvancementDisplayBuilder implements AdvancementDisplayBuilder{
    Component title = Component.empty();
    Component description = Component.empty();
    ItemStack icon = new ItemStack(Material.STONE);
    @Nullable NamespacedKey background;
    AdvancementDisplay.Frame frame = AdvancementDisplay.Frame.TASK;
    boolean showToast = true;
    boolean announceToChat = true;
    boolean isHidden = false;
    private float x;
    private float y;
    boolean customCoordinates;


    @Override
    public @NotNull AdvancementDisplayBuilder setFrame(AdvancementDisplay.@NotNull Frame frame) {
        this.frame = frame;
        return this;
    }

    @Override
    public @NotNull AdvancementDisplayBuilder setTitle(@NotNull Component title) {
        this.title = title;
        return this;
    }

    @Override
    public @NotNull AdvancementDisplayBuilder setDescription(@NotNull Component description) {
        this.description = description;
        return this;
    }

    @Override
    public @NotNull AdvancementDisplayBuilder setIcon(@NotNull ItemStack icon) {
        this.icon = icon;
        return this;
    }

    @Override
    public @NotNull AdvancementDisplayBuilder setShowToast(boolean showToast) {
        this.showToast = showToast;
        return this;
    }

    @Override
    public @NotNull AdvancementDisplayBuilder setAnnounceToChat(boolean announceToChat) {
        this.announceToChat = announceToChat;
        return this;
    }

    @Override
    public @NotNull AdvancementDisplayBuilder setHidden(boolean hidden) {
        isHidden = hidden;
        return this;
    }

    @Override
    public @NotNull AdvancementDisplayBuilder setBackground(@NotNull NamespacedKey background) {
        this.background = background;
        return this;
    }

    @Override
    public @NotNull AdvancementDisplayBuilder setX(float x) {
        this.x = x;
        return this;
    }

    @Override
    public @NotNull AdvancementDisplayBuilder setY(float y) {
        this.y = y;
        return this;
    }

    public DisplayInfo buildNMS(){
        DisplayInfo displayInfo = new DisplayInfo(
            CraftItemStack.asNMSCopy(icon), 
            PaperAdventure.asVanilla(title), 
            PaperAdventure.asVanilla(description),
            background == null ? Optional.empty() : Optional.of(CraftNamespacedKey.toMinecraft(background)),
            asVanillaFrame(frame),
            showToast,
            announceToChat,
            isHidden
            );
        displayInfo.setLocation(x, y);
        return displayInfo;
    }

    public static @NotNull AdvancementType asVanillaFrame(final @NotNull AdvancementDisplay.Frame frameType) {
        return switch (frameType) {
            case TASK -> AdvancementType.TASK;
            case CHALLENGE -> AdvancementType.CHALLENGE;
            case GOAL -> AdvancementType.GOAL;
        };
    }
}
