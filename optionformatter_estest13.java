package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

/**
 * Tests for {@link OptionFormatter}.
 */
public class OptionFormatterTest {

    /**
     * Tests that getOpt() throws a NullPointerException if the OptionFormatter
     * was created with a null Option. This is expected because the formatter
     * needs a valid Option object to retrieve information from.
     */
    @Test(expected = NullPointerException.class)
    public void getOptShouldThrowNullPointerExceptionWhenCreatedWithNullOption() {
        // Arrange: Create a formatter from a null Option.
        // The 'from' method itself does not throw, but creates an invalid state.
        OptionFormatter formatter = OptionFormatter.from((Option) null);

        // Act: Calling getOpt() on this formatter should trigger the NPE.
        formatter.getOpt();

        // Assert: The test passes if a NullPointerException is thrown,
        // which is handled by the @Test(expected) annotation.
    }
}