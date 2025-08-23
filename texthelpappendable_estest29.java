package org.apache.commons.cli.help;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendable_ESTestTest29 extends TextHelpAppendable_ESTest_scaffolding {

    /**
     * Verifies that writeColumnQueues throws an exception when the list of styles
     * is smaller than the list of column queues.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void writeColumnQueuesShouldThrowExceptionWhenStylesListIsShorterThanQueuesList() throws IOException {
        // Arrange: Create an appendable, a single column queue, and an empty list of styles.
        // A StringWriter is used to prevent test output from printing to the console.
        TextHelpAppendable helpAppendable = new TextHelpAppendable(new StringWriter());

        // A list containing one column of data (represented by a queue).
        List<Queue<String>> columnQueues = Collections.singletonList(new LinkedList<>());

        // An empty list of styles. The number of styles (0) is less than the number of columns (1).
        // A Vector is used to match the original test's expectation of an ArrayIndexOutOfBoundsException.
        List<TextStyle> emptyStyles = new Vector<>();

        // Act & Assert: Call the method and expect an exception.
        // The method should fail because it cannot find a style for the first column.
        helpAppendable.writeColumnQueues(columnQueues, emptyStyles);
    }
}