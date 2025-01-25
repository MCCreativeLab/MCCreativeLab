package de.verdox.mccreativelab.advancement;

import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CraftAdvancementRequirementsBuilder implements AdvancementRequirementsBuilder {
    Map<String, Criterion<?>> criteria = new HashMap<>();
    Strategy strategy = Strategy.OR;

    @Override
    public @NotNull AdvancementRequirementsBuilder withStrategy(@NotNull Strategy strategy) {
        this.strategy = strategy;
        return this;
    }

    @Override
    public @NotNull AdvancementRequirementsBuilder addCriterion(@NotNull String name) {
        this.criteria.put(name, CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()));
        return this;
    }

    public AdvancementRequirements buildNMS() {
        AdvancementRequirements.Strategy strategy = this.strategy == Strategy.AND ? AdvancementRequirements.Strategy.AND : AdvancementRequirements.Strategy.OR;
        return strategy.create(criteria.keySet());
    }
    
    public Map<String, Criterion<?>> getCriteria(){
        return criteria;
    }
}
