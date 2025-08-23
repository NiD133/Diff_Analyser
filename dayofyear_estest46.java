package org.threeten.extra;

import org.junit.Test;

import java.time.DateTimeException;
import java.time.Year;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This test class contains tests for the DayOfYear class.
 * The original test class was likely auto-generated, hence the name DayOfYear_ESTestTest46.
 * A more conventional name would be DayOfYearTest.
 */
public class DayOfYearTest {

    /**
     * Tests that attempting to create a {@link Year} from a {@link DayOfYear} fails with a DateTimeException.
     * <p>
     * A {@code DayOfYear} is a partial temporal entity that only represents the day within a year (1-366).
     * It does not contain year information. Therefore, any attempt to extract a year from it should fail.
     */
    @Test
    public void yearFrom_whenSourceIsDayOfYear_throwsDateTimeException() {
        // Arrange: Create a DayOfYear instance. Using a fixed value makes the test deterministic.
        DayOfYear dayOfYear = DayOfYear.of(45);

        // Act & Assert: Verify that calling Year.from() throws the expected exception.
        try {
            Year.from(dayOfYear);
            fail("Expected a DateTimeException to be thrown, but no exception occurred.");
        } catch (DateTimeException e) {
            // Verify that the exception message is informative and confirms the cause.
            String expectedMessageFragment = "Unable to obtain Year from TemporalAccessor";
            assertTrue(
                "Exception message should explain why the conversion failed. Actual: " + e.getMessage(),
                e.getMessage().contains(expectedMessageFragment)
            );
        }
    }
}