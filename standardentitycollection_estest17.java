package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link StandardEntityCollection} class.
 */
public class StandardEntityCollectionTest {

    /**
     * Verifies that calling clear() on an already empty collection
     * correctly results in a collection with zero entities.
     */
    @Test
    public void clear_onEmptyCollection_shouldResultInZeroEntities() {
        // Arrange: Create a new, empty entity collection.
        StandardEntityCollection collection = new StandardEntityCollection();

        // Act: Clear the collection.
        collection.clear();

        // Assert: The collection should still have an entity count of 0.
        assertEquals("Entity count should be 0 after clearing an empty collection", 0, collection.getEntityCount());
    }
}