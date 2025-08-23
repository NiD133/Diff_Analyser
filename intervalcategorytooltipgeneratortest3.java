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

public class IntervalCategoryToolTipGeneratorTestTest3 {

    /**
     * Simple check that hashCode is implemented.
     */
    @Test
    public void testHashCode() {
        IntervalCategoryToolTipGenerator g1 = new IntervalCategoryToolTipGenerator();
        IntervalCategoryToolTipGenerator g2 = new IntervalCategoryToolTipGenerator();
        assertEquals(g1, g2);
        assertEquals(g1.hashCode(), g2.hashCode());
    }
}
