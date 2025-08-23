package org.jfree.chart.entity;

import org.junit.Test;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import static org.junit.Assert.assertNull;

/**
 * Tests for the {@link LegendItemEntity} class, focusing on its properties.
 */
public class LegendItemEntityTest {

    /**
     * Verifies that the series key is null by default after a LegendItemEntity is created.
     */
    @Test
    public void getSeriesKeyShouldReturnNullWhenNotSet() {
        // Arrange: Create a LegendItemEntity with a default shape.
        // The series key is not set during construction.
        Shape area = new Rectangle2D.Double();
        LegendItemEntity<String> entity = new LegendItemEntity<>(area);

        // Act: Retrieve the series key.
        String seriesKey = entity.getSeriesKey();

        // Assert: The retrieved series key should be null.
        assertNull("The series key should be null by default", seriesKey);
    }
}