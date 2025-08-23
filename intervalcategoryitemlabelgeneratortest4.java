package org.jfree.chart.labels;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntervalCategoryItemLabelGeneratorTestTest4 {

    /**
     * Check to ensure that this class implements PublicCloneable.
     */
    @Test
    public void testPublicCloneable() {
        IntervalCategoryItemLabelGenerator g1 = new IntervalCategoryItemLabelGenerator();
        assertTrue(g1 instanceof PublicCloneable);
    }
}
