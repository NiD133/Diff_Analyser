package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link OptionBuilder} class.
 *
 * Note: {@link OptionBuilder} is a deprecated, stateful static utility.
 * Tests on such classes require careful management of the static state
 * between test runs, typically by resetting it in a @Before method.
 */
public class OptionBuilderTest {

    @Test
    public void withValueSeparatorShouldSetSeparatorOnCreatedOption() {
        // Arrange: Configure the builder to use a specific value separator.
        final char expectedSeparator = '>';
        OptionBuilder.withValueSeparator(expectedSeparator);

        // Act: Create an Option instance. The null argument signifies that
        // the option has no short name (e.g., "-f").
        Option option = OptionBuilder.create((String) null);

        // Assert: Verify that the created option has the correct properties.
        assertEquals("The value separator should be set on the created option.",
                     expectedSeparator, option.getValueSeparator());

        // Verify that other properties remain at their default state.
        assertEquals("The number of arguments should be uninitialized by default.",
                     Option.UNINITIALIZED, option.getArgs());
    }
}