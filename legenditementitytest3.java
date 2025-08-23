package org.jfree.chart.entity;

import java.awt.geom.Rectangle2D;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LegendItemEntityTestTest3 {

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        LegendItemEntity<String> e1 = new LegendItemEntity<>(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        LegendItemEntity<String> e2 = TestUtils.serialised(e1);
        assertEquals(e1, e2);
    }
}
