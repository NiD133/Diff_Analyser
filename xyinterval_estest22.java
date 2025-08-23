package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link XYInterval} class.
 */
public class XYIntervalTest {

    /**
     * Verifies the reflexive property of the equals() method, which states
     * that an object must be equal to itself.
     */
    @Test
    public void equals_onSameInstance_shouldReturnTrue() {
        // Arrange: Create an instance of XYInterval with clear, simple values.
        XYInterval interval = new XYInterval(10.0, 11.0, 5.0, 4.5, 5.5);

        // Act & Assert: An object must always be equal to itself.
        // The assertEquals method provides a clear and idiomatic way to test this,
        // as it relies on the .equals() method for comparison.
        assertEquals("An XYInterval instance should be equal to itself.", interval, interval);
    }
}