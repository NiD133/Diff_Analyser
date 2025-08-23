package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.Clock;

/**
 * Tests for {@link Symmetry454Chronology}.
 * This focuses on the specific test case provided for improvement.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests that dateNow(Clock) throws a NullPointerException when the clock is null.
     * <p>
     * The method is expected to be null-hostile, immediately rejecting null arguments
     * as per standard Java library conventions.
     */
    @Test(expected = NullPointerException.class)
    public void dateNow_withNullClock_throwsNullPointerException() {
        // Arrange: Get the singleton instance of the chronology.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;

        // Act & Assert: Calling dateNow with a null clock should throw a NullPointerException.
        // The @Test(expected=...) annotation handles the assertion.
        chronology.dateNow((Clock) null);
    }
}