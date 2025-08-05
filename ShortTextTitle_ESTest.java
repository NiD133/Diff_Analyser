package org.jfree.chart.title;

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
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
)
public class ShortTextTitle_ESTest extends ShortTextTitle_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void renderChartWithTitleAsSubtitle() throws Throwable {
        ShortTextTitle title = new ShortTextTitle("");
        CombinedDomainXYPlot<ChronoLocalDate> plot = new CombinedDomainXYPlot<>();
        JFreeChart chart = new JFreeChart("", plot);
        chart.setSubtitles(List.of(title));
        BufferedImage image = chart.createBufferedImage(1, 10, null);
        assertEquals(1, image.getTileWidth());
    }

    @Test(timeout = 4000)
    public void arrangeWithNullHeightRange() throws Throwable {
        ShortTextTitle title = new ShortTextTitle(".p.");
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        CyclicNumberAxis axis = new CyclicNumberAxis(90.0, Double.MAX_VALUE);
        Size2D size = title.arrangeRR(graphics, axis.DEFAULT_RANGE, null);
        assertEquals("Size2D[width=0.0, height=0.0]", size.toString());
    }

    @Test(timeout = 4000)
    public void arrangeWithSpecificWidthAndHeightRanges() throws Throwable {
        ShortTextTitle title = new ShortTextTitle("Not yet implemented.");
        CombinedDomainXYPlot<ChronoLocalDate> plot = new CombinedDomainXYPlot<>();
        JFreeChart chart = new JFreeChart(null, plot);
        BufferedImage image = chart.createBufferedImage(1, 10, null);
        Graphics2D graphics = image.createGraphics();
        Range range = new Range(229.0, 913.12026869883);
        Size2D size = title.arrangeRR(graphics, range, range);
        assertEquals(134.0, size.width, 0.01);
    }

    @Test(timeout = 4000)
    public void naturalSizeCalculationForSpecificText() throws Throwable {
        ShortTextTitle title = new ShortTextTitle("Polar Zoom Out");
        PiePlot<ChronoLocalDate> plot = new PiePlot<>();
        JFreeChart chart = new JFreeChart(plot);
        BufferedImage image = chart.createBufferedImage(10, 28);
        Graphics2D graphics = image.createGraphics();
        Size2D size = title.arrangeNN(graphics);
        assertEquals(98.0, size.width, 0.01);
    }

    @Test(timeout = 4000)
    public void naturalSizeCalculationForEmptyText() throws Throwable {
        ShortTextTitle title = new ShortTextTitle("");
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        Size2D size = title.arrangeNN(graphics);
        assertEquals(15.0, size.height, 0.01);
    }

    @Test(timeout = 4000)
    public void arrangeWithFixedSmallWidth() throws Throwable {
        ShortTextTitle title = new ShortTextTitle("org.jfree.chart.legend.LegendTitle");
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        Size2D size = title.arrangeFN(graphics, 0.08);
        assertEquals("Size2D[width=0.0, height=0.0]", size.toString());
    }

    @Test(timeout = 4000)
    public void arrangeWithFixedWidthForEmptyText() throws Throwable {
        ShortTextTitle title = new ShortTextTitle("");
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        Size2D size = title.arrangeFN(graphics, 0.4);
        assertEquals(0.4, size.width, 0.01);
    }

    @Test(timeout = 4000)
    public void arrangeWithFixedWidthAndHeightConstraints() throws Throwable {
        ShortTextTitle title = new ShortTextTitle("U");
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        Range range = new Range(1.0, 10.0);
        RectangleConstraint constraint = new RectangleConstraint(range, range);
        Size2D size = title.arrange(graphics, constraint);
        assertEquals(0.0, size.width, 0.01);
    }

    @Test(timeout = 4000)
    public void arrangeWithNegativeMargins() throws Throwable {
        ShortTextTitle title = new ShortTextTitle("U");
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        DefaultBoxAndWhiskerCategoryDataset<ChronoLocalDate, ChronoLocalDate> dataset = 
            new DefaultBoxAndWhiskerCategoryDataset<>();
        Range range = dataset.getRangeBounds(true);
        RectangleConstraint constraint = new RectangleConstraint(range, range);
        title.setMargin(-2187.894, 0.08, 17.0, 0.08);
        Size2D size = title.arrange(graphics, constraint);
        assertEquals(-2153.894, size.height, 0.01);
    }

    @Test(timeout = 4000)
    public void drawWithNullGraphicsThrowsException() {
        ShortTextTitle title = new ShortTextTitle("org.jfree.chart.block.BorderArrangement");
        assertThrows(NullPointerException.class, () -> {
            title.draw(null, (Rectangle2D) null, null);
        });
    }

    @Test(timeout = 4000)
    public void arrangeRRWithNullGraphicsThrowsException() {
        ShortTextTitle title = new ShortTextTitle("A'.f!");
        Range range = ValueAxis.DEFAULT_RANGE;
        assertThrows(NullPointerException.class, () -> {
            title.arrangeRR(null, range, range);
        });
    }

    @Test(timeout = 4000)
    public void arrangeRNWithNullGraphicsThrowsException() {
        ShortTextTitle title = new ShortTextTitle("Fe`R4/pOsm9");
        assertThrows(NullPointerException.class, () -> {
            title.arrangeRN(null, null);
        });
    }

    @Test(timeout = 4000)
    public void arrangeNNWithNullGraphicsThrowsException() {
        ShortTextTitle title = new ShortTextTitle("%gR`.z;.\"}^I4',fXd{");
        assertThrows(NullPointerException.class, () -> {
            title.arrangeNN(null);
        });
    }

    @Test(timeout = 4000)
    public void arrangeFNWithNullGraphicsThrowsException() {
        ShortTextTitle title = new ShortTextTitle("Polar Zoom Out");
        assertThrows(NullPointerException.class, () -> {
            title.arrangeFN(null, -1393.4121);
        });
    }

    @Test(timeout = 4000)
    public void arrangeWithNullConstraintThrowsException() {
        ShortTextTitle title = new ShortTextTitle("org.jfree.chart.annotations.XYShapeAnnotation");
        assertThrows(IllegalArgumentException.class, () -> {
            title.arrange(null, null);
        });
    }

    @Test(timeout = 4000)
    public void constructorWithNullTextThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShortTextTitle(null);
        });
    }

    @Test(timeout = 4000)
    public void drawTitleWithSpecificArea() throws Throwable {
        ShortTextTitle title = new ShortTextTitle("Polar Zoom Out");
        PiePlot<ChronoLocalDate> plot = new PiePlot<>();
        JFreeChart chart = new JFreeChart(plot);
        BufferedImage image = chart.createBufferedImage(10, 28);
        Graphics2D graphics = image.createGraphics();
        Rectangle2D area = new Rectangle2D.Double(1.0, 98.0, 90.0, 1076.462779136287);
        title.draw(graphics, area, new Object());
        assertEquals(1074.462779136287, area.getHeight(), 0.01);
        assertEquals(99.0, area.getMinY(), 0.01);
    }

    @Test(timeout = 4000)
    public void arrangeWithWidthRangeOnly() throws Throwable {
        ShortTextTitle title = new ShortTextTitle(".p.");
        DefaultStatisticalCategoryDataset<JTable.PrintMode, JTable.PrintMode> dataset = 
            new DefaultStatisticalCategoryDataset<>();
        SpiderWebPlot plot = new SpiderWebPlot(dataset);
        JFreeChart chart = new JFreeChart(".p.", plot);
        BufferedImage image = chart.createBufferedImage(10, 10, null);
        Graphics2D graphics = image.createGraphics();
        JapaneseDate date = MockJapaneseDate.now();
        TimeSeries<ChronoLocalDate> series = new TimeSeries<>(date);
        Range initialRange = new Range(-1.0, 0.1);
        Range range = series.findValueRange(initialRange, TimePeriodAnchor.END, MockCalendar.getInstance());
        Size2D size = title.arrangeRN(graphics, range);
        assertEquals(14.0, size.width, 0.01);
    }

    @Test(timeout = 4000)
    public void arrangeRNForEmptyText() throws Throwable {
        ShortTextTitle title = new ShortTextTitle("");
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        Range range = ValueAxis.DEFAULT_RANGE;
        Size2D size = title.arrangeRN(graphics, range);
        assertEquals(15.0, size.height, 0.01);
    }

    @Test(timeout = 4000)
    public void arrangeRNWithSmallRangeReturnsZero() throws Throwable {
        ShortTextTitle title = new ShortTextTitle(".p.");
        DefaultStatisticalCategoryDataset<JTable.PrintMode, JTable.PrintMode> dataset = 
            new DefaultStatisticalCategoryDataset<>();
        SpiderWebPlot plot = new SpiderWebPlot(dataset);
        JFreeChart chart = new JFreeChart(".p.", plot);
        BufferedImage image = chart.createBufferedImage(10, 10, null);
        Graphics2D graphics = image.createGraphics();
        Range range = new Range(-1.0, 0.1);
        Size2D size = title.arrangeRN(graphics, range);
        assertEquals("Size2D[width=0.0, height=0.0]", size.toString());
    }

    @Test(timeout = 4000)
    public void naturalSizeCalculationWithLargeFont() throws Throwable {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY);
        Font largeFont = new Font("y>OwYa{2LeD)?\"", Font.BOLD, 2121918366);
        ShortTextTitle title = new ShortTextTitle("y>OwYa{2LeD)?\"");
        title.setFont(largeFont);
        Graphics2D graphics = image.createGraphics();
        Size2D size = title.arrange(graphics);
        assertEquals(0.0, size.getWidth(), 0.01);
    }

    @Test(timeout = 4000)
    public void arrangeWithNegativeConstraintsThrowsException() {
        ShortTextTitle title = new ShortTextTitle("Not yet implemented.");
        RectangleConstraint constraint = new RectangleConstraint(-158L, -158L);
        assertThrows(RuntimeException.class, () -> {
            title.arrange(null, constraint);
        });
    }

    @Test(timeout = 4000)
    public void arrangeWithUnconstrainedHeightThrowsException() {
        ShortTextTitle title = new ShortTextTitle("k");
        RectangleConstraint constraint = new RectangleConstraint(Double.MAX_VALUE, Double.MAX_VALUE)
            .toUnconstrainedHeight();
        assertThrows(NullPointerException.class, () -> {
            title.arrange(null, constraint);
        });
    }

    @Test(timeout = 4000)
    public void arrangeWithFixedHeightThrowsException() {
        ShortTextTitle title = new ShortTextTitle("iT/Ila@nrA+L");
        Range range = ValueAxis.DEFAULT_RANGE;
        RectangleConstraint constraint = new RectangleConstraint(range, 838.5372107469134);
        assertThrows(RuntimeException.class, () -> {
            title.arrange(null, constraint);
        });
    }

    @Test(timeout = 4000)
    public void arrangeWithUnconstrainedHeightAndFixedWidthThrowsException() {
        ShortTextTitle title = new ShortTextTitle("");
        Range range = ValueAxis.DEFAULT_RANGE;
        RectangleConstraint constraint = new RectangleConstraint(range, 838.5372107469134)
            .toUnconstrainedHeight();
        assertThrows(NullPointerException.class, () -> {
            title.arrange(null, constraint);
        });
    }

    @Test(timeout = 4000)
    public void arrangeWithNegativeWidthAndNullRangeThrowsException() {
        ShortTextTitle title = new ShortTextTitle("");
        RectangleConstraint constraint = new RectangleConstraint(-3132.55, null);
        assertThrows(RuntimeException.class, () -> {
            title.arrange(null, constraint);
        });
    }

    @Test(timeout = 4000)
    public void arrangeWithUnconstrainedWidthThrowsException() {
        ShortTextTitle title = new ShortTextTitle("");
        RectangleConstraint constraint = new RectangleConstraint(null, null)
            .toUnconstrainedWidth();
        assertThrows(RuntimeException.class, () -> {
            title.arrange(null, constraint);
        });
    }

    @Test(timeout = 4000)
    public void arrangeWithUnconstrainedWidthAndFixedHeightThrowsException() {
        ShortTextTitle title = new ShortTextTitle("");
        RectangleConstraint constraint = new RectangleConstraint(838.5372107469134, 838.5372107469134)
            .toUnconstrainedWidth();
        assertThrows(RuntimeException.class, () -> {
            title.arrange(null, constraint);
        });
    }

    @Test(timeout = 4000)
    public void arrangeWithUnconstrainedDimensions() throws Throwable {
        ShortTextTitle title = new ShortTextTitle("U");
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        DefaultBoxAndWhiskerCategoryDataset<ChronoLocalDate, ChronoLocalDate> dataset = 
            new DefaultBoxAndWhiskerCategoryDataset<>();
        Range range = dataset.getRangeBounds(true);
        RectangleConstraint constraint = new RectangleConstraint(range, range);
        Size2D size = title.arrange(graphics, constraint);
        assertEquals(11.0, size.width, 0.01);
    }
}