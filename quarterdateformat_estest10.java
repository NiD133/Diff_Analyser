package org.jfree.chart.axis;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the equals() method in the {@link QuarterDateFormat} class.
 */
public class QuarterDateFormatTest {

    /**
     * Verifies the reflexive property of the equals() method.
     * An object must always be equal to itself.
     */
    @Test
    public void equals_givenSameInstance_returnsTrue() {
        // Arrange: Create an instance of the class.
        QuarterDateFormat formatter = new QuarterDateFormat();

        // Assert: The object must be equal to itself.
        // This is a standard contract of the Object.equals() method.
        assertEquals(formatter, formatter);
    }
}