package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;
import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

/**
 * Contains tests for the equals() method of the {@link PlotRenderingInfo} class.
 */
public class PlotRenderingInfoEqualsTest {

    /**
     * Verifies that two PlotRenderingInfo objects are no longer equal after
     * the plot area of one is modified.
     */
    @Test
    public void equals_shouldReturnFalse_whenPlotAreasDiffer() {
        // Arrange: Create two separate but identical PlotRenderingInfo objects.
        ChartRenderingInfo owner = new ChartRenderingInfo();
        PlotRenderingInfo info1 = new PlotRenderingInfo(owner);
        PlotRenderingInfo info2 = new PlotRenderingInfo(owner);

        // Sanity check: The two newly created objects should be equal.
        assertTrue("Two newly instantiated PlotRenderingInfo objects with the same owner should be equal.",
                info1.equals(info2));

        // Act: Modify the plot area of the first object. The plot area is initially null.
        Rectangle2D newPlotArea = new Rectangle2D.Double(10, 20, 30, 40);
        info1.setPlotArea(newPlotArea);

        // Assert: The two objects should now be considered unequal.
        assertFalse("After setting a non-null plot area on one object, it should no longer be equal to the other.",
                info1.equals(info2));
        
        // A more modern equivalent to the above assertion.
        assertNotEquals("The hash codes should also differ after modification.", 
                info1.hashCode(), info2.hashCode());
    }
}