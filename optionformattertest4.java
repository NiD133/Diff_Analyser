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
 * Tests for the complex deprecated formatting logic in {@link OptionFormatter}.
 */
@DisplayName("OptionFormatter Complex Deprecated Format")
public class OptionComplexDeprecatedFormatTest {

    /**
     * Provides different combinations of deprecation attributes and their expected
     * formatted string output. Each test case is self-contained.
     *
     * @return A stream of arguments for the parameterized test.
     */
    static Stream<Arguments> deprecatedAttributesProvider() {
        return Stream.of(
            Arguments.of("Just deprecated",
                DeprecatedAttributes.builder().get(),
                "[Deprecated]"),

            Arguments.of("Deprecated with 'since'",
                DeprecatedAttributes.builder().setSince("now").get(),
                "[Deprecated since now]"),

            Arguments.of("Deprecated with 'for removal'",
                DeprecatedAttributes.builder().setForRemoval(true).get(),
                "[Deprecated for removal]"),

            Arguments.of("Deprecated with 'for removal' and 'since'",
                DeprecatedAttributes.builder().setForRemoval(true).setSince("now").get(),
                "[Deprecated for removal since now]"),

            Arguments.of("Deprecated with description",
                DeprecatedAttributes.builder().setDescription("Use something else").get(),
                "[Deprecated. Use something else]"),

            Arguments.of("Deprecated with 'for removal' and description",
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

    @ParameterizedTest(name = "{0}")
    @MethodSource("deprecatedAttributesProvider")
    @DisplayName("Should format deprecated option correctly")
    void testComplexDeprecationFormat(final String testName, final DeprecatedAttributes deprecatedAttributes, final String expectedPrefix) {
        // Test case 1: Option with only deprecation attributes
        final Option option = Option.builder("o").deprecated(deprecatedAttributes).get();
        assertEquals(expectedPrefix, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(option),
            "Format should be correct for option without its own description");

        // Test case 2: Option with deprecation attributes and its own description
        final String optionDesc = "The description";
        final Option optionWithDesc = Option.builder("o").desc(optionDesc).deprecated(deprecatedAttributes).get();
        final String expectedWithDesc = expectedPrefix + " " + optionDesc;
        assertEquals(expectedWithDesc, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithDesc),
            "Format should append the option's description");
    }

    /**
     * This test verifies the validation logic of {@code Option.Builder}, ensuring
     * that an option cannot be created with only a description. While this tests
     * {@code Option.Builder}, not {@code OptionFormatter}, it is preserved here
     * from the original test suite.
     */
    @Test
    @DisplayName("Building an Option with only a description should fail")
    void buildingOptionWithOnlyDescriptionShouldFail() {
        assertThrows(IllegalStateException.class, () -> Option.builder().desc("description").build(),
            "build() should throw when only description is set");

        assertThrows(IllegalStateException.class, () -> Option.builder().desc("description").get(),
            "get() should throw when only description is set");
    }
}