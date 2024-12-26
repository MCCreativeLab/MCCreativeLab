package de.verdox.mccreativelab.world.item;

import de.verdox.mccreativelab.MCCreativeLabExtension;
import de.verdox.mccreativelab.behaviour.BehaviourResult;
import de.verdox.mccreativelab.behaviour.ItemBehaviour;
import de.verdox.mccreativelab.generator.Asset;
import de.verdox.mccreativelab.generator.resourcepack.CustomResourcePack;
import de.verdox.mccreativelab.generator.resourcepack.types.ItemTextureData;
import de.verdox.mccreativelab.generator.resourcepack.types.lang.Translatable;
import de.verdox.mccreativelab.impl.mcclab.item.MCCCustomItemType;
import de.verdox.mccreativelab.impl.paper.platform.converter.BukkitAdapter;
import de.verdox.mccreativelab.recipe.CustomItemData;
import de.verdox.mccreativelab.world.block.FakeBlock;
import de.verdox.mccreativelab.wrapper.annotations.MCCRequireVanillaElement;
import de.verdox.mccreativelab.wrapper.block.MCCBlockState;
import de.verdox.mccreativelab.wrapper.entity.MCCEffect;
import de.verdox.mccreativelab.wrapper.entity.MCCEntity;
import de.verdox.mccreativelab.wrapper.item.MCCItemStack;
import de.verdox.mccreativelab.wrapper.item.MCCItemType;
import de.verdox.mccreativelab.wrapper.item.components.MCCFoodProperties;
import de.verdox.mccreativelab.wrapper.registry.MCCReference;
import de.verdox.mccreativelab.wrapper.typed.MCCDataComponentTypes;
import it.unimi.dsi.fastutil.Pair;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class FakeItem implements Keyed, ItemBehaviour {
    private Key key;
    private MCCItemType overwrittenVanillaItemType;
    private int customModelData;
    private FakeItemProperties fakeItemProperties;
    private Consumer<MCCItemStack> componentSetup;
    private Translatable nameTranslation;
    private Function<Translatable, Component> nameFormat;

    public MCCItemType getOverwrittenVanillaItemType() {
        return overwrittenVanillaItemType;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public FakeItemProperties getFakeItemProperties() {
        return fakeItemProperties;
    }

    public Translatable getNameTranslatable() {
        return nameTranslation;
    }

    public Consumer<MCCItemStack> getComponentSetup() {
        return componentSetup;
    }

    public void setFakeItemProperties(FakeItemProperties fakeItemProperties) {
        this.fakeItemProperties = fakeItemProperties;
    }

    public boolean isItem(MCCItemStack stack) {
        return stack.components().get(MCCDataComponentTypes.CUSTOM_MODEL_DATA.get()).getValue() == customModelData;
    }

    public @NotNull MCCItemType asItemType() {
        return new MCCCustomItemType(this);
    }

    public final MCCItemStack createItemStack() {
        MCCItemStack mccItemStack = overwrittenVanillaItemType.createItem();

        mccItemStack.components().edit(MCCDataComponentTypes.MAX_STACK_SIZE.get(), component -> component.set(getMaxStackSize()));
        mccItemStack.components().edit(MCCDataComponentTypes.MAX_DAMAGE.get(), component -> component.set(getMaxDamage()));
        mccItemStack.components().edit(MCCDataComponentTypes.CUSTOM_MODEL_DATA.get(), component -> component.with(mccCustomModelData -> mccCustomModelData.withValue(customModelData)));
        mccItemStack.components().edit(MCCDataComponentTypes.ITEM_NAME.get(), component -> component.set(nameFormat != null ? nameFormat.apply(this.nameTranslation) : nameTranslation.asTranslatableComponent()));

        if (this.fakeItemProperties.foodProperties != null) {
            mccItemStack.components().edit(MCCDataComponentTypes.FOOD.get(), component -> {
                MCCFoodProperties foodProperties = component.create()
                    .withCanAlwaysEat(fakeItemProperties.foodProperties.canAlwaysEat)
                    .withNutrition(fakeItemProperties.foodProperties.nutrition)
                    .withSaturation(fakeItemProperties.foodProperties.saturationModifier)
                    .withEatSeconds(fakeItemProperties.foodProperties.seconds);

                List<MCCFoodProperties.PossibleEffect> possibleEffects = new LinkedList<>();
                for (Pair<MCCEffect, Float> effectFloatPair : fakeItemProperties.foodProperties.effects) {
                    MCCEffect effect = effectFloatPair.key();
                    float chance = effectFloatPair.right();
                    MCCFoodProperties.PossibleEffect possibleEffect =
                        foodProperties.createPossibleEffect()
                            .withProbability(chance)
                            .withEffect(effect);
                    possibleEffects.add(possibleEffect);
                }
                foodProperties.withEffects(possibleEffects);
            });
        }
        // TODO:
        //ItemDataContainer.from(stack);
        ItemStack bukkitStack = BukkitAdapter.unwrap(mccItemStack);
        bukkitStack.setItemBehaviour(this);
        return mccItemStack;
    }

    public float getDestroySpeed(MCCEntity breaker, MCCItemStack tool, MCCBlockState brokenBlock) {
        return 1.0f;
    }

    public void onClick(InventoryClickEvent clickEvent) {

    }

    public int getMaxStackSize() {
        return fakeItemProperties.maxStackSize;
    }

    public int getMaxDamage() {
        return fakeItemProperties.maxDamage;
    }

    public boolean isFireResistant() {
        return fakeItemProperties.isFireResistant;
    }

    @Override
    public final BehaviourResult.@NotNull Object<ItemStack> getCraftRemainingItem(@NotNull ItemStack stack) {
        return new BehaviourResult.Object<>(fakeItemProperties.craftingRemainingItem, BehaviourResult.Object.Type.REPLACE_VANILLA);
    }

    @Override
    public BehaviourResult.@NotNull Bool canDropOnDeath(@NotNull ItemStack stack) {
        return new BehaviourResult.Bool(!fakeItemProperties.preventDrop, BehaviourResult.Bool.Type.REPLACE_VANILLA);
    }

    @Override
    public BehaviourResult.@NotNull Bool isEnchantable(@NotNull ItemStack stack) {
        return new BehaviourResult.Bool(fakeItemProperties.enchantable, BehaviourResult.Bool.Type.REPLACE_VANILLA);
    }

    @Override
    public BehaviourResult.@NotNull Bool canFitInsideContainerItems(@NotNull ItemStack stack) {
        return new BehaviourResult.Bool(fakeItemProperties.fitsInsideContainerItem, BehaviourResult.Bool.Type.REPLACE_VANILLA);
    }

    @Override
    public BehaviourResult.@NotNull Bool canBreakWhenMaxDamage(@NotNull ItemStack stack) {
        return new BehaviourResult.Bool(fakeItemProperties.canBreak, BehaviourResult.Bool.Type.REPLACE_VANILLA);
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    public static class Builder<T extends FakeItem> {
        private final MCCItemType vanillaMaterial;
        private final Key namespacedKey;
        private final Supplier<T> itemBuilder;
        private Consumer<MCCItemStack> componentSetup = itemMeta -> {
        };
        private FakeItemProperties fakeItemProperties = new FakeItemProperties();
        private Asset<CustomResourcePack> texture;
        private final Translatable standardNameTranslation;

        private ItemTextureData itemTextureData;
        private ItemTextureData.ModelType modelType;
        private final String translationKey;

        @Nullable
        private Function<Translatable, Component> nameFormat;

        public Builder(Key namespacedKey, @MCCRequireVanillaElement MCCItemType vanillaMaterial, Supplier<T> itemBuilder) {
            vanillaMaterial.requireVanilla();
            this.itemBuilder = itemBuilder;
            Objects.requireNonNull(vanillaMaterial);
            Objects.requireNonNull(namespacedKey);
            this.vanillaMaterial = vanillaMaterial;
            this.namespacedKey = namespacedKey;
            this.translationKey = "custom_item.description_id." + Key.key(namespacedKey.namespace(), namespacedKey.value()).asString();
            this.standardNameTranslation = new Translatable(translationKey);
        }

        public Builder(Key namespacedKey, @MCCRequireVanillaElement MCCReference<MCCItemType> vanillaMaterial, Supplier<T> itemBuilder) {
            this(namespacedKey, vanillaMaterial.get(), itemBuilder);
        }

        @Deprecated
        public Builder(Key namespacedKey, @MCCRequireVanillaElement Material vanillaMaterial, Supplier<T> itemBuilder) {
            this(namespacedKey, BukkitAdapter.wrap(vanillaMaterial, MCCItemType.class), itemBuilder);
        }

        public Builder<T> withComponentSetup(Consumer<MCCItemStack> itemMetaBuilder) {
            this.componentSetup = itemMetaBuilder;
            return this;
        }

        public Builder<T> withItemTextureData(ItemTextureData itemTextureData) {
            this.itemTextureData = itemTextureData;
            return this;
        }

        public Builder<T> withTranslatedName(Consumer<Translatable> setup) {
            setup.accept(this.standardNameTranslation);
            return this;
        }

        public Builder<T> withNameFormat(Function<Translatable, Component> nameFormat) {
            this.nameFormat = nameFormat;
            return this;
        }

        public Builder<T> withProperties(FakeItemProperties fakeItemProperties) {
            this.fakeItemProperties = fakeItemProperties;
            return this;
        }

        public Builder<T> withTexture(Asset<CustomResourcePack> texture) {
            this.texture = texture;
            return this;
        }

        public Builder<T> withModel(ItemTextureData.ModelType modelType) {
            this.modelType = modelType;
            return this;
        }

        T buildItem() {
            //int customModelData = CustomModelDataProvider.drawCustomModelData(vanillaMaterial);
            // Generating customModelData with deterministic hash value of the item key
            ItemTextureData itemTextureData;
            if (this.itemTextureData != null) {
                itemTextureData = this.itemTextureData;
            } else {
                MCCItemType itemType = BukkitAdapter.wrap(vanillaMaterial);
                itemTextureData = new ItemTextureData(Key.key(namespacedKey.namespace(), "item/" + namespacedKey.value()), itemType, texture, modelType, texture == null && modelType == null);
            }
            int customModelData = itemTextureData.getCustomModelData();
            T value = itemBuilder.get();

            value.setOverwrittenVanillaItemType(vanillaMaterial);
            value.setCustomModelData(customModelData);
            value.setKey(namespacedKey);

            if (this.standardNameTranslation != null && value.getNameTranslatable() == null)
                value.setNameTranslatable(standardNameTranslation);
            if (this.fakeItemProperties != null && value.getFakeItemProperties() == null)
                value.setFakeItemProperties(fakeItemProperties);
            if (this.componentSetup != null && value.getComponentSetup() == null)
                value.setComponentSetup(componentSetup);
            if (this.nameFormat != null) value.setNameFormat(this.nameFormat);

            Objects.requireNonNull(namespacedKey);
            Objects.requireNonNull(vanillaMaterial);

            MCCreativeLabExtension.getCustomResourcePack().registerIfNotAlready(itemTextureData);
            if (standardNameTranslation != null)
                MCCreativeLabExtension.getCustomResourcePack().addTranslation(standardNameTranslation);

            ItemBehaviour.ITEM_BEHAVIOUR.setBehaviour(new CustomItemData(BukkitAdapter.unwrap(vanillaMaterial), customModelData), value);
            return value;
        }
    }

    public static class FakeItemProperties {
        private int maxStackSize = 64;
        private int maxDamage;
        private @Nullable ItemStack craftingRemainingItem;
        private @Nullable FakeItem.FoodProperties foodProperties;
        private boolean isFireResistant;
        private boolean enchantable;
        private boolean fitsInsideContainerItem = true;
        private boolean canBreak = true;
        private boolean preventNormalDurabilityChange;
        private boolean preventDrop;
        private boolean preventInventoryClick;

        boolean isPreventNormalDurabilityChange() {
            return preventNormalDurabilityChange;
        }

        boolean isPreventDrop() {
            return preventDrop;
        }

        boolean isPreventInventoryClick() {
            return preventInventoryClick;
        }

        public FakeItemProperties preventDrop(boolean preventDrop) {
            this.preventDrop = preventDrop;
            return this;
        }

        public FakeItemProperties preventInventoryClick(boolean preventInventoryClick) {
            this.preventInventoryClick = preventInventoryClick;
            return this;
        }

        public int getMaxStackSize() {
            return maxStackSize;
        }

        public int getMaxDamage() {
            return maxDamage;
        }

        public FakeItemProperties food(FoodProperties foodComponent) {
            this.foodProperties = foodComponent;
            return this;
        }

        public FakeItemProperties preventNormalDurabilityChange(boolean preventNormalDurabilityChange) {
            this.preventNormalDurabilityChange = preventNormalDurabilityChange;
            return this;
        }

        public FakeItemProperties breaksWhenMaxDamageReached(boolean canBreak) {
            this.canBreak = canBreak;
            return this;
        }

        public FakeItemProperties fitsInsideContainerItem(boolean fitsInsideContainerItem) {
            this.fitsInsideContainerItem = fitsInsideContainerItem;
            return this;
        }

        public FakeItemProperties enchantable(boolean enchantable) {
            this.enchantable = enchantable;
            return this;
        }

        public FakeItemProperties stacksTo(int maxCount) {
            if (this.maxDamage > 0) {
                throw new RuntimeException("Unable to have damage AND stack.");
            } else {
                this.maxStackSize = maxCount;
                return this;
            }
        }

        public FakeItemProperties defaultDurability(int maxDamage) {
            return this.maxDamage == 0 ? this.durability(maxDamage) : this;
        }

        public FakeItemProperties durability(int maxDamage) {
            this.maxDamage = maxDamage;
            this.maxStackSize = 1;
            return this;
        }

        public FakeItemProperties craftRemainder(ItemStack recipeRemainder) {
            this.craftingRemainingItem = recipeRemainder;
            return this;
        }

        public FakeItemProperties fireResistant() {
            this.isFireResistant = true;
            return this;
        }
    }

    public static class FoodProperties {
        private final int nutrition;
        private final float saturationModifier;
        private final boolean canAlwaysEat;
        private final float seconds;
        private final List<Pair<MCCEffect, Float>> effects;

        FoodProperties(int nutrition, float saturationModifier, boolean canAlwaysEat, float seconds, List<Pair<MCCEffect, Float>> effects) {
            this.nutrition = nutrition;
            this.saturationModifier = saturationModifier;
            this.canAlwaysEat = canAlwaysEat;
            this.seconds = seconds;
            this.effects = effects;
        }

        public int getNutrition() {
            return nutrition;
        }

        public float getSaturationModifier() {
            return saturationModifier;
        }

        public boolean isCanAlwaysEat() {
            return canAlwaysEat;
        }

        public List<Pair<MCCEffect, Float>> getEffects() {
            return effects;
        }

        public static class Builder {
            private int nutrition;
            private float saturationModifier;
            private boolean canAlwaysEat;
            private final List<Pair<MCCEffect, Float>> effects = new LinkedList<>();
            private float seconds;

            public Builder nutrition(int hunger) {
                this.nutrition = hunger;
                return this;
            }

            public Builder saturationMod(float saturationModifier) {
                this.saturationModifier = saturationModifier;
                return this;
            }

            public Builder alwaysEat() {
                this.canAlwaysEat = true;
                return this;
            }

            public Builder effect(MCCEffect effect, float chance) {
                this.effects.add(Pair.of(effect, chance));
                return this;
            }

            public Builder withEatSeconds(float seconds) {
                this.seconds = seconds;
                return this;
            }

            public FoodProperties build() {
                return new FoodProperties(this.nutrition, this.saturationModifier, this.canAlwaysEat, seconds, this.effects);
            }
        }
    }

    protected void setNameTranslatable(Translatable nameTranslation) {
        this.nameTranslation = nameTranslation;
    }

    protected void setOverwrittenVanillaItemType(MCCItemType overwrittenVanillaItemType) {
        this.overwrittenVanillaItemType = overwrittenVanillaItemType;
    }

    protected void setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
    }

    protected void setComponentSetup(Consumer<MCCItemStack> componentSetup) {
        this.componentSetup = componentSetup;
    }

    protected void setNameFormat(Function<Translatable, Component> nameFormat) {
        this.nameFormat = nameFormat;
    }

    protected void setKey(Key key) {
        this.key = key;
    }
}
