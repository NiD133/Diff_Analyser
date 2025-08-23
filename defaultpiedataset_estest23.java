package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.time.chrono.ChronoLocalDate;
import java.util.List;
import java.util.Vector;
import javax.swing.JLayeredPane;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.api.SortOrder;
import org.jfree.chart.api.TableOrder;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.KeyedValues;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.CategoryToPieDataset;
import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.jfree.data.category.SlidingCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.junit.runner.RunWith;

public class DefaultPieDataset_ESTestTest23 extends DefaultPieDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        double[][] doubleArray0 = new double[3][9];
        double[] doubleArray1 = new double[0];
        doubleArray0[2] = doubleArray1;
        DefaultIntervalCategoryDataset defaultIntervalCategoryDataset0 = new DefaultIntervalCategoryDataset(doubleArray0, doubleArray0);
        TableOrder tableOrder0 = TableOrder.BY_COLUMN;
        CategoryToPieDataset categoryToPieDataset0 = new CategoryToPieDataset(defaultIntervalCategoryDataset0, tableOrder0, 1);
        DefaultPieDataset<Long> defaultPieDataset0 = null;
        try {
            defaultPieDataset0 = new DefaultPieDataset<Long>(categoryToPieDataset0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // Index 1 out of bounds for length 0
            //
            verifyException("org.jfree.data.category.DefaultIntervalCategoryDataset", e);
        }
    }
}
