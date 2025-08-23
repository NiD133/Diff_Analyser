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

public class IntervalCategoryToolTipGenerator_ESTestTest1 extends IntervalCategoryToolTipGenerator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        IntervalCategoryToolTipGenerator intervalCategoryToolTipGenerator0 = new IntervalCategoryToolTipGenerator();
        // Undeclared exception!
        try {
            intervalCategoryToolTipGenerator0.createItemArray((CategoryDataset) null, 70, 70);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jfree.chart.labels.IntervalCategoryToolTipGenerator", e);
        }
    }
}
