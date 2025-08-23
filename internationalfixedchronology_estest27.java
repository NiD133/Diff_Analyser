package org.threeten.extra.chrono;

import org.junit.Test;

/**
 * Unit tests for {@link InternationalFixedChronology}.
 */
public class InternationalFixedChronologyTest {

    /**
     * Verifies that calling {@code localDateTime(TemporalAccessor)} with a null argument
     * results in a {@code NullPointerException}. This is standard behavior for methods
     * accepting object arguments that cannot be null.
     */
    @Test(expected = NullPointerException.class)
    public void localDateTime_withNullInput_throwsNullPointerException() {
        // Arrange: Obtain the singleton instance of the chronology.
        // The source class documentation recommends using the INSTANCE field over the constructor.
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;

        // Act & Assert: Calling the method with null should trigger the exception.
        // The @Test(expected=...) annotation asserts that the expected exception is thrown.
        chronology.localDateTime(null);
    }
}