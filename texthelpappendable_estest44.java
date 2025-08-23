package org.apache.commons.cli.help;

import static org.junit.Assert.assertThrows;

import java.io.StringWriter;
import org.junit.Test;

/**
 * Unit tests for the {@link TextHelpAppendable} class.
 */
public class TextHelpAppendableTest {

    /**
     * Verifies that makeColumnQueue() throws a NullPointerException when the input
     * CharSequence is null, as this is an invalid argument.
     */
    @Test
    public void makeColumnQueueShouldThrowNullPointerExceptionWhenTextIsNull() {
        // Arrange: Create a formatter instance and a default style.
        // A StringWriter is used to prevent the test from writing to System.out.
        TextHelpAppendable helpFormatter = new TextHelpAppendable(new StringWriter());
        TextStyle defaultStyle = TextStyle.DEFAULT;

        // Act & Assert: Expect a NullPointerException when calling the method with null text.
        assertThrows(NullPointerException.class, () -> {
            helpFormatter.makeColumnQueue(null, defaultStyle);
        });
    }
}