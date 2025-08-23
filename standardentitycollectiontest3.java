package org.jfree.chart.entity;

import java.awt.geom.Rectangle2D;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.general.DefaultPieDataset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StandardEntityCollectionTestTest3 {

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        PieSectionEntity<String> e1 = new PieSectionEntity<>(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0), new DefaultPieDataset<String>(), 0, 1, "Key", "ToolTip", "URL");
        StandardEntityCollection c1 = new StandardEntityCollection();
        c1.add(e1);
        StandardEntityCollection c2 = TestUtils.serialised(c1);
        assertEquals(c1, c2);
    }
}
