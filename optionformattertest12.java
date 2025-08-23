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
 */
@DisplayName("OptionFormatter Test")
class OptionFormatterTest {

    /**
     * Provides arguments for testing the complex deprecation message format.
     * Each argument set contains a descriptive name, the DeprecatedAttributes object, and the expected formatted string.
     */
    static Stream<Arguments> complexDeprecationFormatProvider() {
        return Stream.of(
            Arguments.of("Basic deprecation",
                DeprecatedAttributes.builder().get(),
                "[Deprecated]"),
            Arguments.of("With 'since' attribute",
                DeprecatedAttributes.builder().setSince("1.1").get(),
                "[Deprecated since 1.1]"),
            Arguments.of("With 'forRemoval' attribute",
                DeprecatedAttributes.builder().setForRemoval(true).get(),
                "[Deprecated for removal]"),
            Arguments.of("With 'description' attribute",
                DeprecatedAttributes.builder().setDescription("Use --new-option instead").get(),
                "[Deprecated. Use --new-option instead]"),
            Arguments.of("With 'forRemoval' and 'since'",
                DeprecatedAttributes.builder().setForRemoval(true).setSince("1.1").get(),
                "[Deprecated for removal since 1.1]"),
            Arguments.of("With 'forRemoval' and 'description'",
                DeprecatedAttributes.builder().setForRemoval(true).setDescription("Use --new-option instead").get(),
                "[Deprecated for removal. Use --new-option instead]"),
            Arguments.of("With 'since' and 'description'",
                DeprecatedAttributes.builder().setSince("1.1").setDescription("Use --new-option instead").get(),
                "[Deprecated since 1.1. Use --new-option instead]"),
            Arguments.of("With all attributes",
                DeprecatedAttributes.builder().setForRemoval(true).setSince("1.1").setDescription("Use --new-option instead").get(),
                "[Deprecated for removal since 1.1. Use --new-option instead]")
        );
    }

    /**
     * Provides arguments for testing different option argument separators.
     */
    static Stream<Arguments> optArgumentSeparatorProvider() {
        return Stream.of(
            Arguments.of(" with argument named ", "[-o with argument named <arg>]", "Custom separator string"),
            Arguments.of(null, "[-o<arg>]", "Null separator should have no space"),
            Arguments.of("=", "[-o=<arg>]", "Equals sign separator")
        );
    }

    @DisplayName("Complex deprecation format should be generated correctly")
    @ParameterizedTest(name = "{0}")
    @MethodSource("complexDeprecationFormatProvider")
    void testComplexDeprecationFormat(String testName, DeprecatedAttributes deprecatedAttributes, String expectedPrefix) {
        // Arrange
        final Option optionWithoutDesc = Option.builder("o").deprecated(deprecatedAttributes).get();
        final Option optionWithDesc = Option.builder("o").desc("The description").deprecated(deprecatedAttributes).get();

        // Act & Assert: Test with an option that has no description of its own.
        // The entire description should come from the deprecation format.
        assertEquals(expectedPrefix, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithoutDesc));

        // Act & Assert: Test with an option that has its own description.
        // The deprecation format should be prefixed to the option's description.
        final String expectedWithDescription = expectedPrefix + " The description";
        assertEquals(expectedWithDescription, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithDesc));
    }

    @DisplayName("Syntax should be formatted correctly with custom argument separators")
    @ParameterizedTest(name = "[{index}] Separator: ''{0}'' -> ''{1}'' ({2})")
    @MethodSource("optArgumentSeparatorProvider")
    void testSetOptArgumentSeparator(String separator, String expectedSyntax, String description) {
        // Arrange
        final Option option = Option.builder("o").longOpt("opt").hasArg().get();
        final OptionFormatter.Builder builder = OptionFormatter.builder().setOptArgSeparator(separator);

        // Act
        final String actualSyntax = builder.build(option).toSyntaxOption();

        // Assert
        assertEquals(expectedSyntax, actualSyntax);
    }
}