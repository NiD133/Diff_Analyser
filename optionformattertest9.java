package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.apache.commons.cli.DeprecatedAttributes;
import org.apache.commons.cli.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link OptionFormatter}.
 */
@DisplayName("OptionFormatter Test")
class OptionFormatterTest {

    // A standard option with an argument, used across multiple tests.
    private static final Option OPTION_WITH_ARG = Option.builder("o").longOpt("opt").hasArg().get();

    /**
     * Provides test cases for various combinations of deprecation attributes.
     * Each case includes the attributes, the expected formatted string, and a descriptive name.
     *
     * @return A stream of arguments for the parameterized test.
     */
    private static Stream<Arguments> provideDeprecatedAttributeFormats() {
        return Stream.of(
            Arguments.of("Simple deprecation",
                DeprecatedAttributes.builder().get(),
                "[Deprecated]"),
            Arguments.of("Deprecation with 'since'",
                DeprecatedAttributes.builder().setSince("now").get(),
                "[Deprecated since now]"),
            Arguments.of("Deprecation with 'forRemoval' and 'since'",
                DeprecatedAttributes.builder().setForRemoval(true).setSince("now").get(),
                "[Deprecated for removal since now]"),
            Arguments.of("Deprecation with 'forRemoval' only",
                DeprecatedAttributes.builder().setForRemoval(true).get(),
                "[Deprecated for removal]"),
            Arguments.of("Deprecation with description only",
                DeprecatedAttributes.builder().setDescription("Use something else").get(),
                "[Deprecated. Use something else]"),
            Arguments.of("Deprecation with 'forRemoval' and description",
                DeprecatedAttributes.builder().setForRemoval(true).setDescription("Use something else").get(),
                "[Deprecated for removal. Use something else]"),
            Arguments.of("Deprecation with 'since' and description",
                DeprecatedAttributes.builder().setSince("then").setDescription("Use something else").get(),
                "[Deprecated since then. Use something else]"),
            Arguments.of("Deprecation with all attributes",
                DeprecatedAttributes.builder().setForRemoval(true).setSince("then").setDescription("Use something else").get(),
                "[Deprecated for removal since then. Use something else]")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideDeprecatedAttributeFormats")
    @DisplayName("COMPLEX_DEPRECATED_FORMAT should render all attributes correctly")
    void complexDeprecatedFormat_shouldRenderAllAttributesCorrectly(
            final String displayName, final DeprecatedAttributes deprecatedAttributes, final String expectedOutput) {
        // Arrange: Create an option with only deprecated attributes
        final Option optionWithoutDesc = Option.builder("o").deprecated(deprecatedAttributes).get();

        // Arrange: Create an option with deprecated attributes and its own description
        final String optionDesc = "The description";
        final Option optionWithDesc = Option.builder("o").desc(optionDesc).deprecated(deprecatedAttributes).get();
        final String expectedOutputWithDesc = expectedOutput + " " + optionDesc;

        // Act & Assert: Verify the format for the option without a description
        assertEquals(expectedOutput, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithoutDesc));

        // Act & Assert: Verify the format for the option with a description
        assertEquals(expectedOutputWithDesc, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithDesc));
    }

    @Test
    @DisplayName("Setting custom argument name delimiters should format the argument name correctly")
    void setArgumentNameDelimiters_withCustomDelimiters_shouldFormatArgNameCorrectly() {
        // Arrange
        final OptionFormatter formatter = OptionFormatter.builder()
            .setArgumentNameDelimiters(" (", ")")
            .build(OPTION_WITH_ARG);

        // Act
        final String argName = formatter.getArgName();

        // Assert
        assertEquals(" (arg)", argName, "Argument name should be wrapped in custom delimiters.");
    }

    private static Stream<Arguments> provideNullOrEmptyDelimiters() {
        return Stream.of(
            Arguments.of("Null start delimiter", null, ""),
            Arguments.of("Null end delimiter", "", null)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideNullOrEmptyDelimiters")
    @DisplayName("Setting null or empty argument name delimiters should result in no delimiters")
    void setArgumentNameDelimiters_withNullOrEmptyDelimiters_shouldResultInNoDelimiters(
            final String displayName, final String startDelimiter, final String endDelimiter) {
        // Arrange
        final OptionFormatter formatter = OptionFormatter.builder()
            .setArgumentNameDelimiters(startDelimiter, endDelimiter)
            .build(OPTION_WITH_ARG);

        // Act
        final String argName = formatter.getArgName();

        // Assert
        assertEquals("arg", argName, "Argument name should not have any delimiters.");
    }
}