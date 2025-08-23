package org.apache.commons.cli.help;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Tests that calling {@link TextHelpAppendable#appendTable(TableDefinition)} does not
     * alter the default indent and left padding settings of the instance.
     */
    @Test
    public void appendTableShouldNotAlterDefaultIndentAndPadding() throws IOException {
        // Arrange: Create a TextHelpAppendable and a simple table definition.
        // Using a StringWriter avoids printing to System.out during tests.
        final TextHelpAppendable helpAppendable = new TextHelpAppendable(new StringWriter());

        final List<TextStyle> columnStyles = Collections.singletonList(TextStyle.DEFAULT);
        final List<String> headers = Collections.singletonList("Header");
        final Collection<List<String>> rows = Collections.singletonList(Collections.singletonList("Data"));
        final TableDefinition tableDefinition = TableDefinition.from("Title", columnStyles, headers, rows);

        // Act: Append the table to the output.
        helpAppendable.appendTable(tableDefinition);

        // Assert: Verify that the indent and padding properties remain at their default values.
        assertEquals("Indent should remain at default value",
            TextHelpAppendable.DEFAULT_INDENT, helpAppendable.getIndent());
        assertEquals("Left pad should remain at default value",
            TextHelpAppendable.DEFAULT_LEFT_PAD, helpAppendable.getLeftPad());
    }
}