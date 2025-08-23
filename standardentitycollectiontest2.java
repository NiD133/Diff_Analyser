package org.jfree.chart.entity;

import java.awt.geom.Rectangle2D;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.data.general.DefaultPieDataset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StandardEntityCollectionTestTest2 {

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        PieSectionEntity<String> e1 = new PieSectionEntity<>(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0), new DefaultPieDataset<String>(), 0, 1, "Key", "ToolTip", "URL");
        StandardEntityCollection c1 = new StandardEntityCollection();
        c1.add(e1);
        StandardEntityCollection c2 = CloneUtils.clone(c1);
        assertNotSame(c1, c2);
        assertSame(c1.getClass(), c2.getClass());
        assertEquals(c1, c2);
        // check independence
        c1.clear();
        assertNotEquals(c1, c2);
        c2.clear();
        assertEquals(c1, c2);
    }
}
