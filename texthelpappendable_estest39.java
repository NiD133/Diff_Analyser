package org.apache.commons.cli.help;

import org.apache.commons.cli.help.TextStyle;
import org.apache.commons.cli.help.TextHelpAppendable;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Tests for {@link TextHelpAppendable}, focusing on exception handling.
 */
public class TextHelpAppendable_ESTestTest39 extends TextHelpAppendable_ESTest_scaffolding {

    /**
     * Tests that calling {@code makeColumnQueues} with a null list for column data
     * results in a {@code NullPointerException}.
     */
    @Test(expected = NullPointerException.class)
    public void makeColumnQueuesShouldThrowNullPointerExceptionWhenColumnDataIsNull() {
        // Arrange: Create a TextHelpAppendable instance and a list for styles.
        TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();
        List<TextStyle> styles = new LinkedList<>(); // The list of styles can be empty for this test.

        // Act & Assert: Call the method with a null argument for columnData.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        textHelpAppendable.makeColumnQueues(null, styles);
    }
}