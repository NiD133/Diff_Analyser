package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class contains an improved version of a test for the {@link Minutes} class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class Minutes_ESTestTest18 extends Minutes_ESTest_scaffolding {

    /**
     * Tests that negating a zero-value Minutes object results in zero.
     * <p>
     * This test also implicitly verifies that {@code Minutes.minutesIn(null)}
     * correctly returns a zero-minute instance as per its contract.
     */
    @Test
    public void negatingMinutesFromNullIntervalShouldReturnZero() {
        // Arrange: The Javadoc for minutesIn() specifies that passing a null interval
        // should return a Minutes object representing zero.
        Minutes zeroMinutes = Minutes.minutesIn(null);

        // Act: Negate the resulting Minutes object.
        Minutes result = zeroMinutes.negated();

        // Assert: The negation of zero should still be zero.
        assertEquals("Negating zero minutes should result in zero.", 0, result.getMinutes());
    }
}