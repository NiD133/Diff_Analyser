package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link XYInterval} class.
 */
public class XYIntervalTest {

    /**
     * Verifies that two XYInterval objects created with the same values are
     * considered equal. This also checks that their hash codes are equal,
     * fulfilling the contract of the equals() method.
     */
    @Test
    public void equals_withTwoIdenticalIntervals_shouldReturnTrue() {
        // Arrange: Create two XYInterval objects with identical properties.
        // Using distinct, non-zero values makes the test more robust.
        XYInterval interval1 = new XYInterval(10.0, 11.0, 10.5, 10.2, 10.8);
        XYInterval interval2 = new XYInterval(10.0, 11.0, 10.5, 10.2, 10.8);

        // Assert: The two intervals should be equal.
        // Using assertEquals() is the standard way to test for object equality.
        // It calls the .equals() method and provides a more informative failure message.
        assertEquals(interval1, interval2);

        // Per the Java contract, equal objects must have equal hash codes.
        assertEquals("Equal objects must have equal hash codes.", interval1.hashCode(), interval2.hashCode());
    }
}