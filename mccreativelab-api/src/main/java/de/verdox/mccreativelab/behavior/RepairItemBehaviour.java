package de.verdox.mccreativelab.behavior;

import de.verdox.mccreativelab.CustomBehaviour;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface RepairItemBehaviour extends Behaviour {
    CustomBehaviour<RepairItemBehaviour> REPAIR_ITEM_BEHAVIOUR = new CustomBehaviour<>(RepairItemBehaviour.class, new RepairItemBehaviour() {},"MCCLab - RepairItemBehaviour");
    default boolean canCombine(@NotNull ItemStack first, @NotNull ItemStack second){
        return first.getType().equals(second.getType());
    }

    @NotNull
    default ItemStack assemble(@NotNull ItemStack first, @NotNull ItemStack second){
        return new ItemStack(first.getType());
    }

}
