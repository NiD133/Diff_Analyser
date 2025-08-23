package org.jfree.chart.plot;

import org.jfree.data.Range;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link MeterInterval} class.
 */
public class MeterIntervalTest {

    /**
     * Verifies the reflexive property of the equals() method.
     * An object must always be equal to itself.
     */
    @Test
    public void equals_givenSameInstance_returnsTrue() {
        // Arrange: Create a MeterInterval instance.
        Range range = new Range(0.0, 50.0);
        MeterInterval interval = new MeterInterval("Normal", range);

        // Act & Assert: An instance should be equal to itself.
        assertTrue("A MeterInterval instance must be equal to itself.", interval.equals(interval));
    }
}