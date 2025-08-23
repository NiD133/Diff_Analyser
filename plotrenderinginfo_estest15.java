package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link PlotRenderingInfo} class.
 */
public class PlotRenderingInfoTest {

    /**
     * Verifies that calling getSubplotIndex() with a null point
     * throws an IllegalArgumentException.
     */
    @Test
    public void getSubplotIndex_withNullPoint_shouldThrowIllegalArgumentException() {
        // Arrange
        PlotRenderingInfo info = new PlotRenderingInfo(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> info.getSubplotIndex(null)
        );

        assertEquals("Null 'source' argument.", exception.getMessage());
    }
}