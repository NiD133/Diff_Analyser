package org.apache.commons.cli.help;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Tests for {@link TextHelpAppendable}.
 * This test class focuses on edge cases related to table rendering.
 */
public class TextHelpAppendable_ESTestTest89 extends TextHelpAppendable_ESTest_scaffolding {

    /**
     * Tests that {@link TextHelpAppendable#appendTable(TableDefinition)} throws
     * an {@link IndexOutOfBoundsException} when the {@link TableDefinition}
     * specifies more column styles than it has headers. This indicates an
     * inconsistent table structure that the method should not handle.
     */
    @Test(timeout = 4000, expected = IndexOutOfBoundsException.class)
    public void testAppendTableWithMismatchedStylesAndHeadersThrowsException() throws IOException {
        // Arrange: Create a TextHelpAppendable that writes to a buffer instead of System.out
        // to avoid polluting console output during tests.
        final StringWriter writer = new StringWriter();
        final TextHelpAppendable helpAppendable = new TextHelpAppendable(writer);

        // Arrange: Define a table with one column style but zero headers. This mismatch
        // is the condition under test.
        final List<TextStyle> columnStyles = Collections.singletonList(TextStyle.DEFAULT);
        final List<String> headers = Collections.emptyList();
        final Collection<List<String>> tableRows = new PriorityQueue<>();
        final TableDefinition tableDefinition = TableDefinition.from("Mismatched Table", columnStyles, headers, tableRows);

        // Act: Attempt to append the malformed table. This call is expected to throw
        // an IndexOutOfBoundsException because the implementation will try to access
        // headers.get(0) when the headers list is empty.
        helpAppendable.appendTable(tableDefinition);

        // Assert: The exception is verified by the @Test(expected=...) annotation,
        // so no explicit assertion is needed here.
    }
}