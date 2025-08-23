package org.jfree.chart.labels;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntervalCategoryItemLabelGeneratorTestTest2 {

    /**
     * Simple check that hashCode is implemented.
     */
    @Test
    public void testHashCode() {
        IntervalCategoryItemLabelGenerator g1 = new IntervalCategoryItemLabelGenerator();
        IntervalCategoryItemLabelGenerator g2 = new IntervalCategoryItemLabelGenerator();
        assertEquals(g1, g2);
        assertEquals(g1.hashCode(), g2.hashCode());
    }
}