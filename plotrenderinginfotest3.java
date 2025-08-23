package org.jfree.chart.plot;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlotRenderingInfoTestTest3 {

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        PlotRenderingInfo p1 = new PlotRenderingInfo(new ChartRenderingInfo());
        PlotRenderingInfo p2 = TestUtils.serialised(p1);
        assertEquals(p1, p2);
    }
}
