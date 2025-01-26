package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when a furnace runs out of fuel
 */
public class FurnaceRunOutOfFuelEvent extends BlockEvent {
    private static final HandlerList handlers = new HandlerList();

    public FurnaceRunOutOfFuelEvent(@NotNull Block theBlock) {
        super(theBlock);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
