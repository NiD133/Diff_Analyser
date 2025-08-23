package org.apache.commons.cli.help;

import org.junit.Test;

import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    @Test
    public void appendTitle_whenConstructedWithNullAppendable_shouldThrowNullPointerException() {
        // Arrange: Create a TextHelpAppendable with a null output Appendable.
        // This is the specific state we want to test.
        final TextHelpAppendable helpAppendable = new TextHelpAppendable(null);
        final String title = "Test Title";

        // Act & Assert: Verify that attempting to write to the null Appendable
        // results in a NullPointerException.
        assertThrows(NullPointerException.class, () -> {
            helpAppendable.appendTitle(title);
        });
    }
}