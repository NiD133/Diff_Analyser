package org.threeten.extra.chrono;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.Clock;

/**
 * Tests for the {@link InternationalFixedChronology} class.
 */
public class InternationalFixedChronologyTest {

    /**
     * A JUnit Rule for verifying that a method throws a specific exception.
     * This is a standard and flexible way to test exception-throwing behavior in JUnit 4.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * Verifies that dateNow(Clock) throws a NullPointerException when passed a null clock.
     * This is the expected behavior, as the method's contract requires a non-null clock.
     */
    @Test
    public void dateNow_nullClock_throwsNullPointerException() {
        // Expect a NullPointerException to be thrown.
        thrown.expect(NullPointerException.class);
        // The original test's structure suggested it was verifying the message from
        // Objects.requireNonNull, so we'll check for that message explicitly.
        thrown.expectMessage("clock");

        // When the dateNow(Clock) method is called with a null argument...
        InternationalFixedChronology.INSTANCE.dateNow((Clock) null);
        
        // ...the test will pass only if the expected exception is thrown.
    }
}