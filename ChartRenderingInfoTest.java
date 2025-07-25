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
     * Tests the equals method to ensure it correctly compares all relevant fields.
     */
    @Test
    public void testEqualsMethod() {
        ChartRenderingInfo info1 = new ChartRenderingInfo();
        ChartRenderingInfo info2 = new ChartRenderingInfo();
        assertEquals(info1, info2, "Newly created instances should be equal");

        info1.setChartArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        assertNotEquals(info1, info2, "Instances should not be equal after modifying chart area of info1");
        
        info2.setChartArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        assertEquals(info1, info2, "Instances should be equal after setting the same chart area");

        info1.getPlotInfo().setDataArea(new Rectangle(1, 2, 3, 4));
        assertNotEquals(info1, info2, "Instances should not be equal after modifying plot data area of info1");
        
        info2.getPlotInfo().setDataArea(new Rectangle(1, 2, 3, 4));
        assertEquals(info1, info2, "Instances should be equal after setting the same plot data area");

        StandardEntityCollection entities1 = new StandardEntityCollection();
        entities1.add(new ChartEntity(new Rectangle(1, 2, 3, 4)));
        info1.setEntityCollection(entities1);
        assertNotEquals(info1, info2, "Instances should not be equal after adding entities to info1");

        StandardEntityCollection entities2 = new StandardEntityCollection();
        entities2.add(new ChartEntity(new Rectangle(1, 2, 3, 4)));
        info2.setEntityCollection(entities2);
        assertEquals(info1, info2, "Instances should be equal after adding the same entities");
    }

    /**
     * Tests the cloning functionality to ensure a deep copy is made.
     * @throws CloneNotSupportedException if cloning is not supported.
     */
    @Test
    public void testCloningFunctionality() throws CloneNotSupportedException {
        ChartRenderingInfo original = new ChartRenderingInfo();
        ChartRenderingInfo clone = CloneUtils.clone(original);

        assertNotSame(original, clone, "Clone should be a different instance");
        assertSame(original.getClass(), clone.getClass(), "Clone should be of the same class");
        assertEquals(original, clone, "Clone should be equal to the original");

        // Verify independence of the clone
        original.getChartArea().setRect(4.0, 3.0, 2.0, 1.0);
        assertNotEquals(original, clone, "Clone should not reflect changes made to the original");

        clone.getChartArea().setRect(4.0, 3.0, 2.0, 1.0);
        assertEquals(original, clone, "Clone should be equal after setting the same chart area");

        original.getEntityCollection().add(new ChartEntity(new Rectangle(1, 2, 2, 1)));
        assertNotEquals(original, clone, "Clone should not reflect changes made to the original's entities");

        clone.getEntityCollection().add(new ChartEntity(new Rectangle(1, 2, 2, 1)));
        assertEquals(original, clone, "Clone should be equal after adding the same entities");
    }

    /**
     * Tests serialization and deserialization to ensure object integrity.
     */
    @Test
    public void testSerialization() {
        ChartRenderingInfo original = new ChartRenderingInfo();
        original.setChartArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        ChartRenderingInfo deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized instance should be equal to the original");
    }

    /**
     * Tests serialization and deserialization with plot info to ensure object integrity.
     */
    @Test
    public void testSerializationWithPlotInfo() {
        ChartRenderingInfo original = new ChartRenderingInfo();
        original.getPlotInfo().setDataArea(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        ChartRenderingInfo deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized instance should be equal to the original");
        assertEquals(deserialized, deserialized.getPlotInfo().getOwner(), "Deserialized plot info owner should be correct");
    }
}