package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link Years} class.
 */
public class YearsTest {

    /**
     * Tests that isGreaterThan() returns true when a positive Years value is compared to null.
     * The method's contract specifies that a null comparison argument should be treated as zero.
     */
    @Test
    public void isGreaterThan_shouldReturnTrue_whenComparingPositiveYearsToNull() {
        // Arrange: A large positive Years value. According to the Javadoc, the null
        // 'other' value in the comparison is treated as Years.ZERO.
        Years positiveYears = Years.MAX_VALUE;

        // Act: Compare the positive Years value to null.
        boolean isGreater = positiveYears.isGreaterThan(null);

        // Assert: The result should be true, as MAX_VALUE is greater than zero.
        assertTrue("Years.MAX_VALUE should be considered greater than null.", isGreater);
    }
}