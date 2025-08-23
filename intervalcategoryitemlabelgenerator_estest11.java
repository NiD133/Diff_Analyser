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

public class IntervalCategoryItemLabelGenerator_ESTestTest11 extends IntervalCategoryItemLabelGenerator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        DecimalFormat decimalFormat0 = new DecimalFormat("qa] ~&9");
        IntervalCategoryItemLabelGenerator intervalCategoryItemLabelGenerator0 = new IntervalCategoryItemLabelGenerator("O`zwofaJ", decimalFormat0);
        double[][] doubleArray0 = new double[3][0];
        double[] doubleArray1 = new double[5];
        doubleArray0[0] = doubleArray1;
        doubleArray0[1] = doubleArray1;
        DefaultIntervalCategoryDataset defaultIntervalCategoryDataset0 = new DefaultIntervalCategoryDataset(doubleArray0, doubleArray0);
        Object[] objectArray0 = intervalCategoryItemLabelGenerator0.createItemArray(defaultIntervalCategoryDataset0, 1, 0);
        assertEquals(5, objectArray0.length);
    }
}
