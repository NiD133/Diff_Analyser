package org.joda.time.format;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link PeriodFormatter}.
 */
public class PeriodFormatterTest {

    /**
     * Tests that calling parsePeriod on a formatter without a parser
     * throws an UnsupportedOperationException.
     */
    @Test
    public void parsePeriod_shouldThrowUnsupportedOperationException_whenParsingIsNotSupported() {
        // Arrange: Create a formatter that is configured without a parser.
        // Such a formatter is not capable of parsing strings.
        PeriodFormatter formatter = new PeriodFormatter(null, null);

        // Act & Assert
        try {
            formatter.parsePeriod("any string");
            fail("Expected an UnsupportedOperationException to be thrown.");
        } catch (UnsupportedOperationException e) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals("Parsing not supported", e.getMessage());
        }
    }
}