package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.time.Clock;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.JapaneseDate;
import javax.swing.JLayeredPane;
import javax.swing.text.DefaultCaret;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.time.MockClock;
import org.evosuite.runtime.mock.java.time.MockLocalDate;
import org.evosuite.runtime.mock.java.time.chrono.MockJapaneseDate;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.junit.runner.RunWith;

public class CategoryItemEntity_ESTestTest9 extends CategoryItemEntity_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        Polygon polygon0 = new Polygon();
        DefaultCaret defaultCaret0 = new DefaultCaret();
        DefaultMultiValueCategoryDataset<Integer, Integer> defaultMultiValueCategoryDataset0 = new DefaultMultiValueCategoryDataset<Integer, Integer>();
        CategoryItemEntity<Integer, Integer> categoryItemEntity0 = new CategoryItemEntity<Integer, Integer>(defaultCaret0, "N`", "N`", defaultMultiValueCategoryDataset0, (Integer) 0, (Integer) 0);
        Integer integer0 = JLayeredPane.POPUP_LAYER;
        CategoryItemEntity<Integer, Integer> categoryItemEntity1 = new CategoryItemEntity<Integer, Integer>(polygon0, "N`", "", defaultMultiValueCategoryDataset0, integer0, (Integer) 0);
        boolean boolean0 = categoryItemEntity0.equals(categoryItemEntity1);
        assertFalse(boolean0);
    }
}
