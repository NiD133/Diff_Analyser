package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test case for the OptionBuilder class.
 * This test focuses on creating an option configured to accept an unlimited number of arguments.
 */
public class OptionBuilderTest {

    @Test
    public void testCreateOptionWithUnlimitedArguments() {
        // Note: OptionBuilder uses static fields for configuration. This is a design
        // flaw that can cause tests to interfere with each other. In a full test suite,
        // a reset mechanism would be needed before each test.

        // Arrange: Configure the builder for an option that can have unlimited arguments.
        // A long option must be set for the create() method to succeed when no short option is provided.
        OptionBuilder.withLongOpt("unlimited-args");
        OptionBuilder.hasArgs();

        // Act: Create the Option instance based on the static configuration.
        Option option = OptionBuilder.create();

        // Assert: Verify that the created option is correctly configured for unlimited arguments.
        // The constant Option.UNLIMITED_VALUES is used instead of the magic number -2.
        assertEquals("The number of arguments should be unlimited.", Option.UNLIMITED_VALUES, option.getArgs());
    }
}