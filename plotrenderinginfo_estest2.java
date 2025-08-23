package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the hashCode() method in the {@link PlotRenderingInfo} class.
 */
public class PlotRenderingInfo_ESTestTest2 {

    /**
     * Verifies that the hashCode is consistent and changes when the plot area is modified.
     * A consistent hashCode is essential for the correct functioning of hash-based collections
     * like HashMap or HashSet.
     */
    @Test
    public void hashCode_shouldBeConsistentAndReflectPlotAreaChanges() {
        // Arrange: Create a PlotRenderingInfo instance and get its initial hashCode.
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
        int initialHashCode = plotInfo.hashCode();

        // Act: Modify the state of the object by setting a new plot area.
        Rectangle2D plotArea = new Rectangle2D.Double(10.0, 20.0, 30.0, 40.0);
        plotInfo.setPlotArea(plotArea);
        int newHashCode = plotInfo.hashCode();

        // Assert:
        // 1. The hashCode must change when a property it depends on is modified.
        assertNotEquals("HashCode should change after setting the plot area.",
                initialHashCode, newHashCode);

        // 2. The hashCode must be consistent (return the same value) across multiple calls
        //    if the object's state has not changed.
        assertEquals("HashCode should be consistent on subsequent calls.",
                newHashCode, plotInfo.hashCode());
    }
}