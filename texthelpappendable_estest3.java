package org.apache.commons.cli.help;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link TextHelpAppendable}.
 * This class contains the refactored test case.
 */
public class TextHelpAppendableRefactoredTest {

    /**
     * Tests that appending a table does not alter the default formatting settings
     * (indentation and max width) of the TextHelpAppendable instance.
     */
    @Test
    public void appendTableShouldNotAlterDefaultFormattingSettings() throws IOException {
        // Arrange: Set up the TextHelpAppendable and a simple table definition.
        // Use a StringWriter to prevent console output and allow for potential output assertions.
        StringWriter writer = new StringWriter();
        TextHelpAppendable helpAppendable = new TextHelpAppendable(writer);

        // Define a minimal table. The content is not important for this test.
        List<TextStyle> styles = Collections.singletonList(TextStyle.DEFAULT);
        List<String> headers = Collections.singletonList("Header");
        Collection<List<String>> rows = Collections.singletonList(Collections.singletonList("Cell Data"));

        // Assuming a TableDefinition factory method with the signature:
        // from(title, styles, headers, rows) as implied by the original test.
        TableDefinition tableDefinition = TableDefinition.from("Table Title", styles, headers, rows);

        // Act: Call the method under test.
        helpAppendable.appendTable(tableDefinition);

        // Assert: Verify that the default settings remain unchanged.
        assertEquals("Default indent should be unchanged after appending a table.",
            TextHelpAppendable.DEFAULT_INDENT, helpAppendable.getIndent());

        assertEquals("Default max width should be unchanged after appending a table.",
            TextHelpAppendable.DEFAULT_WIDTH, helpAppendable.getMaxWidth());
    }
}