package org.jfree.chart;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Locale;
import java.util.SimpleTimeZone;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.CombinedRangeCategoryPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.data.xy.XYDatasetTableModel;

/**
 * Test suite for ChartRenderingInfo class functionality.
 * Tests cover construction, chart area management, entity collection handling,
 * plot rendering info, and object contract methods (equals, hashCode, clone).
 */
public class ChartRenderingInfoTest {

    // ========== Constructor Tests ==========
    
    @Test
    public void testDefaultConstructor_CreatesStandardEntityCollection() {
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo();
        
        EntityCollection entities = renderingInfo.getEntityCollection();
        assertNotNull("Default constructor should create entity collection", entities);
        assertEquals("New entity collection should be empty", 0, entities.getEntityCount());
    }

    @Test
    public void testConstructorWithEntityCollection_StoresProvidedCollection() {
        StandardEntityCollection customCollection = new StandardEntityCollection();
        
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo(customCollection);
        
        EntityCollection storedCollection = renderingInfo.getEntityCollection();
        assertSame("Constructor should store the provided entity collection", 
                   customCollection, storedCollection);
    }

    @Test
    public void testConstructorWithNullEntityCollection_AllowsNullValue() {
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo(null);
        
        EntityCollection entities = renderingInfo.getEntityCollection();
        assertNull("Constructor should accept null entity collection", entities);
    }

    // ========== Chart Area Tests ==========
    
    @Test
    public void testGetChartArea_ReturnsInitialEmptyRectangle() {
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo();
        
        Rectangle2D chartArea = renderingInfo.getChartArea();
        
        assertNotNull("Chart area should not be null", chartArea);
        assertEquals("Initial chart area should have zero width", 0.0, chartArea.getWidth(), 0.01);
        assertEquals("Initial chart area should have zero height", 0.0, chartArea.getHeight(), 0.01);
    }

    @Test
    public void testSetChartArea_WithRectangle_UpdatesChartArea() {
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo();
        Rectangle testRectangle = new Rectangle(10, 20, 100, 200);
        
        renderingInfo.setChartArea(testRectangle);
        Rectangle2D storedArea = renderingInfo.getChartArea();
        
        assertEquals("Chart area X should match", 10.0, storedArea.getX(), 0.01);
        assertEquals("Chart area Y should match", 20.0, storedArea.getY(), 0.01);
        assertEquals("Chart area width should match", 100.0, storedArea.getWidth(), 0.01);
        assertEquals("Chart area height should match", 200.0, storedArea.getHeight(), 0.01);
    }

    @Test
    public void testSetChartArea_WithRectangle2D_UpdatesChartArea() {
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo();
        Rectangle2D.Double testRectangle = new Rectangle2D.Double(5.5, 10.5, 50.0, 75.0);
        
        renderingInfo.setChartArea(testRectangle);
        Rectangle2D storedArea = renderingInfo.getChartArea();
        
        assertEquals("Chart area should match set rectangle", testRectangle.getBounds2D(), storedArea);
    }

    @Test(expected = NullPointerException.class)
    public void testSetChartArea_WithNull_ThrowsException() {
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo();
        
        renderingInfo.setChartArea(null);
    }

    // ========== Entity Collection Tests ==========
    
    @Test
    public void testSetEntityCollection_WithNull_ClearsCollection() {
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo();
        
        renderingInfo.setEntityCollection(null);
        EntityCollection entities = renderingInfo.getEntityCollection();
        
        assertNull("Entity collection should be null after setting to null", entities);
    }

    @Test
    public void testEntityCollection_PopulatedDuringChartRendering() {
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo();
        JFreeChart chart = createTestChart();
        
        // Render chart to populate entity collection
        chart.createBufferedImage(100, 100, renderingInfo);
        
        EntityCollection entities = renderingInfo.getEntityCollection();
        assertTrue("Entity collection should be populated after rendering", 
                   entities.getEntityCount() > 0);
    }

    // ========== Plot Rendering Info Tests ==========
    
    @Test
    public void testGetPlotInfo_ReturnsNonNullPlotInfo() {
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo();
        
        PlotRenderingInfo plotInfo = renderingInfo.getPlotInfo();
        
        assertNotNull("Plot info should not be null", plotInfo);
    }

    @Test
    public void testPlotInfo_CanAddSubplots() {
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = renderingInfo.getPlotInfo();
        PlotRenderingInfo subplotInfo = new PlotRenderingInfo(renderingInfo);
        
        plotInfo.addSubplotInfo(subplotInfo);
        
        assertEquals("Should have one subplot", 1, plotInfo.getSubplotCount());
    }

    // ========== Clear Method Tests ==========
    
