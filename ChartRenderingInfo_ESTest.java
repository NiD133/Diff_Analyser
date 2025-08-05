package org.jfree.chart;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Locale;
import java.util.SimpleTimeZone;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.CombinedRangeCategoryPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.data.xy.XYDatasetTableModel;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, 
                     useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class ChartRenderingInfo_ESTest extends ChartRenderingInfo_ESTest_scaffolding {

    // Tests for basic setup and configuration
    //--------------------------------------------------------------------
    
    @Test(timeout = 4000)
    public void newInstance_ShouldHaveZeroHeightChartArea() {
        ChartRenderingInfo info = new ChartRenderingInfo();
        Rectangle2D.Double chartArea = (Rectangle2D.Double) info.getChartArea();
        assertEquals(0.0, chartArea.height, 0.01);
    }

    @Test(timeout = 4000)
    public void constructorWithEntityCollection_ShouldInitializeEntityCollection() {
        StandardEntityCollection entityCollection = new StandardEntityCollection();
        ChartRenderingInfo info = new ChartRenderingInfo(entityCollection);
        EntityCollection result = info.getEntityCollection();
        assertEquals(0, result.getEntityCount());
    }

    // Tests for chart area operations
    //--------------------------------------------------------------------
    
    @Test(timeout = 4000)
    public void setChartArea_ShouldUpdateStoredValue() {
        ChartRenderingInfo info = new ChartRenderingInfo();
        Rectangle area = new Rectangle(1, 294, -343, 7);
        info.setChartArea(area);
        
        Rectangle2D.Double result = (Rectangle2D.Double) info.getChartArea();
        assertEquals(294.0, result.y, 0.01);
    }

    @Test(timeout = 4000)
    public void setChartAreaWithNegativeDimensions_ShouldCalculateCenterCorrectly() {
        ChartRenderingInfo info = new ChartRenderingInfo();
        Rectangle area = new Rectangle(-521, -782, 0, -2457);
        info.setChartArea(area);
        
        Rectangle2D result = info.getChartArea();
        assertEquals(-2010.5, result.getCenterY(), 0.01);
    }

    @Test(timeout = 4000)
    public void setChartAreaWithDiagonalFrame_ShouldUpdateBoundsCorrectly() {
        ChartRenderingInfo info = new ChartRenderingInfo();
        Rectangle2D.Double area = new Rectangle2D.Double();
        area.setFrameFromDiagonal(2626.9361169318868, 2868.898929, 0.0, 2868.898929);
        info.setChartArea(area);
        
        Rectangle2D result = info.getChartArea();
        assertEquals(0.0, result.getMinX(), 0.01);
    }

    @Test(timeout = 4000)
    public void setChartAreaWithNegativeOrigin_ShouldCalculateMaxXCorrectly() {
        ChartRenderingInfo info = new ChartRenderingInfo();
        Rectangle area = new Rectangle(-1, -1, 1, 7);
        info.setChartArea(area);
        
        Rectangle2D result = info.getChartArea();
        assertEquals(0.0, result.getMaxX(), 0.01);
    }

    @Test(timeout = 4000)
    public void setNullChartArea_ShouldThrowException() {
        ChartRenderingInfo info = new ChartRenderingInfo();
        try {
            info.setChartArea((Rectangle2D) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    // Tests for entity collection operations
    //--------------------------------------------------------------------
    
    @Test(timeout = 4000)
    public void setEntityCollectionToNull_ShouldReturnNull() {
        ChartRenderingInfo info = new ChartRenderingInfo();
        info.setEntityCollection(null);
        assertNull(info.getEntityCollection());
    }

    @Test(timeout = 4000)
    public void renderingChart_ShouldPopulateEntityCollection() {
        ChartRenderingInfo info = new ChartRenderingInfo();
        SimpleTimeZone timeZone = new SimpleTimeZone(1, "Test TimeZone");
        DateAxis dateAxis = new DateAxis("Test Axis", timeZone, Locale.JAPANESE);
        CombinedRangeCategoryPlot plot = new CombinedRangeCategoryPlot(dateAxis);
        JFreeChart chart = new JFreeChart(plot);
        
        chart.createBufferedImage(10, 10, 1749.95538, 2.0, info);
        
        EntityCollection entities = info.getEntityCollection();
        assertEquals(3, entities.getEntityCount());
    }

    // Tests for plot info operations
    //--------------------------------------------------------------------
    
    @Test(timeout = 4000)
    public void addSubplotInfo_ShouldIncreaseSubplotCount() {
        StandardEntityCollection entityCollection = new StandardEntityCollection();
        ChartRenderingInfo info = new ChartRenderingInfo(entityCollection);
        PlotRenderingInfo plotInfo = info.getPlotInfo();
        
        plotInfo.addSubplotInfo(plotInfo);
        PlotRenderingInfo result = info.getPlotInfo();
        
        assertEquals(1, result.getSubplotCount());
    }

    // Tests for equals and hashCode
    //--------------------------------------------------------------------
    
    @Test(timeout = 4000)
    public void equals_WhenComparingSameInstance_ShouldReturnTrue() {
        ChartRenderingInfo info = new ChartRenderingInfo();
        assertTrue(info.equals(info));
    }

    @Test(timeout = 4000)
    public void equals_WhenComparingToClone_ShouldReturnTrue() throws Exception {
        ChartRenderingInfo info = new ChartRenderingInfo();
        Object clone = info.clone();
        assertNotSame(clone, info);
        assertTrue(info.equals(clone));
    }

    @Test(timeout = 4000)
    public void equals_WhenComparingDifferentClass_ShouldReturnFalse() {
        ChartRenderingInfo info = new ChartRenderingInfo();
        XYDatasetTableModel other = new XYDatasetTableModel();
        assertFalse(info.equals(other));
    }

    @Test(timeout = 4000)
    public void equals_AfterSettingEntityCollectionToNull_ShouldDetectDifference() {
        ChartRenderingInfo info1 = new ChartRenderingInfo();
        ChartRenderingInfo info2 = new ChartRenderingInfo();
        info2.setEntityCollection(null);
        
        assertFalse(info1.equals(info2));
    }

    @Test(timeout = 4000)
    public void equals_AfterRenderingChart_ShouldDetectDifference() {
        ChartRenderingInfo info1 = new ChartRenderingInfo();
        ChartRenderingInfo info2 = new ChartRenderingInfo();
        
        SimpleTimeZone timeZone = new SimpleTimeZone(1, "Test TimeZone");
        DateAxis dateAxis = new DateAxis("Test Axis", timeZone, Locale.JAPANESE);
        CombinedRangeCategoryPlot plot = new CombinedRangeCategoryPlot(dateAxis);
        JFreeChart chart = new JFreeChart(plot);
        chart.createBufferedImage(10, 10, 1749.95538, 2.0, info1);
        
        assertFalse(info1.equals(info2));
    }

    @Test(timeout = 4000)
    public void equals_AfterAddingSubplot_ShouldDetectDifference() {
        StandardEntityCollection entityCollection = new StandardEntityCollection();
        ChartRenderingInfo info1 = new ChartRenderingInfo(entityCollection);
        PlotRenderingInfo plotInfo = info1.getPlotInfo();
        plotInfo.addSubplotInfo(plotInfo);
        
        ChartRenderingInfo info2 = new ChartRenderingInfo();
        assertFalse(info1.equals(info2));
    }

    @Test(timeout = 4000)
    public void hashCode_AfterSettingChartArea_ShouldNotThrowException() {
        ChartRenderingInfo info = new ChartRenderingInfo();
        Rectangle area = new Rectangle(-1, -1, 1, 7);
        info.setChartArea(area);
        info.hashCode(); // Should not throw exception
    }

    @Test(timeout = 4000)
    public void hashCode_WithCircularSubplotReference_ShouldThrowStackOverflow() {
        StandardEntityCollection entityCollection = new StandardEntityCollection();
        ChartRenderingInfo info = new ChartRenderingInfo(entityCollection);
        PlotRenderingInfo plotInfo = info.getPlotInfo();
        plotInfo.addSubplotInfo(plotInfo);
        
        try {
            info.hashCode();
            fail("Expected StackOverflowError");
        } catch (StackOverflowError e) {
            // Expected behavior
        }
    }

    // Tests for clear operations
    //--------------------------------------------------------------------
    
    @Test(timeout = 4000)
    public void clear_ShouldResetState() {
        ChartRenderingInfo info = new ChartRenderingInfo();
        info.clear(); // Should not throw exception
    }

    @Test(timeout = 4000)
    public void clear_WithNullEntityCollection_ShouldNotThrowException() {
        ChartRenderingInfo info = new ChartRenderingInfo(null);
        info.clear(); // Should not throw exception
    }

    // Tests for clone operations
    //--------------------------------------------------------------------
    
    @Test(timeout = 4000)
    public void clone_WithNullEntityCollection_ShouldNotThrowException() {
        ChartRenderingInfo info = new ChartRenderingInfo();
        info.setEntityCollection(null);
        Object clone = info.clone();
        assertNotSame(clone, info);
    }
}