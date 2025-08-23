package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains an improved version of a test for the {@link CollectionBag#removeAll(Collection)} method.
 */
public class CollectionBag_ESTestTest38 {

    /**
     * Tests that {@link CollectionBag#removeAll(Collection)} removes all occurrences of
     * the elements present in the given collection, ignoring cardinality.
     * <p>
     * The Javadoc for CollectionBag#removeAll states:
     * "Remove all elements represented in the given collection,
     * <strong>not</strong> respecting cardinality. That is, remove <em>all</em>
     * occurrences of every object contained in the given collection."
     */
    @Test
    public void removeAllShouldRemoveAllCopiesOfContainedElements() {
        // Arrange
        // Create a bag with multiple copies of elements "A" and "B".
        final Bag<String> decoratedBag = new HashBag<>();
        decoratedBag.add("A", 2); // 2 copies of "A"
        decoratedBag.add("B", 3); // 3 copies of "B"
        final Bag<String> bagUnderTest = CollectionBag.collectionBag(decoratedBag);

        // The collection of elements to remove contains one "A" and one "C".
        // "C" is not present in the original bag, which tests that no errors occur.
        final Collection<String> elementsToRemove = Arrays.asList("A", "C");

        // Act
        // This should remove all copies of "A" and do nothing for "C".
        final boolean wasModified = bagUnderTest.removeAll(elementsToRemove);

        // Assert
        // 1. The method should return true, as the bag was modified by removing "A".
        assertTrue("removeAll should return true because an element was removed.", wasModified);

        // 2. The bag should no longer contain any copies of "A".
        assertFalse("Bag should not contain 'A' after removeAll.", bagUnderTest.contains("A"));
        assertEquals("The count of 'A' should be 0.", 0, bagUnderTest.getCount("A"));

        // 3. The element "B" should be unaffected.
        assertEquals("The count of 'B' should remain 3.", 3, bagUnderTest.getCount("B"));

        // 4. The total size should reflect the removal of all copies of "A".
        assertEquals("The total size of the bag should be 3 (the remaining copies of 'B').", 3, bagUnderTest.size());
    }
}