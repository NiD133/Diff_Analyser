package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

/**
 * Tests for the {@link OptionFormatter} class, focusing on edge cases.
 */
public class OptionFormatterTest {

    /**
     * Verifies that calling isRequired() on an OptionFormatter created with a null Option
     * results in a NullPointerException. This is expected because the formatter
     * attempts to delegate the call to the null Option object.
     */
    @Test(expected = NullPointerException.class)
    public void isRequiredShouldThrowNullPointerExceptionWhenCreatedWithNullOption() {
        // Given an OptionFormatter created with a null Option
        OptionFormatter formatter = OptionFormatter.from(null);

        // When isRequired() is called, a NullPointerException is expected.
        formatter.isRequired();
    }
}