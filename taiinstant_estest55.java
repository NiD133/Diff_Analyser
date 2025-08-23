package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link TaiInstant#parse(CharSequence)} method.
 */
public class TaiInstantParseTest {

    /**
     * Tests that a valid string representing the TAI epoch is parsed correctly.
     * The format requires a seconds part, a dot, exactly nine nanosecond digits,
     * and the "s(TAI)" suffix.
     */
    @Test
    public void parse_validEpochString_parsesSuccessfully() {
        // Arrange: Define a valid string for the TAI epoch.
        String epochString = "0.000000000s(TAI)";

        // Act: Call the method under test.
        TaiInstant parsedInstant = TaiInstant.parse(epochString);

        // Assert: The resulting instant should have zero seconds and zero nanos.
        assertEquals("Parsed seconds should be 0", 0L, parsedInstant.getTaiSeconds());
        assertEquals("Parsed nanos should be 0", 0, parsedInstant.getNano());
    }
}