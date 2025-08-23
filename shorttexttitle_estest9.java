package org.jfree.chart.title;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.JapaneseDate;
import java.util.Calendar;
import java.util.List;
import javax.swing.JTable;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.time.chrono.MockJapaneseDate;
import org.evosuite.runtime.mock.java.util.MockCalendar;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CyclicNumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.chart.block.Size2D;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.plot.pie.PiePlot;
import org.jfree.data.Range;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.jfree.data.time.TimePeriodAnchor;
import org.jfree.data.time.TimeSeries;
import org.junit.runner.RunWith;

public class ShortTextTitle_ESTestTest9 extends ShortTextTitle_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        BufferedImage bufferedImage0 = new BufferedImage(10, 10, 10);
        ShortTextTitle shortTextTitle0 = new ShortTextTitle("U");
        Graphics2D graphics2D0 = bufferedImage0.createGraphics();
        DefaultBoxAndWhiskerCategoryDataset<ChronoLocalDate, ChronoLocalDate> defaultBoxAndWhiskerCategoryDataset0 = new DefaultBoxAndWhiskerCategoryDataset<ChronoLocalDate, ChronoLocalDate>();
        Range range0 = defaultBoxAndWhiskerCategoryDataset0.getRangeBounds(true);
        RectangleConstraint rectangleConstraint0 = new RectangleConstraint(range0, range0);
        shortTextTitle0.setMargin((-2187.894), 0.08, 17.0, 0.08);
        Size2D size2D0 = shortTextTitle0.arrange(graphics2D0, rectangleConstraint0);
        assertEquals((-2153.894), size2D0.height, 0.01);
    }
}
