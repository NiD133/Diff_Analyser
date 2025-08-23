package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link IslamicChronology}.
 */
// Note: The original class name and inheritance structure from a test generation
// tool have been kept, but the test method itself has been rewritten for clarity.
public class IslamicChronology_ESTestTest36 extends IslamicChronology_ESTest_scaffolding {

    /**
     * Verifies that the default instance of IslamicChronology, obtained via
     * getInstance(), uses the 16-based leap year pattern, which is documented
     * as the most common and default pattern.
     */
    @Test
    public void getInstance_returnsChronologyWith16BasedLeapYearPatternByDefault() {
        // Arrange: Get the default instance of the IslamicChronology.
        IslamicChronology defaultChronology = IslamicChronology.getInstance();

        // Act: Retrieve the leap year pattern type from the instance.
        IslamicChronology.LeapYearPatternType actualPattern = defaultChronology.getLeapYearPatternType();

        // Assert: The pattern should be the standard 16-based leap year pattern.
        assertEquals(IslamicChronology.LEAP_YEAR_16_BASED, actualPattern);
    }
}