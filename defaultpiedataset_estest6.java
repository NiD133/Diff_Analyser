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

public class DefaultPieDataset_ESTestTest6 extends DefaultPieDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        DefaultPieDataset<Integer> defaultPieDataset0 = new DefaultPieDataset<Integer>();
        Integer integer0 = JLayeredPane.FRAME_CONTENT_LAYER;
        defaultPieDataset0.insertValue(0, integer0, (double) 0);
        assertEquals(1, defaultPieDataset0.getItemCount());
    }
}
