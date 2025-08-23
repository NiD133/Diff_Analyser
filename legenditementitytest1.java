package org.jfree.chart.entity;

import java.awt.geom.Rectangle2D;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LegendItemEntityTestTest1 {

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        LegendItemEntity<String> e1 = new LegendItemEntity<>(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        LegendItemEntity<String> e2 = new LegendItemEntity<>(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        assertEquals(e1, e2);
        e1.setArea(new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0));
        assertNotEquals(e1, e2);
        e2.setArea(new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0));
        assertEquals(e1, e2);
        e1.setToolTipText("New ToolTip");
        assertNotEquals(e1, e2);
        e2.setToolTipText("New ToolTip");
        assertEquals(e1, e2);
        e1.setURLText("New URL");
        assertNotEquals(e1, e2);
        e2.setURLText("New URL");
        assertEquals(e1, e2);
        e1.setDataset(new DefaultCategoryDataset<String, String>());
        assertNotEquals(e1, e2);
        e2.setDataset(new DefaultCategoryDataset<String, String>());
        assertEquals(e1, e2);
        e1.setSeriesKey("A");
        assertNotEquals(e1, e2);
        e2.setSeriesKey("A");
        assertEquals(e1, e2);
    }
}
