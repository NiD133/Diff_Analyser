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

public class CategoryItemEntity_ESTestTest4 extends CategoryItemEntity_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        DefaultCaret defaultCaret0 = new DefaultCaret();
        DefaultMultiValueCategoryDataset<Integer, Integer> defaultMultiValueCategoryDataset0 = new DefaultMultiValueCategoryDataset<Integer, Integer>();
        Clock clock0 = MockClock.systemUTC();
        JapaneseDate japaneseDate0 = MockJapaneseDate.now(clock0);
        CategoryItemEntity<Integer, ChronoLocalDate> categoryItemEntity0 = new CategoryItemEntity<Integer, ChronoLocalDate>(defaultCaret0, "N`", "", defaultMultiValueCategoryDataset0, (Integer) 0, japaneseDate0);
        ChronoLocalDate chronoLocalDate0 = categoryItemEntity0.getColumnKey();
        assertSame(japaneseDate0, chronoLocalDate0);
    }
}
