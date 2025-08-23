package org.jfree.chart.plot;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlotRenderingInfoTestTest1 {

    /**
     * Test the equals() method.
     */
    @Test
    public void testEquals() {
        PlotRenderingInfo p1 = new PlotRenderingInfo(new ChartRenderingInfo());
        PlotRenderingInfo p2 = new PlotRenderingInfo(new ChartRenderingInfo());
        assertEquals(p1, p2);
        assertEquals(p2, p1);
        p1.setPlotArea(new Rectangle(2, 3, 4, 5));
        assertNotEquals(p1, p2);
        p2.setPlotArea(new Rectangle(2, 3, 4, 5));
        assertEquals(p1, p2);
        p1.setDataArea(new Rectangle(2, 4, 6, 8));
        assertNotEquals(p1, p2);
        p2.setDataArea(new Rectangle(2, 4, 6, 8));
        assertEquals(p1, p2);
        p1.addSubplotInfo(new PlotRenderingInfo(null));
        assertNotEquals(p1, p2);
        p2.addSubplotInfo(new PlotRenderingInfo(null));
        assertEquals(p1, p2);
        p1.getSubplotInfo(0).setDataArea(new Rectangle(1, 2, 3, 4));
        assertNotEquals(p1, p2);
        p2.getSubplotInfo(0).setDataArea(new Rectangle(1, 2, 3, 4));
        assertEquals(p1, p2);
    }
}
