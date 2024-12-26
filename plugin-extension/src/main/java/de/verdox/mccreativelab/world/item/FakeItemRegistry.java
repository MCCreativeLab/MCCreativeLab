package de.verdox.mccreativelab.world.item;

import com.google.common.reflect.TypeToken;
import de.verdox.mccreativelab.impl.paper.platform.converter.BukkitAdapter;
import de.verdox.mccreativelab.registry.CustomRegistry;
import de.verdox.mccreativelab.registry.Reference;
import de.verdox.mccreativelab.wrapper.annotations.MCCRequireVanillaElement;
import de.verdox.mccreativelab.wrapper.item.MCCItemStack;
import de.verdox.mccreativelab.wrapper.item.MCCItemType;
import de.verdox.mccreativelab.wrapper.platform.MCCPlatform;
import de.verdox.mccreativelab.wrapper.registry.MCCReference;
import de.verdox.mccreativelab.wrapper.registry.MCCRegistry;
import de.verdox.mccreativelab.wrapper.registry.MCCTypedKey;
import de.verdox.mccreativelab.wrapper.typed.MCCDataComponentTypes;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class FakeItemRegistry extends CustomRegistry<FakeItem> {
    private static MCCReference<MCCRegistry<MCCItemType>> VANILLA_REGISTRY;

    private final Map<Entry, Reference<? extends FakeItem>> fakeItemMapping = new HashMap<>();

    public <T extends FakeItem> Reference<T> register(FakeItem.Builder<T> fakeItemBuilder) {
        T fakeItem = fakeItemBuilder.buildItem();
        Reference<T> result = register(fakeItem.key(), fakeItem);
        fakeItemMapping.put(new Entry(fakeItem.asItemType(), fakeItem.getCustomModelData()), result);

        MCCTypedKey<MCCRegistry<MCCItemType>> registryKey = VANILLA_REGISTRY.unwrapKey().get();
        MCCTypedKey<MCCItemType> typedKey = MCCPlatform.getInstance().getTypedKeyFactory().getKey(result.getKey(), registryKey.key(), new TypeToken<MCCItemType>() {});
        VANILLA_REGISTRY.get().register(typedKey, result.unwrapValue().asItemType());
        return result;
    }

    @Nullable
    public Reference<? extends FakeItem> getFakeItem(Entry entry) {
        if (!fakeItemMapping.containsKey(entry))
            return null;
        return fakeItemMapping.get(entry);
    }

    @Deprecated
    public Reference<? extends FakeItem> getFakeItem(@Nullable ItemStack stack) {
        if(stack == null){
            return null;
        }
        return getFakeItem(Entry.of(stack));
    }

    public record Entry(@MCCRequireVanillaElement MCCItemType vanillaMaterial, int customModelData) {
        public static Entry of(ItemStack stack) {
            //TODO Das geht nicht
            return new Entry(BukkitAdapter.wrap(stack.getType()), stack.hasItemMeta() ? stack.getItemMeta().hasCustomModelData() ? stack.getItemMeta().getCustomModelData() : 0 : 0);
        }
    }

    public static void registerToMinecraftRegistry() {
        VANILLA_REGISTRY = MCCPlatform.getInstance().getRegistryStorage().createMinecraftRegistry(Key.key("mcc", "item"));
    }

    public static MCCReference<MCCRegistry<MCCItemType>> getVanillaRegistry() {
        return VANILLA_REGISTRY;
    }
}
