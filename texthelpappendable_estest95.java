package org.apache.commons.cli.help;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TextHelpAppendable_ESTestTest95 extends TextHelpAppendable_ESTest_scaffolding {

    /**
     * Tests that appendList() throws an IllegalArgumentException if the max width
     * has been set to a negative value.
     */
    @Test
    public void appendListShouldThrowIllegalArgumentExceptionWhenMaxWidthIsNegative() throws IOException {
        // Arrange: Create a formatter with a negative max width.
        // Using a StringWriter avoids printing to System.out during the test.
        StringWriter stringWriter = new StringWriter();
        TextHelpAppendable helpFormatter = new TextHelpAppendable(stringWriter);
        helpFormatter.setMaxWidth(-1);
        List<CharSequence> listItems = Collections.singletonList("A list item.");

        // Act & Assert: Verify that appending a list throws an exception with the correct message.
        try {
            helpFormatter.appendList(true, listItems);
            fail("Expected an IllegalArgumentException because the width is negative.");
        } catch (IllegalArgumentException e) {
            // Verify the exception message is as expected.
            assertEquals("Width must be greater than 0", e.getMessage());
        }
    }
}