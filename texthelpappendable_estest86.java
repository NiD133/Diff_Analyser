package org.apache.commons.cli.help;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * Tests for {@link TextHelpAppendable} focusing on table rendering behavior.
 */
public class TextHelpAppendableTableTest {

    /**
     * Verifies that appendTable() throws an IllegalArgumentException if the configured
     * width is negative. A negative width can be achieved by resizing the help
     * appendable's style with a negative fraction.
     */
    @Test
    public void testAppendTableWithNegativeWidthThrowsIllegalArgumentException() throws IOException {
        // Arrange: Create a help appendable and set its width to a negative value.
        TextHelpAppendable helpAppendable = TextHelpAppendable.systemOut();
        TextStyle.Builder styleBuilder = helpAppendable.getTextStyleBuilder();

        // The internal resize logic is (int)(originalWidth * fraction).
        // Using a negative fraction results in a negative width for the style.
        helpAppendable.resize(styleBuilder, -1.0);

        // Create a minimal table definition required to call the method under test.
        List<TextStyle> columnStyles = Collections.singletonList(TextStyle.DEFAULT);
        List<String> columnHeaders = Collections.singletonList("Header");
        Collection<List<String>> tableData = Collections.emptyList();
        TableDefinition tableDefinition = TableDefinition.from("Title", columnStyles, columnHeaders, tableData);

        // Act & Assert: Expect an IllegalArgumentException with a specific message.
        try {
            helpAppendable.appendTable(tableDefinition);
            fail("Expected an IllegalArgumentException because the width is negative.");
        } catch (final IllegalArgumentException e) {
            assertEquals("Width must be greater than 0", e.getMessage());
        }
    }
}