package de.verdox.mccreativelab.ai.behavior;

import de.verdox.mccreativelab.ai.MemoryStatus;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.bukkit.craftbukkit.entity.memory.CraftMemoryKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.memory.MemoryKey;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CraftAIBehavior<E extends LivingEntity> extends CraftControlledBehavior<E, Behavior<?>> implements AIBehavior<E> {
    public CraftAIBehavior(Behavior<?> handle) {
        super(handle);
    }

    @Override
    public int getMinDuration() {
        return getHandle().getMinDuration();
    }

    @Override
    public int getMaxDuration() {
        return getHandle().getMaxDuration();
    }

    @Override
    public @NotNull Map<MemoryKey<?>, MemoryStatus> getRequiredMemoryStates() {
        return getBukkitRequiredMemoryKeys(getHandle().getEntryCondition());
    }

    private static Map<MemoryKey<?>, MemoryStatus> getBukkitRequiredMemoryKeys(Map<MemoryModuleType<?>, net.minecraft.world.entity.ai.memory.MemoryStatus> bukkitRequiredMemoryStates) {
        Map<MemoryKey<?>, MemoryStatus> map = new HashMap<>();
        bukkitRequiredMemoryStates.forEach((memoryKey, memoryStatus) -> {
            MemoryStatus bukkitMemoryStatus = switch (memoryStatus) {
                case VALUE_PRESENT -> MemoryStatus.VALUE_PRESENT;
                case VALUE_ABSENT -> MemoryStatus.VALUE_ABSENT;
                case REGISTERED -> MemoryStatus.REGISTERED;
            };
            map.put(CraftMemoryKey.minecraftToBukkit(memoryKey), bukkitMemoryStatus);
        });
        return map;
    }
}
