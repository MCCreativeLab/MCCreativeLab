package de.verdox.mccreativelab;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@FunctionalInterface
public interface BasicItemFormat {
    CustomBehaviour<BasicItemFormat> BASIC_FORMAT = new CustomBehaviour<>(BasicItemFormat.class, stack -> {}, "BasicItemFormat");

    /**
     * Applies the BasicItemFormat to an {@link ItemStack}
     * @param stack the item stack
     */
    void applyItemFormat(@NotNull ItemStack stack);

    /**
     * Applies the BasicItemFormat to an {@link ItemStack} and returns the item
     * @param stack the item stack
     * @return the item stack
     */
    @NotNull
    static ItemStack applyItemFormatAndReturn(@NotNull ItemStack stack){
        return applyItemFormatAndReturn(stack, BASIC_FORMAT.getBehaviour());
    }

    /**
     * Applies a BasicItemFormat to an {@link ItemStack} and returns the item
     * @param stack the item stack
     * @param basicItemFormat the item format
     * @return the item stack
     */
    @NotNull
    static ItemStack applyItemFormatAndReturn(@NotNull ItemStack stack, @NotNull BasicItemFormat basicItemFormat) {
        Objects.requireNonNull(stack);
        Objects.requireNonNull(basicItemFormat);
        if(needsConversion(stack)){
            basicItemFormat.applyItemFormat(stack);
            applyConversionTag(stack);
        }
        return stack;
    }

    /**
     * Forces the BasicItemFormat to an {@link ItemStack} and returns the item.
     *
     * @param stack the item stack
     * @return the item stack
     */
    @NotNull
    static ItemStack forceItemFormat(@NotNull ItemStack stack, @NotNull BasicItemFormat basicItemFormat){
        Objects.requireNonNull(stack);
        Objects.requireNonNull(basicItemFormat);
        basicItemFormat.applyItemFormat(stack);
        return stack;
    }

    String randomSessionID = Integer.toHexString(ThreadLocalRandom.current().nextInt(1000000));
    NamespacedKey sessionIDKey = new NamespacedKey("mcclab","session_id");

    /**
     * Returns whether the provided {@link ItemStack} requires BasicItemFormat conversion
     * @param stack the item stack
     * @return true if it needs conversion
     */
    static boolean needsConversion(@Nullable ItemStack stack){
        if(org.bukkit.Bukkit.getServer() == null) // When Bukkit has not loaded yet do not use BasicItemFormat
            return false;
        if(stack == null || stack.isEmpty())
            return false;
        if(!stack.hasItemMeta())
            return true;
        if(!stack.getItemMeta().getPersistentDataContainer().has(sessionIDKey))
            return true;
        var storedID = stack.getItemMeta().getPersistentDataContainer().get(sessionIDKey, PersistentDataType.STRING);
        return !randomSessionID.equals(storedID);
    }

    /**
     * Applies the conversion tag to the provided {@link ItemStack}
     * @param stack the item stack
     */
    static void applyConversionTag(@NotNull ItemStack stack){
        stack.editMeta(meta -> meta.getPersistentDataContainer().set(sessionIDKey, PersistentDataType.STRING, randomSessionID));
    }

    /**
     * Removes the conversion tag from the provided {@link ItemStack}
     * @param stack the item stack
     * @return the item stack
     */
    @NotNull
    static ItemStack removeConversionTag(@NotNull ItemStack stack){
        stack.editMeta(meta -> meta.getPersistentDataContainer().remove(sessionIDKey));
        return stack;
    }
}
