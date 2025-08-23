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

public class CategoryItemEntity_ESTestTest5 extends CategoryItemEntity_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        Polygon polygon0 = new Polygon();
        Rectangle rectangle0 = polygon0.getBounds();
        DefaultMultiValueCategoryDataset<Integer, Integer> defaultMultiValueCategoryDataset0 = new DefaultMultiValueCategoryDataset<Integer, Integer>();
        CategoryItemEntity<Integer, Integer> categoryItemEntity0 = new CategoryItemEntity<Integer, Integer>(rectangle0, "N`", "N`", defaultMultiValueCategoryDataset0, (Integer) 0, (Integer) 0);
        // Undeclared exception!
        try {
            categoryItemEntity0.setDataset((CategoryDataset<Integer, Integer>) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Null 'dataset' argument.
            //
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }
}
