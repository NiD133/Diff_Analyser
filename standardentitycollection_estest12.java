package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Rectangle;

/**
 * Contains tests for the {@link StandardEntityCollection#getEntity(double, double)} method.
 */
public class StandardEntityCollection_ESTestTest12 {

    /**
     * Verifies that getEntity(x, y) returns the correct entity when the
     * specified coordinates fall within that entity's area.
     */
    @Test
    public void getEntity_whenCoordinatesAreWithinBounds_shouldReturnCorrectEntity() {
        // Arrange: Create a collection and add a single entity with a known area.
        StandardEntityCollection collection = new StandardEntityCollection();
        
        // Define an entity with a simple rectangular area from (10, 20) to (110, 70).
        Rectangle entityArea = new Rectangle(10, 20, 100, 50);
        ChartEntity expectedEntity = new ChartEntity(entityArea, "Test ToolTip", "http://www.jfree.org/");
        collection.add(expectedEntity);

        // Act: Attempt to retrieve an entity at a point inside the defined area.
        ChartEntity foundEntity = collection.getEntity(50.0, 50.0);

        // Assert: The correct entity object should be returned.
        assertNotNull("An entity should have been found at the specified coordinates.", foundEntity);
        assertSame("The returned entity should be the exact instance that was added.", expectedEntity, foundEntity);
    }
}