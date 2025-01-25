package de.verdox.mccreativelab.ai;

import de.verdox.mccreativelab.ai.builder.ActivityBuilder;
import de.verdox.mccreativelab.ai.builder.BehaviorFactory;
import de.verdox.mccreativelab.ai.builder.CraftActivityBuilder;
import de.verdox.mccreativelab.ai.builder.CraftBehaviorFactory;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.schedule.Activity;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftEntityActivity;
import org.bukkit.craftbukkit.CraftEntitySchedule;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.entity.memory.CraftMemoryKey;
import org.bukkit.craftbukkit.entity.memory.CraftMemoryMapper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.memory.MemoryKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CraftEntityBrain<E extends LivingEntity> implements EntityBrain<E> {
    private static final CraftBehaviorFactory behaviorFactory = new CraftBehaviorFactory();
    private final Brain<net.minecraft.world.entity.LivingEntity> entityBrain;

    public CraftEntityBrain(Brain<net.minecraft.world.entity.LivingEntity> brain) {
        this.entityBrain = brain;
    }

    @Override
    public @NotNull BehaviorFactory getBehaviorFactory() {
        return behaviorFactory;
    }

    @Override
    public <U> void addMemoryToBrain(@NotNull MemoryKey<U> memoryKey) {
        this.entityBrain.addMemoryToBrain(CraftMemoryKey.bukkitToMinecraft(memoryKey));
    }

    @Override
    public <U> void eraseMemory(@NotNull MemoryKey<U> memoryKey) {
        this.entityBrain.eraseMemory(CraftMemoryKey.bukkitToMinecraft(memoryKey));
    }

    @Override
    public <U> void setMemory(@NotNull MemoryKey<U> type, @Nullable U value) {
        this.entityBrain.setMemory(CraftMemoryKey.bukkitToMinecraft(type), CraftMemoryMapper.toNms(value));
    }

    @Override
    public <U> void setMemoryWithExpiry(@NotNull MemoryKey<U> type, U value, long expiry) {
        this.entityBrain.setMemoryWithExpiry(CraftMemoryKey.bukkitToMinecraft(type), CraftMemoryMapper.toNms(value), expiry);
    }

    @Override
    public <U> @NotNull Optional<U> getMemory(@NotNull MemoryKey<U> type) {
        var nmsOptional = this.entityBrain.getMemory(CraftMemoryKey.bukkitToMinecraft(type));
        if (nmsOptional.isEmpty())
            return Optional.empty();
        Object nmsValue = nmsOptional.get();
        return (Optional<U>) Optional.of(CraftMemoryMapper.fromNms(nmsValue));
    }

    @Override
    public <U> long getTimeUntilExpiry(@NotNull MemoryKey<U> type) {
        return this.entityBrain.getTimeUntilExpiry(CraftMemoryKey.bukkitToMinecraft(type));
    }

    @Override
    public <U> boolean isMemoryValue(@NotNull MemoryKey<U> type, @NotNull U value) {
        return this.entityBrain.isMemoryValue(CraftMemoryKey.bukkitToMinecraft(type), CraftMemoryMapper.toNms(value));
    }

    @Override
    public boolean checkMemory(@NotNull MemoryKey<?> type, @NotNull MemoryStatus state) {
        return this.entityBrain.checkMemory(CraftMemoryKey.bukkitToMinecraft(type), toNMS(state));
    }

    @Override
    public @NotNull EntitySchedule getSchedule() {
        return CraftEntitySchedule.minecraftToBukkit(this.entityBrain.getSchedule(), Registries.SCHEDULE, Registry.ENTITY_SCHEDULE);
    }

    @Override
    public void setSchedule(@NotNull EntitySchedule schedule) {
        this.entityBrain.setSchedule(CraftEntitySchedule.bukkitToMinecraft(schedule, Registries.SCHEDULE));
    }

    @Override
    public void setCoreActivities(@NotNull Set<EntityActivity> coreActivities) {
        this.entityBrain.setCoreActivities(coreActivities.stream()
                                                         .map(entityActivity -> CraftEntityActivity.bukkitToMinecraft(entityActivity, Registries.ACTIVITY))
                                                         .collect(Collectors.toSet()));
    }

    @Override
    public void useDefaultActivity() {
        this.entityBrain.useDefaultActivity();
    }

    @Override
    public @NotNull Optional<EntityActivity> getActiveNonCoreActivity() {
        Optional<Activity> nmsActivityOptional = this.entityBrain.getActiveNonCoreActivity();
        return nmsActivityOptional.map(activity -> CraftEntityActivity.minecraftToBukkit(activity, Registries.ACTIVITY, Registry.ENTITY_ACTIVITY));

    }

    @Override
    public void setActiveActivityIfPossible(@NotNull EntityActivity activity) {
        this.entityBrain.setActiveActivityIfPossible(CraftEntityActivity.bukkitToMinecraft(activity, Registries.ACTIVITY));
    }

    @Override
    public void updateActivityFromSchedule(long timeOfDay, long time) {
        this.entityBrain.updateActivityFromSchedule(timeOfDay, time);
    }

    @Override
    public void setActiveActivityToFirstValid(@NotNull List<EntityActivity> activities) {
        List<Activity> nmsActivities = activities.stream()
                                                 .map(entityActivity -> CraftEntityActivity.bukkitToMinecraft(entityActivity, Registries.ACTIVITY))
                                                 .collect(Collectors.toList());
        this.entityBrain.setActiveActivityToFirstValid(nmsActivities);
    }

    @Override
    public void setDefaultActivity(@NotNull EntityActivity activity) {
        this.entityBrain.setDefaultActivity(CraftEntityActivity.bukkitToMinecraft(activity, Registries.ACTIVITY));
    }

    @Override
    public void addActivity(@NotNull ActivityBuilder<E> activityBuilder, boolean replaceCompleteActivity, boolean replaceActivityRequirements, boolean replaceForgettingMemories) {
        if (activityBuilder instanceof CraftActivityBuilder<E> craftActivityBuilder)
            craftActivityBuilder.addToBrain(this.entityBrain, replaceCompleteActivity, replaceActivityRequirements, replaceForgettingMemories);
    }

    @Override
    public void addActivity(@NotNull EntityActivity activity, @NotNull Consumer<ActivityBuilder<E>> activityBuilder, boolean replaceCompleteActivity, boolean replaceActivityRequirements, boolean replaceForgettingMemories) {
        CraftActivityBuilder<E> craftActivityBuilder1 = new CraftActivityBuilder<>(activity);
        activityBuilder.accept(craftActivityBuilder1);
        addActivity(craftActivityBuilder1, replaceCompleteActivity, replaceActivityRequirements, replaceForgettingMemories);
    }

    @Override
    public boolean isActive(@NotNull EntityActivity activity) {
        return this.entityBrain.isActive(CraftEntityActivity.bukkitToMinecraft(activity, Registries.ACTIVITY));
    }

    @Override
    public @NotNull EntityBrain<E> copyWithoutBehaviours() {
        return new CraftEntityBrain<>(this.entityBrain.copyWithoutBehaviors());
    }

    @Override
    public void stopAll(@NotNull World world, @NotNull E entity) {
        this.entityBrain.stopAll(((CraftWorld) world).getHandle(), ((CraftLivingEntity) entity).getHandle());
    }

    @Override
    public @NotNull ActivityBuilder<E> createActivityBuilder(@NotNull EntityActivity entityActivity) {
        return new CraftActivityBuilder<>(entityActivity);
    }

    @Override
    public void tick(@NotNull World world, @NotNull E entity) {
        this.entityBrain.tick(((CraftWorld) world).getHandle(), ((CraftLivingEntity) entity).getHandle());
    }

    public static net.minecraft.world.entity.ai.memory.MemoryStatus toNMS(MemoryStatus memoryStatus) {
        return switch (memoryStatus) {
            case VALUE_PRESENT -> net.minecraft.world.entity.ai.memory.MemoryStatus.VALUE_PRESENT;
            case VALUE_ABSENT -> net.minecraft.world.entity.ai.memory.MemoryStatus.VALUE_ABSENT;
            case REGISTERED -> net.minecraft.world.entity.ai.memory.MemoryStatus.REGISTERED;
        };
    }

    public static MemoryStatus toBukkit(net.minecraft.world.entity.ai.memory.MemoryStatus memoryStatus) {
        return switch (memoryStatus) {
            case VALUE_PRESENT -> MemoryStatus.VALUE_PRESENT;
            case VALUE_ABSENT -> MemoryStatus.VALUE_ABSENT;
            case REGISTERED -> MemoryStatus.REGISTERED;
        };
    }
}
