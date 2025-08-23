package org.apache.commons.cli.help;

import org.junit.Test;
import java.io.IOException;

/**
 * Tests for the {@link TextHelpAppendable} class, focusing on exception handling.
 */
public class TextHelpAppendable_ESTestTest36 extends TextHelpAppendable_ESTest_scaffolding {

    /**
     * Verifies that calling printWrapped() with a null string argument
     * correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void printWrappedWithNullStringShouldThrowNullPointerException() throws IOException {
        // Arrange: Create an instance of the class under test.
        // Using systemOut() is safe here because the method will throw an exception
        // before any attempt is made to write to the output stream.
        final TextHelpAppendable helpAppendable = TextHelpAppendable.systemOut();

        // Act: Call the method with a null argument.
        // The @Test(expected) annotation will handle the assertion.
        helpAppendable.printWrapped(null);
    }
}