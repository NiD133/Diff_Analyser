package org.apache.commons.cli.help;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Verifies that calling appendParagraph with an empty CharSequence does not
     * alter the default formatting properties (width, padding, indent) of a
     * TextHelpAppendable instance created via the systemOut() factory method.
     */
    @Test
    public void appendEmptyParagraphShouldNotAlterDefaultFormatting() throws IOException {
        // Arrange: Create a TextHelpAppendable instance with default settings.
        final TextHelpAppendable helpAppendable = TextHelpAppendable.systemOut();
        final CharSequence emptyParagraph = "";

        // Act: Append an empty paragraph. This action should not have any side
        // effects on the object's formatting configuration.
        helpAppendable.appendParagraph(emptyParagraph);

        // Assert: Confirm that the formatting properties remain unchanged and are
        // still set to their documented default values.
        assertEquals("Max width should remain at the default value",
                TextHelpAppendable.DEFAULT_WIDTH, helpAppendable.getMaxWidth());
        assertEquals("Left pad should remain at the default value",
                TextHelpAppendable.DEFAULT_LEFT_PAD, helpAppendable.getLeftPad());
        assertEquals("Indent should remain at the default value",
                TextHelpAppendable.DEFAULT_INDENT, helpAppendable.getIndent());
    }
}