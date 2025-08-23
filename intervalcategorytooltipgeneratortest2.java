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

public class IntervalCategoryToolTipGeneratorTestTest2 {

    /**
     * Check that the subclass is not equal to an instance of the superclass.
     */
    @Test
    public void testEquals2() {
        IntervalCategoryToolTipGenerator g1 = new IntervalCategoryToolTipGenerator();
        StandardCategoryToolTipGenerator g2 = new StandardCategoryToolTipGenerator(IntervalCategoryToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT_STRING, NumberFormat.getInstance());
        assertNotEquals(g1, g2);
    }
}
