package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// The original test class name and hierarchy are preserved.
public class CollectionBag_ESTestTest41 extends CollectionBag_ESTest_scaffolding {

    /**
     * Tests that addAll correctly adds all elements from a source collection
     * to the decorated bag and returns true, indicating the bag was modified.
     */
    @Test
    public void addAllShouldAddElementsFromCollectionAndReturnTrueWhenModified() {
        // Arrange
        final Integer element = 2358;

        // 1. Create the underlying bag that will be decorated.
        //    Start with two instances of the element.
        Bag<Integer> underlyingBag = new HashBag<>();
        underlyingBag.add(element, 2);

        // 2. The CollectionBag decorator is the object under test.
        CollectionBag<Integer> collectionBag = new CollectionBag<>(underlyingBag);

        // 3. Create the source collection containing elements to be added.
        //    This bag also contains two instances of the same element.
        Bag<Integer> elementsToAdd = new TreeBag<>();
        elementsToAdd.add(element, 2);

        // Act
        // Add all elements from the source collection to the decorated bag.
        boolean wasModified = collectionBag.addAll(elementsToAdd);

        // Assert
        // 1. Verify that the method returns true, as the collection was modified.
        assertTrue("addAll should return true when the bag is modified.", wasModified);

        // 2. Verify the final state of the bag.
        //    The bag should now contain the sum of the elements.
        //    Initial count (2) + added count (2) = 4.
        final int expectedTotalCount = 4;
        assertEquals("The total size of the bag should be the sum of the initial and added elements.",
                expectedTotalCount, collectionBag.size());
        assertEquals("The count of the specific element should be the sum of its initial and added counts.",
                expectedTotalCount, collectionBag.getCount(element));
    }
}