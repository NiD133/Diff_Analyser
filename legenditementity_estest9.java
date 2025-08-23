package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

import java.awt.geom.Rectangle2D;

/**
 * Unit tests for the {@link LegendItemEntity} class, focusing on equality checks.
 */
public class LegendItemEntityTest {

    /**
     * Verifies that the equals() method returns false when comparing two entities
     * with different series keys. This test specifically checks a case where one
     * entity has a series key and the other has a null series key.
     */
    @Test
    public void equals_shouldReturnFalse_whenSeriesKeysAreDifferent() {
        // Arrange
        // Create two LegendItemEntity objects with the same area shape.
        Rectangle2D.Double commonArea = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);

        LegendItemEntity<Integer> entityWithKey = new LegendItemEntity<>(commonArea);
        entityWithKey.setSeriesKey(100);

        LegendItemEntity<Integer> entityWithoutKey = new LegendItemEntity<>(commonArea);
        // The series key for entityWithoutKey remains null by default.

        // Act & Assert
        // The entities should not be equal because their series keys differ (100 vs. null).
        assertFalse("Entities with different series keys should not be equal",
                entityWithKey.equals(entityWithoutKey));
    }
}