package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.TemporalField;
import static org.threeten.extra.DayOfMonth.of;

/**
 * Test suite for the {@link DayOfMonth} class, focusing on exception handling.
 */
public class DayOfMonthTest {

    /**
     * Tests that calling the range() method with a null TemporalField argument
     * throws a NullPointerException.
     *
     * The JavaDoc for range(TemporalField) specifies that the 'field' argument must not be null.
     * This test verifies that the method correctly enforces this precondition.
     */
    @Test(expected = NullPointerException.class)
    public void testRangeWithNullFieldThrowsException() {
        // Given a valid DayOfMonth instance
        DayOfMonth dayOfMonth = of(15);

        // When the range() method is called with a null field
        // Then a NullPointerException should be thrown
        dayOfMonth.range((TemporalField) null);
    }
}