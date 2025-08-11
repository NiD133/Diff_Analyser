package org.jfree.chart;

import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.CombinedRangeCategoryPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.junit.Test;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;
import java.util.SimpleTimeZone;

import static org.junit.Assert.*;

/**
 * A suite of tests for the {@link ChartRenderingInfo} class.
 * The tests cover constructors, property accessors, state manipulation,
 * and core object methods like equals, hashCode, clone, and serialization.
 */
public class ChartRenderingInfoTest {

    //region Constructor and Initial State Tests

    @Test
    public void testDefaultConstructor_InitializesWithDefaults() {
        // Act
        ChartRenderingInfo info = new ChartRenderingInfo();

        // Assert
        assertNotNull("PlotInfo should not be null on default construction.", info.getPlotInfo());
        assertNotNull("EntityCollection should not be null on default construction.", info.getEntityCollection());
        assertTrue("EntityCollection should be a StandardEntityCollection by default.",
                info.getEntityCollection() instanceof StandardEntityCollection);
        assertEquals("Default chart area should be an empty rectangle.", new Rectangle2D.Double(), info.getChartArea());
    }

    @Test
    public void testConstructor_WithEntityCollection_StoresReference() {
        // Arrange
        EntityCollection entities = new StandardEntityCollection();

        // Act
        ChartRenderingInfo info = new ChartRenderingInfo(entities);

        // Assert
        assertSame("The provided entity collection should be stored.", entities, info.getEntityCollection());
    }

    @Test
    public void testConstructor_WithNullEntityCollection_StoresNull() {
        // Act
        ChartRenderingInfo info = new ChartRenderingInfo(null);

        // Assert
        assertNull("A null entity collection should be stored as null.", info.getEntityCollection());
    }

    //endregion

    //region Property Accessor Tests (Getters/Setters)

    @Test
    public void testSetAndGetChartArea() {
        // Arrange
        ChartRenderingInfo info = new ChartRenderingInfo();
        Rectangle2D area = new Rectangle2D.Double(10.0, 20.0, 30.0, 40.0);

        // Act
        info.setChartArea(area);
        Rectangle2D retrievedArea = info.getChartArea();

        // Assert
        assertEquals("The retrieved chart area should be equal to the one set.", area, retrievedArea);
        assertNotSame("The retrieved chart area should be a defensive copy.", area, retrievedArea);
    }

    @Test(expected = NullPointerException.class)
    public void testSetChartArea_WithNull_ThrowsNullPointerException() {
        // Arrange
        ChartRenderingInfo info = new ChartRenderingInfo();

        // Act
        info.setChartArea(null); // Should throw
    }

    @Test
    public void testSetAndGetEntityCollection() {
        // Arrange
        ChartRenderingInfo info = new ChartRenderingInfo();
        EntityCollection entities = new StandardEntityCollection();

        // Act
        info.setEntityCollection(entities);

        // Assert
        assertSame("The retrieved entity collection should be the same instance as the one set.",
                entities, info.getEntityCollection());
    }

    @Test
    public void testGetPlotInfo_ReturnsLiveReference() {
        // Arrange
        ChartRenderingInfo info = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = info.getPlotInfo();
        Rectangle2D dataArea = new Rectangle(1, 2, 3, 4);

        // Act
        plotInfo.setDataArea(dataArea);

        // Assert
        assertEquals("Modifying the retrieved PlotRenderingInfo should affect the internal state.",
                dataArea, info.getPlotInfo().getDataArea());
    }

    //endregion

    //region State Manipulation Tests

    @Test
    public void testClear_ResetsChartAreaAndPlotInfo_ButNotEntities() {
        // Arrange
        ChartRenderingInfo info = new ChartRenderingInfo();
        info.setChartArea(new Rectangle(1, 2, 3, 4));
        PlotRenderingInfo oldPlotInfo = info.getPlotInfo();
        EntityCollection entities = info.getEntityCollection();

        // Act
        info.clear();

        // Assert
        assertEquals("Chart area should be reset to an empty rectangle.", new Rectangle2D.Double(), info.getChartArea());
        assertNotSame("PlotInfo should be a new instance after clear.", oldPlotInfo, info.getPlotInfo());
        assertSame("EntityCollection should not be changed by clear().", entities, info.getEntityCollection());
    }

    //endregion

    //region Core Object Method Tests (equals, hashCode, clone)

    @Test
    public void testEquals_WithSelf_ReturnsTrue() {
        ChartRenderingInfo info = new ChartRenderingInfo();
        assertTrue("An object should be equal to itself.", info.equals(info));
    }

    @Test
    public void testEquals_WithEqualObjects_ReturnsTrue() {
        ChartRenderingInfo info1 = new ChartRenderingInfo();
        ChartRenderingInfo info2 = new ChartRenderingInfo();
        assertTrue("Two default-constructed objects should be equal.", info1.equals(info2));
    }

