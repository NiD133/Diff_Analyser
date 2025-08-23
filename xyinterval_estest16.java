package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the equals() method of the {@link XYInterval} class.
 */
public class XYIntervalTest {

    /**
     * Verifies that two XYInterval objects are not considered equal if their
     * yHigh values differ, while all other properties are the same.
     */
    @Test
    public void equals_shouldReturnFalse_whenOnlyYHighIsDifferent() {
        // Arrange: Create a base interval and another interval that is identical
        // except for the yHigh value.
        XYInterval baseInterval = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        XYInterval differentYHighInterval = new XYInterval(0.0, 0.0, 0.0, 0.0, -482.4691);

        // Act & Assert: The intervals should not be equal.
        // The comparison should also be symmetric, as required by the equals() contract.
        assertFalse("Intervals with different yHigh values should not be equal.",
                    baseInterval.equals(differentYHighInterval));
        
        assertFalse("The equals() method should be symmetric.",
                    differentYHighInterval.equals(baseInterval));
    }
}