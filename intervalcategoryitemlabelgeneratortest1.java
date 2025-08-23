package org.jfree.chart.labels;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntervalCategoryItemLabelGeneratorTestTest1 {

    /**
     * Tests the equals() method.
     */
    @Test
    public void testEquals() {
        IntervalCategoryItemLabelGenerator g1 = new IntervalCategoryItemLabelGenerator();
        IntervalCategoryItemLabelGenerator g2 = new IntervalCategoryItemLabelGenerator();
        assertEquals(g1, g2);
        assertEquals(g2, g1);
        g1 = new IntervalCategoryItemLabelGenerator("{3} - {4}", new DecimalFormat("0.000"));
        assertNotEquals(g1, g2);
        g2 = new IntervalCategoryItemLabelGenerator("{3} - {4}", new DecimalFormat("0.000"));
        assertEquals(g1, g2);
        g1 = new IntervalCategoryItemLabelGenerator("{3} - {4}", new SimpleDateFormat("d-MMM"));
        assertNotEquals(g1, g2);
        g2 = new IntervalCategoryItemLabelGenerator("{3} - {4}", new SimpleDateFormat("d-MMM"));
        assertEquals(g1, g2);
    }
}
