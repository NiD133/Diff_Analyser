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

public class IntervalCategoryToolTipGeneratorTestTest6 {

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        IntervalCategoryToolTipGenerator g1 = new IntervalCategoryToolTipGenerator("{3} - {4}", DateFormat.getInstance());
        IntervalCategoryToolTipGenerator g2 = TestUtils.serialised(g1);
        assertEquals(g1, g2);
    }
}
