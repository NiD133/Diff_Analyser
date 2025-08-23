package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.awt.Rectangle;

/**
 * Contains tests for the {@link StandardEntityCollection} class, focusing on cloning behavior.
 */
public class StandardEntityCollection_ESTestTest8 extends StandardEntityCollection_ESTest_scaffolding {

    /**
     * Verifies that calling clone() on a collection does not alter the original collection.
     * This test ensures the clone operation is non-destructive to the source object.
     */
    @Test
    public void clone_shouldNotModifyOriginalCollection() throws CloneNotSupportedException {
        // Arrange: Create a collection and add one entity to it.
        StandardEntityCollection originalCollection = new StandardEntityCollection();
        ChartEntity entity = new ChartEntity(new Rectangle(10, 20, 30, 40), "Test Tooltip");
        originalCollection.add(entity);

        // Act: Clone the collection. The result is intentionally not used because this
        // test specifically verifies that the original collection remains unchanged.
        originalCollection.clone();

        // Assert: The entity count of the original collection should be unaffected.
        assertEquals("Cloning should not alter the size of the original collection.",
                1, originalCollection.getEntityCount());
    }
}