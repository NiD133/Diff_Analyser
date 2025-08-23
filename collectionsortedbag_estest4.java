package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link CollectionSortedBag#add(Object)} method.
 */
// The original test class name is kept for context. 
// In a real-world scenario, this would likely be part of a single CollectionSortedBagTest class.
public class CollectionSortedBag_ESTestTest4 extends CollectionSortedBag_ESTest_scaffolding {

    /**
     * Tests that adding a new element to an empty CollectionSortedBag
     * returns true and results in the element being present in the bag.
     */
    @Test
    public void addShouldSucceedForNewElement() {
        // Arrange
        final SortedBag<Locale.Category> underlyingBag = new TreeBag<>();
        final SortedBag<Locale.Category> decoratedBag = new CollectionSortedBag<>(underlyingBag);
        final Locale.Category elementToAdd = Locale.Category.DISPLAY;

        // Act
        final boolean addResult = decoratedBag.add(elementToAdd);

        // Assert
        assertTrue("The add() method should return true to indicate the collection was modified.", addResult);
        assertTrue("The bag should contain the element after it has been added.", decoratedBag.contains(elementToAdd));
    }
}