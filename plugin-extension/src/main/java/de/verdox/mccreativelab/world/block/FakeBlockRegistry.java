package de.verdox.mccreativelab.world.block;

import com.google.common.reflect.TypeToken;
import de.verdox.mccreativelab.behaviour.BlockBehaviour;
import de.verdox.mccreativelab.impl.mcclab.block.MCCCustomBlockType;
import de.verdox.mccreativelab.registry.Reference;
import de.verdox.mccreativelab.world.block.behaviour.ReusedStateBehaviour;
import de.verdox.mccreativelab.world.block.display.ReusedBlockStateDisplay;
import de.verdox.mccreativelab.registry.CustomRegistry;
import de.verdox.mccreativelab.util.storage.palette.IdMap;
import de.verdox.mccreativelab.wrapper.block.MCCBlockType;
import de.verdox.mccreativelab.wrapper.platform.MCCPlatform;
import de.verdox.mccreativelab.wrapper.registry.MCCReference;
import de.verdox.mccreativelab.wrapper.registry.MCCRegistry;
import de.verdox.mccreativelab.wrapper.registry.MCCTypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class FakeBlockRegistry extends CustomRegistry<FakeBlock> {
    private static MCCReference<MCCRegistry<MCCBlockType>> VANILLA_REGISTRY;

    public static final IdMap<FakeBlock.FakeBlockState> FAKE_BLOCK_STATE_ID_MAP = new IdMap<>();

    private static final Map<BlockData, FakeBlock.FakeBlockState> reusedBlockStates = new HashMap<>();

    public static void setupFakeBlocks() {
        BlockBehaviour.BLOCK_BEHAVIOUR.setBehaviour(Material.NOTE_BLOCK, new ReusedStateBehaviour(Material.NOTE_BLOCK));
    }

    public <T extends FakeBlock> T register(FakeBlock.Builder<T> fakeBlockBuilder) {
        T fakeBlock = fakeBlockBuilder.buildBlock();
        Reference<T> reference = register(fakeBlockBuilder.namespacedKey, fakeBlock);
        for (FakeBlock.FakeBlockState fakeBlockState : fakeBlockBuilder.blockStates) {
            fakeBlockState.linkFakeBlock(fakeBlock);
            fakeBlockState.getProperties().makeImmutable();
            FAKE_BLOCK_STATE_ID_MAP.add(fakeBlockState);
            if (fakeBlockState.getFakeBlockDisplay() instanceof ReusedBlockStateDisplay reusedBlockStateDisplay)
                reusedBlockStates.put(reusedBlockStateDisplay.getHitBox().getBlockData(), fakeBlockState);
        }

        MCCTypedKey<MCCRegistry<MCCBlockType>> registryKey = VANILLA_REGISTRY.unwrapKey().get();
        MCCTypedKey<MCCBlockType> typedKey = MCCPlatform.getInstance().getTypedKeyFactory().getKey(reference.getKey(), registryKey.key(), new TypeToken<>() {});
        VANILLA_REGISTRY.get().register(typedKey, new MCCCustomBlockType(fakeBlock));

        Bukkit.getLogger().info("Registering fake block " + fakeBlockBuilder.namespacedKey);
        return fakeBlock;
    }

    public boolean hasReusedAnyBlockStates(){
        return !reusedBlockStates.isEmpty();
    }

    @Nullable
    public static FakeBlock.FakeBlockState getFakeBlockStateFromBlockData(BlockData blockData) {
        if (reusedBlockStates.containsKey(blockData))
            return reusedBlockStates.get(blockData);
        return null;
    }

    public static Map<BlockData, FakeBlock.FakeBlockState> getReusedBlockStates() {
        return Map.copyOf(reusedBlockStates);
    }

    public static void registerToMinecraftRegistry(){
        VANILLA_REGISTRY = MCCPlatform.getInstance().getRegistryStorage().createMinecraftRegistry(Key.key("mcc", "block"));
    }

    public static MCCReference<MCCRegistry<MCCBlockType>> getVanillaRegistry() {
        return VANILLA_REGISTRY;
    }
}
