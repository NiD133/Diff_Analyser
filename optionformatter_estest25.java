package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link OptionFormatter}.
 * This class contains the test case from the original OptionFormatter_ESTestTest25.
 */
public class OptionFormatter_ESTestTest25 extends OptionFormatter_ESTest_scaffolding {

    /**
     * Verifies that the isRequired() method of OptionFormatter correctly reflects
     * the 'required' status of the underlying Option object.
     */
    @Test(timeout = 4000)
    public void testIsRequiredReturnsFalseWhenOptionIsNotRequired() {
        // Arrange: Create an option that is not required.
        // By default, an Option is not required unless explicitly configured.
        Option nonRequiredOption = new Option("o", "option", true, "An example option.");

        // Act: Create an OptionFormatter for the non-required option.
        // Using the static 'from' method is a simple way to get a formatter with default settings.
        OptionFormatter formatter = OptionFormatter.from(nonRequiredOption);

        // Assert: The formatter should report that the option is not required,
        // correctly delegating the check to the wrapped Option instance.
        assertFalse("isRequired() should return false for a non-required option.", formatter.isRequired());
    }
}