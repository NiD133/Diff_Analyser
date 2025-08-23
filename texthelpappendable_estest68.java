package org.apache.commons.cli.help;

import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import org.junit.Test;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Tests that appendList throws a NegativeArraySizeException if the left padding
     * is set to a negative value. This happens because the negative padding value is
     * used to calculate the size of an internal buffer.
     */
    @Test
    public void appendListShouldThrowExceptionForNegativeLeftPadding() {
        // Arrange: Create a TextHelpAppendable with a StringWriter to avoid console output
        // and set a negative left padding value, which is the condition under test.
        final StringWriter writer = new StringWriter();
        final TextHelpAppendable helpAppendable = new TextHelpAppendable(writer);
        helpAppendable.setLeftPad(-1);

        // Act & Assert: Verify that calling appendList throws the expected exception.
        // The use of assertThrows clearly states the expected outcome.
        assertThrows(NegativeArraySizeException.class, () -> {
            helpAppendable.appendList(true, Collections.singletonList("a list item"));
        });
    }

    /*
     * Note: The following is an alternative using JUnit 4's 'expected' parameter,
     * which is also a significant improvement over the original try-catch block.
     * This approach is more concise but less flexible than assertThrows if you
     * need to inspect the exception object itself.
     */
    @Test(expected = NegativeArraySizeException.class)
    public void appendListShouldThrowExceptionForNegativeLeftPadding_Junit4Style() throws IOException {
        // Arrange
        final TextHelpAppendable helpAppendable = new TextHelpAppendable(new StringWriter());
        helpAppendable.setLeftPad(-1);

        // Act
        helpAppendable.appendList(true, Collections.singletonList("a list item"));

        // Assert is handled by the @Test(expected=...) annotation
    }
}