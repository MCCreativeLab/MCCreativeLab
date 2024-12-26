package de.verdox.mccreativelab.generator.resourcepack.types.gui;

import de.verdox.mccreativelab.generator.Asset;
import de.verdox.mccreativelab.generator.resourcepack.CustomResourcePack;
import de.verdox.mccreativelab.generator.resourcepack.types.gui.element.GUIButton;
import de.verdox.mccreativelab.generator.resourcepack.types.gui.element.GUIElement;
import de.verdox.mccreativelab.generator.resourcepack.types.gui.element.active.ActiveGUIElement;
import de.verdox.mccreativelab.generator.resourcepack.types.rendered.ComponentRendered;
import de.verdox.mccreativelab.generator.resourcepack.types.rendered.util.ScreenPosition;
import de.verdox.mccreativelab.generator.resourcepack.types.rendered.util.TextType;
import de.verdox.mccreativelab.generator.resourcepack.types.rendered.element.single.SingleHudTexture;
import de.verdox.mccreativelab.generator.resourcepack.types.rendered.RenderedElementBehavior;
import de.verdox.mccreativelab.wrapper.entity.types.MCCPlayer;
import de.verdox.mccreativelab.wrapper.inventory.MCCContainer;

import de.verdox.mccreativelab.wrapper.inventory.MCCMenuType;
import de.verdox.mccreativelab.wrapper.inventory.MCCMenuTypes;
import de.verdox.mccreativelab.wrapper.inventory.types.MCCSharedContainerMenu;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CustomGUIBuilder extends ComponentRendered<CustomGUIBuilder, ActiveGUI> {
    private final MCCMenuType<? extends MCCSharedContainerMenu<?, ?>> type;
    Consumer<ActiveGUI> onOpen;
    Consumer<ActiveGUI> onClose;
    Consumer<ActiveGUI> whileOpen;
    int updateInterval = 20;
    BiConsumer<GUIClickAction, ActiveGUI> clickConsumer;
    private final Set<Integer> blockedSlots = new HashSet<>();
    final Map<String, GUIElement> guiElements = new HashMap<>();
    private boolean allSlotsBlocked;
    private boolean usePlayerSlots;
    private final Map<GUIElement, GUIElementBehavior<?>> guiElementBehaviors = new HashMap<>();

    public CustomGUIBuilder(@NotNull Key namespacedKey, @NotNull MCCMenuType<? extends MCCSharedContainerMenu<?, ?>> type) {
        super(namespacedKey);
        this.type = type;
    }

    public CustomGUIBuilder(@NotNull Key namespacedKey, int amountChestRows) {
        super(namespacedKey);
        this.type = switch (amountChestRows) {
            case 1 -> MCCMenuTypes.GENERIC_9x1;
            case 2 -> MCCMenuTypes.GENERIC_9x2;
            case 3 -> MCCMenuTypes.GENERIC_9x3;
            case 4 -> MCCMenuTypes.GENERIC_9x4;
            case 5 -> MCCMenuTypes.GENERIC_9x5;
            case 6 -> MCCMenuTypes.GENERIC_9x6;
            default ->
                throw new IllegalStateException("Minecraft does not allow containers with " + amountChestRows + " rows.");
        };
    }

    public BiConsumer<GUIClickAction, ActiveGUI> getClickConsumer() {
        return clickConsumer;
    }

    public Map<GUIElement, GUIElementBehavior<?>> getGuiElementBehaviors() {
        return guiElementBehaviors;
    }

    public CustomGUIBuilder withUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
        return this;
    }

    public CustomGUIBuilder withOverlayTexture(Asset<CustomResourcePack> texture) throws IOException {
        return withOverlayTexture(texture, getTopLeftPos());
    }

    public CustomGUIBuilder usePlayerSlots() {
        this.usePlayerSlots = true;
        return this;
    }

    public ScreenPosition getTopLeftPos() {
        return TextType.getTopLeftCorner(type);
    }

    @Override
    public void beforeResourceInstallation(CustomResourcePack customPack) throws IOException {
        super.beforeResourceInstallation(customPack);
    }

    public CustomGUIBuilder withOverlayTexture(Asset<CustomResourcePack> texture, ScreenPosition screenPosition) throws IOException {
        return withTexture("overlay", texture,
            // 13 hoch / 8 nach links
            screenPosition, new RenderedElementBehavior<>() {
                @Override
                public void whileOpen(ActiveGUI parentElement, SingleHudTexture.RenderedSingleHudTexture element, Audience audience) {
                    element.setVisible(true);
                }
            });
    }

    public CustomGUIBuilder withButton(String buttonName, int x, int y, Consumer<GUIButton.Builder> setup) {
        return withButton(buttonName, x, y, setup, null);
    }

    public CustomGUIBuilder withButton(String buttonName, int x, int y, Consumer<GUIButton.Builder> setup, @Nullable GUIElementBehavior<?> behavior) {
        int index = x + (y * 9);
        return withButton(buttonName, index, setup, behavior);
    }

    public CustomGUIBuilder withButton(String buttonName, int index, Consumer<GUIButton.Builder> setup) {
        return withButton(buttonName, index, setup, new GUIElementBehavior<>() {
            @Override
            public void onOpen(ActiveGUI parentElement, ActiveGUIElement<?> element) {
                element.setVisible(true);
            }
        });
    }

    public CustomGUIBuilder withButton(String buttonName, int index, Consumer<GUIButton.Builder> setup, @Nullable GUIElementBehavior<?> behavior) {
        if (index < 0)
            throw new IllegalArgumentException("Index must be >= 0");
        int preferredSize = getType().containerSize();
        if (index > preferredSize)
            throw new IllegalArgumentException("Index must be < " + preferredSize);
        GUIButton.Builder builder = new GUIButton.Builder(index);
        setup.accept(builder);
        String textFieldID = "clickable_button_" + buttonName + "_text";
        String textureName = "clickable_button_" + buttonName + "_texture";

        GUIButton guiButton = builder.build(textureName, textFieldID);
        int x = index % 9;
        int y = index / 9;
        var screenPosOfTexture = ScreenPosition.getScreenPositionOfSlot(x, y, this, ScreenPosition.SlotOffset.TOP_LEFT_CORNER);


        withText(textFieldID, screenPosOfTexture.withLayer(3).addToXOffset(3)
            .addToYOffset(-6), guiButton.getButtonTextAlignment(), guiButton.getTextScale(), null);

        try {
            if (guiButton.getButtonTexture() != null)
                withTexture(textureName, guiButton.getButtonTexture(), screenPosOfTexture.withLayer(2), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (behavior != null)
            guiElementBehaviors.put(guiButton, behavior);

        guiElements.put(buttonName, guiButton);
        return this;
    }

    public CustomGUIBuilder onOpen(Consumer<ActiveGUI> onOpen) {
        this.onOpen = onOpen;
        return this;
    }

    public CustomGUIBuilder whileOpen(Consumer<ActiveGUI> whileOpen) {
        this.whileOpen = whileOpen;
        return this;
    }

    public CustomGUIBuilder onClose(Consumer<ActiveGUI> onClose) {
        this.onClose = onClose;
        return this;
    }

    public CustomGUIBuilder withClick(BiConsumer<GUIClickAction, ActiveGUI> clickConsumer) {
        this.clickConsumer = clickConsumer;
        return this;
    }

    public CustomGUIBuilder withBlockedSlots(int... blockedSlots) {
        for (int blockedSlot : blockedSlots)
            this.blockedSlots.add(blockedSlot);
        return this;
    }

    public CustomGUIBuilder withBlockedPattern(int startRow, int endRow, int... blockedSlots) {
        for (int i = startRow; i <= endRow; i++)
            for (int blockedSlot : blockedSlots)
                withBlockedSlots(blockedSlot + (i * 9));
        return this;
    }

    public CustomGUIBuilder blockAllSlots() {
        allSlotsBlocked = true;
        return this;
    }

    protected ScreenPosition convertScreenPosition(ScreenPosition screenPosition) {
        return screenPosition.withTextType(getCorrectTextType());
    }

    public TextType getCorrectTextType() {
        return TextType.getByInventoryType(type);
    }

    public boolean isSlotBlocked(int slot) {
        return allSlotsBlocked || this.blockedSlots.contains(slot);
    }

    public boolean isUsePlayerSlots() {
        return usePlayerSlots;
    }

    @NotNull
    MCCMenuType<? extends MCCSharedContainerMenu<?, ?>> getType() {
        return type;
    }

    public Set<Integer> getBlockedSlots() {
        if (allSlotsBlocked) {
            var set = new HashSet<Integer>();
            for (int i = 0; i < type.containerSize(); i++)
                set.add(i);
            return set;
        }
        return new HashSet<>(blockedSlots);
    }

    /**
     * Creates a new active gui but pushes the predecessor gui to the gui stack, so it can be reopened when this new gui closes.
     *
     * @param predecessor  the predecessor gui
     * @param initialSetup the initial setup for the new gui
     */
    public ActiveGUI asNestedGUI(MCCPlayer player, ActiveGUI predecessor, @Nullable Consumer<ActiveGUI> initialSetup) {
        return createMenuForPlayer(player, activeGUI -> {
            predecessor.trackGUIInStack(player);
            if (initialSetup != null)
                initialSetup.accept(activeGUI);
        });
    }

    /**
     * Creates a new active gui but pushes the predecessor gui to the gui stack, so it can be reopened when this new gui closes.
     *
     * @param predecessor the predecessor gui
     */
    public ActiveGUI asNestedGUI(MCCPlayer player, ActiveGUI predecessor) {
        return asNestedGUI(player, predecessor, null);
    }

    public ActiveGUI createMenuForPlayer(MCCPlayer player) {
        return createMenuForPlayer(player, null);
    }

    public ActiveGUI createMenuForPlayer(MCCPlayer player, @Nullable Consumer<ActiveGUI> initialSetup) {
        ActiveGUI activeGUI = new ActiveGUI(this, gui -> {
            if (initialSetup != null)
                initialSetup.accept(gui);
        });

        activeGUI.openToPlayer(player);

        return activeGUI;
    }

    /**
     * Creates a new active gui but pushes the predecessor gui to the gui stack, so it can be reopened when this new gui closes.
     *
     * @param predecessor  the predecessor gui
     * @param initialSetup the initial setup for the new gui
     */

    public ActiveGUI asNestedGUI(MCCPlayer player, ActiveGUI predecessor, @NotNull MCCContainer inventory, @Nullable Consumer<ActiveGUI> initialSetup) {
        return linkToExistingInventory(player, inventory, activeGUI -> {
            predecessor.trackGUIInStack(player);
            if (initialSetup != null)
                initialSetup.accept(activeGUI);
        });
    }


    public ActiveGUI linkToExistingInventory(MCCPlayer player, @NotNull MCCContainer inventory, @Nullable Consumer<ActiveGUI> initialSetup) {
        ActiveGUI activeGUI = new ActiveGUI(this, inventory, gui -> {
            if (initialSetup != null)
                initialSetup.accept(gui);
        });
        activeGUI.openToPlayer(player);
        return activeGUI;
    }


    public ActiveGUI linkToExistingInventory(MCCPlayer player, @NotNull MCCContainer inventory) {
        return linkToExistingInventory(player, inventory, null);
    }

}
