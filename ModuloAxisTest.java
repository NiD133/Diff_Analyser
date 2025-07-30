package org.jfree.chart.axis;

import org.jfree.chart.TestUtils;
import org.jfree.data.Range;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ModuloAxis} class.
 */
public class ModuloAxisTest {

    /**
     * Tests that a {@link ModuloAxis} object can be cloned correctly.
     * The cloned object should be a separate instance but equal in content.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        ModuloAxis originalAxis = new ModuloAxis("Test", new Range(0.0, 1.0));
        ModuloAxis clonedAxis = (ModuloAxis) originalAxis.clone();
        
        assertNotSame(originalAxis, clonedAxis, "Cloned object should not be the same instance");
        assertSame(originalAxis.getClass(), clonedAxis.getClass(), "Cloned object should be of the same class");
        assertEquals(originalAxis, clonedAxis, "Cloned object should be equal in content");
    }

    /**
     * Tests the {@code equals} method to ensure it correctly distinguishes
     * between different {@link ModuloAxis} objects based on their fields.
     */
    @Test
    public void testEquals() {
        ModuloAxis axis1 = new ModuloAxis("Test", new Range(0.0, 1.0));
        ModuloAxis axis2 = new ModuloAxis("Test", new Range(0.0, 1.0));
        
        assertEquals(axis1, axis2, "Axes with identical properties should be equal");

        axis1.setDisplayRange(0.1, 1.1);
        assertNotEquals(axis1, axis2, "Axes with different display ranges should not be equal");
        
        axis2.setDisplayRange(0.1, 1.1);
        assertEquals(axis1, axis2, "Axes should be equal after setting the same display range");
    }

    /**
     * Tests that two equal {@link ModuloAxis} objects have the same hash code.
     */
    @Test
    public void testHashCode() {
        ModuloAxis axis1 = new ModuloAxis("Test", new Range(0.0, 1.0));
        ModuloAxis axis2 = new ModuloAxis("Test", new Range(0.0, 1.0));
        
        assertEquals(axis1, axis2, "Axes with identical properties should be equal");
        assertEquals(axis1.hashCode(), axis2.hashCode(), "Equal axes should have the same hash code");
    }

    /**
     * Tests the serialization and deserialization of a {@link ModuloAxis} object.
     * The deserialized object should be equal to the original.
     */
    @Test
    public void testSerialization() {
        ModuloAxis originalAxis = new ModuloAxis("Test", new Range(0.0, 1.0));
        ModuloAxis deserializedAxis = TestUtils.serialised(originalAxis);
        
        assertEquals(originalAxis, deserializedAxis, "Deserialized axis should be equal to the original");
    }
}