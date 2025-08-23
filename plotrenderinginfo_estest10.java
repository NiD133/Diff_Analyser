package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;
import java.awt.geom.Rectangle2D;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link PlotRenderingInfo} class.
 */
public class PlotRenderingInfoTest {

    /**
     * Verifies that getDataArea() correctly returns the Rectangle2D object
     * that was previously set by setDataArea().
     */
    @Test
    public void testGetDataAreaReturnsPreviouslySetDataArea() {
        // Arrange
        // The ChartRenderingInfo owner is not relevant for this specific test.
        PlotRenderingInfo plotInfo = new PlotRenderingInfo(null);
        Rectangle2D expectedDataArea = new Rectangle2D.Double(10.0, 20.0, 100.0, 50.0);

        // Act
        plotInfo.setDataArea(expectedDataArea);
        Rectangle2D actualDataArea = plotInfo.getDataArea();

        // Assert
        // We assert that the retrieved object is the exact same instance that was set.
        // This is a stronger guarantee than just comparing field values.
        assertSame("The retrieved data area should be the same instance as the one set.",
                expectedDataArea, actualDataArea);
        
        // For completeness, we can also verify the objects are equal.
        assertEquals("The retrieved data area should be equal to the one set.",
                expectedDataArea, actualDataArea);
    }
}