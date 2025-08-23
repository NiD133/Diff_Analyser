package org.jfree.chart;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChartRenderingInfoTestTest1 {

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        ChartRenderingInfo i1 = new ChartRenderingInfo();
        ChartRenderingInfo i2 = new ChartRenderingInfo();
        assertEquals(i1, i2);
        i1.setChartArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        assertNotEquals(i1, i2);
        i2.setChartArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        assertEquals(i1, i2);
        i1.getPlotInfo().setDataArea(new Rectangle(1, 2, 3, 4));
        assertNotEquals(i1, i2);
        i2.getPlotInfo().setDataArea(new Rectangle(1, 2, 3, 4));
        assertEquals(i1, i2);
        StandardEntityCollection e1 = new StandardEntityCollection();
        e1.add(new ChartEntity(new Rectangle(1, 2, 3, 4)));
        i1.setEntityCollection(e1);
        assertNotEquals(i1, i2);
        StandardEntityCollection e2 = new StandardEntityCollection();
        e2.add(new ChartEntity(new Rectangle(1, 2, 3, 4)));
        i2.setEntityCollection(e2);
    }
}
