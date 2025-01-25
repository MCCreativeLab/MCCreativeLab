package de.verdox.mccreativelab.event;

import io.papermc.paper.event.world.WorldEffectEvent;
import io.papermc.paper.event.world.WorldSoundEvent;
import net.kyori.adventure.sound.Sound;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameRules;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftEffect;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EventToPacketFactory {
    public static void globalLevelEvent(ServerLevel serverLevel, int eventId, BlockPos pos, int data) {
        if (serverLevel.getGameRules().getBoolean(GameRules.RULE_GLOBAL_SOUND_EVENTS)) {
            WorldEffectEvent worldEffectEvent = createWorldEffectEvent(serverLevel, null, eventId, pos, data, true);
            callEventAndBroadcastPacket(worldEffectEvent);
        } else {
            levelEvent(serverLevel, null, eventId, pos, data);
        }
    }

    public static void levelEvent(ServerLevel serverLevel, @javax.annotation.Nullable net.minecraft.world.entity.player.Player except, int eventId, BlockPos pos, int data) {
        WorldEffectEvent worldEffectEvent = createWorldEffectEvent(serverLevel, except, eventId, pos, data, false);
        callEventAndBroadcastPacket(worldEffectEvent);
    }

    public static void playSeededSound(ServerLevel serverLevel, @javax.annotation.Nullable net.minecraft.world.entity.player.Player except, double x, double y, double z, Holder<SoundEvent> sound, SoundSource category, float volume, float pitch, long seed) {
        WorldSoundEvent worldSoundEvent = createWorldSoundEvent(serverLevel, except, null, x, y, z, sound, category, volume, pitch, seed);
        if (worldSoundEvent != null) callEventAndBroadcastPacket(worldSoundEvent, false);
    }

    public static void playSeededSound(ServerLevel serverLevel, @javax.annotation.Nullable net.minecraft.world.entity.player.Player except, Entity entity, Holder<SoundEvent> sound, SoundSource category, float volume, float pitch, long seed) {
        WorldSoundEvent worldSoundEvent = createWorldSoundEvent(serverLevel, except, entity, entity.getX(), entity.getY(), entity.getZ(), sound, category, volume, pitch, seed);
        if (worldSoundEvent != null) callEventAndBroadcastPacket(worldSoundEvent, false);
    }

    public static WorldSoundEvent createWorldSoundEvent(ServerLevel serverLevel, @Nullable net.minecraft.world.entity.player.Player except, @Nullable Entity entity, double x, double y, double z, Holder<SoundEvent> sound, SoundSource category, float volume, float pitch, long seed) {
        if (sound.unwrapKey().isEmpty())
            return null;
        NamespacedKey soundType = CraftNamespacedKey.fromMinecraft(sound.unwrapKey().get().location());
        Player player = except != null ? (Player) except.getBukkitEntity() : null;
        Sound.Source adventureSource = Sound.Source.valueOf(category.getName().toUpperCase());
        Location location = new Location(serverLevel.getWorld(), x, y, z);

        return new WorldSoundEvent(serverLevel.getWorld(), location, player, entity != null ? entity.getBukkitEntity() : null, soundType, adventureSource, volume, pitch, seed);
    }

    public static WorldEffectEvent createWorldEffectEvent(ServerLevel serverLevel, @Nullable net.minecraft.world.entity.player.Player except, int eventId, BlockPos pos, int data, boolean global) {
        Effect bukkitEffect = Effect.getById(eventId);
        Player player = except != null ? (Player) except.getBukkitEntity() : null;
        Location location = new Location(serverLevel.getWorld(), pos.getX(), pos.getY(), pos.getZ());

        return new WorldEffectEvent(serverLevel.getWorld(), location, player, bukkitEffect, data, global);
    }

    public static void callEventAndBroadcastPacket(WorldEffectEvent event) {
        if (!event.callEvent()) return;
        ServerLevel serverLevel = ((CraftWorld) event.getWorld()).getHandle();
        int data;
        if (event.getData() instanceof Integer integer)
            data = integer;
        else
            data = CraftEffect.getDataValue(event.getEffect(), event.getData());

        ClientboundLevelEventPacket clientboundLevelEventPacket = new ClientboundLevelEventPacket(event.getEffect()
                                                                                                       .getId(), new BlockPos(event
            .getSoundLocation().getBlockX(), event.getSoundLocation().getBlockY(), event.getSoundLocation()
                                                                                        .getBlockZ()), data, event.isGlobal());

        if (event.isGlobal())
            serverLevel.getServer().getPlayerList().broadcastAll(clientboundLevelEventPacket);
        else {
            ServerPlayer serverPlayer = event.getExcept() != null ? ((CraftPlayer) event.getExcept()).getHandle() : null;
            serverLevel.getServer().getPlayerList()
                       .broadcast(serverPlayer, event.getSoundLocation().getX(), event.getSoundLocation().getY(), event
                           .getSoundLocation().getZ(), 64.0D, serverLevel.dimension(), clientboundLevelEventPacket);
        }
    }

    public static void callEventAndBroadcastPacket(WorldSoundEvent event, boolean global) {
        if (!event.callEvent()) return;
        ServerLevel serverLevel = ((CraftWorld) event.getWorld()).getHandle();
        final double posX = event.getEmitter() != null ? event.getEmitter().getLocation().getX() : event
            .getSoundLocation().getX();
        final double posY = event.getEmitter() != null ? event.getEmitter().getLocation().getY() : event
            .getSoundLocation().getY();
        final double posZ = event.getEmitter() != null ? event.getEmitter().getLocation().getZ() : event
            .getSoundLocation().getZ();
        final ResourceLocation name = io.papermc.paper.adventure.PaperAdventure.asVanilla(event.getSound());
        final Optional<SoundEvent> soundEvent = BuiltInRegistries.SOUND_EVENT.getOptional(name);
        final SoundSource source = io.papermc.paper.adventure.PaperAdventure.asVanilla(event.getSoundCategory());
        final Holder<SoundEvent> soundEventHolder = soundEvent.map(BuiltInRegistries.SOUND_EVENT::wrapAsHolder)
                                                              .orElseGet(() -> Holder.direct(SoundEvent.createVariableRangeEvent(name)));

        ClientboundSoundPacket packet = new ClientboundSoundPacket(soundEventHolder, source, posX, posY, posZ, event.getVolume(), event.getPitch(), event.getSeed());
        ServerPlayer serverPlayer = event.getExcept() != null ? ((CraftPlayer) event.getExcept()).getHandle() : null;

        if (global)
            serverLevel.getServer().getPlayerList().broadcastAll(packet, serverPlayer);
        else
            serverLevel.getServer().getPlayerList().broadcast(serverPlayer, posX, posY, posZ, soundEventHolder.value()
                                                                                                              .getRange(event.getVolume()), serverLevel.dimension(), packet);
    }

}
