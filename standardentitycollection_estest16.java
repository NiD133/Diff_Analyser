package org.jfree.chart.entity;

import org.junit.Test;

/**
 * Unit tests for the {@link StandardEntityCollection} class, focusing on
 * exception handling for invalid access.
 */
public class StandardEntityCollectionTest {

    /**
     * Verifies that attempting to retrieve an entity from an empty collection
     * throws an {@link IndexOutOfBoundsException}.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getEntityOnEmptyCollectionShouldThrowIndexOutOfBoundsException() {
        // Arrange: Create a new, empty entity collection.
        StandardEntityCollection collection = new StandardEntityCollection();

        // Act & Assert: Attempting to get an entity at index 0 should throw
        // an IndexOutOfBoundsException, which is verified by the @Test annotation.
        collection.getEntity(0);
    }
}