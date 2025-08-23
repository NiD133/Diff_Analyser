package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;
import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

/**
 * This test class is a refactoring of an auto-generated test for PlotRenderingInfo.
 * The original class name was PlotRenderingInfo_ESTestTest18.
 */
public class PlotRenderingInfoTest {

    /**
     * Verifies that cloning a PlotRenderingInfo object with a null data area
     * results in a new, independent, but equal object.
     */
    @Test
    public void clone_shouldCreateIndependentCopy_whenDataAreaIsNull() throws CloneNotSupportedException {
        // Arrange: Create a PlotRenderingInfo instance and set its data area to null.
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo originalInfo = new PlotRenderingInfo(chartInfo);
        originalInfo.setDataArea(null);

        // Act: Clone the PlotRenderingInfo object.
        PlotRenderingInfo clonedInfo = (PlotRenderingInfo) originalInfo.clone();

        // Assert: The cloned object should be a new instance, but equal to the original.
        assertNotSame("The cloned object must be a different instance.", originalInfo, clonedInfo);
        assertEquals("The cloned object's state should be equal to the original's.", originalInfo, clonedInfo);
        
        // Also, explicitly verify that the null data area was correctly handled.
        assertNull("The data area of the cloned object should also be null.", clonedInfo.getDataArea());
    }
}