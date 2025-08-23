package org.threeten.extra;

import org.junit.Test;

/**
 * Tests for the {@link DayOfYear#range(java.time.temporal.TemporalField)} method.
 */
public class DayOfYear_ESTestTest24 extends DayOfYear_ESTest_scaffolding {

    /**
     * Tests that calling range() with a null TemporalField throws a NullPointerException,
     * as specified by the method's contract.
     */
    @Test(expected = NullPointerException.class)
    public void range_whenFieldIsNull_throwsNullPointerException() {
        // Arrange: Create an arbitrary DayOfYear instance. The specific value does not matter.
        DayOfYear dayOfYear = DayOfYear.of(150);

        // Act: Call the range() method with a null argument.
        // Assert: A NullPointerException is expected, as declared in the @Test annotation.
        dayOfYear.range(null);
    }
}