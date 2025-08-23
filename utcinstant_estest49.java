package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Duration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * This test class contains improved versions of tests for UtcInstant.
 * The original test class name and scaffolding are preserved for context.
 */
public class UtcInstant_ESTestTest49 extends UtcInstant_ESTest_scaffolding {

    /**
     * Tests that the equals() method correctly returns false for two different UtcInstant instances.
     * This test also implicitly verifies the behavior of the minus(Duration) method, which is used
     * to create the second distinct instant.
     */
    @Test
    public void equals_returnsFalseForDifferentInstants() {
        // Arrange: Create an initial instant at the start of a Modified Julian Day.
        UtcInstant instant1 = UtcInstant.ofModifiedJulianDay(0L, 0L);

        // Create a second, different instant by subtracting a duration.
        Duration durationToSubtract = Duration.ofSeconds(1526L);
        UtcInstant instant2 = instant1.minus(durationToSubtract);

        // Act: Compare the two distinct instants for equality.
        boolean areEqual = instant1.equals(instant2);

        // Assert: The instants should not be equal.
        assertFalse("Instants with different timeline positions should not be equal", areEqual);
        assertNotEquals("assertNotEquals should confirm the instants are different", instant1, instant2);

        // Further assertions to verify the state of the modified instant, confirming *why* it's different.
        // Subtracting 1526 seconds from the start of MJD 0 should result in an instant on the previous day (MJD -1).
        assertEquals("Modified Julian Day should be decremented", -1L, instant2.getModifiedJulianDay());

        // The nano-of-day should be the total nanoseconds in a standard day minus the nanoseconds subtracted.
        // A standard day has 86,400 seconds.
        final long SECONDS_IN_A_DAY = 86400L;
        final long NANOS_PER_SECOND = 1_000_000_000L;
        long expectedNanoOfDay = (SECONDS_IN_A_DAY - 1526L) * NANOS_PER_SECOND;

        assertEquals("Nano-of-day should be near the end of the previous day",
                expectedNanoOfDay, instant2.getNanoOfDay());
    }
}