package de.verdox.mccreativelab;

import org.jetbrains.annotations.NotNull;

public enum InteractionResult {
    SUCCESS,
    SUCCESS_NO_ITEM_USED,
    CONSUME,
    CONSUME_PARTIAL,
    PASS,
    FAIL;

    public boolean consumesAction() {
        return this == SUCCESS || this == CONSUME || this == CONSUME_PARTIAL || this == SUCCESS_NO_ITEM_USED;
    }

    public boolean shouldSwing() {
        return this == SUCCESS || this == SUCCESS_NO_ITEM_USED;
    }

    public boolean indicateItemUse() {
        return this == SUCCESS || this == CONSUME;
    }

    @NotNull public static InteractionResult sidedSuccess(boolean swingHand) {
        return swingHand ? SUCCESS : CONSUME;
    }
}
