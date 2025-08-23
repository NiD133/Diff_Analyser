package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link OptionFormatter} class.
 * Note: The original class name 'OptionFormatter_ESTestTest26' and its scaffolding
 * were artifacts of a test generation tool and have been simplified for clarity.
 */
public class OptionFormatterTest {

    /**
     * Verifies that the isRequired() method correctly reports an option as not required
     * when the underlying Option object has not been explicitly marked as required.
     */
    @Test
    public void isRequiredShouldReturnFalseWhenOptionIsNotRequired() {
        // Arrange: Create a standard option. By default, an Option is not required.
        final Option nonRequiredOption = new Option("f", "file", true, "The file to process");
        final OptionFormatter formatter = OptionFormatter.from(nonRequiredOption);

        // Act & Assert: The isRequired() method should reflect the Option's default state.
        assertFalse("The formatter should report the option is not required by default.",
                formatter.isRequired());
    }
}