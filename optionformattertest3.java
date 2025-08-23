package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.apache.commons.cli.DeprecatedAttributes;
import org.apache.commons.cli.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for formatting deprecated {@link Option} instances, primarily focusing on
 * {@link OptionFormatter#COMPLEX_DEPRECATED_FORMAT}.
 */
@DisplayName("OptionFormatter Deprecation Formatting")
public class OptionFormatterTest {

    /**
     * Provides different deprecation scenarios and their expected formatted string output.
     * Each argument set is self-contained and describes a specific case.
     *
     * @return A stream of arguments, each containing DeprecatedAttributes, the expected
     *         formatted string, and a description of the test case.
     */
    static Stream<Arguments> provideDeprecatedAttributesAndExpectedFormat() {
        return Stream.of(
            Arguments.of(
                "Basic deprecation",
                DeprecatedAttributes.builder().get(),
                "[Deprecated]"
            ),
            Arguments.of(
                "Deprecation with 'since'",
                DeprecatedAttributes.builder().setSince("1.5").get(),
                "[Deprecated since 1.5]"
            ),
            Arguments.of(
                "Deprecation with 'forRemoval'",
                DeprecatedAttributes.builder().setForRemoval(true).get(),
                "[Deprecated for removal]"
            ),
            Arguments.of(
                "Deprecation with 'forRemoval' and 'since'",
                DeprecatedAttributes.builder().setForRemoval(true).setSince("1.5").get(),
                "[Deprecated for removal since 1.5]"
            ),
            Arguments.of(
                "Deprecation with description",
                DeprecatedAttributes.builder().setDescription("Use --new-option instead").get(),
                "[Deprecated. Use --new-option instead]"
            ),
            Arguments.of(
                "Deprecation with 'forRemoval' and description",
                DeprecatedAttributes.builder().setForRemoval(true).setDescription("Use --new-option instead").get(),
                "[Deprecated for removal. Use --new-option instead]"
            ),
            Arguments.of(
                "Deprecation with 'since' and description",
                DeprecatedAttributes.builder().setSince("1.5").setDescription("Use --new-option instead").get(),
                "[Deprecated since 1.5. Use --new-option instead]"
            ),
            Arguments.of(
                "Full deprecation details",
                DeprecatedAttributes.builder().setForRemoval(true).setSince("1.5").setDescription("Use --new-option instead").get(),
                "[Deprecated for removal since 1.5. Use --new-option instead]"
            )
        );
    }

    @DisplayName("COMPLEX_DEPRECATED_FORMAT should correctly combine all attributes")
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("provideDeprecatedAttributesAndExpectedFormat")
    void testComplexDeprecationFormat(final String testCase, final DeprecatedAttributes deprecatedAttributes, final String expectedFormat) {
        // Arrange
        final Option optionWithoutDesc = Option.builder("o").deprecated(deprecatedAttributes).get();
        final Option optionWithDesc = Option.builder("o").desc("The description").deprecated(deprecatedAttributes).get();
        final String expectedFormatWithOptionDesc = expectedFormat + " The description";

        // Act & Assert: Test formatting on an option without its own description.
        // The output should only contain the deprecation message.
        assertEquals(expectedFormat, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithoutDesc));

        // Act & Assert: Test formatting on an option that also has its own description.
        // The output should append the option's description after the deprecation message.
        assertEquals(expectedFormatWithOptionDesc, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithDesc));
    }

    @Test
    @DisplayName("Option.Builder should throw IllegalStateException if no name is provided")
    void buildingOptionWithoutNameShouldThrowException() {
        // Note: This test validates Option.Builder behavior (related to JIRA issue CLI-343)
        // rather than OptionFormatter. An Option must have at least a short or long name
        // to be valid.
        
        // Arrange
        final Option.Builder builder = Option.builder().required(false);

        // Act & Assert
        assertThrows(IllegalStateException.class, builder::build,
            "build() should throw when no option name is set.");
        assertThrows(IllegalStateException.class, builder::get,
            "get() should throw (alias for build()) when no option name is set.");
    }
}