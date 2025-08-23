package org.threeten.extra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test suite for the {@link DayOfYear} class, focusing on specific method contracts.
 */
public class DayOfYearTest {

    /**
     * Verifies that calling adjustInto() with a null argument throws a NullPointerException.
     * The method contract requires the temporal object to be non-null.
     */
    @Test
    public void adjustInto_withNullTemporal_throwsNullPointerException() {
        // Arrange: Create a deterministic DayOfYear instance. Any valid day will suffice.
        DayOfYear dayOfYear = DayOfYear.of(150);

        // Act & Assert: Expect a NullPointerException when calling the method with null.
        try {
            dayOfYear.adjustInto(null);
            fail("Expected a NullPointerException to be thrown, but it wasn't.");
        } catch (NullPointerException e) {
            // Verify that the exception message is as expected from Objects.requireNonNull.
            // The parameter name is "temporal".
            assertEquals("temporal", e.getMessage());
        }
    }
}