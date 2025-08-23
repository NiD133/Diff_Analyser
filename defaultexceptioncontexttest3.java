package org.apache.commons.lang3.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DefaultExceptionContext}.
 * This class focuses on scenarios involving the formatting of exception messages.
 */
public class DefaultExceptionContextTest extends AbstractExceptionContextTest<DefaultExceptionContext> {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        exceptionContext = new DefaultExceptionContext();
        super.setUp();
    }

    /**
     * Tests that getFormattedExceptionMessage correctly includes context entries
     * where the value is null, formatting them as 'label=null'.
     */
    @Test
    void getFormattedExceptionMessageShouldCorrectlyFormatNullValues() {
        // Arrange
        final String firstLabel = "Error Code";
        final String secondLabel = "Request ID";
        exceptionContext.addContextValue(firstLabel, null);
        exceptionContext.addContextValue(secondLabel, null);

        // Based on the implementation, the format uses '\n' as a line separator.
        final String lineSeparator = "\n";
        final String expectedMessage = TEST_MESSAGE + lineSeparator +
            "Exception Context:" + lineSeparator +
            "\t[1:" + firstLabel + "=null]" + lineSeparator +
            "\t[2:" + secondLabel + "=null]" + lineSeparator +
            "---------------------------------";

        // Act
        final String actualMessage = exceptionContext.getFormattedExceptionMessage(TEST_MESSAGE);

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }
}