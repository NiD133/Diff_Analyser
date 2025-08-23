package org.jfree.chart.plot;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlotRenderingInfoTestTest2 {

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        PlotRenderingInfo p1 = new PlotRenderingInfo(new ChartRenderingInfo());
        p1.setPlotArea(new Rectangle2D.Double());
        PlotRenderingInfo p2 = CloneUtils.clone(p1);
        assertNotSame(p1, p2);
        assertSame(p1.getClass(), p2.getClass());
        assertEquals(p1, p2);
        // check independence
        p1.getPlotArea().setRect(1.0, 2.0, 3.0, 4.0);
        assertNotEquals(p1, p2);
        p2.getPlotArea().setRect(1.0, 2.0, 3.0, 4.0);
        assertEquals(p1, p2);
        p1.getDataArea().setRect(4.0, 3.0, 2.0, 1.0);
        assertNotEquals(p1, p2);
        p2.getDataArea().setRect(4.0, 3.0, 2.0, 1.0);
        assertEquals(p1, p2);
    }
}
