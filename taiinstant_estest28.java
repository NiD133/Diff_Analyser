package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Instant;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link TaiInstant} class, focusing on comparison logic.
 */
public class TaiInstantComparisonTest {

    /**
     * Tests that compareTo() correctly identifies an instant as being before another.
     * This test also verifies that withTaiSeconds() correctly creates a new instant
     * while preserving the nano-of-second value.
     */
    @Test
    public void compareTo_returnsNegative_whenInstantIsBeforeAnother() {
        // Arrange
        // The value "2014-02-15T10:15:30.320Z" is the fixed instant returned by MockInstant.now().
        // Using an explicit Instant makes the origin of the nano value (320,000,000) clear.
        Instant baseJavaInstant = Instant.parse("2014-02-15T10:15:30.320Z");
        TaiInstant laterInstant = TaiInstant.of(baseJavaInstant);

        // Create an earlier instant by setting the TAI seconds to a smaller value.
        long earlierSeconds = -2L;
        TaiInstant earlierInstant = laterInstant.withTaiSeconds(earlierSeconds);

        // Act
        int comparisonResult = earlierInstant.compareTo(laterInstant);

        // Assert
        // 1. Verify the result of the comparison.
        assertTrue(
            "An earlier instant should compare as less than a later one.",
            comparisonResult < 0);

        // 2. Verify the state of the newly created 'earlierInstant'.
        assertEquals("The TAI seconds should be updated to the new value.",
            earlierSeconds, earlierInstant.getTaiSeconds());
        assertEquals("The nano-of-second should be preserved from the original instant.",
            laterInstant.getNano(), earlierInstant.getNano());
    }
}