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

public class DefaultPieDataset_ESTestTest21 extends DefaultPieDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        DefaultBoxAndWhiskerCategoryDataset<Integer, Integer> defaultBoxAndWhiskerCategoryDataset0 = new DefaultBoxAndWhiskerCategoryDataset<Integer, Integer>();
        Vector<Integer> vector0 = new Vector<Integer>();
        Integer integer0 = JLayeredPane.MODAL_LAYER;
        defaultBoxAndWhiskerCategoryDataset0.add((List<? extends Number>) vector0, integer0, integer0);
        TableOrder tableOrder0 = TableOrder.BY_COLUMN;
        CategoryToPieDataset categoryToPieDataset0 = new CategoryToPieDataset(defaultBoxAndWhiskerCategoryDataset0, tableOrder0, (-374));
        DefaultPieDataset<Integer> defaultPieDataset0 = null;
        try {
            defaultPieDataset0 = new DefaultPieDataset<Integer>(categoryToPieDataset0);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
        }
    }
}
