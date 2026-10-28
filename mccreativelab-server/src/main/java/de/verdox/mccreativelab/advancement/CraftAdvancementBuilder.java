package de.verdox.mccreativelab.advancement;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.advancements.*;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class CraftAdvancementBuilder implements AdvancementBuilder {
    @Nullable
    NamespacedKey parent;
    CraftAdvancementDisplayBuilder craftAdvancementDisplayBuilder = new CraftAdvancementDisplayBuilder();
    CraftAdvancementRewardBuilder craftAdvancementRewardBuilder = new CraftAdvancementRewardBuilder();
    CraftAdvancementRequirementsBuilder craftAdvancementRequirementsBuilder = new CraftAdvancementRequirementsBuilder();
    boolean sendsTelemetryEvent;
    @Nullable
    Component name;

    @Override
    public @NotNull AdvancementBuilder withParent(@NotNull NamespacedKey namespacedKey) {
        parent = namespacedKey;
        return this;
    }

    @Override
    public @NotNull AdvancementBuilder withDisplay(@NotNull Consumer<AdvancementDisplayBuilder> builder) {
        builder.accept(craftAdvancementDisplayBuilder);
        return this;
    }

    @Override
    public @NotNull AdvancementBuilder withRewards(@NotNull Consumer<AdvancementRewardBuilder> builder) {
        builder.accept(craftAdvancementRewardBuilder);
        return this;
    }

    @Override
    public @NotNull AdvancementBuilder withRequirements(@NotNull Consumer<AdvancementRequirementsBuilder> builder) {
        builder.accept(craftAdvancementRequirementsBuilder);
        return this;
    }

    @Override
    public @NotNull Advancement addToBukkit(@NotNull NamespacedKey namespacedKey) {
        Optional<Identifier> parent = this.parent == null ? Optional.empty() : Optional.of(CraftNamespacedKey.toMinecraft(this.parent));
        Optional<net.minecraft.network.chat.Component> name = this.name == null ? Optional.empty() : Optional.of(PaperAdventure.asVanilla(this.name));
        DisplayInfo displayInfo = craftAdvancementDisplayBuilder.buildNMS();
        Map<String, Criterion<?>> criteria = craftAdvancementRequirementsBuilder.getCriteria();
        AdvancementRequirements advancementRequirements = craftAdvancementRequirementsBuilder.buildNMS();

        if (criteria.isEmpty())
            throw new IllegalArgumentException("You cannot add the advancement " + namespacedKey.asString() + " because it has no criteria.");

        net.minecraft.advancements.Advancement nms = new net.minecraft.advancements.Advancement(
            parent,
            Optional.of(displayInfo),
            craftAdvancementRewardBuilder.build(),
            criteria,
            advancementRequirements,
            sendsTelemetryEvent,
            name
        );

        Identifier minecraftkey = CraftNamespacedKey.toMinecraft(namespacedKey);
        final com.google.common.collect.ImmutableMap.Builder<Identifier, AdvancementHolder> mapBuilder = com.google.common.collect.ImmutableMap.builder();
        mapBuilder.putAll(MinecraftServer.getServer().getAdvancements().advancements);

        final AdvancementHolder holder = new AdvancementHolder(minecraftkey, nms);
        mapBuilder.put(minecraftkey, holder);

        MinecraftServer.getServer().getAdvancements().advancements = mapBuilder.build();
        final AdvancementTree tree = MinecraftServer.getServer().getAdvancements().tree();
        tree.addAll(List.of(holder));

        {
            final AdvancementNode node = tree.get(minecraftkey);
            if (node != null) {
                final AdvancementNode root = node.root();
                if (root.holder().value().display().isPresent()) {
                    TreeNodePosition.run(root);
                    if (craftAdvancementDisplayBuilder.customCoordinates) {
                        craftAdvancementDisplayBuilder.applyCoordinates(node);
                    }
                }
            }
        }

        Advancement bukkit = Bukkit.getAdvancement(namespacedKey);

        if (bukkit != null) {
            MinecraftServer.getServer().getPlayerList().getPlayers().forEach(player -> {
                player.getAdvancements().reload(MinecraftServer.getServer().getAdvancements());
                player.getAdvancements().flushDirty(player, false);
            });
            return bukkit;
        }
        else Bukkit.getLogger().warning("Could not add Advancement " + namespacedKey.asString() + " to bukkit");

        return null;
    }
}
