package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the parsing functionality of {@link UtcInstant}.
 */
public class UtcInstantParseTest {

    /**
     * Tests that parsing a string representing an instant before the
     * Modified Julian Day epoch (1858-11-17) produces the correct
     * internal representation.
     */
    @Test
    public void testParse_forInstantBeforeMjdEpoch() {
        // Arrange: Define an instant string from before the MJD epoch.
        // The date 1857-01-29 is 657 days before the MJD epoch date of 1858-11-17.
        String instantString = "1857-01-29T00:00:00.000001876Z";
        long expectedMjd = -657L;
        long expectedNanoOfDay = 1876L;

        // Act: Parse the string to create a UtcInstant.
        UtcInstant parsedInstant = UtcInstant.parse(instantString);

        // Assert: Verify that the internal components (MJD and nano-of-day) are correct.
        assertEquals(
            "The Modified Julian Day should be correctly calculated for a pre-epoch date.",
            expectedMjd,
            parsedInstant.getModifiedJulianDay()
        );
        assertEquals(
            "The nano-of-day should be extracted correctly from the string.",
            expectedNanoOfDay,
            parsedInstant.getNanoOfDay()
        );
    }
}