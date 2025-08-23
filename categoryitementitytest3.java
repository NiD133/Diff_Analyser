package org.jfree.chart.entity;

import java.awt.geom.Rectangle2D;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CategoryItemEntityTestTest3 {

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        DefaultCategoryDataset<String, String> d = new DefaultCategoryDataset<>();
        d.addValue(1.0, "R1", "C1");
        d.addValue(2.0, "R1", "C2");
        d.addValue(3.0, "R2", "C1");
        d.addValue(4.0, "R2", "C2");
        CategoryItemEntity<String, String> e1 = new CategoryItemEntity<>(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0), "ToolTip", "URL", d, "R2", "C2");
        CategoryItemEntity<String, String> e2 = TestUtils.serialised(e1);
        assertEquals(e1, e2);
    }
}