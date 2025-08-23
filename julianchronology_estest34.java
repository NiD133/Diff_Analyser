package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.TemporalAccessor;

/**
 * Unit tests for {@link JulianChronology}.
 */
public class JulianChronologyTest {

    /**
     * Tests that {@link JulianChronology#localDateTime(TemporalAccessor)} throws
     * a {@code NullPointerException} when the temporal accessor is null.
     */
    @Test(expected = NullPointerException.class)
    public void localDateTime_shouldThrowNullPointerException_whenInputIsNull() {
        // Arrange: The system under test is the JulianChronology singleton instance.
        JulianChronology chronology = JulianChronology.INSTANCE;

        // Act & Assert: Call the method with null input, expecting an exception.
        chronology.localDateTime(null);
    }
}