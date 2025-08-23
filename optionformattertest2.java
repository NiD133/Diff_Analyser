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
 *
 * <p>This class focuses on testing the generation of deprecation messages and option syntax strings.
 */
@DisplayName("OptionFormatter Test")
public class OptionFormatterTest {

    /**
     * Provides arguments for testing the complex deprecation format.
     * Each argument set contains a test case description, the DeprecatedAttributes object, and the expected formatted prefix.
     *
     * @return A stream of arguments for the test.
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
            Arguments.of("Deprecated with all attributes",
                DeprecatedAttributes.builder().setForRemoval(true).setSince("then").setDescription("Use something else").get(),
                "[Deprecated for removal since then. Use something else]")
        );
    }

    /**
     * Provides arguments for testing the toSyntaxOption() method.
     * Each argument set contains a test case description, the Option to format, and the expected syntax string.
     *
     * @return A stream of arguments for the test.
     */
    private static Stream<Arguments> provideOptionsForSyntaxFormatting() {
        return Stream.of(
            Arguments.of("Optional short option with argument",
                Option.builder("o").longOpt("opt").hasArg().get(),
                "[-o <arg>]"),
            Arguments.of("Optional short option with custom argument name",
                Option.builder("o").longOpt("opt").hasArg().argName("other").get(),
                "[-o <other>]"),
            Arguments.of("Required short option with argument",
                Option.builder("o").longOpt("opt").hasArg().required().argName("other").get(),
                "-o <other>"),
            Arguments.of("Required short option without argument",
                Option.builder("o").longOpt("opt").required().get(),
                "-o"),
            Arguments.of("Optional short option without argument",
                Option.builder("o").get(),
                "[-o]"),
            Arguments.of("Optional long option with argument",
                Option.builder().longOpt("opt").hasArg().argName("other").get(),
                "[--opt <other>]"),
            Arguments.of("Required long option with argument",
                Option.builder().longOpt("opt").required().hasArg().argName("other").get(),
                "--opt <other>"),
            Arguments.of("Optional multi-character short option with argument",
                Option.builder("ot").longOpt("opt").hasArg().get(),
                "[-ot <arg>]")
        );
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("provideDeprecatedAttributesAndExpectedFormat")
    @DisplayName("COMPLEX_DEPRECATED_FORMAT should correctly format various deprecation attributes")
    void testComplexDeprecationFormat(final String testCaseName, final DeprecatedAttributes deprecatedAttributes, final String expectedPrefix) {
        // Test with no option description
        final Option optionWithoutDesc = Option.builder("o").deprecated(deprecatedAttributes).get();
        assertEquals(expectedPrefix, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithoutDesc),
            "Formatting should be correct for option without its own description");

        // Test with an option description, which should be appended
        final String optionDesc = "The description";
        final Option optionWithDesc = Option.builder("o").desc(optionDesc).deprecated(deprecatedAttributes).get();
        final String expectedWithDesc = expectedPrefix + " " + optionDesc;
        assertEquals(expectedWithDesc, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithDesc),
            "Formatting should be correct for option with its own description");
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("provideOptionsForSyntaxFormatting")
    @DisplayName("toSyntaxOption should generate correct syntax strings for various option configurations")
    void testToSyntaxOption(final String testCaseName, final Option option, final String expectedSyntax) {
        // Arrange
        final OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        final String actualSyntax = formatter.toSyntaxOption();

        // Assert
        assertEquals(expectedSyntax, actualSyntax);
    }
}