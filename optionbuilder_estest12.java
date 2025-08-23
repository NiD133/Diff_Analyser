package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class focuses on verifying the behavior of the OptionBuilder.
 * Note: OptionBuilder is a deprecated class that uses static fields, which is
 * an anti-pattern for testability. Tests for such a class require careful
* state management, typically by resetting the builder's state before each test.
 */
public class OptionBuilderTest {

    /**
     * Tests that an Option created without specifying argument requirements
     * defaults to having an uninitialized number of arguments.
     */
    @Test
    public void createOptionWithoutArgsShouldHaveUninitializedArgCount() {
        // Arrange: Configure a basic option with a long name.
        // We do not call hasArg(), hasArgs(), or related methods.
        OptionBuilder.withLongOpt("file");

        // Act: Create the Option instance.
        Option option = OptionBuilder.create();

        // Assert: The number of arguments should be the default uninitialized value,
        // represented by the Option.UNINITIALIZED constant (-1).
        assertEquals("Expected default argument count to be uninitialized.",
                Option.UNINITIALIZED, option.getArgs());
    }
}