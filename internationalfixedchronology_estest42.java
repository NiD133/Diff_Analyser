package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * Tests for {@link InternationalFixedChronology} focusing on exception handling.
 */
// The original class name and inheritance are preserved to show a direct refactoring.
public class InternationalFixedChronology_ESTestTest42 extends InternationalFixedChronology_ESTest_scaffolding {

    /**
     * Tests that zonedDateTime(Instant, ZoneId) throws a NullPointerException
     * when the provided Instant is null.
     */
    @Test(expected = NullPointerException.class)
    public void zonedDateTime_throwsNullPointerException_forNullInstant() {
        // Arrange: Get the singleton instance of the chronology and a valid ZoneId.
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        ZoneId zone = ZoneOffset.MAX;

        // Act: Call the method under test with a null Instant.
        // Assert: The @Test annotation asserts that a NullPointerException is thrown.
        chronology.zonedDateTime(null, zone);
    }
}