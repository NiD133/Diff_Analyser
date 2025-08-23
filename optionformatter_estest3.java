package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link OptionFormatter}.
 * This class provides a more understandable version of an auto-generated test case.
 */
public class OptionFormatterTest {

    /**
     * Tests that toSyntaxOption() correctly formats an option that has an argument but no name,
     * using a custom argument separator.
     *
     * <p>An option with an argument but no name should be rendered as just the
     * separator and the argument name. Since the option is not required by default,
     * the entire syntax should be enclosed in optional delimiters ("[]").
     * </p>
     */
    @Test
    public void toSyntaxOptionShouldFormatOptionalNamelessOptionWithCustomArgumentSeparator() {
        // Arrange: Set up the test conditions
        final String customArgSeparator = "Deprecated";

        // Create an option that requires an argument but has no short or long name.
        // The 'true' flag indicates that the option has an argument.
        final Option namelessOptionWithArg = new Option(null, null, true, "description");

        // Create a formatter with a custom separator to be placed between an option and its argument.
        final OptionFormatter formatter = OptionFormatter.builder()
                .setOptArgSeparator(customArgSeparator)
                .build(namelessOptionWithArg);

        // Act: Call the method under test
        final String formattedSyntax = formatter.toSyntaxOption();

        // Assert: Verify the outcome
        // Expected format: [separator<argument_name>]
        // 1. The option is not required, so it's wrapped in brackets "[...]".
        // 2. The option has no name, so only the separator and argument name appear inside.
        // 3. The custom separator "Deprecated" is used.
        // 4. The default argument name is "arg", wrapped in angle brackets "<...>".
        final String expectedSyntax = "[Deprecated<arg>]";
        assertEquals("The syntax for an optional, nameless option should use the custom separator.",
                expectedSyntax, formattedSyntax);
    }
}