package org.jfree.chart.plot;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.TestUtils;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link PlotRenderingInfo} class.
 */
public class PlotRenderingInfoTest {

    /**
     * Tests the equals() method of the PlotRenderingInfo class.
     * Verifies that two instances are considered equal if they have the same state,
     * and not equal if their state differs.
     */
    @Test
    public void testEquals() {
        // Create two instances of PlotRenderingInfo with default state
        PlotRenderingInfo info1 = new PlotRenderingInfo(new ChartRenderingInfo());
        PlotRenderingInfo info2 = new PlotRenderingInfo(new ChartRenderingInfo());

        // Verify that two new instances are equal
        assertEquals(info1, info2);
        assertEquals(info2, info1);

        // Change plot area of info1 and verify inequality
        info1.setPlotArea(new Rectangle(2, 3, 4, 5));
        assertNotEquals(info1, info2);

        // Set the same plot area for info2 and verify equality
        info2.setPlotArea(new Rectangle(2, 3, 4, 5));
        assertEquals(info1, info2);

        // Change data area of info1 and verify inequality
        info1.setDataArea(new Rectangle(2, 4, 6, 8));
        assertNotEquals(info1, info2);

        // Set the same data area for info2 and verify equality
        info2.setDataArea(new Rectangle(2, 4, 6, 8));
        assertEquals(info1, info2);

        // Add subplot info to info1 and verify inequality
        info1.addSubplotInfo(new PlotRenderingInfo(null));
        assertNotEquals(info1, info2);

        // Add the same subplot info to info2 and verify equality
        info2.addSubplotInfo(new PlotRenderingInfo(null));
        assertEquals(info1, info2);

        // Change data area of subplot in info1 and verify inequality
        info1.getSubplotInfo(0).setDataArea(new Rectangle(1, 2, 3, 4));
        assertNotEquals(info1, info2);

        // Set the same data area for subplot in info2 and verify equality
        info2.getSubplotInfo(0).setDataArea(new Rectangle(1, 2, 3, 4));
        assertEquals(info1, info2);
    }

    /**
     * Tests the cloning functionality of the PlotRenderingInfo class.
     * Ensures that a cloned instance is equal to the original but is a separate object.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        // Create an instance of PlotRenderingInfo and set plot area
        PlotRenderingInfo original = new PlotRenderingInfo(new ChartRenderingInfo());
        original.setPlotArea(new Rectangle2D.Double());

        // Clone the original instance
        PlotRenderingInfo clone = CloneUtils.clone(original);

        // Verify that the clone is a separate instance but equal to the original
        assertNotSame(original, clone);
        assertSame(original.getClass(), clone.getClass());
        assertEquals(original, clone);

        // Modify the plot area of the original and verify inequality
        original.getPlotArea().setRect(1.0, 2.0, 3.0, 4.0);
        assertNotEquals(original, clone);

        // Set the same plot area for the clone and verify equality
        clone.getPlotArea().setRect(1.0, 2.0, 3.0, 4.0);
        assertEquals(original, clone);

        // Modify the data area of the original and verify inequality
        original.getDataArea().setRect(4.0, 3.0, 2.0, 1.0);
        assertNotEquals(original, clone);

        // Set the same data area for the clone and verify equality
        clone.getDataArea().setRect(4.0, 3.0, 2.0, 1.0);
        assertEquals(original, clone);
    }

    /**
     * Tests the serialization and deserialization functionality of the PlotRenderingInfo class.
     * Ensures that a serialized and then deserialized instance is equal to the original.
     */
    @Test
    public void testSerialization() {
        // Create an instance of PlotRenderingInfo
        PlotRenderingInfo original = new PlotRenderingInfo(new ChartRenderingInfo());

        // Serialize and then deserialize the instance
        PlotRenderingInfo deserialized = TestUtils.serialised(original);

        // Verify that the deserialized instance is equal to the original
        assertEquals(original, deserialized);
    }
}