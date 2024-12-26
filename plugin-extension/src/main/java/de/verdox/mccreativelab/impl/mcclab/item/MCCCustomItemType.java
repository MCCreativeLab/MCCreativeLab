package de.verdox.mccreativelab.impl.mcclab.item;

import de.verdox.mccreativelab.conversion.converter.MCCConverter;
import de.verdox.mccreativelab.world.block.FakeBlockRegistry;
import de.verdox.mccreativelab.world.item.FakeItem;
import de.verdox.mccreativelab.world.item.FakeItemRegistry;
import de.verdox.mccreativelab.wrapper.item.MCCItemStack;
import de.verdox.mccreativelab.wrapper.item.MCCItemType;
import de.verdox.mccreativelab.wrapper.item.components.MCCDataComponentMap;
import de.verdox.mccreativelab.wrapper.platform.MCCHandle;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class MCCCustomItemType extends MCCHandle<FakeItem> implements MCCItemType {
    public static final MCCConverter<FakeItem, MCCCustomItemType> CONVERTER = converter(MCCCustomItemType.class, FakeItem.class, MCCCustomItemType::new, MCCHandle::getHandle);

    public MCCCustomItemType(FakeItem handle) {
        super(handle);
    }

    @Override
    public @NotNull MCCItemStack createItem() {
        return handle.createItemStack();
    }

    @Override
    public MCCDataComponentMap getItemStandardComponentMap() {
        //TODO implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public @NotNull Key key() {
        return handle.key();
    }

    @Override
    public boolean isVanilla() {
        return false;
    }

    @Override
    public Key getRegistryKey() {
        return FakeItemRegistry.getVanillaRegistry().unwrapKey().get().key();
    }
}
