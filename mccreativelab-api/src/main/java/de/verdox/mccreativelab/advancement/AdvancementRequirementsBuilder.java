package de.verdox.mccreativelab.advancement;

import org.jetbrains.annotations.NotNull;

public interface AdvancementRequirementsBuilder {

    @NotNull AdvancementRequirementsBuilder withStrategy(@NotNull Strategy strategy);
    @NotNull AdvancementRequirementsBuilder addCriterion(@NotNull String name);


    enum Strategy {
        OR,
        AND,
    }
}
