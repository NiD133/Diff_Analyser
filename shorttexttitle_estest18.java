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

public class ShortTextTitle_ESTestTest18 extends ShortTextTitle_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        ShortTextTitle shortTextTitle0 = new ShortTextTitle(".p.");
        DefaultStatisticalCategoryDataset<JTable.PrintMode, JTable.PrintMode> defaultStatisticalCategoryDataset0 = new DefaultStatisticalCategoryDataset<JTable.PrintMode, JTable.PrintMode>();
        SpiderWebPlot spiderWebPlot0 = new SpiderWebPlot(defaultStatisticalCategoryDataset0);
        JFreeChart jFreeChart0 = new JFreeChart(".p.", spiderWebPlot0);
        BufferedImage bufferedImage0 = jFreeChart0.createBufferedImage(10, 10, (ChartRenderingInfo) null);
        Graphics2D graphics2D0 = bufferedImage0.createGraphics();
        JapaneseDate japaneseDate0 = MockJapaneseDate.now();
        TimeSeries<ChronoLocalDate> timeSeries0 = new TimeSeries<ChronoLocalDate>(japaneseDate0);
        Range range0 = new Range((-1.0), 0.1);
        TimePeriodAnchor timePeriodAnchor0 = TimePeriodAnchor.END;
        Calendar calendar0 = MockCalendar.getInstance();
        Range range1 = timeSeries0.findValueRange(range0, timePeriodAnchor0, calendar0);
        Size2D size2D0 = shortTextTitle0.arrangeRN(graphics2D0, range1);
        assertEquals(14.0, size2D0.width, 0.01);
    }
}
