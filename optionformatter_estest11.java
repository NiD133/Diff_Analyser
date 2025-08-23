package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

/**
 * Tests for {@link OptionFormatter} to ensure its methods handle various inputs correctly.
 */
public class OptionFormatterTest {

    /**
     * Verifies that toSyntaxOption() throws a NullPointerException if the OptionFormatter
     * was created with a null Option. This is the expected behavior, as the method
     * needs to access the Option's properties to generate the syntax string.
     */
    @Test(expected = NullPointerException.class)
    public void toSyntaxOptionShouldThrowNullPointerExceptionWhenOptionIsNull() {
        // Arrange: Create an OptionFormatter from a null Option.
        // The static factory method allows this, but subsequent operations may fail.
        final OptionFormatter formatter = OptionFormatter.from((Option) null);

        // Act & Assert: Calling toSyntaxOption() should trigger a NullPointerException.
        // The exception is caught and verified by the @Test(expected=...) annotation.
        formatter.toSyntaxOption();
    }
}