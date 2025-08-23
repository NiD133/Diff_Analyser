package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link OptionBuilder} class, focusing on the configuration
 * of a value separator.
 */
public class OptionBuilderTest {

    /**
     * Verifies that an option created after calling {@code withValueSeparator(char)}
     * correctly retains the specified character as its value separator.
     */
    @Test
    public void withValueSeparatorShouldSetSeparatorOnCreatedOption() {
        // Arrange
        final char expectedSeparator = 'S';
        final char optionName = 'S';

        // Act
        // Configure the builder to use a specific value separator and then create the option.
        OptionBuilder.withValueSeparator(expectedSeparator);
        Option createdOption = OptionBuilder.create(optionName);

        // Assert
        // The primary assertion: confirm the value separator was set correctly.
        assertEquals("The value separator should match the one provided to the builder.",
                     expectedSeparator, createdOption.getValueSeparator());

        // Also, verify other basic properties to ensure the option is well-formed.
        assertEquals("The option's key should match the name used for creation.",
                     "S", createdOption.getKey());
        assertEquals("The number of arguments should default to UNINITIALIZED.",
                     Option.UNINITIALIZED, createdOption.getArgs());
    }
}