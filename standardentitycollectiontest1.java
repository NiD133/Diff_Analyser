package org.jfree.chart.entity;

import java.awt.geom.Rectangle2D;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.general.DefaultPieDataset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StandardEntityCollectionTestTest1 {

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        StandardEntityCollection c1 = new StandardEntityCollection();
        StandardEntityCollection c2 = new StandardEntityCollection();
        assertEquals(c1, c2);
        PieSectionEntity<String> e1 = new PieSectionEntity<>(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0), new DefaultPieDataset<String>(), 0, 1, "Key", "ToolTip", "URL");
        c1.add(e1);
        assertNotEquals(c1, c2);
        PieSectionEntity<String> e2 = new PieSectionEntity<>(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0), new DefaultPieDataset<String>(), 0, 1, "Key", "ToolTip", "URL");
        c2.add(e2);
        assertEquals(c1, c2);
    }
}
