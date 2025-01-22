package de.verdox.mccreativelab.behavior;

import de.verdox.mccreativelab.CustomBehaviour;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the internal server behaviour when items are fomatted
 */
public interface ItemDisplayBehaviour extends Behaviour {
    CustomBehaviour<ItemDisplayBehaviour> BASIC_LORE_BEHAVIOUR = new CustomBehaviour<>(ItemDisplayBehaviour.class, new ItemDisplayBehaviour() {}, "MCCLab - WorldGenerationBehaviour");

    /**
     * Returns the basic style an item lore gets.
     * @param stack The ItemStack getting the format
     * @return the style
     */
    @NotNull
    default Style basicLoreStyle(@NotNull ItemStack stack){
        return Style.empty().color(TextColor.fromCSSHexString("#aa00aa")).decoration(TextDecoration.ITALIC, true);
    }

    /**
     * Returns the basic style an item name gets.
     * @param stack The ItemStack getting the format
     * @return the style
     */
    @NotNull
    default Style basicCustomNameStyle(@NotNull ItemStack stack){
        return Style.empty().decoration(TextDecoration.ITALIC, true);
    }
}
