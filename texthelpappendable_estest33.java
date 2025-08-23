package org.apache.commons.cli.help;

import org.junit.Test;

/**
 * Tests for {@link TextHelpAppendable} to ensure its methods handle various inputs correctly.
 */
public class TextHelpAppendableTest {

    /**
     * Verifies that the printWrapped method throws a NullPointerException
     * when the input text is null, as this is an invalid argument.
     */
    @Test(expected = NullPointerException.class)
    public void printWrappedShouldThrowNullPointerExceptionForNullText() throws Exception {
        // Arrange: Create an instance of the class under test and a style object.
        // The actual Appendable implementation (System.out) is not relevant here,
        // as the null check should happen before any writing occurs.
        final TextHelpAppendable helpAppendable = TextHelpAppendable.systemOut();
        final TextStyle defaultStyle = TextStyle.DEFAULT;

        // Act: Call the method with a null string.
        // This action is expected to throw a NullPointerException.
        helpAppendable.printWrapped(null, defaultStyle);

        // Assert: The test passes if the expected NullPointerException is thrown.
        // This is handled declaratively by the @Test(expected=...) annotation.
    }
}