package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

/**
 * Tests for {@link OptionFormatter}.
 */
public class OptionFormatterTest {

    /**
     * Verifies that calling getLongOpt() on a formatter created with a null Option
     * results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void getLongOptShouldThrowNullPointerExceptionWhenOptionIsNull() {
        // Arrange: Create a formatter from a null Option.
        // The OptionFormatter instance is successfully created, but its internal 'option' field is null.
        final OptionFormatter formatter = OptionFormatter.from((Option) null);

        // Act: Attempt to access the long option from the null internal Option.
        // This is expected to throw the exception.
        formatter.getLongOpt();

        // Assert: The test is successful if a NullPointerException is thrown,
        // as declared by the @Test(expected=...) annotation.
    }
}