package org.jfree.chart;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Locale;
import java.util.SimpleTimeZone;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.CombinedRangeCategoryPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class ChartRenderingInfo_ESTest extends ChartRenderingInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testSetAndGetChartArea() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        Rectangle rectangle = new Rectangle(-1, -1, 1, 7);
        chartInfo.setChartArea(rectangle);
        assertEquals(rectangle, chartInfo.getChartArea());
    }

    @Test(timeout = 4000)
    public void testPlotRenderingInfoSubplotCount() {
        StandardEntityCollection entityCollection = new StandardEntityCollection();
        ChartRenderingInfo chartInfo = new ChartRenderingInfo(entityCollection);
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
        plotInfo.addSubplotInfo(plotInfo);
        assertEquals(1, plotInfo.getSubplotCount());
    }

    @Test(timeout = 4000)
    public void testSetEntityCollectionToNull() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        chartInfo.setEntityCollection(null);
        assertNull(chartInfo.getEntityCollection());
    }

    @Test(timeout = 4000)
    public void testEntityCollectionAfterChartRendering() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        SimpleTimeZone timeZone = new SimpleTimeZone(1, "TestZone");
        Locale locale = Locale.JAPANESE;
        DateAxis dateAxis = new DateAxis("TestAxis", timeZone, locale);
        CombinedRangeCategoryPlot plot = new CombinedRangeCategoryPlot(dateAxis);
        JFreeChart chart = new JFreeChart(plot);
        chart.createBufferedImage(10, 10, 1749.95538, 2.0F, chartInfo);
        EntityCollection entityCollection = chartInfo.getEntityCollection();
        assertEquals(3, entityCollection.getEntityCount());
    }

    @Test(timeout = 4000)
    public void testChartAreaWithNegativeDimensions() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo(null);
        Rectangle rectangle = new Rectangle(1, 294, -343, 7);
        chartInfo.setChartArea(rectangle);
        Rectangle2D.Double chartArea = (Rectangle2D.Double) chartInfo.getChartArea();
        assertEquals(294.0, chartArea.y, 0.01);
    }

    @Test(timeout = 4000)
    public void testChartAreaCenterYCalculation() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        Rectangle rectangle = new Rectangle(-521, -782, 0, -2457);
        chartInfo.setChartArea(rectangle);
        Rectangle2D chartArea = chartInfo.getChartArea();
        assertEquals(-2010.5, chartArea.getCenterY(), 0.01);
    }

    @Test(timeout = 4000)
    public void testSetChartAreaUsingRectangle2D() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        Rectangle2D.Double rectangle2D = new Rectangle2D.Double();
        rectangle2D.setFrameFromDiagonal(2626.9361169318868, 2868.898929, 0.0, 2868.898929);
        chartInfo.setChartArea(rectangle2D);
        Rectangle2D chartArea = chartInfo.getChartArea();
        assertEquals(0.0, chartArea.getMinX(), 0.01);
    }

    @Test(timeout = 4000)
    public void testMaxXCalculation() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        Rectangle rectangle = new Rectangle(-1, -1, 1, 7);
        chartInfo.setChartArea(rectangle);
        Rectangle2D chartArea = chartInfo.getChartArea();
        assertEquals(0.0, chartArea.getMaxX(), 0.01);
    }

    @Test(timeout = 4000)
    public void testSetNullChartAreaThrowsException() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        try {
            chartInfo.setChartArea(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testHashCodeWithCircularReferenceThrowsException() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
        plotInfo.addSubplotInfo(plotInfo);
        try {
            chartInfo.hashCode();
            fail("Expecting exception: StackOverflowError");
        } catch (StackOverflowError e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentEntityCollections() {
        ChartRenderingInfo chartInfo1 = new ChartRenderingInfo();
        ChartRenderingInfo chartInfo2 = new ChartRenderingInfo();
        assertTrue(chartInfo1.equals(chartInfo2));

        chartInfo2.setEntityCollection(null);
        assertFalse(chartInfo1.equals(chartInfo2));
    }

    @Test(timeout = 4000)
    public void testEqualsAfterRendering() {
        ChartRenderingInfo chartInfo1 = new ChartRenderingInfo();
        ChartRenderingInfo chartInfo2 = new ChartRenderingInfo();
        assertTrue(chartInfo1.equals(chartInfo2));

        SimpleTimeZone timeZone = new SimpleTimeZone(1, "TestZone");
        Locale locale = Locale.JAPANESE;
        DateAxis dateAxis = new DateAxis("TestAxis", timeZone, locale);
        CombinedRangeCategoryPlot plot = new CombinedRangeCategoryPlot(dateAxis);
        JFreeChart chart = new JFreeChart(plot);
        chart.createBufferedImage(10, 10, 1749.95538, 2.0F, chartInfo1);

        assertFalse(chartInfo1.equals(chartInfo2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentObjectType() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        assertFalse(chartInfo.equals(new Object()));
    }

    @Test(timeout = 4000)
    public void testEqualsWithSameInstance() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        assertTrue(chartInfo.equals(chartInfo));
    }

    @Test(timeout = 4000)
    public void testCloneAndEquals() throws CloneNotSupportedException {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        Object clone = chartInfo.clone();
        assertNotSame(clone, chartInfo);
        assertTrue(chartInfo.equals(clone));
    }

    @Test(timeout = 4000)
    public void testClearChartRenderingInfo() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        chartInfo.clear();
    }

    @Test(timeout = 4000)
    public void testClearChartRenderingInfoWithNullEntities() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo(null);
        chartInfo.clear();
    }

    @Test(timeout = 4000)
    public void testCloneWithNullEntityCollection() throws CloneNotSupportedException {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        chartInfo.setEntityCollection(null);
        Object clone = chartInfo.clone();
        assertNotSame(clone, chartInfo);
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentPlotInfo() {
        StandardEntityCollection entityCollection = new StandardEntityCollection();
        ChartRenderingInfo chartInfo1 = new ChartRenderingInfo(entityCollection);
        PlotRenderingInfo plotInfo = chartInfo1.getPlotInfo();
        ChartRenderingInfo chartInfo2 = new ChartRenderingInfo();
        assertTrue(chartInfo2.equals(chartInfo1));

        plotInfo.addSubplotInfo(plotInfo);
        assertFalse(chartInfo1.equals(chartInfo2));
    }

    @Test(timeout = 4000)
    public void testEntityCollectionEntityCount() {
        StandardEntityCollection entityCollection = new StandardEntityCollection();
        ChartRenderingInfo chartInfo = new ChartRenderingInfo(entityCollection);
        assertEquals(0, chartInfo.getEntityCollection().getEntityCount());
    }

    @Test(timeout = 4000)
    public void testDefaultChartAreaHeight() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        Rectangle2D.Double chartArea = (Rectangle2D.Double) chartInfo.getChartArea();
        assertEquals(0.0, chartArea.height, 0.01);
    }
}