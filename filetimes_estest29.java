package org.apache.commons.io.file.attribute;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link FileTimes}.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#ntfsTimeToInstant(BigDecimal)} throws an
     * ArithmeticException when given a BigDecimal with a fractional part.
     * The underlying implementation expects a whole number representing
     * 100-nanosecond intervals and cannot perform rounding.
     */
    @Test
    public void ntfsTimeToInstant_shouldThrowArithmeticException_whenNtfsTimeHasFractionalPart() {
        // Arrange: An NTFS time represented as a BigDecimal with a non-zero fractional part.
        final BigDecimal ntfsTimeWithFractionalPart = new BigDecimal("82.95");

        // Act & Assert
        try {
            FileTimes.ntfsTimeToInstant(ntfsTimeWithFractionalPart);
            fail("Expected an ArithmeticException because the input BigDecimal requires rounding.");
        } catch (final ArithmeticException e) {
            // Assert: Verify the exception message to confirm the cause.
            // This specific message is thrown by BigDecimal when an exact conversion to a long is not possible.
            assertEquals("Rounding necessary", e.getMessage());
        }
    }
}