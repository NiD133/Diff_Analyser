package org.jfree.chart.labels;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntervalCategoryItemLabelGeneratorTestTest3 {

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        IntervalCategoryItemLabelGenerator g1 = new IntervalCategoryItemLabelGenerator();
        IntervalCategoryItemLabelGenerator g2 = (IntervalCategoryItemLabelGenerator) g1.clone();
        assertNotSame(g1, g2);
        assertSame(g1.getClass(), g2.getClass());
        assertEquals(g1, g2);
    }
}
