package org.apache.commons.collections4.bag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.collection.TransformedCollectionTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link TransformedBag}.
 * <p>
 * This class extends {@link AbstractBagTest} to ensure the contract of the
 * {@link Bag} interface is met. It also contains specific tests for the

 * transformation behavior.
 *
 * @param <T> the type of the elements in the bag under test
 */
public class TransformedBagTest<T> extends AbstractBagTest<T> {

    public TransformedBagTest() {
        super(TransformedBagTest.class.getSimpleName());
    }



    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    protected int getIterationBehaviour() {
        return UNORDERED;
    }

    /**
     * Creates a new {@link TransformedBag} with a no-op transformer for the
     * generic contract tests.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Bag<T> makeObject() {
        return TransformedBag.transformingBag(new HashBag<>(), (Transformer<T, T>) TransformedCollectionTest.NOOP_TRANSFORMER);
    }

    // --- Specific behavior tests for TransformedBag ---

    /**
     * This test verifies the core transformation logic of TransformedBag using a
     * transformer that converts Strings to Integers.
     * <p>
     * NOTE: This test relies on the generic type {@code T} being compatible with
     * {@code Object}. The {@link TransformedBag} API requires a common supertype for
     * both the pre-transformed type (String) and the post-transformed type (Integer).
     * The necessary casts and suppressed warnings are a consequence of this API design
     * when dealing with cross-type transformations.
     */
    @Test
    @DisplayName("Verifies elements are transformed on add, and subsequent operations use the transformed object")
    @SuppressWarnings("unchecked")
    void testTransformationOnAddContainsAndRemove() {
        // Arrange
        final Transformer<String, Integer> stringToIntegerTransformer =
                TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER;

        // The bag's generic type must be a supertype of both String and Integer, i.e., Object.
        // We must cast the specific transformer to the bag's generic transformer type.
        final Bag<T> bag = TransformedBag.transformingBag(
                new HashBag<>(),
                (Transformer<T, T>) stringToIntegerTransformer);

        assertTrue(bag.isEmpty(), "A new bag should be empty.");

        final String[] stringsToAdd = {"1", "3", "5", "7", "2", "4", "6"};

        // Act & Assert: Add elements and verify they are transformed and correctly stored.
        for (int i = 0; i < stringsToAdd.length; i++) {
            final String originalElement = stringsToAdd[i];
            final Integer transformedElement = Integer.valueOf(originalElement);

            bag.add((T) originalElement);

            assertEquals(i + 1, bag.size(), "Bag size should increment on each add.");
            // The bag should contain the transformed Integer, not the original String.
            assertTrue(bag.contains(transformedElement), "Bag should contain the transformed element.");
            assertFalse(bag.contains(originalElement), "Bag should not contain the original element.");
        }

        // Act & Assert: Test removal logic
        final String firstOriginalElement = stringsToAdd[0]; // "1"
        final Integer firstTransformedElement = Integer.valueOf(firstOriginalElement); // 1

        // Attempting to remove the original String should fail because it was never truly added.
        assertFalse(bag.remove(firstOriginalElement), "Removing the original (untransformed) element should fail.");
        assertEquals(stringsToAdd.length, bag.size(), "Bag size should not change after a failed removal.");

        // Removing the transformed Integer should succeed.
        assertTrue(bag.remove(firstTransformedElement), "Removing the transformed element should succeed.");
        assertEquals(stringsToAdd.length - 1, bag.size(), "Bag size should decrement after a successful removal.");
    }
}