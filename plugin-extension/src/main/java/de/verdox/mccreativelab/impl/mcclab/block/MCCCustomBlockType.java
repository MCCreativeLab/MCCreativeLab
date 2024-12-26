package de.verdox.mccreativelab.impl.mcclab.block;

import de.verdox.mccreativelab.MCCreativeLabExtension;
import de.verdox.mccreativelab.conversion.converter.MCCConverter;
import de.verdox.mccreativelab.world.block.FakeBlock;
import de.verdox.mccreativelab.world.block.FakeBlockRegistry;
import de.verdox.mccreativelab.wrapper.block.MCCBlockSoundGroup;
import de.verdox.mccreativelab.wrapper.block.MCCBlockState;
import de.verdox.mccreativelab.wrapper.block.MCCBlockType;
import de.verdox.mccreativelab.wrapper.platform.MCCHandle;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class MCCCustomBlockType extends MCCHandle<FakeBlock> implements MCCBlockType {
    public static final MCCConverter<FakeBlock, MCCCustomBlockType> CONVERTER = converter(MCCCustomBlockType.class, FakeBlock.class, MCCCustomBlockType::new, MCCHandle::getHandle);

    private final List<MCCBlockState> states;

    public MCCCustomBlockType(FakeBlock fakeBlock) {
        super(fakeBlock);
        states = Arrays.stream(this.getHandle().getFakeBlockStates()).map(fakeBlockState -> (MCCBlockState) new MCCCustomBlockState(fakeBlockState)).toList();
    }

    @Override
    public List<MCCBlockState> getAllBlockStates() {
        return states;
    }

    @Override
    public MCCBlockState getDefaultState() {
        return new MCCCustomBlockState(getHandle().getDefaultBlockState());
    }

    @Override
    public MCCBlockSoundGroup getSoundGroup() {
        //TODO Else vanilla
        return getHandle().getDefaultBlockState().getFakeBlockSoundGroup().asMCCBlockSoundGroup();
    }


    @Override
    public float getExplosionResistance() {
        return getHandle().getDefaultBlockState().getProperties().getExplosionResistance();
    }

    @Override
    public float getHardness() {
        return getHandle().getDefaultBlockState().getProperties().getHardness();
    }

    @Override
    public float getFriction() {
        return 0;
    }

    @Override
    public float getJumpFactor() {
        return getHandle().getDefaultBlockState().getProperties().getJumpFactor();
    }

    @Override
    public float getSpeedFactor() {
        return getHandle().getDefaultBlockState().getProperties().getSpeedFactor();
    }

    @Override
    public @NotNull Key key() {
        return getHandle().key();
    }

    @Override
    public boolean isVanilla() {
        return false;
    }

    @Override
    public Key getRegistryKey() {
        return FakeBlockRegistry.getVanillaRegistry().unwrapKey().get().key();
    }
}
