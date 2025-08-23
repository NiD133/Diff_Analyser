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

public class ShortTextTitle_ESTestTest21 extends ShortTextTitle_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        BufferedImage bufferedImage0 = new BufferedImage(1, 1, 1);
        Font font0 = new Font("y>OwYa{2LeD)?\"", 1, 2121918366);
        ShortTextTitle shortTextTitle0 = new ShortTextTitle("y>OwYa{2LeD)?\"");
        shortTextTitle0.setFont(font0);
        Graphics2D graphics2D0 = bufferedImage0.createGraphics();
        Size2D size2D0 = shortTextTitle0.arrange(graphics2D0);
        assertEquals(0.0, size2D0.getWidth(), 0.01);
    }
}
