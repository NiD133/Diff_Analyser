package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.assertNull;

import java.awt.Rectangle;

/**
 * Tests for the {@link StandardEntityCollection} class.
 */
public class StandardEntityCollectionTest {

    /**
     * Verifies that getEntity(x, y) returns null when the collection contains
     * entities, but none of them enclose the specified coordinates.
     */
    @Test
    public void getEntityByCoordinates_WhenNoEntityContainsPoint_ShouldReturnNull() {
        // Arrange: Create a collection with one entity whose shape is a rectangle.
        StandardEntityCollection entityCollection = new StandardEntityCollection();
        Rectangle entityArea = new Rectangle(10, 10, 100, 100);
        ChartEntity chartEntity = new ChartEntity(entityArea);
        entityCollection.add(chartEntity);

        // Act: Attempt to retrieve an entity from a point (0, 0) that is
        // clearly outside the entity's area.
        ChartEntity foundEntity = entityCollection.getEntity(0.0, 0.0);

        // Assert: The method should return null as no entity was found.
        assertNull("Expected no entity to be found at coordinates (0,0)", foundEntity);
    }
}