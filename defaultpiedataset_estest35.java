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

public class DefaultPieDataset_ESTestTest35 extends DefaultPieDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test34() throws Throwable {
        DefaultKeyedValuesDataset<Integer> defaultKeyedValuesDataset0 = new DefaultKeyedValuesDataset<Integer>();
        SortOrder sortOrder0 = SortOrder.ASCENDING;
        defaultKeyedValuesDataset0.sortByValues(sortOrder0);
        assertEquals(0, defaultKeyedValuesDataset0.getItemCount());
    }
}