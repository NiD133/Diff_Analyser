package org.apache.commons.cli.help;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Test suite for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Tests that writeColumnQueues throws an IndexOutOfBoundsException when the list of styles
     * is smaller than the list of column queues, as it cannot retrieve a style for each queue.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void writeColumnQueuesShouldThrowExceptionWhenStylesListIsSmallerThanQueuesList() throws IOException {
        // Arrange
        // An instance of the class under test. Using systemOut() is sufficient as no output
        // is generated before the exception is thrown.
        TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();

        // Create a list containing a single column queue.
        List<Queue<String>> columnQueues = new ArrayList<>();
        columnQueues.add(new LinkedList<>());

        // Create an empty list of styles. This list is shorter than the columnQueues list.
        List<TextStyle> styles = Collections.emptyList();

        // Act & Assert
        // This call is expected to throw an IndexOutOfBoundsException because the method will
        // attempt to access styles.get(0) when the list size is 0.
        textHelpAppendable.writeColumnQueues(columnQueues, styles);
    }
}