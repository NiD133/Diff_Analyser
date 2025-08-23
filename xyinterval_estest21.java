package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link XYInterval} class.
 */
public class XYIntervalTest {

    /**
     * Verifies that the equals() method returns false when an XYInterval object
     * is compared with an object of an incompatible type.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithIncompatibleType() {
        // Arrange: Create an XYInterval instance and an object of a different type.
        XYInterval interval = new XYInterval(1.0, 2.0, 1.5, 1.4, 1.6);
        Object otherObject = new Object();

        // Act & Assert: The result of the comparison should be false.
        assertFalse("An XYInterval should not be equal to an object of a different type.",
                interval.equals(otherObject));
    }
}