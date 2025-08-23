package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.apache.commons.cli.DeprecatedAttributes;
import org.apache.commons.cli.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("OptionFormatter Test")
class OptionFormatterTest {

    /**
     * Provides test cases for the COMPLEX_DEPRECATED_FORMAT function.
     * Each argument set contains a descriptive name, the DeprecatedAttributes object,
     * and the expected formatted string prefix.
     */
    static Stream<Arguments> deprecatedAttributesSource() {
        return Stream.of(
            Arguments.of("Simple deprecation",
                DeprecatedAttributes.builder().get(),
                "[Deprecated]"),
            Arguments.of("With 'since' attribute",
                DeprecatedAttributes.builder().setSince("now").get(),
                "[Deprecated since now]"),
            Arguments.of("With 'since' and 'for removal'",
                DeprecatedAttributes.builder().setSince("now").setForRemoval(true).get(),
                "[Deprecated for removal since now]"),
            Arguments.of("With 'for removal' only",
                DeprecatedAttributes.builder().setForRemoval(true).get(),
                "[Deprecated for removal]"),
            Arguments.of("With deprecation description",
                DeprecatedAttributes.builder().setDescription("Use something else").get(),
                "[Deprecated. Use something else]"),
            Arguments.of("With 'for removal' and deprecation description",
                DeprecatedAttributes.builder().setForRemoval(true).setDescription("Use something else").get(),
                "[Deprecated for removal. Use something else]"),
            Arguments.of("With 'since' and deprecation description",
                DeprecatedAttributes.builder().setSince("then").setDescription("Use something else").get(),
                "[Deprecated since then. Use something else]"),
            Arguments.of("With all attributes",
                DeprecatedAttributes.builder().setForRemoval(true).setSince("then").setDescription("Use something else").get(),
                "[Deprecated for removal since then. Use something else]")
        );
    }

    @DisplayName("COMPLEX_DEPRECATED_FORMAT should generate correct deprecation messages")
    @ParameterizedTest(name = "{0}")
    @MethodSource("deprecatedAttributesSource")
    void complexDeprecatedFormat_shouldGenerateCorrectString(
            final String testCaseName, final DeprecatedAttributes deprecatedAttributes, final String expectedPrefix) {

        // Test case 1: Option without its own description
        final Option optionWithoutDesc = Option.builder("o").deprecated(deprecatedAttributes).get();
        assertEquals(expectedPrefix, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithoutDesc),
            "Format should be correct for an option without a description.");

        // Test case 2: Option with its own description, which should be appended
        final String optionDesc = "The description";
        final Option optionWithDesc = Option.builder("o").desc(optionDesc).deprecated(deprecatedAttributes).get();
        final String expectedWithDesc = expectedPrefix + " " + optionDesc;
        assertEquals(expectedWithDesc, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithDesc),
            "Format should correctly append the option's description.");
    }

    /**
     * Provides test cases for the getBothOpt() method.
     * Each argument set contains a descriptive name, the Option object, and the expected combined string.
     */
    static Stream<Arguments> bothOptSource() {
        return Stream.of(
            Arguments.of("With short and long option",
                Option.builder("o").longOpt("opt").get(),
                "-o, --opt"),
            Arguments.of("With only long option",
                Option.builder().longOpt("opt").get(),
                "--opt"),
            Arguments.of("With only short option",
                Option.builder("o").get(),
                "-o")
        );
    }

    @DisplayName("getBothOpt() should correctly combine short and long option names")
    @ParameterizedTest(name = "{0}")
    @MethodSource("bothOptSource")
    void getBothOpt_shouldReturnCorrectlyFormattedString(final String testCaseName, final Option option, final String expected) {
        final OptionFormatter formatter = OptionFormatter.from(option);
        assertEquals(expected, formatter.getBothOpt());
    }
}