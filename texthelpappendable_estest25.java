package org.apache.commons.cli.help;

import org.junit.Test;

import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Verifies that attempting to write to a read-only buffer throws a ReadOnlyBufferException.
     */
    @Test
    public void writeColumnQueues_whenUsingReadOnlyBuffer_throwsException() {
        // Arrange: Create a TextHelpAppendable backed by a read-only buffer.
        CharBuffer readOnlyBuffer = CharBuffer.wrap("");
        TextHelpAppendable textHelp = new TextHelpAppendable(readOnlyBuffer);

        // To trigger a write attempt, we must provide non-empty column data.
        List<String> columnData = Collections.singletonList("some data");
        List<TextStyle> styles = Collections.singletonList(TextStyle.builder().build());
        List<Queue<String>> columnQueues = textHelp.makeColumnQueues(columnData, styles);

        // Act & Assert: An attempt to write should fail with a ReadOnlyBufferException.
        assertThrows(ReadOnlyBufferException.class, () -> {
            textHelp.writeColumnQueues(columnQueues, styles);
        });
    }
}