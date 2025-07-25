package org.jfree.chart;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link ChartRenderingInfo} class.  These tests aim to verify
 * the core functionality of the class, including equality checks, cloning,
 * and serialization.  Each test focuses on a specific aspect of the class
 * to ensure comprehensive coverage.
 */
public class ChartRenderingInfoTest {

    private ChartRenderingInfo chartRenderingInfo1;
    private ChartRenderingInfo chartRenderingInfo2;

    @BeforeEach
    void setUp() {
        chartRenderingInfo1 = new ChartRenderingInfo();
        chartRenderingInfo2 = new ChartRenderingInfo();
    }

    @Test
    @DisplayName("Test equality of ChartRenderingInfo objects")
    public void testEquals() {
        // Initially, two ChartRenderingInfo objects should be equal
        assertEquals(chartRenderingInfo1, chartRenderingInfo2, "Initially, objects should be equal.");

        // Modify chartArea in object1 and check inequality
        Rectangle2D chartArea = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
        chartRenderingInfo1.setChartArea(chartArea);
        assertNotEquals(chartRenderingInfo1, chartRenderingInfo2, "Objects should be unequal after modifying chartArea in object1.");

        // Modify chartArea in object2 to match object1 and check equality again
        chartRenderingInfo2.setChartArea(chartArea);
        assertEquals(chartRenderingInfo1, chartRenderingInfo2, "Objects should be equal after modifying chartArea in object2 to match object1.");

        // Modify dataArea in object1 and check inequality
        Rectangle dataArea = new Rectangle(1, 2, 3, 4);
        chartRenderingInfo1.getPlotInfo().setDataArea(dataArea);
        assertNotEquals(chartRenderingInfo1, chartRenderingInfo2, "Objects should be unequal after modifying dataArea in object1.");

        // Modify dataArea in object2 to match object1 and check equality again
        chartRenderingInfo2.getPlotInfo().setDataArea(dataArea);
        assertEquals(chartRenderingInfo1, chartRenderingInfo2, "Objects should be equal after modifying dataArea in object2 to match object1.");

        // Modify entityCollection in object1 and check inequality
        StandardEntityCollection entityCollection1 = new StandardEntityCollection();
        entityCollection1.add(new ChartEntity(new Rectangle(1, 2, 3, 4)));
        chartRenderingInfo1.setEntityCollection(entityCollection1);
        assertNotEquals(chartRenderingInfo1, chartRenderingInfo2, "Objects should be unequal after modifying entityCollection in object1.");

        // Modify entityCollection in object2 to match object1 and check equality again
        StandardEntityCollection entityCollection2 = new StandardEntityCollection();
        entityCollection2.add(new ChartEntity(new Rectangle(1, 2, 3, 4)));
        chartRenderingInfo2.setEntityCollection(entityCollection2);
        assertEquals(chartRenderingInfo1, chartRenderingInfo2, "Objects should be equal after modifying entityCollection in object2 to match object1.");
    }

    @Test
    @DisplayName("Test cloning of ChartRenderingInfo objects")
    public void testCloning() throws CloneNotSupportedException {
        // Clone the first object
        ChartRenderingInfo clonedInfo = CloneUtils.clone(chartRenderingInfo1);

        // Verify that the cloned object is not the same instance as the original
        assertNotSame(chartRenderingInfo1, clonedInfo, "Cloned object should not be the same instance as the original.");

        // Verify that the cloned object is of the same class as the original
        assertSame(chartRenderingInfo1.getClass(), clonedInfo.getClass(), "Cloned object should be of the same class as the original.");

        // Verify that the cloned object is equal to the original
        assertEquals(chartRenderingInfo1, clonedInfo, "Cloned object should be equal to the original.");

        // Modify chartArea in the original object and check that the cloned object remains unchanged
        chartRenderingInfo1.getChartArea().setRect(4.0, 3.0, 2.0, 1.0);
        assertNotEquals(chartRenderingInfo1, clonedInfo, "Cloned object should remain unchanged after modifying chartArea in the original.");

        // Update chartArea in cloned object and check equality
        clonedInfo.getChartArea().setRect(4.0, 3.0, 2.0, 1.0);
        assertEquals(chartRenderingInfo1, clonedInfo, "Cloned object should be equal to original after updating chartArea.");

        // Add an entity to the entity collection of the original object
        chartRenderingInfo1.getEntityCollection().add(new ChartEntity(new Rectangle(1, 2, 2, 1)));
        assertNotEquals(chartRenderingInfo1, clonedInfo, "Cloned object should remain unchanged after modifying entityCollection in the original.");

        // Update entityCollection in cloned object and check equality
        clonedInfo.getEntityCollection().add(new ChartEntity(new Rectangle(1, 2, 2, 1)));
        assertEquals(chartRenderingInfo1, clonedInfo, "Cloned object should be equal to original after updating entityCollection.");
    }

    @Test
    @DisplayName("Test serialization and deserialization of ChartRenderingInfo objects")
    public void testSerialization() {
        // Set a value for chartArea in the first object
        chartRenderingInfo1.setChartArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));

        // Serialize and deserialize the first object
        ChartRenderingInfo deserializedInfo = TestUtils.serialised(chartRenderingInfo1);

        // Verify that the deserialized object is equal to the original
        assertEquals(chartRenderingInfo1, deserializedInfo, "Deserialized object should be equal to the original.");
    }

    @Test
    @DisplayName("Test serialization with PlotRenderingInfo dataArea")
    public void testSerialization2() {
        // Set a value for the dataArea in the PlotRenderingInfo of the first object
        chartRenderingInfo1.getPlotInfo().setDataArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));

        // Serialize and deserialize the first object
        ChartRenderingInfo deserializedInfo = TestUtils.serialised(chartRenderingInfo1);

        // Verify that the deserialized object is equal to the original
        assertEquals(chartRenderingInfo1, deserializedInfo, "Deserialized object should be equal to the original.");

        // Verify that the owner of PlotRenderingInfo is the ChartRenderingInfo object
        assertEquals(deserializedInfo, deserializedInfo.getPlotInfo().getOwner(), "Owner of PlotRenderingInfo should be the ChartRenderingInfo object.");
    }
}