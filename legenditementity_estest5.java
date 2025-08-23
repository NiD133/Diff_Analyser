package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 * Tests for the {@link LegendItemEntity} class.
 */
public class LegendItemEntityTest {

    /**
     * Verifies that the equals() method returns false when comparing a LegendItemEntity
     * with an object of a completely different type.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentType() {
        // Arrange: Create a LegendItemEntity and a standard Java Object.
        Shape area = new Rectangle2D.Double();
        LegendItemEntity<String> legendItemEntity = new LegendItemEntity<>(area);
        Object otherObject = new Object();

        // Act: Compare the two objects for equality.
        boolean isEqual = legendItemEntity.equals(otherObject);

        // Assert: The result should be false, as they are different types.
        assertFalse("A LegendItemEntity should not be equal to an object of a different class.", isEqual);
    }
}