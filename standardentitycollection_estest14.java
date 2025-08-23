package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link StandardEntityCollection} class.
 */
public class StandardEntityCollectionTest {

    /**
     * Verifies that a newly instantiated StandardEntityCollection is empty.
     * This test ensures that getEntityCount() returns 0 for a fresh instance.
     */
    @Test
    public void newCollectionShouldHaveZeroEntities() {
        // Arrange: Create a new, empty entity collection.
        StandardEntityCollection collection = new StandardEntityCollection();

        // Act: Get the number of entities in the collection.
        int entityCount = collection.getEntityCount();

        // Assert: The count should be zero.
        assertEquals("A newly created collection should be empty.", 0, entityCount);
    }
}