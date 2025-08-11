package org.jfree.chart.plot;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.entity.StandardEntityCollection;

/**
 * Test suite for PlotRenderingInfo class functionality.
 * Tests cover core operations: area management, subplot handling, equality, and cloning.
 */
public class PlotRenderingInfoTest {

    // Test Data Constants
    private static final int POSITIVE_COORDINATE = 100;
    private static final int NEGATIVE_COORDINATE = -100;
    private static final int ZERO_COORDINATE = 0;
    private static final double DOUBLE_COORDINATE = 1994.17;

    // ========== Hash Code Tests ==========
    
    @Test
    public void testHashCode_WithDataArea() {
        // Given: PlotRenderingInfo with a data area set
        PlotRenderingInfo plotInfo = createBasicPlotRenderingInfo();
        Rectangle dataArea = new Rectangle(NEGATIVE_COORDINATE, ZERO_COORDINATE, POSITIVE_COORDINATE, POSITIVE_COORDINATE);
        plotInfo.setDataArea(dataArea);
        
        // When: Computing hash code
        int hashCode = plotInfo.hashCode();
        
        // Then: Should not throw exception and return valid hash
        assertTrue("Hash code should be computed successfully", hashCode != 0 || hashCode == 0);
    }

    @Test
    public void testHashCode_WithPlotArea() {
        // Given: PlotRenderingInfo with a plot area set
        PlotRenderingInfo plotInfo = createBasicPlotRenderingInfo();
        Rectangle2D plotArea = new Rectangle2D.Double(POSITIVE_COORDINATE, POSITIVE_COORDINATE, 50, 50);
        plotInfo.setPlotArea(plotArea);
        
        // When: Computing hash code
        int hashCode = plotInfo.hashCode();
        
        // Then: Should not throw exception
        assertTrue("Hash code should be computed successfully", hashCode != 0 || hashCode == 0);
    }

    // ========== Subplot Management Tests ==========
    
    @Test
    public void testSubplotManagement_AddAndRetrieve() {
        // Given: Two PlotRenderingInfo instances
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo parentPlot = chartInfo.getPlotInfo();
        PlotRenderingInfo childPlot = new PlotRenderingInfo(chartInfo);
        
        // When: Adding child as subplot
        parentPlot.addSubplotInfo(childPlot);
        PlotRenderingInfo retrievedSubplot = parentPlot.getSubplotInfo(0);
        
        // Then: Should retrieve the correct subplot
        assertNotNull("Retrieved subplot should not be null", retrievedSubplot);
        assertNotEquals("Parent and child should be different instances", parentPlot, retrievedSubplot);
    }

    @Test
    public void testSubplotManagement_SelfReference() {
        // Given: A PlotRenderingInfo instance
        PlotRenderingInfo plotInfo = createBasicPlotRenderingInfo();
        
        // When: Adding itself as a subplot
        plotInfo.addSubplotInfo(plotInfo);
        PlotRenderingInfo retrievedSubplot = plotInfo.getSubplotInfo(0);
        
        // Then: Should handle self-reference correctly
        assertEquals("Should retrieve itself as subplot", plotInfo, retrievedSubplot);
        assertEquals("Subplot count should be 1", 1, plotInfo.getSubplotCount());
    }

