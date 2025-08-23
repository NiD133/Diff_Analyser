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

public class CategoryItemEntity_ESTestTest7 extends CategoryItemEntity_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        DefaultCaret defaultCaret0 = new DefaultCaret();
        CategoryItemEntity<Integer, Integer> categoryItemEntity0 = null;
        try {
            categoryItemEntity0 = new CategoryItemEntity<Integer, Integer>(defaultCaret0, "r\"s!dma&B8-JXkb$0-", "r\"s!dma&B8-JXkb$0-", (CategoryDataset) null, (Integer) 0, (Integer) 0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Null 'dataset' argument.
            //
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }
}
