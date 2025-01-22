package de.verdox.mccreativelab.behavior.interaction;

import de.verdox.mccreativelab.InteractionResult;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public record ItemStackInteraction(@NotNull InteractionResult interactionResult, @NotNull ItemStack stack){
}
