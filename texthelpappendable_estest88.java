package org.apache.commons.cli.help;

import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test for {@link TextHelpAppendable}.
 * This test was improved to clarify its intent and improve maintainability.
 */
public class TextHelpAppendable_ESTestTest88 extends TextHelpAppendable_ESTest_scaffolding {

    /**
     * Tests that calling appendTable does not alter the default configuration
     * values for maximum width and indent.
     */
    @Test(timeout = 4000)
    public void appendTable_shouldNotChangeDefaultWidthAndIndent() throws IOException {
        // Arrange: Create a TextHelpAppendable instance.
        // The actual output destination is not relevant for this test.
        TextHelpAppendable helpAppendable = TextHelpAppendable.systemOut();

        // Pre-condition check: Verify the instance starts with the expected default values.
        assertEquals("Pre-condition: Max width should be the default",
                TextHelpAppendable.DEFAULT_WIDTH, helpAppendable.getMaxWidth());
        assertEquals("Pre-condition: Indent should be the default",
                TextHelpAppendable.DEFAULT_INDENT, helpAppendable.getIndent());

        // Create a minimal but valid TableDefinition to pass to the method.
        List<TextStyle> columnStyles = Collections.singletonList(TextStyle.DEFAULT);
        List<String> headers = Collections.singletonList("Header");
        Collection<List<String>> rows = Collections.singletonList(Collections.singletonList("Data"));
        TableDefinition tableDefinition = TableDefinition.from("Title", columnStyles, headers, rows);

        // Act: Call the method under test.
        helpAppendable.appendTable(tableDefinition);

        // Assert: Verify that the properties have not been changed by the method call.
        assertEquals("Max width should remain default after appending a table",
                TextHelpAppendable.DEFAULT_WIDTH, helpAppendable.getMaxWidth());
        assertEquals("Indent should remain default after appending a table",
                TextHelpAppendable.DEFAULT_INDENT, helpAppendable.getIndent());
    }
}