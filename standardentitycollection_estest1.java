package org.jfree.chart.entity;

import org.junit.Test;
import java.awt.Rectangle;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link StandardEntityCollection} class.
 */
public class StandardEntityCollectionTest {

    /**
     * Verifies that getEntity(x, y) returns null when no entity in the
     * collection contains the specified coordinates. This scenario is tested
     * by adding an entity with an empty area.
     */
    @Test
    public void getEntityByCoordinates_WhenNoEntityContainsPoint_ShouldReturnNull() {
        // Arrange: Create a collection and add a single chart entity.
        // The entity's area is an empty rectangle (negative width), so it cannot contain any point.
        StandardEntityCollection collection = new StandardEntityCollection();
        Rectangle emptyArea = new Rectangle(0, 0, -1, 0);
        ChartEntity entityWithEmptyArea = new ChartEntity(emptyArea, "Test Tooltip");
        collection.add(entityWithEmptyArea);

        // Act: Attempt to retrieve an entity at an arbitrary point.
        ChartEntity foundEntity = collection.getEntity(10.0, 20.0);

        // Assert: Verify that no entity was found, as the point is not inside any entity's area.
        assertNull("Expected getEntity() to return null for coordinates outside any entity's area.", foundEntity);
    }
}