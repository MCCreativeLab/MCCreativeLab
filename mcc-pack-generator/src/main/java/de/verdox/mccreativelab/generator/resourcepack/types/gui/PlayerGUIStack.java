package de.verdox.mccreativelab.generator.resourcepack.types.gui;

import de.verdox.mccreativelab.wrapper.entity.types.MCCPlayer;
import de.verdox.mccreativelab.wrapper.inventory.MCCContainerCloseReason;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.logging.Logger;

/**
 * Tracks the GUIs opened by a player
 */
public class PlayerGUIStack {
    private static final Logger LOGGER = Logger.getLogger(PlayerGUIStack.class.getSimpleName());

    private final Stack<StackElement> stack = new Stack<>();
    private final MCCPlayer player;

    public static PlayerGUIStack load(MCCPlayer player) {
        if (!player.getTempData().containsData("playerGUIStack")) {
            player.getTempData().storeData("playerGUIStack", new PlayerGUIStack(player));
        }

        return player.getTempData().getData(PlayerGUIStack.class, "playerGUIStack");
    }

    public PlayerGUIStack(MCCPlayer player) {
        this.player = player;
        //LOGGER.info("Creating player gui stack for player " + player.getUUID());
    }

    public void onActiveGuiClose(@NotNull ActiveGUI closedActiveGui, MCCContainerCloseReason closeReason) {
        Objects.requireNonNull(closedActiveGui);
        Objects.requireNonNull(closeReason);
        if (stack.isEmpty() || closeReason.equals(MCCContainerCloseReason.OPEN_NEW))
            return;
        if (closeReason.equals(MCCContainerCloseReason.CLOSED_BY_VIEWER)) {
            popAndOpenLast(player, closedActiveGui);
        } else
            clear();
    }

    public void popAndOpenLast(MCCPlayer player, ActiveGUI activeGUI) {
        StackElement stackElement = stack.pop();
        //LOGGER.info("Pop: " + stackElement.activeGUI.getComponentRendered().key());
        if (stackElement.activeGUI.getComponentRendered().equals(activeGUI.getComponentRendered()))
            return;
        //LOGGER.info("Open: " + stackElement.activeGUI.getComponentRendered().key());
        stackElement.activeGUI.getComponentRendered().createMenuForPlayer(player);
    }

    public void trackGUI(ActiveGUI activeGUI) {
        StackElement stackElement = new StackElement(activeGUI, activeGUI.tempData);
        stack.push(stackElement);
        //LOGGER.info("Tracking: " + activeGUI.getComponentRendered().key());
    }

    public void clear() {
        stack.clear();
    }

    private record StackElement(ActiveGUI activeGUI, Map<String, Object> tempData) {

    }
}
