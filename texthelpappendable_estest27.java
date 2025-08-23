package org.apache.commons.cli.help;

import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

/**
 * Contains a test case for the {@link TextHelpAppendable} class.
 */
public class TextHelpAppendable_ESTestTest27 extends TextHelpAppendable_ESTest_scaffolding {

    /**
     * Tests that {@link TextHelpAppendable#writeColumnQueues(List, List)} throws a
     * {@link NegativeArraySizeException} if the left padding has been set to a negative value.
     * The exception is expected because an underlying utility method will attempt to create
     * a padding string, which involves allocating an array with a negative size.
     *
     * @throws IOException if an I/O error occurs (not expected in this test case).
     */
    @Test(expected = NegativeArraySizeException.class)
    public void writeColumnQueuesShouldThrowExceptionForNegativeLeftPadding() throws IOException {
        // Arrange: Create a TextHelpAppendable and configure it with an invalid state.
        final TextHelpAppendable helpAppendable = TextHelpAppendable.systemOut();
        final int negativeLeftPad = -2243;
        helpAppendable.setLeftPad(negativeLeftPad);

        // The actual column data and styles are not relevant, as the exception is
        // triggered by the negative padding value. Empty lists are sufficient.
        final List<String> columnData = Collections.emptyList();
        final List<TextStyle> styles = Collections.emptyList();
        final List<Queue<String>> columnQueues = helpAppendable.makeColumnQueues(columnData, styles);

        // Act: Attempt to write the columns. This action is expected to trigger the exception.
        helpAppendable.writeColumnQueues(columnQueues, styles);

        // Assert: The test is expected to throw a NegativeArraySizeException.
        // This is handled by the @Test(expected = ...) annotation. The test will
        // fail automatically if this exception is not thrown.
    }
}