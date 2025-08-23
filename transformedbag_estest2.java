package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Contains tests for the {@link TransformedBag} class, focusing on its interaction
 * with the decorated bag's behavior.
 */
public class TransformedBag_ESTestTest2 {

    /**
     * Tests that TransformedBag.remove(object, nCopies) correctly delegates to the
     * underlying bag, which may use a custom comparator to find and remove elements.
     * In this case, the remove operation succeeds even with an object of a different
     * type because the underlying TreeBag's comparator considers them equal.
     */
    @Test
    public void testRemoveUsesUnderlyingBagComparatorForElementMatching() {
        // Arrange

        // 1. Create a comparator that considers all objects to be equal.
        // This is the key to testing that the remove operation depends on the
        // underlying bag's equality definition, not standard .equals().
        @SuppressWarnings("unchecked")
        final Comparator<Object> allEqualComparator = mock(Comparator.class);
        doReturn(0).when(allEqualComparator).compare(any(), any());

        // 2. Set up the bags. The TransformedBag will decorate a TreeBag
        // that uses our custom "all-equal" comparator.
        final TreeBag<Integer> underlyingBag = new TreeBag<>(allEqualComparator);
        final Transformer<Object, Integer> transformer = new ConstantTransformer<>(null);
        final TransformedBag<Integer> transformedBag = new TransformedSortedBag<>(underlyingBag, transformer);

        // 3. Add an element directly to the underlying bag to bypass the transformer.
        final Integer elementInBag = 100;
        underlyingBag.add(elementInBag);
        assertEquals("Precondition: The bag should contain one element.", 1, underlyingBag.size());

        // 4. Define the object to remove. It is a different type and value,
        // but the comparator will report it as equal to elementInBag.
        final Object objectToRemove = "a different object";
        final int copiesToRemove = 5; // A number greater than the count in the bag.

        // Act
        // Attempt to remove the object. The TransformedBag delegates this to the
        // TreeBag, which uses the allEqualComparator to find and remove elementInBag.
        final boolean wasModified = transformedBag.remove(objectToRemove, copiesToRemove);

        // Assert
        assertTrue("The remove operation should report that the bag was modified.", wasModified);
        assertTrue("The bag should be empty after the element is removed.", underlyingBag.isEmpty());
    }
}