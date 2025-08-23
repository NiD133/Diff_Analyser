package org.jfree.chart.labels;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.chrono.ChronoLocalDate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.text.MockDateFormat;
import org.evosuite.runtime.mock.java.text.MockSimpleDateFormat;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.junit.runner.RunWith;

public class IntervalCategoryToolTipGenerator_ESTestTest10 extends IntervalCategoryToolTipGenerator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        MockSimpleDateFormat mockSimpleDateFormat0 = new MockSimpleDateFormat();
        IntervalCategoryToolTipGenerator intervalCategoryToolTipGenerator0 = new IntervalCategoryToolTipGenerator("org.jfree.chart.labels.IntervalCategoryToolTipGenerator", mockSimpleDateFormat0);
        double[][] doubleArray0 = new double[5][4];
        DefaultIntervalCategoryDataset defaultIntervalCategoryDataset0 = new DefaultIntervalCategoryDataset(doubleArray0, doubleArray0);
        Object[] objectArray0 = intervalCategoryToolTipGenerator0.createItemArray(defaultIntervalCategoryDataset0, 0, 0);
        assertEquals(5, objectArray0.length);
    }
}
