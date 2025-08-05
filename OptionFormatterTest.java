/*
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.BiFunction;
import java.util.function.Function;
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
 * Tests {@link OptionFormatter}.
 */
@DisplayName("OptionFormatter Test")
class OptionFormatterTest {

    /**
     * Provides different deprecation attribute configurations and their expected string representation.
     * This data is used to test the COMPLEX_DEPRECATED_FORMAT function.
     */
    public static Stream<Arguments> deprecatedAttributesProvider() {
        return Stream.of(
            Arguments.of(DeprecatedAttributes.builder().get(), "[Deprecated]", "Simple deprecation"),
            Arguments.of(DeprecatedAttributes.builder().setSince("now").get(), "[Deprecated since now]", "With 'since'"),
            Arguments.of(DeprecatedAttributes.builder().setForRemoval(true).get(), "[Deprecated for removal]", "With 'for removal'"),
            Arguments.of(DeprecatedAttributes.builder().setForRemoval(true).setSince("now").get(), "[Deprecated for removal since now]", "With 'for removal' and 'since'"),
            Arguments.of(DeprecatedAttributes.builder().setDescription("Use X").get(), "[Deprecated. Use X]", "With description"),
            Arguments.of(DeprecatedAttributes.builder().setForRemoval(true).setDescription("Use X").get(), "[Deprecated for removal. Use X]", "With 'for removal' and description"),
            Arguments.of(DeprecatedAttributes.builder().setSince("then").setDescription("Use X").get(), "[Deprecated since then. Use X]", "With 'since' and description"),
            Arguments.of(DeprecatedAttributes.builder().setForRemoval(true).setSince("then").setDescription("Use X").get(), "[Deprecated for removal since then. Use X]", "With all attributes")
        );
    }

    /**
     * Provides different option configurations and their expected syntax string.
     */
    private static Stream<Arguments> syntaxOptionProvider() {
        return Stream.of(
            Arguments.of("Optional with short, long, and arg", Option.builder("o").longOpt("opt").hasArg().build(), "[-o <arg>]"),
            Arguments.of("Optional with short, long, and custom arg name", Option.builder("o").longOpt("opt").hasArg().argName("other").build(), "[-o <other>]"),
            Arguments.of("Required with short, long, and custom arg name", Option.builder("o").longOpt("opt").hasArg().required().argName("other").build(), "-o <other>"),
            Arguments.of("Required with short, long, no arg", Option.builder("o").longOpt("opt").required().build(), "-o"),
            Arguments.of("Optional with short only, no arg", Option.builder("o").build(), "[-o]"),
            Arguments.of("Optional with long only and custom arg name", Option.builder().longOpt("opt").hasArg().argName("other").build(), "[--opt <other>]"),
            Arguments.of("Required with long only and custom arg name", Option.builder().longOpt("opt").required().hasArg().argName("other").build(), "--opt <other>"),
            Arguments.of("Optional with multi-char short opt and arg", Option.builder("ot").longOpt("opt").hasArg().build(), "[-ot <arg>]")
        );
    }

    /**
     * Asserts that two formatters produce identical output for all their methods.
     */
    private void assertEquivalent(final OptionFormatter formatter1, final OptionFormatter formatter2) {
        assertEquals(formatter1.toSyntaxOption(), formatter2.toSyntaxOption());
        assertEquals(formatter1.toSyntaxOption(true), formatter2.toSyntaxOption(true));
        assertEquals(formatter1.toSyntaxOption(false), formatter2.toSyntaxOption(false));
        assertEquals(formatter1.getOpt(), formatter2.getOpt());
        assertEquals(formatter1.getLongOpt(), formatter2.getLongOpt());
        assertEquals(formatter1.getBothOpt(), formatter2.getBothOpt());
        assertEquals(formatter1.getDescription(), formatter2.getDescription());
        assertEquals(formatter1.getArgName(), formatter2.getArgName());
        assertEquals(formatter1.toOptional("foo"), formatter2.toOptional("foo"));
    }

    @Test
    @DisplayName("toOptional() should wrap text in configured delimiters")
    void testToOptional() {
        // Arrange
        final Option option = Option.builder("o").build();
        final OptionFormatter defaultFormatter = OptionFormatter.from(option);
        final OptionFormatter customFormatter = OptionFormatter.builder().setOptionalDelimiters("-> ", " <-").build(option);

        // Act & Assert for default delimiters
        assertEquals("[what]", defaultFormatter.toOptional("what"));
        assertEquals("", defaultFormatter.toOptional(""), "Empty string should return empty string");
        assertEquals("", defaultFormatter.toOptional(null), "Null should return empty string");

        // Act & Assert for custom delimiters
        assertEquals("-> what <-", customFormatter.toOptional("what"));
    }

    @DisplayName("toSyntaxOption() should generate correct syntax string for various option configurations")
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("syntaxOptionProvider")
    void testToSyntaxOption(final String testCaseName, final Option option, final String expectedSyntax) {
        // Arrange
        final OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        final String actualSyntax = formatter.toSyntaxOption();

        // Assert
        assertEquals(expectedSyntax, actualSyntax);
    }