    @Test
    public void testEquals_WithDifferentChartArea_ReturnsFalse() {
        ChartRenderingInfo info1 = new ChartRenderingInfo();
        ChartRenderingInfo info2 = new ChartRenderingInfo();
        info2.setChartArea(new Rectangle(1, 1, 1, 1));
        assertFalse("Objects with different chart areas should not be equal.", info1.equals(info2));
    }

    @Test
    public void testEquals_WithDifferentEntityCollection_ReturnsFalse() {
        ChartRenderingInfo info1 = new ChartRenderingInfo();
        ChartRenderingInfo info2 = new ChartRenderingInfo(null);
        assertFalse("Objects with different entity collections should not be equal.", info1.equals(info2));
    }

    @Test
    public void testEquals_WithDifferentPlotInfo_ReturnsFalse() {
        ChartRenderingInfo info1 = new ChartRenderingInfo();
        ChartRenderingInfo info2 = new ChartRenderingInfo();
        info2.getPlotInfo().addSubplotInfo(new PlotRenderingInfo(info2)); // Modify plot info
        assertFalse("Objects with different plot info should not be equal.", info1.equals(info2));
    }

    @Test
    public void testEquals_WithNull_ReturnsFalse() {
        ChartRenderingInfo info = new ChartRenderingInfo();
        assertFalse("An object should not be equal to null.", info.equals(null));
    }

    @Test
    public void testEquals_WithDifferentClass_ReturnsFalse() {
        ChartRenderingInfo info = new ChartRenderingInfo();
        assertFalse("An object should not be equal to an object of a different class.", info.equals("String"));
    }

    @Test
    public void testHashCode_IsConsistentWithEquals() {
        ChartRenderingInfo info1 = new ChartRenderingInfo();
        ChartRenderingInfo info2 = new ChartRenderingInfo();
        assertEquals("Equal objects must have equal hash codes.", info1.hashCode(), info2.hashCode());

        info1.setChartArea(new Rectangle(1, 1, 1, 1));
        assertNotEquals("Unequal objects should ideally have different hash codes.", info1.hashCode(), info2.hashCode());
    }

    @Test(expected = StackOverflowError.class)
    public void testHashCode_WithCircularPlotInfo_ThrowsStackOverflowError() {
        // Arrange
        ChartRenderingInfo info = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = info.getPlotInfo();
        plotInfo.addSubplotInfo(plotInfo); // Create a circular reference

        // Act
        info.hashCode(); // Should cause a stack overflow due to recursion
    }

    @Test
    public void testClone_CreatesIndependentCopy() throws CloneNotSupportedException {
        // Arrange
        ChartRenderingInfo original = new ChartRenderingInfo();
        original.setChartArea(new Rectangle(10, 20, 30, 40));
        original.getPlotInfo().setDataArea(new Rectangle(5, 6, 7, 8));

        // Act
        ChartRenderingInfo clone = (ChartRenderingInfo) original.clone();

        // Assert
        assertTrue("Clone should be equal to the original.", original.equals(clone));
        assertNotSame("Clone should be a different object instance.", original, clone);

        // Verify deep copies where expected
        assertNotSame("ChartArea should be a deep copy.", original.getChartArea(), clone.getChartArea());
        assertNotSame("PlotInfo should be a deep copy.", original.getPlotInfo(), clone.getPlotInfo());

        // Verify shallow copies where expected
        assertSame("EntityCollection should be a shallow copy (same reference).",
                original.getEntityCollection(), clone.getEntityCollection());
    }

    //endregion

    //region Serialization and Integration Tests

    @Test
    public void testSerialization() throws Exception {
        // Arrange
        ChartRenderingInfo original = new ChartRenderingInfo();
        original.setChartArea(new Rectangle(1, 2, 3, 4));
        original.getPlotInfo().setDataArea(new Rectangle(5, 6, 7, 8));

        // Act
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(buffer);
        out.writeObject(original);
        out.close();

        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
        ChartRenderingInfo deserialized = (ChartRenderingInfo) in.readObject();
        in.close();

        // Assert
        assertEquals("Deserialized object should be equal to the original.", original, deserialized);
    }


    /**
     * An integration-style test to verify that the ChartRenderingInfo object is
     * correctly populated when a chart is drawn.
     */
    @Test
    public void testState_AfterChartDrawing() {
        // Arrange
        ChartRenderingInfo info = new ChartRenderingInfo();
        // Create a simple chart that will generate entities
        DateAxis dateAxis = new DateAxis("Date", new SimpleTimeZone(0, "GMT"), Locale.UK);
        CombinedRangeCategoryPlot plot = new CombinedRangeCategoryPlot(dateAxis);
        JFreeChart chart = new JFreeChart("Test Chart", plot);

        // Act
        // Drawing the chart populates the ChartRenderingInfo object
        chart.createBufferedImage(300, 200, info);

        // Assert
        assertNotNull("Chart area should be populated after drawing.", info.getChartArea());
        assertFalse("Chart area should not be empty after drawing.", info.getChartArea().isEmpty());
        assertTrue("Entity collection should contain entities after drawing.",
                info.getEntityCollection().getEntityCount() > 0);
    }

    //endregion
}