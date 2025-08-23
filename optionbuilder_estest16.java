package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link OptionBuilder} class.
 *
 * Note: {@link OptionBuilder} is deprecated. These tests are maintained to verify
 * its legacy behavior.
 */
public class OptionBuilderTest {

    @Test
    public void testCreateOptionWithSingleArgument() {
        // Arrange: Configure the builder to create an option that requires one argument.
        // The OptionBuilder is a static builder that maintains state between calls.
        OptionBuilder.hasArg(true);

        // Act: Create the option with the short name 'S'.
        // The create() method also resets the builder's static state for the next use.
        Option option = OptionBuilder.create('S');

        // Assert: Verify that the created option has the expected properties.
        
        // The option's ID should correspond to its short name 'S'.
        // Using the character literal 'S' is more understandable than its ASCII value 83.
        assertEquals('S', option.getId());
        
        // The option should be configured to accept exactly one argument because hasArg(true) was called.
        assertEquals(1, option.getArgs());
    }
}