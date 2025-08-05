package org.jfree.chart.title;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JTable;
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
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ShortTextTitleTest extends ShortTextTitle_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testCreateBufferedImageWithEmptyTitle() throws Throwable {
        ShortTextTitle title = new ShortTextTitle("");
        CombinedDomainXYPlot<ChronoLocalDate> plot = new CombinedDomainXYPlot<>();
        JFreeChart chart = new JFreeChart("", plot);
        chart.setSubtitles(List.of(title));
        BufferedImage image = chart.createBufferedImage(1, 10, null);
        assertEquals(1, image.getTileWidth());
    }

    @Test(timeout = 4000)
    public void testArrangeRRWithCyclicNumberAxis() throws Throwable {
        ShortTextTitle title = new ShortTextTitle(".p.");
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        CyclicNumberAxis axis = new CyclicNumberAxis(90.0, Double.MAX_VALUE);
        Size2D size = title.arrangeRR(graphics, axis.DEFAULT_RANGE, null);
        assertEquals("Size2D[width=0.0, height=0.0]", size.toString());
    }

    @Test(timeout = 4000)
    public void testArrangeRRWithSpecificRange() throws Throwable {
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
    public void testArrangeNNWithPiePlot() throws Throwable {
        ShortTextTitle title = new ShortTextTitle("Polar Zoom Out");
        PiePlot<ChronoLocalDate> plot = new PiePlot<>();
        JFreeChart chart = new JFreeChart(plot);
        BufferedImage image = chart.createBufferedImage(10, 28);
        Graphics2D graphics = image.createGraphics();
        Size2D size = title.arrangeNN(graphics);
        assertEquals(98.0, size.width, 0.01);
    }

    @Test(timeout = 4000)
    public void testArrangeNNWithEmptyTitle() throws Throwable {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        ShortTextTitle title = new ShortTextTitle("");
        Graphics2D graphics = image.createGraphics();
        Size2D size = title.arrangeNN(graphics);
        assertEquals(15.0, size.height, 0.01);
    }

    @Test(timeout = 4000)
    public void testArrangeFNWithSpecificWidth() throws Throwable {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        ShortTextTitle title = new ShortTextTitle("org.jfree.chart.legend.LegendTitle");
        Graphics2D graphics = image.createGraphics();
        Size2D size = title.arrangeFN(graphics, 0.08);
        assertEquals("Size2D[width=0.0, height=0.0]", size.toString());
    }

    @Test(timeout = 4000)
    public void testArrangeFNWithEmptyTitle() throws Throwable {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        ShortTextTitle title = new ShortTextTitle("");
        Size2D size = title.arrangeFN(graphics, 0.4);
        assertEquals(0.4, size.width, 0.01);
    }

    @Test(timeout = 4000)
    public void testArrangeWithRectangleConstraint() throws Throwable {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        Range range = new Range(1.0F, 10);
        RectangleConstraint constraint = new RectangleConstraint(range, range);
        ShortTextTitle title = new ShortTextTitle("U");
        Size2D size = title.arrange(graphics, constraint);
        assertEquals(0.0, size.width, 0.01);
    }

    @Test(timeout = 4000)
    public void testArrangeWithNegativeMargin() throws Throwable {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        ShortTextTitle title = new ShortTextTitle("U");
        Graphics2D graphics = image.createGraphics();
        DefaultBoxAndWhiskerCategoryDataset<ChronoLocalDate, ChronoLocalDate> dataset = new DefaultBoxAndWhiskerCategoryDataset<>();
        Range range = dataset.getRangeBounds(true);
        RectangleConstraint constraint = new RectangleConstraint(range, range);
        title.setMargin(-2187.894, 0.08, 17.0, 0.08);
        Size2D size = title.arrange(graphics, constraint);
        assertEquals(-2153.894, size.height, 0.01);
    }

    @Test(timeout = 4000)
    public void testDrawWithNullGraphics() throws Throwable {
        ShortTextTitle title = new ShortTextTitle("org.jfree.chart.block.BorderArrangement");
        try {
            title.draw(null, null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testArrangeRRWithNullGraphics() throws Throwable {
        Range range = ValueAxis.DEFAULT_RANGE;
        ShortTextTitle title = new ShortTextTitle("A'.f!");
        try {
            title.arrangeRR(null, range, range);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testArrangeRNWithNullGraphics() throws Throwable {
        ShortTextTitle title = new ShortTextTitle("Fe`R4/pOsm9");
        try {
            title.arrangeRN(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testArrangeNNWithNullGraphics() throws Throwable {
        ShortTextTitle title = new ShortTextTitle("%gR`.z;.\"}^I4',fXd{");
        try {
            title.arrangeNN(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testArrangeFNWithNullGraphics() throws Throwable {
        ShortTextTitle title = new ShortTextTitle("Polar Zoom Out");
        try {
            title.arrangeFN(null, -1393.4121);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testArrangeWithNullConstraint() throws Throwable {
        ShortTextTitle title = new ShortTextTitle("org.jfree.chart.annotations.XYShapeAnnotation");
        try {
            title.arrange(null, null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullText() throws Throwable {
        try {
            new ShortTextTitle(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testDrawWithValidGraphics() throws Throwable {
        ShortTextTitle title = new ShortTextTitle("Polar Zoom Out");
        PiePlot<ChronoLocalDate> plot = new PiePlot<>();
        JFreeChart chart = new JFreeChart(plot);
        BufferedImage image = chart.createBufferedImage(10, 28);
        Graphics2D graphics = image.createGraphics();
        Rectangle2D.Double area = new Rectangle2D.Double(1.0, 98.0, 90.0, 1076.462779136287);
        Object params = new Object();
        title.draw(graphics, area, params);
        assertEquals(1074.462779136287, area.height, 0.01);
        assertEquals(99.0, area.getMinY(), 0.01);
    }

    @Test(timeout = 4000)
    public void testArrangeRNWithSpiderWebPlot() throws Throwable {
        ShortTextTitle title = new ShortTextTitle(".p.");
        DefaultStatisticalCategoryDataset<JTable.PrintMode, JTable.PrintMode> dataset = new DefaultStatisticalCategoryDataset<>();
        SpiderWebPlot plot = new SpiderWebPlot(dataset);
        JFreeChart chart = new JFreeChart(".p.", plot);
        BufferedImage image = chart.createBufferedImage(10, 10, null);
        Graphics2D graphics = image.createGraphics();
        Range range = new Range(-1.0, 0.1);
        Size2D size = title.arrangeRN(graphics, range);
        assertEquals(14.0, size.width, 0.01);
    }

    @Test(timeout = 4000)
    public void testArrangeRNWithDefaultRange() throws Throwable {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        ShortTextTitle title = new ShortTextTitle("");
        Graphics2D graphics = image.createGraphics();
        Range range = ValueAxis.DEFAULT_RANGE;
        Size2D size = title.arrangeRN(graphics, range);
        assertEquals(15.0, size.height, 0.01);
    }

    @Test(timeout = 4000)
    public void testArrangeRNWithSpecificRange() throws Throwable {
        ShortTextTitle title = new ShortTextTitle(".p.");
        DefaultStatisticalCategoryDataset<JTable.PrintMode, JTable.PrintMode> dataset = new DefaultStatisticalCategoryDataset<>();
        SpiderWebPlot plot = new SpiderWebPlot(dataset);
        JFreeChart chart = new JFreeChart(".p.", plot);
        BufferedImage image = chart.createBufferedImage(10, 10, null);
        Graphics2D graphics = image.createGraphics();
        Range range = new Range(-1.0, 0.1);
        Size2D size = title.arrangeRN(graphics, range);
        assertEquals("Size2D[width=0.0, height=0.0]", size.toString());
    }

    @Test(timeout = 4000)
    public void testArrangeWithFont() throws Throwable {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Font font = new Font("y>OwYa{2LeD)?\"", Font.BOLD, 2121918366);
        ShortTextTitle title = new ShortTextTitle("y>OwYa{2LeD)?\"");
        title.setFont(font);
        Graphics2D graphics = image.createGraphics();
        Size2D size = title.arrange(graphics);
        assertEquals(0.0, size.getWidth(), 0.01);
    }

    @Test(timeout = 4000)
    public void testArrangeWithNegativeRectangleConstraint() throws Throwable {
        RectangleConstraint constraint = new RectangleConstraint(-158L, -158L);
        ShortTextTitle title = new ShortTextTitle("Not yet implemented.");
        try {
            title.arrange(null, constraint);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testArrangeWithUnconstrainedHeight() throws Throwable {
        RectangleConstraint constraint = new RectangleConstraint(Double.MAX_VALUE, Double.MAX_VALUE);
        ShortTextTitle title = new ShortTextTitle("k");
        RectangleConstraint unconstrainedHeight = constraint.toUnconstrainedHeight();
        try {
            title.arrange(null, unconstrainedHeight);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testArrangeWithUnconstrainedWidth() throws Throwable {
        Range range = ValueAxis.DEFAULT_RANGE;
        RectangleConstraint constraint = new RectangleConstraint(range, 838.5372107469134);
        ShortTextTitle title = new ShortTextTitle("iT/Ila@nrA+L");
        try {
            title.arrange(null, constraint);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testArrangeWithNegativeWidthConstraint() throws Throwable {
        ShortTextTitle title = new ShortTextTitle("");
        RectangleConstraint constraint = new RectangleConstraint(-3132.55, null);
        try {
            title.arrange(null, constraint);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testArrangeWithNullWidthConstraint() throws Throwable {
        ShortTextTitle title = new ShortTextTitle("");
        RectangleConstraint constraint = new RectangleConstraint(null, null);
        RectangleConstraint unconstrainedWidth = constraint.toUnconstrainedWidth();
        try {
            title.arrange(null, unconstrainedWidth);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testArrangeWithValidGraphics() throws Throwable {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        ShortTextTitle title = new ShortTextTitle("U");
        Graphics2D graphics = image.createGraphics();
        DefaultBoxAndWhiskerCategoryDataset<ChronoLocalDate, ChronoLocalDate> dataset = new DefaultBoxAndWhiskerCategoryDataset<>();
        Range range = dataset.getRangeBounds(true);
        RectangleConstraint constraint = new RectangleConstraint(range, range);
        Size2D size = title.arrange(graphics, constraint);
        assertEquals(11.0, size.width, 0.01);
    }
}