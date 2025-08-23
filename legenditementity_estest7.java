package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.awt.Rectangle;

/**
 * Unit tests for the {@link LegendItemEntity} class.
 */
public class LegendItemEntityTest {

    /**
     * Verifies that the toString() method produces the correct output when the
     * dataset and seriesKey properties have not been set (i.e., are null).
     */
    @Test
    public void toString_withNullDatasetAndSeriesKey_returnsCorrectStringRepresentation() {
        // Arrange: Create a LegendItemEntity with default (null) properties.
        Rectangle area = new Rectangle(0, 0, 0, 0);
        LegendItemEntity<String> legendItemEntity = new LegendItemEntity<>(area);
        String expected = "LegendItemEntity: seriesKey=null, dataset=null";

        // Act: Call the toString() method.
        String actual = legendItemEntity.toString();

        // Assert: Check if the output matches the expected format.
        assertEquals(expected, actual);
    }
}