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

public class ShortTextTitle_ESTestTest1 extends ShortTextTitle_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        ShortTextTitle shortTextTitle0 = new ShortTextTitle("");
        CombinedDomainXYPlot<ChronoLocalDate> combinedDomainXYPlot0 = new CombinedDomainXYPlot<ChronoLocalDate>();
        JFreeChart jFreeChart0 = new JFreeChart("", combinedDomainXYPlot0);
        List<Title> list0 = List.of((Title) shortTextTitle0);
        jFreeChart0.setSubtitles(list0);
        BufferedImage bufferedImage0 = jFreeChart0.createBufferedImage(1, 10, (ChartRenderingInfo) null);
        assertEquals(1, bufferedImage0.getTileWidth());
    }
}
