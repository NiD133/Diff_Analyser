package org.apache.commons.cli.help;

import org.junit.Test;
import java.io.IOException;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Tests that attempting to write with a TextHelpAppendable instance that was
     * constructed with a null Appendable results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void appendHeaderShouldThrowNullPointerExceptionWhenConstructedWithNullAppendable() throws IOException {
        // Arrange: Create a TextHelpAppendable with a null Appendable.
        // Any method that attempts to write to the underlying output should fail.
        TextHelpAppendable helpFormatter = new TextHelpAppendable(null);

        // Act: Attempt to append a header. This action is expected to throw the exception.
        helpFormatter.appendHeader(1, "Test Header");

        // Assert: The test will pass if a NullPointerException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}