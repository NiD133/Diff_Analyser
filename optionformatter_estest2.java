package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link OptionFormatter} class.
 * This class focuses on improving the understandability of an auto-generated test case.
 */
public class OptionFormatter_ESTestTest2 extends OptionFormatter_ESTest_scaffolding {

    /**
     * Tests that the syntax for an optional option, which has an argument but no name,
     * is formatted correctly when a custom default argument name is provided.
     */
    @Test
    public void toSyntaxOption_forOptionalOptionWithNoNameAndCustomArgName_returnsCorrectlyFormattedString() {
        // Arrange
        // Create an option that has an argument but no short or long name.
        // By default, an option is not required, so this will be treated as optional.
        final Option optionWithArgButNoName = new Option(null, null, true, null);
        final String customArgName = "[Deprecated";

        // Build a formatter with a custom default argument name.
        final OptionFormatter formatter = OptionFormatter.builder()
                .setDefaultArgName(customArgName)
                .build(optionWithArgButNoName);

        // The expected format for an optional option with no name is:
        // "[ optArgSeparator<defaultArgName>]" which defaults to "[ <arg>]".
        // With our custom defaultArgName, it becomes "[ <[Deprecated>]".
        final String expectedSyntax = "[ <[Deprecated>]";

        // Act
        final String actualSyntax = formatter.toSyntaxOption();

        // Assert
        assertEquals(expectedSyntax, actualSyntax);
    }
}