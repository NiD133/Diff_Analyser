package org.jfree.chart.labels;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.chrono.ThaiBuddhistEra;
import javax.swing.JLayeredPane;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.text.MockDateFormat;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.junit.runner.RunWith;

public class IntervalCategoryItemLabelGenerator_ESTestTest1 extends IntervalCategoryItemLabelGenerator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        IntervalCategoryItemLabelGenerator intervalCategoryItemLabelGenerator0 = new IntervalCategoryItemLabelGenerator();
        DefaultStatisticalCategoryDataset<ThaiBuddhistEra, ThaiBuddhistEra> defaultStatisticalCategoryDataset0 = new DefaultStatisticalCategoryDataset<ThaiBuddhistEra, ThaiBuddhistEra>();
        ThaiBuddhistEra thaiBuddhistEra0 = ThaiBuddhistEra.BE;
        Integer integer0 = JLayeredPane.DRAG_LAYER;
        ThaiBuddhistEra thaiBuddhistEra1 = ThaiBuddhistEra.BEFORE_BE;
        defaultStatisticalCategoryDataset0.add((Number) integer0, (Number) integer0, thaiBuddhistEra1, thaiBuddhistEra1);
        defaultStatisticalCategoryDataset0.add((double) 0, (double) 0, thaiBuddhistEra0, thaiBuddhistEra0);
        // Undeclared exception!
        try {
            intervalCategoryItemLabelGenerator0.createItemArray(defaultStatisticalCategoryDataset0, 0, 1);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Cannot format given Object as a Number
            //
            verifyException("java.text.DecimalFormat", e);
        }
    }
}
