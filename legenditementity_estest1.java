package org.jfree.chart.entity;

import org.junit.Test;
import java.awt.geom.Rectangle2D;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link LegendItemEntity} class.
 */
public class LegendItemEntityTest {

    /**
     * Verifies that the getSeriesKey() method correctly returns the value
     * that was previously set by the setSeriesKey() method.
     */
    @Test
    public void testSeriesKeyGetterAndSetter() {
        // Arrange: Create a LegendItemEntity and a sample series key.
        Rectangle2D.Double area = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
        LegendItemEntity<String> entity = new LegendItemEntity<>(area);
        String expectedKey = "Series 1";

        // Act: Set the series key and then retrieve it.
        entity.setSeriesKey(expectedKey);
        String actualKey = entity.getSeriesKey();

        // Assert: The retrieved key should be the same instance as the one set.
        assertSame("The retrieved series key should be the same as the one that was set.",
                expectedKey, actualKey);
    }
}