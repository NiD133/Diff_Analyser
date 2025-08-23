package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.apache.commons.cli.DeprecatedAttributes;
import org.apache.commons.cli.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link OptionFormatter}.
 * Focuses on formatting deprecated options and setting a default argument name.
 */
@DisplayName("OptionFormatter Tests")
class OptionFormatterTest {

    /**
     * Provides test cases for formatting deprecated options. Each case includes a descriptive name,
     * the {@link DeprecatedAttributes} to test, and the expected formatted string.
     *
     * @return A stream of arguments for the parameterized test.
     */
    private static Stream<Arguments> provideDeprecatedAttributesAndExpectedFormat() {
        return Stream.of(
            Arguments.of("Just deprecated",
                DeprecatedAttributes.builder().get(),
                "[Deprecated]"),
            Arguments.of("Deprecated with 'since'",
                DeprecatedAttributes.builder().setSince("now").get(),
                "[Deprecated since now]"),
            Arguments.of("Deprecated for removal with 'since'",
                DeprecatedAttributes.builder().setForRemoval(true).setSince("now").get(),
                "[Deprecated for removal since now]"),
            Arguments.of("Deprecated for removal without 'since'",
                DeprecatedAttributes.builder().setForRemoval(true).get(),
                "[Deprecated for removal]"),
            Arguments.of("Deprecated with description",
                DeprecatedAttributes.builder().setDescription("Use something else").get(),
                "[Deprecated. Use something else]"),
            Arguments.of("Deprecated for removal with description",
                DeprecatedAttributes.builder().setForRemoval(true).setDescription("Use something else").get(),
                "[Deprecated for removal. Use something else]"),
            Arguments.of("Deprecated with 'since' and description",
                DeprecatedAttributes.builder().setSince("then").setDescription("Use something else").get(),
                "[Deprecated since then. Use something else]"),
            Arguments.of("Deprecated for removal with 'since' and description",
                DeprecatedAttributes.builder().setForRemoval(true).setSince("then").setDescription("Use something else").get(),
                "[Deprecated for removal since then. Use something else]")
        );
    }

    /**
     * Provides test cases for the default argument name functionality.
     *
     * @return A stream of arguments: the name to set and the expected output.
     */
    private static Stream<Arguments> provideDefaultArgNames() {
        return Stream.of(
            Arguments.of("a custom name", "foo", "<foo>"),
            Arguments.of("an empty name", "", "<arg>"),
            Arguments.of("a null name", null, "<arg>")
        );
    }

    @ParameterizedTest(name = "Case: {0}")
    @MethodSource("provideDeprecatedAttributesAndExpectedFormat")
    @DisplayName("Should format complex deprecation messages correctly")
    void testComplexDeprecationFormat(final String caseDescription, final DeprecatedAttributes deprecatedAttributes, final String expectedFormat) {
        // Test case 1: Option without its own description
        final Option optionWithoutDesc = Option.builder("o").deprecated(deprecatedAttributes).get();
        assertEquals(expectedFormat, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithoutDesc),
            "Format should be correct for option without a description.");

        // Test case 2: Option with its own description
        final Option optionWithDesc = Option.builder("o").desc("The description").deprecated(deprecatedAttributes).get();
        final String expectedWithAppendedDesc = expectedFormat + " The description";
        assertEquals(expectedWithAppendedDesc, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithDesc),
            "Format should append the option's description.");
    }

    @ParameterizedTest(name = "Setting default arg name to {0} should result in {2}")
    @MethodSource("provideDefaultArgNames")
    @DisplayName("Should handle default argument name settings")
    void testSetDefaultArgName(final String caseDescription, final String defaultName, final String expectedArgName) {
        final Option option = Option.builder("o").longOpt("opt").hasArg().get();

        final OptionFormatter formatter = OptionFormatter.builder()
            .setDefaultArgName(defaultName)
            .build(option);

        assertEquals(expectedArgName, formatter.getArgName());
    }
}