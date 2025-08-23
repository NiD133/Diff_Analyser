package org.jfree.chart.plot;

import org.jfree.data.Range;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link MeterInterval} class, focusing on the equals() method.
 */
public class MeterIntervalTest {

    /**
     * Verifies that two MeterInterval instances are considered equal if they are
     * constructed with the same label and range.
     */
    @Test
    public void testEqualsReturnsTrueForIdenticalInstances() {
        // Arrange: Create two separate MeterInterval objects with identical properties.
        String label = "Normal";
        Range range = new Range(0.0, 50.0);
        MeterInterval interval1 = new MeterInterval(label, range);
        MeterInterval interval2 = new MeterInterval(label, range);

        // Act & Assert: The two instances should be equal.
        // Using assertEquals is more expressive for equality checks than assertTrue.
        assertEquals(interval1, interval2);
    }
}