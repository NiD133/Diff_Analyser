package org.jfree.chart.plot;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class PlotRenderingInfoTest extends PlotRenderingInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testSetDataAreaWithNegativeCoordinates() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
        Rectangle negativeRectangle = new Rectangle(-352, 0);
        plotInfo.setDataArea(negativeRectangle);
        assertEquals(0.0, plotInfo.getDataArea().getX(), 0.01);
    }

    @Test(timeout = 4000)
    public void testSetPlotAreaWithLineBounds() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
        Point point = new Point(4047, 4047);
        Line2D.Float line = new Line2D.Float(point, point);
        Rectangle2D lineBounds = line.getBounds2D();
        plotInfo.setPlotArea(lineBounds);
        assertNotNull(plotInfo.getPlotArea());
    }

    @Test(timeout = 4000)
    public void testAddSubplotInfo() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo mainPlotInfo = chartInfo.getPlotInfo();
        PlotRenderingInfo subplotInfo = new PlotRenderingInfo(chartInfo);
        mainPlotInfo.addSubplotInfo(subplotInfo);
        PlotRenderingInfo retrievedSubplotInfo = mainPlotInfo.getSubplotInfo(0);
        assertNotSame(mainPlotInfo, retrievedSubplotInfo);
    }

    @Test(timeout = 4000)
    public void testSubplotCountAfterAddingSubplot() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = new PlotRenderingInfo(chartInfo);
        plotInfo.addSubplotInfo(plotInfo);
        assertEquals(1, plotInfo.getSubplotCount());
    }

    @Test(timeout = 4000)
    public void testSetPlotAreaWithEmptyRectangle() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        Rectangle emptyRectangle = new Rectangle();
        PlotRenderingInfo plotInfo = new PlotRenderingInfo(chartInfo);
        plotInfo.setPlotArea(emptyRectangle);
        assertEquals(0.0, plotInfo.getPlotArea().getMaxY(), 0.01);
    }

    @Test(timeout = 4000)
    public void testSetPlotAreaWithDimension() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo((EntityCollection) null);
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
        Dimension dimension = new Dimension(1595, 2);
        Rectangle dimensionRectangle = new Rectangle(dimension);
        plotInfo.setPlotArea(dimensionRectangle);
        assertEquals(1595.0, plotInfo.getPlotArea().getWidth(), 0.01);
    }

    @Test(timeout = 4000)
    public void testOwnerIsNullWhenNotSet() {
        PlotRenderingInfo plotInfo = new PlotRenderingInfo((ChartRenderingInfo) null);
        assertNull(plotInfo.getOwner());
    }

    @Test(timeout = 4000)
    public void testSetDataAreaToNull() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = new PlotRenderingInfo(chartInfo);
        plotInfo.setDataArea(null);
        assertNull(plotInfo.getDataArea());
    }

    @Test(timeout = 4000)
    public void testSetDataAreaWithZeroHeightRectangle() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
        Rectangle zeroHeightRectangle = new Rectangle(0, 37, 4281, 0);
        plotInfo.setDataArea(zeroHeightRectangle);
        assertEquals(0.0, plotInfo.getDataArea().getHeight(), 0.01);
    }

    @Test(timeout = 4000)
    public void testSetDataAreaWithNegativeCoordinatesRectangle() {
        PlotRenderingInfo plotInfo = new PlotRenderingInfo((ChartRenderingInfo) null);
        Rectangle2D.Double negativeRectangle = new Rectangle2D.Double(-452.97, -452.97, -452.97, -452.97);
        plotInfo.setDataArea(negativeRectangle);
        assertEquals(-452.97, plotInfo.getDataArea().getHeight(), 0.01);
    }

    @Test(timeout = 4000)
    public void testSetDataAreaWithLineBounds() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = new PlotRenderingInfo(chartInfo);
        Line2D.Float line = new Line2D.Float(0.0F, -1725.4F, 0.0F, 1.0F);
        Rectangle lineBounds = line.getBounds();
        plotInfo.setDataArea(lineBounds);
        assertEquals(-1726.0, plotInfo.getDataArea().getY(), 0.01);
    }

    @Test(timeout = 4000)
    public void testSetDataAreaWithRectangle() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        Rectangle rectangle = new Rectangle(1173, 1173);
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
        plotInfo.setDataArea(rectangle);
        assertEquals(0, plotInfo.getDataArea().getY());
    }

    @Test(timeout = 4000)
    public void testClonePlotRenderingInfo() throws CloneNotSupportedException {
        StandardEntityCollection entityCollection = new StandardEntityCollection();
        ChartRenderingInfo chartInfo = new ChartRenderingInfo(entityCollection);
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
        Object clonedPlotInfo = plotInfo.clone();
        assertNotSame(clonedPlotInfo, plotInfo);
    }

    @Test(timeout = 4000)
    public void testEqualsAndHashCode() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo1 = chartInfo.getPlotInfo();
        PlotRenderingInfo plotInfo2 = new PlotRenderingInfo(chartInfo);
        assertTrue(plotInfo1.equals(plotInfo2));
        assertEquals(plotInfo1.hashCode(), plotInfo2.hashCode());

        plotInfo2.addSubplotInfo(plotInfo1);
        assertFalse(plotInfo1.equals(plotInfo2));
    }

    @Test(timeout = 4000)
    public void testGetSubplotIndexWithNullSource() {
        PlotRenderingInfo plotInfo = new PlotRenderingInfo((ChartRenderingInfo) null);
        try {
            plotInfo.getSubplotIndex((Point2D) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Null 'source' argument.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetSubplotIndexWithPoint() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = new PlotRenderingInfo(chartInfo);
        Point point = new Point();
        plotInfo.addSubplotInfo(plotInfo);
        int subplotIndex = plotInfo.getSubplotIndex(point);
        assertEquals(-1, subplotIndex);
    }

    @Test(timeout = 4000)
    public void testGetSubplotInfoWithInvalidIndex() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
        try {
            plotInfo.getSubplotInfo(1);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }
}