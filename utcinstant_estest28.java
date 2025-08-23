package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Instant;
import static org.junit.Assert.assertEquals;
import org.evosuite.runtime.mock.java.time.MockInstant;

public class UtcInstant_ESTestTest28 extends UtcInstant_ESTest_scaffolding {

    /**
     * Tests that compareTo() correctly identifies an earlier instant created from the TAI epoch
     * as being before a later instant created from the standard Java epoch.
     */
    @Test
    public void compareTo_returnsNegative_whenComparingEarlierToLaterInstant() {
        // Arrange
        // Create a UtcInstant from a TaiInstant.
        // The TAI epoch is 1958-01-01. This instant is 3217 seconds and 1000 nanoseconds after it,
        // representing a point in time in 1958.
        TaiInstant taiInstantIn1958 = TaiInstant.ofTaiSeconds(3217L, 1000L);
        UtcInstant earlierInstant = taiInstantIn1958.toUtcInstant();

        // Create another UtcInstant from a standard java.time.Instant.
        // The Java epoch is 1970-01-01. This instant is 3217 milliseconds after it,
        // representing a point in time in 1970.
        Instant javaInstantIn1970 = MockInstant.ofEpochMilli(3217L);
        UtcInstant laterInstant = UtcInstant.of(javaInstantIn1970);

        // Act
        int comparisonResult = earlierInstant.compareTo(laterInstant);

        // Assert
        // The 1958 instant should be considered "less than" the 1970 instant.
        assertEquals(-1, comparisonResult);

        // The original test also verified the nano-of-day calculation for the later instant.
        // 3217 ms from the start of the day is 3,217,000,000 nanoseconds.
        assertEquals(3_217_000_000L, laterInstant.getNanoOfDay());
    }
}