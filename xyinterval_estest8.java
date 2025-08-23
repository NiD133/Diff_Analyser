package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link XYInterval} class.
 */
public class XYIntervalTest {

    /**
     * Verifies that the getYHigh() method correctly returns the value
     * provided to the constructor.
     */
    @Test
    public void getYHigh_shouldReturnTheValueProvidedInConstructor() {
        // Arrange: Create an XYInterval with distinct values to ensure we are
        // testing the correct property.
        double expectedYHigh = 5.5;
        XYInterval interval = new XYInterval(1.0, 2.0, 3.0, 4.0, expectedYHigh);

        // Act: Call the method under test.
        double actualYHigh = interval.getYHigh();

        // Assert: Check that the returned value is the one we expect.
        // A delta of 0.0 is used for an exact match, as no floating-point
        // arithmetic that could introduce precision errors has occurred.
        assertEquals(expectedYHigh, actualYHigh, 0.0);
    }
}