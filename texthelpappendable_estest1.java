package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;

/**
 * Tests for {@link TextHelpAppendable} to verify list formatting.
 */
public class TextHelpAppendableTest {

    /**
     * Verifies that an ordered list is formatted with numbered items and the correct indentation.
     * This test ensures that appendList correctly processes a collection of strings,
     * prefixes each with a number, and applies the default indentation for lists.
     */
    @Test
    public void appendOrderedListShouldFormatItemsWithNumbersAndIndentation() throws IOException {
        // Arrange: Create a StringWriter to capture the output and a TextHelpAppendable instance.
        StringWriter stringWriter = new StringWriter();
        TextHelpAppendable helpAppendable = new TextHelpAppendable(stringWriter);
        Collection<CharSequence> listItems = Arrays.asList("First item", "Second item");

        // Act: Call the method under test to append the items as an ordered list.
        helpAppendable.appendList(true, listItems);

        // Assert: Verify that the captured output matches the expected formatted string.
        // The expected format is based on the default list indentation (DEFAULT_LIST_INDENT = 7).
        final String EOL = System.lineSeparator();
        final String expectedOutput = "       1. First item" + EOL +
                                      "       2. Second item" + EOL;

        assertEquals(expectedOutput, stringWriter.toString());
    }
}