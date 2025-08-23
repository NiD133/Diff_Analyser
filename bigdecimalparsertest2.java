package com.fasterxml.jackson.core.io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Contains tests for the error-handling behavior of {@link BigDecimalParser},
 * particularly for very long inputs.
 */
public class BigDecimalParserTestTest2 extends com.fasterxml.jackson.core.JUnit5TestBase {

    /**
     * Verifies that parsing a very long, invalid string with {@code parseWithFastParser}
     * throws a {@code NumberFormatException}.
     * <p>
     * It also checks that the exception message correctly indicates that the reported
     * input value has been truncated. This is a crucial feature to prevent overly
     * long error messages from consuming excessive memory or cluttering logs.
     * <p>
     * The SUT's documentation states it expects pre-validated input; this test
     * verifies the robustness of its error reporting when that contract is violated.
     */
    @Test
    void parseWithFastParser_withLongInvalidString_throwsNFEWithTruncatedMessage() {
        // ARRANGE
        // Create an invalid numeric string that is longer than the parser's internal
        // reporting limit (BigDecimalParser.MAX_CHARS_TO_REPORT is 1000).
        final int longStringLength = 1500;
        final String veryLongInvalidString = "A".repeat(longStringLength);

        // ACT & ASSERT
        // Expect a NumberFormatException to be thrown due to the invalid input.
        NumberFormatException exception = assertThrows(NumberFormatException.class, () -> {
            BigDecimalParser.parseWithFastParser(veryLongInvalidString);
        });

        // Assert that the exception message is formatted as expected for long inputs.
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.startsWith("Value \"AAAAA"),
                "Exception message should start with a preview of the invalid value.");
        assertTrue(actualMessage.contains("truncated"),
                "Exception message should explicitly state that the reported value was truncated.");
    }
}