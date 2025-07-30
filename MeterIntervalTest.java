package org.jfree.chart.plot;

import java.awt.BasicStroke;
import java.awt.Color;

import org.jfree.chart.TestUtils;
import org.jfree.data.Range;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link MeterInterval} class.
 */
public class MeterIntervalTest {

    /**
     * Tests the {@code equals} method to ensure it correctly compares
     * all relevant fields of the {@link MeterInterval} class.
     */
    @Test
    public void testEquals() {
        // Create two identical MeterInterval objects
        MeterInterval interval1 = createMeterInterval("Label 1", 1.2, 3.4, Color.RED, 1.0f, Color.BLUE);
        MeterInterval interval2 = createMeterInterval("Label 1", 1.2, 3.4, Color.RED, 1.0f, Color.BLUE);

        // Assert that both objects are equal
        assertEquals(interval1, interval2);
        assertEquals(interval2, interval1);

        // Modify one field and assert inequality
        interval1 = createMeterInterval("Label 2", 1.2, 3.4, Color.RED, 1.0f, Color.BLUE);
        assertNotEquals(interval1, interval2);

        // Update the second object to match the first and assert equality
        interval2 = createMeterInterval("Label 2", 1.2, 3.4, Color.RED, 1.0f, Color.BLUE);
        assertEquals(interval1, interval2);
    }

    /**
     * Tests that the {@link MeterInterval} class is not cloneable,
     * as it is designed to be immutable.
     */
    @Test
    public void testCloning() {
        MeterInterval interval = new MeterInterval("X", new Range(1.0, 2.0));
        assertFalse(interval instanceof Cloneable, "MeterInterval should not be cloneable");
    }

    /**
     * Tests the serialization and deserialization process to ensure
     * that a {@link MeterInterval} object remains equal to its original
     * state after being serialized and then deserialized.
     */
    @Test
    public void testSerialization() {
        MeterInterval original = new MeterInterval("X", new Range(1.0, 2.0));
        MeterInterval deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized object should be equal to the original");
    }

    /**
     * Helper method to create a {@link MeterInterval} with specified parameters.
     *
     * @param label the label for the interval
     * @param lower the lower bound of the range
     * @param upper the upper bound of the range
     * @param outlineColor the color of the outline
     * @param strokeWidth the width of the outline stroke
     * @param backgroundColor the background color
     * @return a new {@link MeterInterval} instance
     */
    private MeterInterval createMeterInterval(String label, double lower, double upper, Color outlineColor, float strokeWidth, Color backgroundColor) {
        return new MeterInterval(label, new Range(lower, upper), outlineColor, new BasicStroke(strokeWidth), backgroundColor);
    }
}