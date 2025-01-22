package de.verdox.mccreativelab.recipe;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public record CustomItemData(@NotNull Material material, int customModelData) {
    @NotNull
    public ItemStack createStack() {
        var stack = new ItemStack(material);
        if (customModelData != 0)
            stack.editMeta(meta -> meta.setCustomModelData(customModelData));
        return stack;
    }

    public boolean isSame(@NotNull ItemStack stackToCheck) {
        if (!stackToCheck.getType().equals(material))
            return false;
        if (customModelData == 0)
            return !stackToCheck.hasItemMeta() || !stackToCheck.getItemMeta().hasCustomModelData() || stackToCheck
                .getItemMeta().getCustomModelData() == customModelData;
        else
            return stackToCheck.getType().equals(material)
                && stackToCheck.hasItemMeta()
                && stackToCheck.getItemMeta().hasCustomModelData()
                && stackToCheck.getItemMeta().getCustomModelData() == customModelData;
    }

    @NotNull
    public static CustomItemData fromItemStack(@NotNull ItemStack stack) {
        return new CustomItemData(stack.getType(), stack.hasItemMeta() ? stack.getItemMeta().hasCustomModelData() ? stack.getItemMeta().getCustomModelData() : 0 : 0);
    }
}
