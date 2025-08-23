package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link OptionBuilder}.
 *
 * Note: {@link OptionBuilder} is a static builder, which is a challenging design for testing
 * as state can leak between tests. In a complete test suite, it would be crucial to reset
 * the builder's state before each test, for example, in a method annotated with @Before.
 */
public class OptionBuilderTest {

    @Test
    public void buildOptionWithDefaultValueSeparator() {
        // Arrange
        // A long option is required for the create() method to succeed.
        OptionBuilder.withLongOpt("my-option");

        // Act
        // Configure the option to use the default value separator and then create it.
        Option option = OptionBuilder.withValueSeparator().create();

        // Assert
        // Verify that the option was created with the correct default properties.
        assertEquals("The value separator should be the default '=' character.", '=', option.getValueSeparator());
        assertEquals("The number of arguments should be uninitialized (-1).", -1, option.getArgs());
    }
}