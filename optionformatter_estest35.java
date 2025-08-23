package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link OptionFormatter.Builder}.
 */
public class OptionFormatterBuilderTest {

    @Test
    public void toArgNameShouldWrapArgumentWithDefaultDelimiters() {
        // Arrange: Create a builder, which uses default settings.
        // The default argument name delimiters are '<' and '>'.
        OptionFormatter.Builder builder = OptionFormatter.builder();
        final String argName = "file";

        // Act: Format the argument name.
        String formattedArgName = builder.toArgName(argName);

        // Assert: Verify the argument name is correctly wrapped.
        assertEquals("The argument name should be enclosed in default angle brackets.",
                "<file>", formattedArgName);
    }
}