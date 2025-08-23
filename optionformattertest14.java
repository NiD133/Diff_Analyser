package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.BiFunction;
import java.util.stream.Stream;
import org.apache.commons.cli.DeprecatedAttributes;
import org.apache.commons.cli.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link OptionFormatter} class, focusing on deprecation and syntax formatting.
 */
@DisplayName("OptionFormatter Tests")
public class OptionFormatterTest {

    // A standard option used across multiple tests to reduce duplication.
    private static final Option TEST_OPTION_WITH_ARG = Option.builder("o").longOpt("opt").hasArg().get();

    /**
     * Provides various combinations of deprecated attributes and their expected string format.
     * Each test case is self-contained, creating a new DeprecatedAttributes object.
     *
     * @return A stream of arguments for the parameterized test.
     */
    private static Stream<Arguments> provideDeprecatedAttributeCombinations() {
        return Stream.of(
            Arguments.of("Simple deprecation",
                DeprecatedAttributes.builder().get(),
                "[Deprecated]"),
            Arguments.of("With 'since' attribute",
                DeprecatedAttributes.builder().setSince("now").get(),
                "[Deprecated since now]"),
            Arguments.of("With 'for removal' attribute",
                DeprecatedAttributes.builder().setForRemoval(true).get(),
                "[Deprecated for removal]"),
            Arguments.of("With 'description' attribute",
                DeprecatedAttributes.builder().setDescription("Use something else").get(),
                "[Deprecated. Use something else]"),
            Arguments.of("With 'for removal' and 'since'",
                DeprecatedAttributes.builder().setForRemoval(true).setSince("now").get(),
                "[Deprecated for removal since now]"),
            Arguments.of("With 'for removal' and 'description'",
                DeprecatedAttributes.builder().setForRemoval(true).setDescription("Use something else").get(),
                "[Deprecated for removal. Use something else]"),
            Arguments.of("With 'since' and 'description'",
                DeprecatedAttributes.builder().setSince("then").setDescription("Use something else").get(),
                "[Deprecated since then. Use something else]"),
            Arguments.of("With all attributes",
                DeprecatedAttributes.builder().setForRemoval(true).setSince("then").setDescription("Use something else").get(),
                "[Deprecated for removal since then. Use something else]")
        );
    }

    /**
     * Tests that the {@code COMPLEX_DEPRECATED_FORMAT} function correctly formats an option's
     * deprecation notice based on its attributes. It also verifies that the option's own
     * description is appended correctly.
     */
    @DisplayName("COMPLEX_DEPRECATED_FORMAT should generate correct string for various attributes")
    @ParameterizedTest(name = "Case: {0}")
    @MethodSource("provideDeprecatedAttributeCombinations")
    void complexDeprecatedFormat_shouldGenerateCorrectString(
            final String testCaseName, final DeprecatedAttributes deprecatedAttributes, final String expectedFormat) {
        // Test case 1: Option without its own description.
        final Option optionWithoutDesc = Option.builder("o").deprecated(deprecatedAttributes).get();
        assertEquals(expectedFormat, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithoutDesc),
                "Format should be correct for an option without a description.");

        // Test case 2: Option with its own description, which should be appended.
        final String optionDescription = "The description";
        final Option optionWithDesc = Option.builder("o").desc(optionDescription).deprecated(deprecatedAttributes).get();
        final String expectedFormatWithDesc = expectedFormat + " " + optionDescription;
        assertEquals(expectedFormatWithDesc, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithDesc),
                "Format should include the option's own description at the end.");
    }

    @Test
    @DisplayName("A custom syntax format function should be used when provided")
    void shouldUseCustomSyntaxFormatFunctionWhenSet() {
        final String expectedOutput = "Custom Syntax";
        final BiFunction<OptionFormatter, Boolean, String> customSyntaxFormatter = (formatter, isRequired) -> expectedOutput;

        final OptionFormatter.Builder builder = OptionFormatter.builder().setSyntaxFormatFunction(customSyntaxFormatter);
        final OptionFormatter formatter = builder.build(TEST_OPTION_WITH_ARG);

        assertEquals(expectedOutput, formatter.toSyntaxOption());
    }

    @Test
    @DisplayName("The default syntax format function should be used when the custom one is set to null")
    void shouldUseDefaultSyntaxFormatFunctionWhenSetToNull() {
        final String expectedDefaultSyntax = "[-o <arg>]";

        final OptionFormatter.Builder builder = OptionFormatter.builder().setSyntaxFormatFunction(null);
        final OptionFormatter formatter = builder.build(TEST_OPTION_WITH_ARG);

        assertEquals(expectedDefaultSyntax, formatter.toSyntaxOption());
    }
}