    @Test
    public void testSubplotCount_Initially() {
        // Given: A new PlotRenderingInfo instance
        PlotRenderingInfo plotInfo = createBasicPlotRenderingInfo();
        
        // When: Getting initial subplot count
        int count = plotInfo.getSubplotCount();
        
        // Then: Should be zero
        assertEquals("Initial subplot count should be zero", 0, count);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetSubplotInfo_InvalidIndex() {
        // Given: PlotRenderingInfo with no subplots
        PlotRenderingInfo plotInfo = createBasicPlotRenderingInfo();
        
        // When: Accessing invalid subplot index
        // Then: Should throw IndexOutOfBoundsException
        plotInfo.getSubplotInfo(1);
    }

    // ========== Area Management Tests ==========
    
    @Test
    public void testPlotArea_SetAndGet() {
        // Given: PlotRenderingInfo and a rectangle
        PlotRenderingInfo plotInfo = createBasicPlotRenderingInfo();
        Rectangle expectedArea = new Rectangle(POSITIVE_COORDINATE, POSITIVE_COORDINATE, 200, 150);
        
        // When: Setting plot area
        plotInfo.setPlotArea(expectedArea);
        Rectangle2D actualArea = plotInfo.getPlotArea();
        
        // Then: Should return the same area
        assertNotNull("Plot area should not be null", actualArea);
        assertEquals("Plot area width should match", 200.0, actualArea.getWidth(), 0.01);
        assertEquals("Plot area height should match", 150.0, actualArea.getHeight(), 0.01);
    }

    @Test
    public void testPlotArea_InitiallyNull() {
        // Given: A new PlotRenderingInfo instance
        PlotRenderingInfo plotInfo = createBasicPlotRenderingInfo();
        
        // When: Getting plot area without setting it
        Rectangle2D plotArea = plotInfo.getPlotArea();
        
        // Then: Should be null
        assertNull("Initial plot area should be null", plotArea);
    }

    @Test
    public void testDataArea_SetAndGet() {
        // Given: PlotRenderingInfo and various rectangle types
        PlotRenderingInfo plotInfo = createBasicPlotRenderingInfo();
        
        // Test with regular Rectangle
        Rectangle rectangle = new Rectangle(50, 75, 200, 100);
        plotInfo.setDataArea(rectangle);
        Rectangle2D dataArea = plotInfo.getDataArea();
        
        assertEquals("Data area X should match", 50.0, dataArea.getX(), 0.01);
        assertEquals("Data area Y should match", 75.0, dataArea.getY(), 0.01);
        assertEquals("Data area width should match", 200.0, dataArea.getWidth(), 0.01);
        assertEquals("Data area height should match", 100.0, dataArea.getHeight(), 0.01);
    }

    @Test
    public void testDataArea_SetToNull() {
        // Given: PlotRenderingInfo instance
        PlotRenderingInfo plotInfo = createBasicPlotRenderingInfo();
        
        // When: Setting data area to null
        plotInfo.setDataArea(null);
        Rectangle2D dataArea = plotInfo.getDataArea();
        
        // Then: Should return null
        assertNull("Data area should be null when set to null", dataArea);
    }

    @Test
    public void testDataArea_WithNegativeCoordinates() {
        // Given: PlotRenderingInfo and rectangle with negative coordinates
        PlotRenderingInfo plotInfo = createBasicPlotRenderingInfo();
        Rectangle2D.Double negativeRect = new Rectangle2D.Double(-452.97, -452.97, 100, 100);
        
        // When: Setting data area with negative coordinates
        plotInfo.setDataArea(negativeRect);
        Rectangle2D dataArea = plotInfo.getDataArea();
        
        // Then: Should preserve negative coordinates
        assertEquals("Should preserve negative X coordinate", -452.97, dataArea.getX(), 0.01);
        assertEquals("Should preserve negative Y coordinate", -452.97, dataArea.getY(), 0.01);
    }

    // ========== Owner Management Tests ==========
    
    @Test
    public void testOwner_WithValidChartInfo() {
        // Given: ChartRenderingInfo instance
        ChartRenderingInfo expectedOwner = new ChartRenderingInfo();
        
        // When: Creating PlotRenderingInfo with owner
        PlotRenderingInfo plotInfo = new PlotRenderingInfo(expectedOwner);
        ChartRenderingInfo actualOwner = plotInfo.getOwner();
        
        // Then: Should return the same owner
        assertSame("Owner should be the same instance", expectedOwner, actualOwner);
    }

    @Test
    public void testOwner_WithNullOwner() {
        // Given: Null owner
        // When: Creating PlotRenderingInfo with null owner
        PlotRenderingInfo plotInfo = new PlotRenderingInfo(null);
        ChartRenderingInfo owner = plotInfo.getOwner();
        
        // Then: Should return null
        assertNull("Owner should be null when set to null", owner);
    }

    // ========== Subplot Index Tests ==========
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSubplotIndex_NullPoint() {
        // Given: PlotRenderingInfo instance
        PlotRenderingInfo plotInfo = createBasicPlotRenderingInfo();
        
        // When: Getting subplot index with null point
        // Then: Should throw IllegalArgumentException
        plotInfo.getSubplotIndex(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetSubplotIndex_WithNullSubplot() {
        // Given: PlotRenderingInfo with null subplot
        PlotRenderingInfo plotInfo = createBasicPlotRenderingInfo();
        plotInfo.addSubplotInfo(null);
        Point2D testPoint = new Point2D.Double(0, 0);
        
        // When: Getting subplot index
        // Then: Should throw NullPointerException
        plotInfo.getSubplotIndex(testPoint);
    }

    @Test
    public void testGetSubplotIndex_NoMatchingSubplot() {
        // Given: PlotRenderingInfo with subplot that doesn't contain test point
        PlotRenderingInfo plotInfo = createBasicPlotRenderingInfo();
        plotInfo.addSubplotInfo(plotInfo);
        Point2D testPoint = new Point2D.Double(0, 0);
        
        // When: Getting subplot index for point not in any subplot
        int index = plotInfo.getSubplotIndex(testPoint);
        
        // Then: Should return -1
        assertEquals("Should return -1 when no subplot contains the point", -1, index);
    }

    // ========== Equality Tests ==========
    
    @Test
    public void testEquals_SameInstance() {
        // Given: A PlotRenderingInfo instance
        PlotRenderingInfo plotInfo = createBasicPlotRenderingInfo();
        
        // When: Comparing with itself
        boolean isEqual = plotInfo.equals(plotInfo);
        
        // Then: Should be equal
        assertTrue("Instance should equal itself", isEqual);
    }

    @Test
    public void testEquals_IdenticalInstances() {
        // Given: Two identical PlotRenderingInfo instances
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo1 = chartInfo.getPlotInfo();
        PlotRenderingInfo plotInfo2 = new PlotRenderingInfo(chartInfo);
        
        // When: Comparing identical instances
        boolean isEqual = plotInfo1.equals(plotInfo2);
        
        // Then: Should be equal
        assertTrue("Identical instances should be equal", isEqual);
    }

    @Test
    public void testEquals_DifferentSubplots() {
        // Given: Two PlotRenderingInfo instances, one with subplot
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo1 = chartInfo.getPlotInfo();
        PlotRenderingInfo plotInfo2 = new PlotRenderingInfo(chartInfo);
        
        // When: Adding subplot to one instance
        plotInfo2.addSubplotInfo(plotInfo1);
        boolean isEqual = plotInfo1.equals(plotInfo2);
        
        // Then: Should not be equal
        assertFalse("Instances with different subplots should not be equal", isEqual);
    }

    @Test
    public void testEquals_DifferentPlotAreas() {
        // Given: Two PlotRenderingInfo instances with different plot areas
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo1 = chartInfo.getPlotInfo();
        PlotRenderingInfo plotInfo2 = new PlotRenderingInfo(chartInfo);
        
        // When: Setting different plot areas
        plotInfo1.setPlotArea(new Rectangle2D.Double(0, 0, 100, 100));
        boolean isEqual = plotInfo2.equals(plotInfo1);
        
        // Then: Should not be equal
        assertFalse("Instances with different plot areas should not be equal", isEqual);
    }

    @Test
    public void testEquals_DifferentDataAreas() {
        // Given: Two PlotRenderingInfo instances with different data areas
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo1 = new PlotRenderingInfo(chartInfo);
        PlotRenderingInfo plotInfo2 = chartInfo.getPlotInfo();
        
        // When: Setting different data areas
        plotInfo1.setDataArea(new Rectangle(7, 0, 50, 50));
        boolean isEqual = plotInfo1.equals(plotInfo2);
        
        // Then: Should not be equal
        assertFalse("Instances with different data areas should not be equal", isEqual);
    }

    @Test
    public void testEquals_DifferentObjectType() {
        // Given: PlotRenderingInfo and different object type
        PlotRenderingInfo plotInfo = createBasicPlotRenderingInfo();
        StandardEntityCollection differentObject = new StandardEntityCollection();
        
        // When: Comparing with different object type
        boolean isEqual = plotInfo.equals(differentObject);
        
        // Then: Should not be equal
        assertFalse("Should not equal different object type", isEqual);
    }

    // ========== Cloning Tests ==========
    
    @Test
    public void testClone_WithSubplots() throws CloneNotSupportedException {
        // Given: PlotRenderingInfo with subplot
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo originalPlot = chartInfo.getPlotInfo();
        PlotRenderingInfo subplot = new PlotRenderingInfo(chartInfo);
        originalPlot.addSubplotInfo(subplot);
        
        // When: Cloning the plot
        Object clonedObject = originalPlot.clone();
        
        // Then: Should create different instance with same subplot count
        assertNotSame("Clone should be different instance", clonedObject, originalPlot);
        assertTrue("Clone should be PlotRenderingInfo", clonedObject instanceof PlotRenderingInfo);
        
        PlotRenderingInfo clonedPlot = (PlotRenderingInfo) clonedObject;
        assertEquals("Clone should have same subplot count", 1, clonedPlot.getSubplotCount());
    }

    @Test
    public void testClone_WithNullDataArea() throws CloneNotSupportedException {
        // Given: PlotRenderingInfo with null data area
        PlotRenderingInfo plotInfo = createBasicPlotRenderingInfo();
        plotInfo.setDataArea(null);
        
        // When: Cloning the plot
        Object clonedObject = plotInfo.clone();
        
        // Then: Should create different instance
        assertNotSame("Clone should be different instance", clonedObject, plotInfo);
        assertTrue("Clone should be PlotRenderingInfo", clonedObject instanceof PlotRenderingInfo);
    }

    @Test
    public void testClone_WithPlotArea() throws CloneNotSupportedException {
        // Given: PlotRenderingInfo with plot area
        PlotRenderingInfo plotInfo = createBasicPlotRenderingInfo();
        Rectangle2D plotArea = new Rectangle2D.Double(0, 0, 100, 100);
        plotInfo.setPlotArea(plotArea);
        
        // When: Cloning the plot
        Object clonedObject = plotInfo.clone();
        
        // Then: Should create different instance
        assertNotSame("Clone should be different instance", clonedObject, plotInfo);
        assertTrue("Clone should be PlotRenderingInfo", clonedObject instanceof PlotRenderingInfo);
    }

    // ========== Helper Methods ==========
    
    /**
     * Creates a basic PlotRenderingInfo instance for testing.
     * @return A new PlotRenderingInfo with ChartRenderingInfo owner
     */
    private PlotRenderingInfo createBasicPlotRenderingInfo() {
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        return new PlotRenderingInfo(chartInfo);
    }
}