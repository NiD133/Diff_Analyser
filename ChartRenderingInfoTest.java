package org.jfree.chart;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ChartRenderingInfo} class.
 */
public class ChartRenderingInfoTest {

    /**
     * Tests the equals method to ensure it correctly differentiates between
     * instances based on their fields.
     */
    @Test
    public void testEquals() {
        ChartRenderingInfo info1 = new ChartRenderingInfo();
        ChartRenderingInfo info2 = new ChartRenderingInfo();
        
        // Initially, both instances should be equal
        assertEquals(info1, info2);

        // Modify chart area in info1 and verify inequality
        info1.setChartArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        assertNotEquals(info1, info2);

        // Set the same chart area in info2 and verify equality
        info2.setChartArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        assertEquals(info1, info2);

        // Modify plot data area in info1 and verify inequality
        info1.getPlotInfo().setDataArea(new Rectangle(1, 2, 3, 4));
        assertNotEquals(info1, info2);

        // Set the same plot data area in info2 and verify equality
        info2.getPlotInfo().setDataArea(new Rectangle(1, 2, 3, 4));
        assertEquals(info1, info2);

        // Modify entity collection in info1 and verify inequality
        StandardEntityCollection entities1 = new StandardEntityCollection();
        entities1.add(new ChartEntity(new Rectangle(1, 2, 3, 4)));
        info1.setEntityCollection(entities1);
        assertNotEquals(info1, info2);

        // Set the same entity collection in info2 and verify equality
        StandardEntityCollection entities2 = new StandardEntityCollection();
        entities2.add(new ChartEntity(new Rectangle(1, 2, 3, 4)));
        info2.setEntityCollection(entities2);
        assertEquals(info1, info2);
    }

    /**
     * Tests the cloning functionality to ensure a deep copy is created.
     * @throws CloneNotSupportedException if cloning is not supported.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        ChartRenderingInfo originalInfo = new ChartRenderingInfo();
        ChartRenderingInfo clonedInfo = CloneUtils.clone(originalInfo);

        // Verify that the cloned instance is a separate object
        assertNotSame(originalInfo, clonedInfo);
        assertSame(originalInfo.getClass(), clonedInfo.getClass());
        assertEquals(originalInfo, clonedInfo);

        // Modify the chart area in the original and verify independence
        originalInfo.getChartArea().setRect(4.0, 3.0, 2.0, 1.0);
        assertNotEquals(originalInfo, clonedInfo);

        // Apply the same modification to the clone and verify equality
        clonedInfo.getChartArea().setRect(4.0, 3.0, 2.0, 1.0);
        assertEquals(originalInfo, clonedInfo);

        // Modify the entity collection in the original and verify independence
        originalInfo.getEntityCollection().add(new ChartEntity(new Rectangle(1, 2, 2, 1)));
        assertNotEquals(originalInfo, clonedInfo);

        // Apply the same modification to the clone and verify equality
        clonedInfo.getEntityCollection().add(new ChartEntity(new Rectangle(1, 2, 2, 1)));
        assertEquals(originalInfo, clonedInfo);
    }

    /**
     * Tests serialization and deserialization to ensure object integrity.
     */
    @Test
    public void testSerialization() {
        ChartRenderingInfo originalInfo = new ChartRenderingInfo();
        originalInfo.setChartArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        
        // Serialize and deserialize the object
        ChartRenderingInfo deserializedInfo = TestUtils.serialised(originalInfo);
        
        // Verify that the deserialized instance is equal to the original
        assertEquals(originalInfo, deserializedInfo);
    }

    /**
     * Tests serialization and deserialization with plot info to ensure object integrity.
     */
    @Test
    public void testSerializationWithPlotInfo() {
        ChartRenderingInfo originalInfo = new ChartRenderingInfo();
        originalInfo.getPlotInfo().setDataArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        
        // Serialize and deserialize the object
        ChartRenderingInfo deserializedInfo = TestUtils.serialised(originalInfo);
        
        // Verify that the deserialized instance is equal to the original
        assertEquals(originalInfo, deserializedInfo);
        
        // Verify that the plot info owner is correctly set
        assertEquals(deserializedInfo, deserializedInfo.getPlotInfo().getOwner());
    }
}