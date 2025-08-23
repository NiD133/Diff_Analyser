package org.jfree.chart.labels;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntervalCategoryItemLabelGeneratorTestTest5 {

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        IntervalCategoryItemLabelGenerator g1 = new IntervalCategoryItemLabelGenerator("{3} - {4}", DateFormat.getInstance());
        IntervalCategoryItemLabelGenerator g2 = TestUtils.serialised(g1);
        assertEquals(g1, g2);
    }
}
