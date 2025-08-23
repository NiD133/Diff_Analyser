package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the deprecated {@link OptionBuilder} class.
 * This test suite assumes that the static state of OptionBuilder is reset between test runs.
 */
public class OptionBuilderTest {

    /**
     * Tests that an Option created after calling hasArgs(int) has the specified number of arguments.
     */
    @Test
    public void hasArgsWithCountShouldSetTheNumberOfArguments() {
        // Arrange: Define the expected number of arguments and configure the builder.
        final int expectedArgCount = 235;
        OptionBuilder.hasArgs(expectedArgCount);

        // Act: Create an Option. The builder uses the static state set above.
        // The short name (null) is not relevant for this test.
        Option option = OptionBuilder.create(null);

        // Assert: Verify that the created option has the correct number of arguments.
        assertEquals("The number of arguments should match the value set.",
                     expectedArgCount, option.getArgs());
    }
}