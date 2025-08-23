package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test class focuses on verifying the behavior of the CollectionSortedBag.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class CollectionSortedBagTest {

    /**
     * Tests that addAll successfully adds all elements from a given collection,
     * including duplicates, to an initially empty bag.
     */
    @Test
    public void addAllShouldAddElementsWithDuplicatesToEmptyBag() {
        // Arrange
        // Create the SUT (System Under Test): an empty CollectionSortedBag decorating a TreeBag.
        final SortedBag<Locale.Category> underlyingBag = new TreeBag<>();
        final CollectionSortedBag<Locale.Category> collectionBag = new CollectionSortedBag<>(underlyingBag);

        // Create a collection containing duplicate elements to be added to the bag.
        final Locale.Category elementToAdd = Locale.Category.DISPLAY;
        final Collection<Locale.Category> elementsToAdd = new ArrayList<>();
        elementsToAdd.add(elementToAdd);
        elementsToAdd.add(elementToAdd);

        // Act
        // Add all elements from the collection to the bag.
        final boolean wasModified = collectionBag.addAll(elementsToAdd);

        // Assert
        // 1. The addAll operation should report that the bag was modified.
        assertTrue("addAll should return true when new elements are added.", wasModified);

        // 2. The bag's total size should reflect the number of elements added.
        assertEquals("The bag size should be 2 after adding two elements.", 2, collectionBag.size());

        // 3. The count for the specific element should be 2, confirming duplicates were added.
        assertEquals("The count of the added element should be 2.", 2, collectionBag.getCount(elementToAdd));
    }
}