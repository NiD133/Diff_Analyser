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

public class ShortTextTitle_ESTestTest17 extends ShortTextTitle_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        ShortTextTitle shortTextTitle0 = new ShortTextTitle("Polar Zoom Out");
        PiePlot<ChronoLocalDate> piePlot0 = new PiePlot<ChronoLocalDate>();
        JFreeChart jFreeChart0 = new JFreeChart(piePlot0);
        BufferedImage bufferedImage0 = jFreeChart0.createBufferedImage(10, 28);
        Graphics2D graphics2D0 = bufferedImage0.createGraphics();
        Rectangle2D.Double rectangle2D_Double0 = new Rectangle2D.Double(1.0, 98.0, 90.0, 1076.462779136287);
        Object object0 = new Object();
        shortTextTitle0.draw(graphics2D0, (Rectangle2D) rectangle2D_Double0, object0);
        assertEquals(1074.462779136287, rectangle2D_Double0.height, 0.01);
        assertEquals(99.0, rectangle2D_Double0.getMinY(), 0.01);
    }
}
