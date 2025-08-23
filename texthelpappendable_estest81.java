package org.apache.commons.cli.help;

import org.junit.Test;
import java.io.StringWriter;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the initial state and default configuration of {@link TextHelpAppendable}.
 */
public class TextHelpAppendableDefaultsTest {

    /**
     * Verifies that a new TextHelpAppendable instance is initialized with the
     * correct default layout values for width, padding, and indentation.
     */
    @Test
    public void shouldInitializeWithDefaultLayoutValues() {
        // Arrange: Create a new TextHelpAppendable instance.
        // A StringWriter is a simple and standard Appendable implementation for testing.
        StringWriter writer = new StringWriter();
        TextHelpAppendable helpAppendable = new TextHelpAppendable(writer);

        // Act: No action is performed, as this test verifies the initial state
        // immediately after construction.

        // Assert: Check that the layout properties match the defined default constants.
        assertEquals("Default max width",
                TextHelpAppendable.DEFAULT_WIDTH, helpAppendable.getMaxWidth());
        assertEquals("Default left padding",
                TextHelpAppendable.DEFAULT_LEFT_PAD, helpAppendable.getLeftPad());
        assertEquals("Default indentation",
                TextHelpAppendable.DEFAULT_INDENT, helpAppendable.getIndent());
    }
}