    @DisplayName("COMPLEX_DEPRECATED_FORMAT function should produce detailed deprecation messages")
    @ParameterizedTest(name = "{2}")
    @MethodSource("deprecatedAttributesProvider")
    void testComplexDeprecatedFormatFunction(final DeprecatedAttributes deprecatedAttributes, final String expectedPrefix, final String testName) {
        // Arrange
        final Option optionWithoutDesc = Option.builder("o").deprecated(deprecatedAttributes).build();
        final Option optionWithDesc = Option.builder("o").desc("The description").deprecated(deprecatedAttributes).build();

        // Act & Assert for option without its own description
        assertEquals(expectedPrefix, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithoutDesc));

        // Act & Assert for option with its own description
        assertEquals(expectedPrefix + " The description", OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithDesc));
    }

    @Test
    @DisplayName("Builder copy constructor should create an equivalent builder")
    void testBuilderCopyConstructor() {
        // Arrange
        final Function<Option, String> customDeprecatedFunc = o -> "Ooo Deprecated";
        final BiFunction<OptionFormatter, Boolean, String> customSyntaxFunc = (o, b) -> "Yep, it worked";

        final OptionFormatter.Builder originalBuilder = OptionFormatter.builder()
            .setLongOptPrefix("l")
            .setOptPrefix("s")
            .setArgumentNameDelimiters("{", "}")
            .setDefaultArgName("Some Argument")
            .setOptSeparator(" and ")
            .setOptionalDelimiters("?>", "<?")
            .setSyntaxFormatFunction(customSyntaxFunc)
            .setDeprecatedFormatFunction(customDeprecatedFunc);

        final Option simpleOption = Option.builder("o").longOpt("opt").build();
        final Option complexOption = Option.builder("o").longOpt("opt").deprecated().required().build();

        // Create a formatter from the original builder to pass to the copy constructor
        final OptionFormatter formatterToCopy = originalBuilder.build(simpleOption);

        // Act
        final OptionFormatter.Builder copiedBuilder = new OptionFormatter.Builder(formatterToCopy);

        // Assert that formatters from both builders produce identical output for different options
        assertEquivalent(originalBuilder.build(simpleOption), copiedBuilder.build(simpleOption));
        assertEquivalent(originalBuilder.build(complexOption), copiedBuilder.build(complexOption));
    }

    @Test
    @DisplayName("toSyntaxOption(boolean) should respect the required override flag")
    void testToSyntaxOptionWithRequiredOverride() {
        // Arrange
        final Option optionalOption = Option.builder("o").longOpt("opt").hasArg().build(); // Optional by default
        final Option requiredOption = Option.builder("o").longOpt("opt").hasArg().required().build(); // Required by default

        final OptionFormatter optionalFormatter = OptionFormatter.from(optionalOption);
        final OptionFormatter requiredFormatter = OptionFormatter.from(requiredOption);

        // Act & Assert
        assertEquals("-o <arg>", optionalFormatter.toSyntaxOption(true), "Optional option should be rendered as required");
        assertEquals("[-o <arg>]", optionalFormatter.toSyntaxOption(false), "Optional option should be rendered as optional");
        assertEquals("[-o <arg>]", requiredFormatter.toSyntaxOption(false), "Required option should be rendered as optional");
        assertEquals("-o <arg>", requiredFormatter.toSyntaxOption(true), "Required option should be rendered as required");
    }

    @Test
    @DisplayName("getBothOpt() should correctly format short and long options")
    void testGetBothOpt() {
        // Arrange
        final Option both = Option.builder("o").longOpt("opt").build();
        final Option longOnly = Option.builder().longOpt("opt").build();
        final Option shortOnly = Option.builder("o").build();

        // Act & Assert
        assertEquals("-o, --opt", OptionFormatter.from(both).getBothOpt());
        assertEquals("--opt", OptionFormatter.from(longOnly).getBothOpt());
        assertEquals("-o", OptionFormatter.from(shortOnly).getBothOpt());
    }

    @Nested
    @DisplayName("Description Formatting Tests")
    class DescriptionFormattingTest {

        private final Option normalOption = Option.builder("o").desc("The description").build();
        private final Option simpleDeprecatedOption = Option.builder("o").desc("The description").deprecated().build();
        private final Option complexDeprecatedOption = Option.builder("o").desc("The description")
            .deprecated(DeprecatedAttributes.builder().setForRemoval(true).setSince("now").setDescription("Use something else").get()).build();

        @Test
        @DisplayName("Default formatter should return raw description for all options")
        void testGetDescriptionWithDefaultFormatter() {
            assertEquals("The description", OptionFormatter.from(normalOption).getDescription(), "Normal option");
            assertEquals("The description", OptionFormatter.from(simpleDeprecatedOption).getDescription(), "Simple deprecated option");
            assertEquals("The description", OptionFormatter.from(complexDeprecatedOption).getDescription(), "Complex deprecated option");
        }

        @Test
        @DisplayName("Simple deprecated formatter should prefix description for deprecated options")
        void testGetDescriptionWithSimpleDeprecatedFormatter() {
            // Arrange
            final OptionFormatter.Builder builder = OptionFormatter.builder().setDeprecatedFormatFunction(OptionFormatter.SIMPLE_DEPRECATED_FORMAT);

            // Act & Assert
            assertEquals("The description", builder.build(normalOption).getDescription(), "Normal option should not be affected");
            assertEquals("[Deprecated] The description", builder.build(simpleDeprecatedOption).getDescription(), "Simple deprecated option should be prefixed");
            assertEquals("[Deprecated] The description", builder.build(complexDeprecatedOption).getDescription(), "Complex deprecated option should be treated as simple");
        }

        @Test
        @DisplayName("Complex deprecated formatter should add detailed prefix for deprecated options")
        void testGetDescriptionWithComplexDeprecatedFormatter() {
            // Arrange
            final OptionFormatter.Builder builder = OptionFormatter.builder().setDeprecatedFormatFunction(OptionFormatter.COMPLEX_DEPRECATED_FORMAT);

            // Act & Assert
            assertEquals("The description", builder.build(normalOption).getDescription(), "Normal option should not be affected");
            assertEquals("[Deprecated] The description", builder.build(simpleDeprecatedOption).getDescription(), "Simple deprecated option should get basic prefix");
            assertEquals("[Deprecated for removal since now. Use something else] The description",
                builder.build(complexDeprecatedOption).getDescription(), "Complex deprecated option should get detailed prefix");
        }
    }

    @Nested
    @DisplayName("Builder Customization Tests")
    class BuilderCustomizationTest {

        private final Option testOption = Option.builder("o").longOpt("opt").hasArg().build();

        @Test
        @DisplayName("setArgumentNameDelimiters should customize argument name format")
        void testSetArgumentNameDelimiters() {
            assertEquals("with argument named arg.", OptionFormatter.builder().setArgumentNameDelimiters("with argument named ", ".").build(testOption).getArgName());
            assertEquals("arg", OptionFormatter.builder().setArgumentNameDelimiters(null, "").build(testOption).getArgName());
            assertEquals("arg", OptionFormatter.builder().setArgumentNameDelimiters("", null).build(testOption).getArgName());
        }

        @Test
        @DisplayName("setDefaultArgName should override the default 'arg'")
        void testSetDefaultArgName() {
            assertEquals("<foo>", OptionFormatter.builder().setDefaultArgName("foo").build(testOption).getArgName());
            assertEquals("<arg>", OptionFormatter.builder().setDefaultArgName("").build(testOption).getArgName(), "Empty name should fall back to default");
            assertEquals("<arg>", OptionFormatter.builder().setDefaultArgName(null).build(testOption).getArgName(), "Null name should fall back to default");
        }

        @Test
        @DisplayName("setLongOptPrefix should override the default '--'")
        void testSetLongOptPrefix() {
            assertEquals("foopt", OptionFormatter.builder().setLongOptPrefix("fo").build(testOption).getLongOpt());
            assertEquals("opt", OptionFormatter.builder().setLongOptPrefix("").build(testOption).getLongOpt());
            assertEquals("--opt", OptionFormatter.builder().setLongOptPrefix(null).build(testOption).getLongOpt(), "Null prefix should fall back to default");
        }

        @Test
        @DisplayName("setOptArgSeparator should customize the separator between option and argument")
        void testSetOptArgumentSeparator() {
            assertEquals("[-o with <arg>]", OptionFormatter.builder().setOptArgSeparator(" with ").build(testOption).toSyntaxOption());
            assertEquals("[-o=<arg>]", OptionFormatter.builder().setOptArgSeparator("=").build(testOption).toSyntaxOption());
            assertEquals("[-o<arg>]", OptionFormatter.builder().setOptArgSeparator(null).build(testOption).toSyntaxOption(), "Null separator should result in no space");
        }

        @Test
        @DisplayName("setOptSeparator should customize the separator between short and long options")
        void testSetOptSeparator() {
            assertEquals("-o and --opt", OptionFormatter.builder().setOptSeparator(" and ").build(testOption).getBothOpt());
            assertEquals("-o--opt", OptionFormatter.builder().setOptSeparator("").build(testOption).getBothOpt(), "Empty string should result in direct concatenation");
            assertEquals("-o, --opt", OptionFormatter.builder().setOptSeparator(null).build(testOption).getBothOpt(), "Null should fall back to default separator");
        }

        @Test
        @DisplayName("setSyntaxFormatFunction should allow complete override of syntax generation")
        void testSetSyntaxFormatFunction() {
            // Arrange
            final BiFunction<OptionFormatter, Boolean, String> customFunc = (o, b) -> "Custom Syntax";
            final OptionFormatter.Builder builder = OptionFormatter.builder();

            // Act & Assert for custom function
            assertEquals("Custom Syntax", builder.setSyntaxFormatFunction(customFunc).build(testOption).toSyntaxOption());

            // Act & Assert for null (restoring default behavior)
            assertEquals("[-o <arg>]", builder.setSyntaxFormatFunction(null).build(testOption).toSyntaxOption());
        }
    }
}