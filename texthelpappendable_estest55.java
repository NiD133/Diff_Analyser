package org.apache.commons.cli.help;

import org.junit.Test;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Tests for {@link TextHelpAppendable} focusing on buffer handling.
 */
public class TextHelpAppendable_ESTestTest55 extends TextHelpAppendable_ESTest_scaffolding {

    /**
     * Verifies that appending a table to a TextHelpAppendable backed by a buffer
     * that is too small results in a BufferOverflowException.
     */
    @Test(timeout = 4000, expected = BufferOverflowException.class)
    public void appendTableShouldThrowBufferOverflowWhenBufferIsTooSmall() throws IOException {
        // Arrange: Create an Appendable with a buffer that is too small to hold the table's title.
        final int bufferCapacity = 1;
        CharBuffer tinyBuffer = CharBuffer.allocate(bufferCapacity);
        TextHelpAppendable helpAppendable = new TextHelpAppendable(tinyBuffer);

        // Arrange: Create a table definition with a title that is larger than the buffer's capacity.
        // The table's content (styles, headers, rows) can be empty for this specific test.
        TableDefinition tableDefinition = TableDefinition.from(
                "Table Title", // This title is longer than the buffer's capacity of 1.
                new LinkedList<>(),
                new ArrayList<>(),
                new ArrayDeque<>());

        // Act: Attempt to append the table. This should immediately try to write the title
        // and cause the underlying small buffer to overflow.
        helpAppendable.appendTable(tableDefinition);

        // Assert: The test expects a BufferOverflowException, which is handled by the
        // @Test(expected=...) annotation.
    }
}