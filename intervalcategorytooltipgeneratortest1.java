package org.jfree.chart.labels;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntervalCategoryToolTipGeneratorTestTest1 {

    /**
     * Tests the equals() method.
     */
    @Test
    public void testEquals() {
        IntervalCategoryToolTipGenerator g1 = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator g2 = new IntervalCategoryToolTipGenerator();
        assertEquals(g1, g2);
        assertEquals(g2, g1);
        g1 = new IntervalCategoryToolTipGenerator("{3} - {4}", new DecimalFormat("0.000"));
        assertNotEquals(g1, g2);
        g2 = new IntervalCategoryToolTipGenerator("{3} - {4}", new DecimalFormat("0.000"));
        assertEquals(g1, g2);
        g1 = new IntervalCategoryToolTipGenerator("{3} - {4}", new SimpleDateFormat("d-MMM"));
        assertNotEquals(g1, g2);
        g2 = new IntervalCategoryToolTipGenerator("{3} - {4}", new SimpleDateFormat("d-MMM"));
        assertEquals(g1, g2);
    }
}
