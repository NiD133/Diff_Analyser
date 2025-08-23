package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.apache.commons.cli.DeprecatedAttributes;
import org.apache.commons.cli.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link OptionFormatter}.
 */
@DisplayName("OptionFormatter Test")
class OptionFormatterTest {

    @Nested
    @DisplayName("Deprecation Formatting")
    class DeprecationFormatting {

        static Stream<Arguments> deprecatedAttributesData() {
            // Each Arguments instance is self-contained, making it easy to understand
            // the inputs for each specific test case without tracking state.
            return Stream.of(
                Arguments.of("Default deprecation",
                    DeprecatedAttributes.builder().get(),
                    "[Deprecated]"),
                Arguments.of("Deprecation with 'since'",
                    DeprecatedAttributes.builder().setSince("now").get(),
                    "[Deprecated since now]"),
                Arguments.of("Deprecation with 'forRemoval' and 'since'",
                    DeprecatedAttributes.builder().setForRemoval(true).setSince("now").get(),
                    "[Deprecated for removal since now]"),
                Arguments.of("Deprecation with 'forRemoval' only",
                    DeprecatedAttributes.builder().setForRemoval(true).setSince(null).get(),
                    "[Deprecated for removal]"),
                Arguments.of("Deprecation with 'description' only",
                    DeprecatedAttributes.builder().setForRemoval(false).setDescription("Use something else").get(),
                    "[Deprecated. Use something else]"),
                Arguments.of("Deprecation with 'forRemoval' and 'description'",
                    DeprecatedAttributes.builder().setForRemoval(true).setDescription("Use something else").get(),
                    "[Deprecated for removal. Use something else]"),
                Arguments.of("Deprecation with 'since' and 'description'",
                    DeprecatedAttributes.builder().setForRemoval(false).setSince("then").setDescription("Use something else").get(),
                    "[Deprecated since then. Use something else]"),
                Arguments.of("Deprecation with all attributes",
                    DeprecatedAttributes.builder().setForRemoval(true).setSince("then").setDescription("Use something else").get(),
                    "[Deprecated for removal since then. Use something else]")
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("deprecatedAttributesData")
        @DisplayName("COMPLEX_DEPRECATED_FORMAT should format based on attributes")
        void testComplexDeprecatedFormat(final String testName, final DeprecatedAttributes deprecatedAttributes, final String expectedFormat) {
            // Test case 1: Option without its own description
            final Option option = Option.builder("o").deprecated(deprecatedAttributes).get();
            assertEquals(expectedFormat, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(option));

            // Test case 2: Option with its own description, which should be appended
            final String optionDescription = "The description";
            final Option optionWithDesc = Option.builder("o").desc(optionDescription).deprecated(deprecatedAttributes).get();
            final String expectedFormatWithDesc = expectedFormat + " " + optionDescription;
            assertEquals(expectedFormatWithDesc, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithDesc));
        }
    }

    @Nested
    @DisplayName("Separator Handling")
    class SeparatorHandling {

        private static final Option OPTION_WITH_LONG_OPT = Option.builder("o").longOpt("opt").hasArg().get();

        @Test
        @DisplayName("should use a custom option separator")
        void shouldUseCustomOptSeparator() {
            final OptionFormatter formatter = OptionFormatter.builder()
                .setOptSeparator(" and ")
                .build(OPTION_WITH_LONG_OPT);

            assertEquals("-o and --opt", formatter.getBothOpt());
        }

        @Test
        @DisplayName("should treat an empty option separator as an empty string")
        void shouldTreatEmptyOptSeparatorAsEmpty() {
            final OptionFormatter formatter = OptionFormatter.builder()
                .setOptSeparator("")
                .build(OPTION_WITH_LONG_OPT);

            assertEquals("-o--opt", formatter.getBothOpt(),
                "An empty separator should result in no separator between options.");
        }

        @Test
        @DisplayName("should treat a null option separator as an empty string")
        void shouldTreatNullOptSeparatorAsEmpty() {
            final OptionFormatter formatter = OptionFormatter.builder()
                .setOptSeparator(null)
                .build(OPTION_WITH_LONG_OPT);

            assertEquals("-o--opt", formatter.getBothOpt(),
                "A null separator should result in no separator between options.");
        }
    }
}