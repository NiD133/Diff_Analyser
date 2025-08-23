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

public class IntervalCategoryToolTipGenerator_ESTestTest8 extends IntervalCategoryToolTipGenerator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        IntervalCategoryToolTipGenerator intervalCategoryToolTipGenerator0 = new IntervalCategoryToolTipGenerator();
        boolean boolean0 = intervalCategoryToolTipGenerator0.equals(intervalCategoryToolTipGenerator0);
        assertTrue(boolean0);
    }
}
