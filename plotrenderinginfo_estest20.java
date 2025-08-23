package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PlotRenderingInfo_ESTestTest20 extends PlotRenderingInfo_ESTest_scaffolding {

    /**
     * Verifies that the equals() method correctly returns false when one
     * PlotRenderingInfo object has a subplot and the other does not.
     */
    @Test
    public void equals_returnsFalseWhenSubplotListsDiffer() {
        // Arrange: Create two PlotRenderingInfo objects that are initially identical.
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo info1 = chartInfo.getPlotInfo();
        PlotRenderingInfo info2 = new PlotRenderingInfo(chartInfo);

        // Sanity check: The two instances should be equal before any modifications.
        assertEquals("Initially, the two PlotRenderingInfo objects should be equal.", info1, info2);

        // Act: Modify one of the instances by adding a subplot to it. This makes
        // its internal list of subplots different from the other's.
        info2.addSubplotInfo(info1);

        // Assert: After the modification, the two instances should no longer be equal.
        assertNotEquals("After adding a subplot to one object, they should no longer be equal.", info1, info2);
    }
}