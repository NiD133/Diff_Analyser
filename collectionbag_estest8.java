package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertThrows;

/**
 * Contains tests for the {@link CollectionBag} class.
 * This focuses on the interaction between CollectionBag's removeAll method
 * and the constraints of the underlying decorated bag.
 */
public class CollectionBagTest {

    /**
     * Tests that removeAll throws a NullPointerException when the decorated bag
     * is a TreeBag (which disallows nulls) and the collection to be removed
     * contains a null element.
     */
    @Test
    public void removeAllWithNullFromTreeBagBackedBagShouldThrowNPE() {
        // Arrange
        // Create a CollectionBag that decorates a TreeBag.
        // TreeBag does not permit null elements.
        Bag<Object> treeBagBackedCollectionBag = new CollectionBag<>(new TreeBag<>());

        // Create a collection that contains a single null element.
        Collection<Object> collectionWithNull = Collections.singleton(null);

        // Act & Assert
        // The removeAll operation should fail with a NullPointerException when it
        // attempts to process the null element against the underlying TreeBag.
        assertThrows(NullPointerException.class, () -> {
            treeBagBackedCollectionBag.removeAll(collectionWithNull);
        });
    }
}