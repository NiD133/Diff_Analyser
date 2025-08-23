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
 * Tests for formatting options, focusing on deprecation messages and custom prefixes.
 */
@DisplayName("OptionFormatter Customization Tests")
class OptionFormatterTest {

    /**
     * Provides test cases for various combinations of deprecation attributes.
     * Each case is self-contained and describes the attributes being tested.
     *
     * @return A stream of arguments, each containing a display name, the attributes, and the expected formatted string.
     */
    private static Stream<Arguments> provideComplexDeprecationFormatCases() {
        return Stream.of(
            Arguments.of("Deprecated only",
                DeprecatedAttributes.builder().get(),
                "[Deprecated]"),
            Arguments.of("Deprecated with 'since'",
                DeprecatedAttributes.builder().setSince("now").get(),
                "[Deprecated since now]"),
            Arguments.of("Deprecated for removal with 'since'",
                DeprecatedAttributes.builder().setForRemoval(true).setSince("now").get(),
                "[Deprecated for removal since now]"),
            Arguments.of("Deprecated for removal only",
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

    @DisplayName("Complex deprecation format should be generated correctly")
    @ParameterizedTest(name = "{0}")
    @MethodSource("provideComplexDeprecationFormatCases")
    void testComplexDeprecationFormat(final String displayName, final DeprecatedAttributes attributes, final String expectedPrefix) {
        // Test with a deprecated option that has no description
        final Option optionWithoutDesc = Option.builder("o").deprecated(attributes).get();
        assertEquals(expectedPrefix, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithoutDesc),
            "Format should be correct for option without a description");

        // Test with a deprecated option that also has its own description
        final Option optionWithDesc = Option.builder("o").desc("The description").deprecated(attributes).get();
        final String expectedWithDescription = expectedPrefix + " The description";
        assertEquals(expectedWithDescription, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithDesc),
            "Format should include the option's description");
    }

    /**
     * Provides test cases for setting the long option prefix.
     *
     * @return A stream of arguments, each containing a prefix and the expected formatted long option.
     */
    private static Stream<Arguments> provideLongOptPrefixCases() {
        return Stream.of(
            Arguments.of("fo", "foopt"),   // Custom prefix
            Arguments.of("", "opt"),      // Empty prefix should result in no prefix
            Arguments.of(null, "opt")     // Null prefix should be treated as empty
        );
    }

    @DisplayName("Setting a long option prefix should format the long option correctly")
    @ParameterizedTest(name = "Prefix: ''{0}'' -> ''{1}''")
    @MethodSource("provideLongOptPrefixCases")
    void testSetLongOptPrefix(final String prefix, final String expectedLongOpt) {
        final Option option = Option.builder("o").longOpt("opt").hasArg().get();
        final OptionFormatter formatter = OptionFormatter.builder().setLongOptPrefix(prefix).build(option);

        assertEquals(expectedLongOpt, formatter.getLongOpt());
    }
}