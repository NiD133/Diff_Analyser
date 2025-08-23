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

public class DefaultPieDataset_ESTestTest20 extends DefaultPieDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        SlidingCategoryDataset<ChronoLocalDate, ChronoLocalDate> slidingCategoryDataset0 = new SlidingCategoryDataset<ChronoLocalDate, ChronoLocalDate>((CategoryDataset<ChronoLocalDate, ChronoLocalDate>) null, (-1795), (-1795));
        TableOrder tableOrder0 = TableOrder.BY_COLUMN;
        CategoryToPieDataset categoryToPieDataset0 = new CategoryToPieDataset(slidingCategoryDataset0, tableOrder0, (-1795));
        DefaultPieDataset<Integer> defaultPieDataset0 = null;
        try {
            defaultPieDataset0 = new DefaultPieDataset<Integer>(categoryToPieDataset0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jfree.data.category.SlidingCategoryDataset", e);
        }
    }
}