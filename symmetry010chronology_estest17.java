package org.threeten.extra.chrono;

import org.junit.Test;

/**
 * Tests for the {@link Symmetry010Chronology} class, focusing on exception handling.
 */
public class Symmetry010ChronologyTest {

    /**
     * Tests that calling zonedDateTime() with a null TemporalAccessor throws a NullPointerException.
     * The underlying implementation delegates to ZoneId.from(temporal), which is the expected
     * source of the exception.
     */
    @Test(expected = NullPointerException.class)
    public void zonedDateTime_withNullTemporal_shouldThrowNullPointerException() {
        // Arrange: Get the singleton instance of the chronology.
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;

        // Act: Attempt to create a ZonedDateTime from a null temporal accessor.
        // The method is expected to throw a NullPointerException.
        chronology.zonedDateTime(null);

        // Assert: The test passes if a NullPointerException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}