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

public class DefaultPieDataset_ESTestTest30 extends DefaultPieDataset_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        DefaultPieDataset<Integer> defaultPieDataset0 = new DefaultPieDataset<Integer>();
        Integer integer0 = JLayeredPane.DRAG_LAYER;
        defaultPieDataset0.setValue(integer0, (Number) null);
        DefaultPieDataset<Integer> defaultPieDataset1 = new DefaultPieDataset<Integer>(defaultPieDataset0);
        Integer integer1 = JLayeredPane.POPUP_LAYER;
        defaultPieDataset1.clear();
        defaultPieDataset1.setValue(integer1, (Number) integer0);
        boolean boolean0 = defaultPieDataset0.equals(defaultPieDataset1);
        assertEquals(1, defaultPieDataset1.getItemCount());
        assertFalse(boolean0);
    }
}