    @Test
    public void testClear_WithEntityCollection_ClearsSuccessfully() {
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo();
        
        renderingInfo.clear(); // Should not throw exception
        
        // Verify state is still valid
        assertNotNull("Chart area should still exist after clear", renderingInfo.getChartArea());
        assertNotNull("Plot info should still exist after clear", renderingInfo.getPlotInfo());
    }

    @Test
    public void testClear_WithNullEntityCollection_HandlesGracefully() {
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo(null);
        
        renderingInfo.clear(); // Should not throw exception
        
        assertNull("Entity collection should remain null", renderingInfo.getEntityCollection());
    }

    // ========== Equals Method Tests ==========
    
    @Test
    public void testEquals_WithSameInstance_ReturnsTrue() {
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo();
        
        boolean isEqual = renderingInfo.equals(renderingInfo);
        
        assertTrue("Object should equal itself", isEqual);
    }

    @Test
    public void testEquals_WithEquivalentInstance_ReturnsTrue() {
        ChartRenderingInfo renderingInfo1 = new ChartRenderingInfo();
        ChartRenderingInfo renderingInfo2 = new ChartRenderingInfo();
        
        boolean isEqual = renderingInfo1.equals(renderingInfo2);
        
        assertTrue("Equivalent instances should be equal", isEqual);
    }

    @Test
    public void testEquals_WithDifferentEntityCollection_ReturnsFalse() {
        ChartRenderingInfo renderingInfo1 = new ChartRenderingInfo();
        ChartRenderingInfo renderingInfo2 = new ChartRenderingInfo();
        
        renderingInfo2.setEntityCollection(null);
        boolean isEqual = renderingInfo1.equals(renderingInfo2);
        
        assertFalse("Instances with different entity collections should not be equal", isEqual);
    }

    @Test
    public void testEquals_WithDifferentClass_ReturnsFalse() {
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo();
        XYDatasetTableModel differentObject = new XYDatasetTableModel();
        
        boolean isEqual = renderingInfo.equals(differentObject);
        
        assertFalse("Should not equal object of different class", isEqual);
    }

    @Test
    public void testEquals_WithDifferentPlotInfo_ReturnsFalse() {
        ChartRenderingInfo renderingInfo1 = new ChartRenderingInfo();
        ChartRenderingInfo renderingInfo2 = new ChartRenderingInfo();
        
        // Modify plot info in one instance
        PlotRenderingInfo plotInfo = renderingInfo1.getPlotInfo();
        plotInfo.addSubplotInfo(new PlotRenderingInfo(renderingInfo1));
        
        boolean isEqual = renderingInfo1.equals(renderingInfo2);
        
        assertFalse("Instances with different plot info should not be equal", isEqual);
    }

    // ========== HashCode Tests ==========
    
    @Test
    public void testHashCode_WithSimpleInstance_ReturnsValidHashCode() {
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo();
        Rectangle chartArea = new Rectangle(10, 10, 100, 100);
        renderingInfo.setChartArea(chartArea);
        
        int hashCode = renderingInfo.hashCode();
        
        // Just verify it doesn't throw an exception and returns some value
        assertNotEquals("Hash code should not be zero", 0, hashCode);
    }

    @Test(expected = StackOverflowError.class)
    public void testHashCode_WithCircularReference_ThrowsStackOverflowError() {
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = renderingInfo.getPlotInfo();
        
        // Create circular reference
        plotInfo.addSubplotInfo(plotInfo);
        
        renderingInfo.hashCode(); // Should throw StackOverflowError
    }

    // ========== Clone Tests ==========
    
    @Test
    public void testClone_CreatesIndependentCopy() throws CloneNotSupportedException {
        ChartRenderingInfo original = new ChartRenderingInfo();
        
        Object cloned = original.clone();
        
        assertNotSame("Clone should be different instance", original, cloned);
        assertEquals("Clone should be equal to original", original, cloned);
    }

    @Test
    public void testClone_WithNullEntityCollection_ClonesSuccessfully() throws CloneNotSupportedException {
        ChartRenderingInfo original = new ChartRenderingInfo();
        original.setEntityCollection(null);
        
        Object cloned = original.clone();
        
        assertNotSame("Clone should be different instance", original, cloned);
    }

    // ========== Helper Methods ==========
    
    /**
     * Creates a simple test chart for testing purposes.
     */
    private JFreeChart createTestChart() {
        SimpleTimeZone timeZone = new SimpleTimeZone(1, "Test Zone");
        DateAxis dateAxis = new DateAxis("Test Axis", timeZone, Locale.ENGLISH);
        CombinedRangeCategoryPlot plot = new CombinedRangeCategoryPlot(dateAxis);
        return new JFreeChart(plot);
    }
}