package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.apache.commons.cli.DeprecatedAttributes;
import org.apache.commons.cli.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for how OptionFormatter handles deprecated options and formats their descriptions.
 */
@DisplayName("OptionFormatter Deprecation Handling")
class OptionFormatterDeprecationTest {

    private static final String BASE_DESCRIPTION = "The description";

    /**
     * Tests the COMPLEX_DEPRECATED_FORMAT function directly. This format is used to generate
     * detailed deprecation messages.
     */
    @Nested
    @DisplayName("COMPLEX_DEPRECATED_FORMAT function")
    class ComplexDeprecatedFormatTest {

        // This data provider is now stateless and readable. Each entry is self-contained.
        static Stream<Arguments> provideDeprecatedAttributesAndExpectedStrings() {
            return Stream.of(
                Arguments.of("is deprecated",
                    DeprecatedAttributes.builder().get(),
                    "[Deprecated]"),
                Arguments.of("is deprecated with since",
                    DeprecatedAttributes.builder().setSince("now").get(),
                    "[Deprecated since now]"),
                Arguments.of("is deprecated for removal",
                    DeprecatedAttributes.builder().setForRemoval(true).get(),
                    "[Deprecated for removal]"),
                Arguments.of("is deprecated for removal with since",
                    DeprecatedAttributes.builder().setForRemoval(true).setSince("now").get(),
                    "[Deprecated for removal since now]"),
                Arguments.of("is deprecated with a reason",
                    DeprecatedAttributes.builder().setDescription("Use something else").get(),
                    "[Deprecated. Use something else]"),
                Arguments.of("is deprecated for removal with a reason",
                    DeprecatedAttributes.builder().setForRemoval(true).setDescription("Use something else").get(),
                    "[Deprecated for removal. Use something else]"),
                Arguments.of("is deprecated with since and a reason",
                    DeprecatedAttributes.builder().setSince("then").setDescription("Use something else").get(),
                    "[Deprecated since then. Use something else]"),
                Arguments.of("is deprecated for removal with since and a reason",
                    DeprecatedAttributes.builder().setForRemoval(true).setSince("then").setDescription("Use something else").get(),
                    "[Deprecated for removal since then. Use something else]")
            );
        }

        @ParameterizedTest(name = "An option that {0} should produce prefix: {2}")
        @MethodSource("provideDeprecatedAttributesAndExpectedStrings")
        @DisplayName("should generate correct deprecation prefix")
        void testComplexDeprecationFormat(final String testCaseName, final DeprecatedAttributes attributes, final String expectedPrefix) {
            // Test case 1: Option with only deprecation attributes
            final Option option = Option.builder("o").deprecated(attributes).get();
            assertEquals(expectedPrefix, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(option),
                "The format should be correct for an option with no base description.");

            // Test case 2: Option with deprecation attributes and a base description
            final Option optionWithDesc = Option.builder("o").desc(BASE_DESCRIPTION).deprecated(attributes).get();
            final String expectedFullMessage = expectedPrefix + " " + BASE_DESCRIPTION;
            assertEquals(expectedFullMessage, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithDesc),
                "The format should correctly prepend the prefix to the existing description.");
        }
    }

    /**
     * Tests how an OptionFormatter instance formats the final description string
     * based on its configured deprecation format function.
     */
    @Nested
    @DisplayName("getDescription() method with different formatters")
    class GetDescriptionTest {

        private Option normalOption;
        private Option simpleDeprecatedOption;
        private Option complexDeprecatedOption;

        @BeforeEach
        void setUp() {
            normalOption = Option.builder("o").desc(BASE_DESCRIPTION).get();
            simpleDeprecatedOption = Option.builder("d").desc(BASE_DESCRIPTION).deprecated().get();
            complexDeprecatedOption = Option.builder("c").desc(BASE_DESCRIPTION)
                .deprecated(DeprecatedAttributes.builder()
                    .setForRemoval(true)
                    .setSince("now")
                    .setDescription("Use something else")
                    .get())
                .get();
        }

        @Test
        @DisplayName("Default formatter should not add any deprecation notice")
        void testGetDescriptionWithDefaultFormatter() {
            // The default formatter (NO_DEPRECATED_FORMAT) should only return the original description.
            assertEquals(BASE_DESCRIPTION, OptionFormatter.from(normalOption).getDescription(),
                "Description for a normal option should not be modified.");
            assertEquals(BASE_DESCRIPTION, OptionFormatter.from(simpleDeprecatedOption).getDescription(),
                "Description for a deprecated option should not be modified by default.");
            assertEquals(BASE_DESCRIPTION, OptionFormatter.from(complexDeprecatedOption).getDescription(),
                "Description for a complex deprecated option should not be modified by default.");
        }

        @Test
        @DisplayName("Formatter with SIMPLE_DEPRECATED_FORMAT should add a basic notice")
        void testGetDescriptionWithSimpleFormat() {
            final OptionFormatter.Builder builder = OptionFormatter.builder()
                .setDeprecatedFormatFunction(OptionFormatter.SIMPLE_DEPRECATED_FORMAT);

            assertEquals(BASE_DESCRIPTION, builder.build(normalOption).getDescription(),
                "Normal option description should be unchanged.");
            assertEquals("[Deprecated] " + BASE_DESCRIPTION, builder.build(simpleDeprecatedOption).getDescription(),
                "Simple deprecated option should have the '[Deprecated]' prefix.");
            assertEquals("[Deprecated] " + BASE_DESCRIPTION, builder.build(complexDeprecatedOption).getDescription(),
                "Complex deprecated option should also have the simple '[Deprecated]' prefix.");
        }

        @Test
        @DisplayName("Formatter with COMPLEX_DEPRECATED_FORMAT should add a detailed notice")
        void testGetDescriptionWithComplexFormat() {
            final OptionFormatter.Builder builder = OptionFormatter.builder()
                .setDeprecatedFormatFunction(OptionFormatter.COMPLEX_DEPRECATED_FORMAT);

            final String expectedComplexNotice = "[Deprecated for removal since now. Use something else] " + BASE_DESCRIPTION;

            assertEquals(BASE_DESCRIPTION, builder.build(normalOption).getDescription(),
                "Normal option description should be unchanged.");
            assertEquals("[Deprecated] " + BASE_DESCRIPTION, builder.build(simpleDeprecatedOption).getDescription(),
                "Simple deprecated option should have a basic prefix even with the complex formatter.");
            assertEquals(expectedComplexNotice, builder.build(complexDeprecatedOption).getDescription(),
                "Complex deprecated option should have a detailed, multi-part prefix.");
        }
    }
}