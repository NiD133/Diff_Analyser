package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link OptionFormatter}.
 */
public class OptionFormatterTest {

    /**
     * Verifies that the toOptional() method correctly wraps a string with the
     * default delimiters, which are '[' and ']'.
     */
    @Test
    public void toOptionalShouldWrapTextWithDefaultDelimiters() {
        // Arrange: Create a formatter using the static factory method.
        // Passing null ensures that the formatter is initialized with default settings.
        OptionFormatter formatter = OptionFormatter.from((Option) null);
        final String textToWrap = ", ";
        final String expectedWrappedText = "[, ]";

        // Act: Call the method under test.
        final String actualWrappedText = formatter.toOptional(textToWrap);

        // Assert: Check if the output matches the expected wrapped string.
        assertEquals(expectedWrappedText, actualWrappedText);
    }
}