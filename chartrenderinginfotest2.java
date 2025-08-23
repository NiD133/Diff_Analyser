package org.jfree.chart;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChartRenderingInfoTestTest2 {

    /**
     * Confirm that cloning works.
     * @throws java.lang.CloneNotSupportedException
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        ChartRenderingInfo i1 = new ChartRenderingInfo();
        ChartRenderingInfo i2 = CloneUtils.clone(i1);
        assertNotSame(i1, i2);
        assertSame(i1.getClass(), i2.getClass());
        assertEquals(i1, i2);
        // check independence
        i1.getChartArea().setRect(4.0, 3.0, 2.0, 1.0);
        assertNotEquals(i1, i2);
        i2.getChartArea().setRect(4.0, 3.0, 2.0, 1.0);
        assertEquals(i1, i2);
        i1.getEntityCollection().add(new ChartEntity(new Rectangle(1, 2, 2, 1)));
        assertNotEquals(i1, i2);
        i2.getEntityCollection().add(new ChartEntity(new Rectangle(1, 2, 2, 1)));
        assertEquals(i1, i2);
    }
}
