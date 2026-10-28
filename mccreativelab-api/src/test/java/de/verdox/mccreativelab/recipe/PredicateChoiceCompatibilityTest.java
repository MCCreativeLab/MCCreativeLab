package de.verdox.mccreativelab.recipe;

import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;




class PredicateChoiceCompatibilityTest {
    @Test
    void mccFactoryPreservesAllExamplesAndPredicate() {
        ItemStack first = mock(ItemStack.class);
        ItemStack second = mock(ItemStack.class);
        var choice = RecipeChoice.PredicateChoice.create(item -> item == first, first, second);

        assertEquals(List.of(first, second), choice.recipeBookExamples());
        assertTrue(choice.test(first));
        assertFalse(choice.test(second));
        assertTrue(choice.predicate().test(first));
    }

    @Test
    void paperFactorySupportsMccAccessors() {
        ItemStack example = mock(ItemStack.class);
        when(example.clone()).thenReturn(example);
        var choice = RecipeChoice.predicateChoice(item -> item == example, example);

        assertEquals(List.of(example), choice.recipeBookExamples());
        assertTrue(choice.predicate().test(example));
        assertFalse(choice.predicate().test(mock(ItemStack.class)));
    }

    @Test
    void rejectsMissingExamplesAndNullPredicates() {
        assertThrows(IllegalArgumentException.class, () -> RecipeChoice.PredicateChoice.create(item -> true));
        assertThrows(NullPointerException.class, () -> RecipeChoice.PredicateChoice.create(null, mock(ItemStack.class)));
        assertThrows(NullPointerException.class, () -> RecipeChoice.PredicateChoice.create(item -> true, (List<ItemStack>) null));
    }
}
