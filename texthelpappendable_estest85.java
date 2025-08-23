package org.apache.commons.cli.help;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link TextHelpAppendable} class.
 */
public class TextHelpAppendableTest {

    /**
     * Tests that calling {@link TextHelpAppendable#appendHeader(int, CharSequence)} with a null
     * text is handled gracefully, without throwing an exception or altering the object's
     * configuration settings.
     */
    @Test
    public void appendHeaderWithNullTextShouldNotAlterConfiguration() throws IOException {
        // Arrange: Create a TextHelpAppendable instance.
        // The constructor sets the default values for width, padding, and indentation.
        TextHelpAppendable helpAppendable = new TextHelpAppendable(null);

        // Act: Call the method under test with a null CharSequence.
        // The level '7' is an arbitrary value used simply to call the method.
        helpAppendable.appendHeader(7, null);

        // Assert: Verify that the configuration properties remain unchanged and are still set
        // to their default values, confirming the null input had no side effects on state.
        assertEquals("Max width should remain at the default value",
                TextHelpAppendable.DEFAULT_WIDTH, helpAppendable.getMaxWidth());
        assertEquals("Indent should remain at the default value",
                TextHelpAppendable.DEFAULT_INDENT, helpAppendable.getIndent());
        assertEquals("Left pad should remain at the default value",
                TextHelpAppendable.DEFAULT_LEFT_PAD, helpAppendable.getLeftPad());
    }
}