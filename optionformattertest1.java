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

@DisplayName("OptionFormatter Test")
public class OptionFormatterTest {

    /**
     * Provides test cases for various combinations of deprecation attributes.
     * Each case includes the DeprecatedAttributes object and the expected formatted string.
     *
     * @return A stream of arguments for the parameterized test.
     */
    static Stream<Arguments> provideComplexDeprecatedFormatCases() {
        return Stream.of(
            // Case 1: Basic deprecation
            Arguments.of(DeprecatedAttributes.builder().build(), "[Deprecated]"),
            // Case 2: Deprecated with a 'since' version
            Arguments.of(DeprecatedAttributes.builder().setSince("now").build(), "[Deprecated since now]"),
            // Case 3: Deprecated for removal with a 'since' version
            Arguments.of(DeprecatedAttributes.builder().setForRemoval(true).setSince("now").build(), "[Deprecated for removal since now]"),
            // Case 4: Deprecated for removal without a 'since' version
            Arguments.of(DeprecatedAttributes.builder().setForRemoval(true).build(), "[Deprecated for removal]"),
            // Case 5: Deprecated with a description
            Arguments.of(DeprecatedAttributes.builder().setDescription("Use something else").build(), "[Deprecated. Use something else]"),
            // Case 6: Deprecated for removal with a description
            Arguments.of(DeprecatedAttributes.builder().setForRemoval(true).setDescription("Use something else").build(), "[Deprecated for removal. Use something else]"),
            // Case 7: Deprecated with 'since' and description
            Arguments.of(DeprecatedAttributes.builder().setSince("then").setDescription("Use something else").build(), "[Deprecated since then. Use something else]"),
            // Case 8: Deprecated for removal with 'since' and description
            Arguments.of(DeprecatedAttributes.builder().setForRemoval(true).setSince("then").setDescription("Use something else").build(), "[Deprecated for removal since then. Use something else]")
        );
    }

    @DisplayName("COMPLEX_DEPRECATED_FORMAT should correctly format all deprecation attributes")
    @ParameterizedTest(name = "should format to: {1}")
    @MethodSource("provideComplexDeprecatedFormatCases")
    void complexDeprecatedFormatShouldRenderCorrectly(final DeprecatedAttributes deprecatedAttributes, final String expectedFormat) {
        // Test with an option that has no separate description
        final Option optionWithoutDesc = Option.builder("o").deprecated(deprecatedAttributes).build();
        assertEquals(expectedFormat, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithoutDesc));

        // Test with an option that also has a separate description
        final String description = "The description";
        final Option optionWithDesc = Option.builder("o").desc(description).deprecated(deprecatedAttributes).build();
        final String expectedFormatWithDesc = expectedFormat + " " + description;
        assertEquals(expectedFormatWithDesc, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithDesc));
    }

    @Test
    @DisplayName("toOptional() should wrap text with default delimiters '[]'")
    void toOptionalShouldUseDefaultDelimiters() {
        final Option option = Option.builder("o").hasArg().build();
        final OptionFormatter formatter = OptionFormatter.from(option);

        assertEquals("[what]", formatter.toOptional("what"));
    }

    @Test
    @DisplayName("toOptional() should return an empty string for null or empty input")
    void toOptionalShouldHandleNullAndEmptyInput() {
        final Option option = Option.builder("o").hasArg().build();
        final OptionFormatter formatter = OptionFormatter.from(option);

        assertEquals("", formatter.toOptional(""), "Empty string should return an empty string");
        assertEquals("", formatter.toOptional(null), "Null should return an empty string");
    }

    @Test
    @DisplayName("toOptional() should wrap text with custom delimiters")
    void toOptionalShouldUseCustomDelimiters() {
        final Option option = Option.builder("o").hasArg().build();
        final OptionFormatter formatter = OptionFormatter.builder()
            .setOptionalDelimiters("-> ", " <-")
            .build(option);

        assertEquals("-> what <-", formatter.toOptional("what"));
    }
}