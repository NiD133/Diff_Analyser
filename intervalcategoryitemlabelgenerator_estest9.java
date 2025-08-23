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

public class IntervalCategoryItemLabelGenerator_ESTestTest9 extends IntervalCategoryItemLabelGenerator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        DefaultStatisticalCategoryDataset<ThaiBuddhistEra, ThaiBuddhistEra> defaultStatisticalCategoryDataset0 = new DefaultStatisticalCategoryDataset<ThaiBuddhistEra, ThaiBuddhistEra>();
        ThaiBuddhistEra thaiBuddhistEra0 = ThaiBuddhistEra.BEFORE_BE;
        defaultStatisticalCategoryDataset0.add((-2270.359128073), (-2270.359128073), thaiBuddhistEra0, thaiBuddhistEra0);
        DateFormat dateFormat0 = DateFormat.getDateTimeInstance();
        IntervalCategoryItemLabelGenerator intervalCategoryItemLabelGenerator0 = new IntervalCategoryItemLabelGenerator("({0}, {1}) = {3} - {4}", dateFormat0);
        Object[] objectArray0 = intervalCategoryItemLabelGenerator0.createItemArray(defaultStatisticalCategoryDataset0, 0, 0);
        assertEquals(5, objectArray0.length);
    }
}
