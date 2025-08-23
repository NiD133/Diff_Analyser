package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

/**
 * Tests for {@link OptionFormatter}.
 */
public class OptionFormatterTest {

    /**
     * Tests that attempting to get the argument name from a formatter
     * initialized with a null Option results in a NullPointerException.
     * This is because the formatter will try to access methods on the null option object.
     */
    @Test(expected = NullPointerException.class)
    public void getArgNameShouldThrowNullPointerExceptionWhenCreatedWithNullOption() {
        // Arrange: Create an OptionFormatter from a null Option.
        // The factory method allows this, but subsequent operations are expected to fail.
        OptionFormatter formatter = OptionFormatter.from((Option) null);

        // Act & Assert: Calling getArgName() should throw a NullPointerException
        // because it attempts to dereference the null internal option.
        formatter.getArgName();
    }
}