package org.threeten.extra;

import org.junit.Test;

/**
 * This test class contains tests for the {@link DayOfYear} class.
 */
public class DayOfYear_ESTestTest31 {

    /**
     * Tests that calling the query() method with a null argument
     * correctly throws a NullPointerException.
     *
     * The {@link java.time.temporal.TemporalAccessor#query(java.time.temporal.TemporalQuery)}
     * contract implies that a null query object is not permitted.
     */
    @Test(expected = NullPointerException.class)
    public void query_withNullArgument_throwsNullPointerException() {
        // Arrange: Create an arbitrary DayOfYear instance. The specific day (e.g., 150)
        // does not affect the outcome of this test.
        DayOfYear dayOfYear = DayOfYear.of(150);

        // Act: Call the query() method with a null argument.
        dayOfYear.query(null);

        // Assert: The test is expected to throw a NullPointerException.
        // This is handled by the `expected` attribute of the @Test annotation.
    }
}