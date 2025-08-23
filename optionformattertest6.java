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

@DisplayName("Tests for OptionFormatter formats")
class OptionFormatterFormatTest {

    @Nested
    @DisplayName("Complex Deprecated Format")
    class ComplexDeprecatedFormatTest {

        static Stream<Arguments> deprecatedAttributesData() {
            // Each test case is now self-contained and stateless.
            // A descriptive name is added as the first argument for clarity in test reports.
            return Stream.of(
                Arguments.of("Simple deprecation",
                    DeprecatedAttributes.builder().get(),
                    "[Deprecated]"),
                Arguments.of("With 'since'",
                    DeprecatedAttributes.builder().setSince("now").get(),
                    "[Deprecated since now]"),
                Arguments.of("With 'for removal'",
                    DeprecatedAttributes.builder().setForRemoval(true).get(),
                    "[Deprecated for removal]"),
                Arguments.of("With 'for removal' and 'since'",
                    DeprecatedAttributes.builder().setForRemoval(true).setSince("now").get(),
                    "[Deprecated for removal since now]"),
                Arguments.of("With description",
                    DeprecatedAttributes.builder().setDescription("Use something else").get(),
                    "[Deprecated. Use something else]"),
                Arguments.of("With 'since' and description",
                    DeprecatedAttributes.builder().setSince("then").setDescription("Use something else").get(),
                    "[Deprecated since then. Use something else]"),
                Arguments.of("With 'for removal' and description",
                    DeprecatedAttributes.builder().setForRemoval(true).setDescription("Use something else").get(),
                    "[Deprecated for removal. Use something else]"),
                Arguments.of("With all attributes",
                    DeprecatedAttributes.builder().setForRemoval(true).setSince("then").setDescription("Use something else").get(),
                    "[Deprecated for removal since then. Use something else]")
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("deprecatedAttributesData")
        @DisplayName("should generate correct string for various deprecation attributes")
        void testComplexDeprecationFormat(final String testName, final DeprecatedAttributes deprecatedAttributes, final String expectedPrefix) {
            final Option optionWithoutDesc = Option.builder("o").deprecated(deprecatedAttributes).get();
            final Option optionWithDesc = Option.builder("o").desc("The description").deprecated(deprecatedAttributes).get();

            // Test case 1: Option has no description of its own.
            assertEquals(expectedPrefix, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithoutDesc));

            // Test case 2: Option has its own description, which should be appended.
            final String expectedWithDesc = expectedPrefix + " The description";
            assertEquals(expectedWithDesc, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithDesc));
        }
    }

    @Nested
    @DisplayName("Default Syntax Format")
    class DefaultSyntaxFormatTest {

        @Test
        @DisplayName("should enclose optional option in brackets by default")
        void toSyntaxOptionForOptionalOption() {
            final Option option = Option.builder("o").longOpt("opt").hasArg().get();
            final OptionFormatter formatter = OptionFormatter.from(option);
            assertEquals("[-o <arg>]", formatter.toSyntaxOption());
        }

        @Test
        @DisplayName("should not enclose required option in brackets by default")
        void toSyntaxOptionForRequiredOption() {
            final Option option = Option.builder("o").longOpt("opt").hasArg().required().get();
            final OptionFormatter formatter = OptionFormatter.from(option);
            assertEquals("-o <arg>", formatter.toSyntaxOption());
        }

        @Test
        @DisplayName("should allow forcing optional display for a required option")
        void toSyntaxOptionCanOverrideRequiredToOptional() {
            final Option requiredOption = Option.builder("o").longOpt("opt").hasArg().required().get();
            final OptionFormatter formatter = OptionFormatter.from(requiredOption);
            assertEquals("[-o <arg>]", formatter.toSyntaxOption(false));
        }

        @Test
        @DisplayName("should allow forcing required display for an optional option")
        void toSyntaxOptionCanOverrideOptionalToRequired() {
            final Option optionalOption = Option.builder("o").longOpt("opt").hasArg().get();
            final OptionFormatter formatter = OptionFormatter.from(optionalOption);
            assertEquals("-o <arg>", formatter.toSyntaxOption(true));
        }
    }
}