package de.verdox.mccreativelab.ai.behavior;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.memory.CraftMemoryKey;
import org.bukkit.entity.memory.MemoryKey;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class WrappedPaperAIBehavior<T extends org.bukkit.entity.LivingEntity> extends Behavior<LivingEntity> {
    private final CustomAIBehavior<T> paperCustomAIBehaviour;

    public WrappedPaperAIBehavior(CustomAIBehavior<T> paperCustomAIBehaviour) {
        super(getNMSRequiredMemoryState(paperCustomAIBehaviour.getRequiredMemoryStates()), paperCustomAIBehaviour.getMinDuration(), paperCustomAIBehaviour.getMaxDuration());
        this.paperCustomAIBehaviour = paperCustomAIBehaviour;
    }

    @Override
    public String debugString() {
        return paperCustomAIBehaviour.getClass().getSimpleName();
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel world, @NotNull LivingEntity entity) {
        if (!isRightEntityType(entity))
            return false;
        org.bukkit.entity.LivingEntity livingEntity = entity.getBukkitLivingEntity();
        return paperCustomAIBehaviour.checkExtraStartConditions(world.getWorld(), paperCustomAIBehaviour.getEntityType().cast(livingEntity), Bukkit.getCurrentTick());
    }

    @Override
    protected void start(@NotNull ServerLevel world, @NotNull LivingEntity entity, long time) {
        if (!isRightEntityType(entity)) {
            stop(world, entity, time);
            return;
        }
        org.bukkit.entity.LivingEntity livingEntity = entity.getBukkitLivingEntity();
        paperCustomAIBehaviour.start(world.getWorld(), paperCustomAIBehaviour.getEntityType().cast(livingEntity), Bukkit.getCurrentTick());
    }

    @Override
    protected void stop(@NotNull ServerLevel world, @NotNull LivingEntity entity, long time) {
        if (!isRightEntityType(entity))
            return;
        org.bukkit.entity.LivingEntity livingEntity = entity.getBukkitLivingEntity();
        paperCustomAIBehaviour.stop(world.getWorld(), paperCustomAIBehaviour.getEntityType().cast(livingEntity), Bukkit.getCurrentTick());
    }

    @Override
    protected void tick(@NotNull ServerLevel world, @NotNull LivingEntity entity, long time) {
        org.bukkit.entity.LivingEntity livingEntity = entity.getBukkitLivingEntity();
        paperCustomAIBehaviour.tick(world.getWorld(), paperCustomAIBehaviour.getEntityType().cast(livingEntity), Bukkit.getCurrentTick());
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel world, @NotNull LivingEntity entity, long time) {
        if (!isRightEntityType(entity))
            return false;
        org.bukkit.entity.LivingEntity livingEntity = entity.getBukkitLivingEntity();
        return paperCustomAIBehaviour.canStillUse(world.getWorld(), paperCustomAIBehaviour.getEntityType().cast(livingEntity), Bukkit.getCurrentTick());
    }

    private final boolean isRightEntityType(@NotNull LivingEntity livingEntity) {
        return paperCustomAIBehaviour.getEntityType().isAssignableFrom(livingEntity.getBukkitLivingEntity().getClass());
    }

    private static Map<MemoryModuleType<?>, MemoryStatus> getNMSRequiredMemoryState(Map<MemoryKey<?>, de.verdox.mccreativelab.ai.MemoryStatus> bukkitRequiredMemoryStates) {
        Map<MemoryModuleType<?>, MemoryStatus> map = new HashMap<>();
        bukkitRequiredMemoryStates.forEach((memoryKey, memoryStatus) -> {
            MemoryStatus nmsMemoryStatus = switch (memoryStatus) {
                case VALUE_PRESENT -> MemoryStatus.VALUE_PRESENT;
                case VALUE_ABSENT -> MemoryStatus.VALUE_ABSENT;
                case REGISTERED -> MemoryStatus.REGISTERED;
            };
            map.put(CraftMemoryKey.bukkitToMinecraft(memoryKey), nmsMemoryStatus);
        });
        return map;
    }
}
