package org.apache.commons.cli.help;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Test;

/**
 * Tests for the {@link TextHelpAppendable} class, focusing on table rendering logic.
 */
public class TextHelpAppendableTest {

    /**
     * Verifies that appendTable throws an ArrayIndexOutOfBoundsException when the
     * table definition provides more styles than it has columns. This scenario
     * represents an invalid state where the formatting rules do not match the
     * table structure.
     *
     * @throws IOException if an I/O error occurs (not expected in this test).
     */
    @Test
    public void appendTableWithMoreStylesThanColumnsShouldThrowException() throws IOException {
        // Arrange
        // Use a StringWriter to prevent test output from polluting the console.
        final TextHelpAppendable helpAppendable = new TextHelpAppendable(new StringWriter());

        // Define a table with one style but zero columns (headers). This mismatch
        // is expected to cause an error when the appendable tries to access the
        // non-existent column header corresponding to the provided style.
        final List<TextStyle> columnStyles = Collections.singletonList(TextStyle.DEFAULT);
        final List<String> columnHeaders = Collections.emptyList();
        final Set<List<String>> tableRows = Collections.emptySet();
        final TableDefinition invalidTable = TableDefinition.from("Invalid Table", columnStyles, columnHeaders, tableRows);

        // Act & Assert
        try {
            helpAppendable.appendTable(invalidTable);
            fail("Expected an ArrayIndexOutOfBoundsException due to the mismatch between styles and columns.");
        } catch (final ArrayIndexOutOfBoundsException e) {
            // This exception is expected. The test passes if this block is reached.
            // The implementation attempts to access a column header that does not exist.
        }
    }
}