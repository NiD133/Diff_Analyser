package org.jfree.chart.plot.dial;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link DialBackground} class.
 */
public class DialBackgroundTest {

    /**
     * Verifies that the equals() method returns false when a DialBackground
     * instance is compared with an object of a completely different type.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentType() {
        // Arrange: Create a DialBackground instance and an instance of an unrelated class.
        DialBackground dialBackground = new DialBackground();
        Object otherObject = new Object();

        // Act & Assert: The equals method should return false.
        assertFalse("A DialBackground instance should not be equal to an object of a different type.",
                dialBackground.equals(otherObject));
    }
}