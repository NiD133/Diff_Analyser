/*
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on the License.
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.cli.DeprecatedAttributes;
import org.apache.commons.cli.Option;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link OptionFormatter} functionality including formatting options,
 * handling deprecated attributes, and builder pattern operations.
 */
class OptionFormatterTest {

    // Test data constants for better maintainability
    private static final String SHORT_OPTION = "o";
    private static final String LONG_OPTION = "opt";
    private static final String CUSTOM_ARG_NAME = "other";
    private static final String SAMPLE_DESCRIPTION = "The description";
    private static final String DEPRECATION_DESCRIPTION = "Use something else";

    /**
     * Provides test data for various deprecated attribute combinations.
     * Each test case contains a DeprecatedAttributes object and its expected formatted output.
     */
    public static Stream<Arguments> deprecatedAttributesData() {
        final List<Arguments> testCases = new ArrayList<>();
        final DeprecatedAttributes.Builder deprecatedBuilder = DeprecatedAttributes.builder();

        // Basic deprecation without additional info
        testCases.add(Arguments.of(deprecatedBuilder.get(), "[Deprecated]"));

        // Deprecation with 'since' information
        deprecatedBuilder.setSince("now");
        testCases.add(Arguments.of(deprecatedBuilder.get(), "[Deprecated since now]"));

        // Deprecation for removal with 'since' information
        deprecatedBuilder.setForRemoval(true);
        testCases.add(Arguments.of(deprecatedBuilder.get(), "[Deprecated for removal since now]"));

        // Deprecation for removal without 'since' information
        deprecatedBuilder.setSince(null);
        testCases.add(Arguments.of(deprecatedBuilder.get(), "[Deprecated for removal]"));

        // Deprecation with description but not for removal
        deprecatedBuilder.setForRemoval(false);
        deprecatedBuilder.setDescription(DEPRECATION_DESCRIPTION);
        testCases.add(Arguments.of(deprecatedBuilder.get(), "[Deprecated. Use something else]"));

        // Deprecation for removal with description
        deprecatedBuilder.setForRemoval(true);
        testCases.add(Arguments.of(deprecatedBuilder.get(), "[Deprecated for removal. Use something else]"));

        // Deprecation with 'since' and description
        deprecatedBuilder.setForRemoval(false);
        deprecatedBuilder.setSince("then");
        testCases.add(Arguments.of(deprecatedBuilder.get(), "[Deprecated since then. Use something else]"));

        // Complete deprecation info: for removal, with 'since' and description
        deprecatedBuilder.setForRemoval(true);
        testCases.add(Arguments.of(deprecatedBuilder.get(), "[Deprecated for removal since then. Use something else]"));

        return testCases.stream();
    }

    /**
     * Helper method to verify that two OptionFormatter instances produce equivalent output
     * across all their public methods.
     */
    private void assertFormattersAreEquivalent(final OptionFormatter formatter1, final OptionFormatter formatter2) {
        assertEquals(formatter1.toSyntaxOption(), formatter2.toSyntaxOption(), "toSyntaxOption() should match");
        assertEquals(formatter1.toSyntaxOption(true), formatter2.toSyntaxOption(true), "toSyntaxOption(true) should match");
        assertEquals(formatter1.toSyntaxOption(false), formatter2.toSyntaxOption(false), "toSyntaxOption(false) should match");
        assertEquals(formatter1.getOpt(), formatter2.getOpt(), "getOpt() should match");
        assertEquals(formatter1.getLongOpt(), formatter2.getLongOpt(), "getLongOpt() should match");
        assertEquals(formatter1.getBothOpt(), formatter2.getBothOpt(), "getBothOpt() should match");
        assertEquals(formatter1.getDescription(), formatter2.getDescription(), "getDescription() should match");
        assertEquals(formatter1.getArgName(), formatter2.getArgName(), "getArgName() should match");
        assertEquals(formatter1.toOptional("foo"), formatter2.toOptional("foo"), "toOptional() should match");
    }

    @Test
    void testOptionalTextWrapping() {
        // Test with default optional delimiters
        final Option optionWithArg = createBasicOptionWithArg();
        OptionFormatter formatter = OptionFormatter.from(optionWithArg);
        
        assertEquals("[what]", formatter.toOptional("what"), "Should wrap text in default brackets");
        assertEquals("", formatter.toOptional(""), "Empty string should return empty string");
        assertEquals("", formatter.toOptional(null), "Null should return empty string");

        // Test with custom optional delimiters
        formatter = OptionFormatter.builder()
                .setOptionalDelimiters("-> ", " <-")
                .build(optionWithArg);
        assertEquals("-> what <-", formatter.toOptional("what"), "Should wrap text in custom delimiters");
    }

    @Test
    void testSyntaxOptionFormatting() {
        OptionFormatter formatter;

        // Optional option with argument
        Option option = createBasicOptionWithArg();
        formatter = OptionFormatter.from(option);
        assertEquals("[-o <arg>]", formatter.toSyntaxOption(), "Optional option with default arg name");

        // Optional option with custom argument name
        option = createOptionWithCustomArgName();
        formatter = OptionFormatter.from(option);
        assertEquals("[-o <other>]", formatter.toSyntaxOption(), "Optional option with custom arg name");

        // Required option with custom argument name
        option = createRequiredOptionWithCustomArgName();
        formatter = OptionFormatter.from(option);
        assertEquals("-o <other>", formatter.toSyntaxOption(), "Required option with custom arg name");

        // Required option without argument
        option = createRequiredOptionWithoutArg();
        formatter = OptionFormatter.from(option);
        assertEquals("-o", formatter.toSyntaxOption(), "Required option without argument");

        // Optional short option without argument
        option = createOptionalShortOptionWithoutArg();
        formatter = OptionFormatter.from(option);
        assertEquals("[-o]", formatter.toSyntaxOption(), "Optional short option without argument");

        // Optional long option with argument
        option = createOptionalLongOptionWithArg();
        formatter = OptionFormatter.from(option);
        assertEquals("[--opt <other>]", formatter.toSyntaxOption(), "Optional long option with argument");

        // Required long option with argument
        option = createRequiredLongOptionWithArg();
        formatter = OptionFormatter.from(option);
        assertEquals("--opt <other>", formatter.toSyntaxOption(), "Required long option with argument");

        // Multi-character short option
        option = createMultiCharShortOption();
        formatter = OptionFormatter.from(option);
        assertEquals("[-ot <arg>]", formatter.toSyntaxOption(), "Multi-character short option");
    }

    @Test
    void testOptionBuilderValidation_MissingOptionIdentifier() {
        // Test CLI-343: Option builder should require either short or long option
        assertThrows(IllegalStateException.class, 
                () -> Option.builder().required(false).build(),
                "Should throw when building option without short or long option identifier");
        
        assertThrows(IllegalStateException.class, 
                () -> Option.builder().required(false).get(),
                "Should throw when getting option without short or long option identifier");
    }

    @Test
    void testOptionBuilderValidation_DescriptionOnly() {
        // Test CLI-343: Option builder should require either short or long option even with description
        assertThrows(IllegalStateException.class, 
                () -> Option.builder().desc(SAMPLE_DESCRIPTION).build(),
                "Should throw when building option with only description");
        
        assertThrows(IllegalStateException.class, 
                () -> Option.builder().desc(SAMPLE_DESCRIPTION).get(),
                "Should throw when getting option with only description");
    }

    @ParameterizedTest(name = "Deprecated attributes: {1}")
    @MethodSource("deprecatedAttributesData")
    void testComplexDeprecationFormatting(final DeprecatedAttributes deprecatedAttributes, final String expectedFormat) {
        // Test option without description
        final Option optionWithoutDesc = Option.builder(SHORT_OPTION).deprecated(deprecatedAttributes).get();
        assertEquals(expectedFormat, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithoutDesc),
                "Complex deprecation format should match expected output");

        // Test option with description
        final Option optionWithDesc = Option.builder(SHORT_OPTION)
                .desc(SAMPLE_DESCRIPTION)
                .deprecated(deprecatedAttributes)
                .get();
        assertEquals(expectedFormat + " " + SAMPLE_DESCRIPTION, 
                OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(optionWithDesc),
                "Complex deprecation format with description should append description");
    }

    @Test
    void testBuilderCopyConstructor() {
        // Create a fully customized formatter
        final Function<Option, String> customDeprecationFunction = option -> "Ooo Deprecated";
        final BiFunction<OptionFormatter, Boolean, String> customSyntaxFunction = (formatter, required) -> "Yep, it worked";
        
        final OptionFormatter.Builder originalBuilder = OptionFormatter.builder()
                .setLongOptPrefix("l")
                .setOptPrefix("s")
                .setArgumentNameDelimiters("{", "}")
                .setDefaultArgName("Some Argument")
                .setOptSeparator(" and ")
                .setOptionalDelimiters("?>", "<?")
                .setSyntaxFormatFunction(customSyntaxFunction)
                .setDeprecatedFormatFunction(customDeprecationFunction);

        // Test with regular option
        Option regularOption = Option.builder(SHORT_OPTION).longOpt(LONG_OPTION).get();
        OptionFormatter originalFormatter = originalBuilder.build(regularOption);
        OptionFormatter.Builder copiedBuilder = new OptionFormatter.Builder(originalFormatter);
        assertFormattersAreEquivalent(originalFormatter, copiedBuilder.build(regularOption));

        // Test with deprecated required option
        Option deprecatedRequiredOption = Option.builder(SHORT_OPTION)
                .longOpt(LONG_OPTION)
                .deprecated()
                .required()
                .get();
        originalFormatter = originalBuilder.build(deprecatedRequiredOption);
        copiedBuilder = new OptionFormatter.Builder(originalFormatter);
        assertFormattersAreEquivalent(originalFormatter, copiedBuilder.build(deprecatedRequiredOption));
    }

    @Test
    void testSyntaxFormattingWithRequiredOverride() {
        // Test optional option formatted as required and vice versa
        Option optionalOption = createBasicOptionWithArg();
        OptionFormatter formatter = OptionFormatter.from(optionalOption);
        assertEquals("[-o <arg>]", formatter.toSyntaxOption(), "Default formatting for optional option");
        assertEquals("-o <arg>", formatter.toSyntaxOption(true), "Force required formatting");

        Option requiredOption = createRequiredOptionWithCustomArgName();
        formatter = OptionFormatter.from(requiredOption);
        assertEquals("-o <other>", formatter.toSyntaxOption(), "Default formatting for required option");
        assertEquals("[-o <other>]", formatter.toSyntaxOption(false), "Force optional formatting");
    }

    @Test
    void testBothOptFormatting() {
        // Test option with both short and long forms
        Option bothOptions = createBasicOptionWithArg();
        OptionFormatter formatter = OptionFormatter.from(bothOptions);
        assertEquals("-o, --opt", formatter.getBothOpt(), "Should show both options separated by comma");

        // Test long option only
        Option longOnlyOption = createOptionalLongOptionWithArg();
        formatter = OptionFormatter.from(longOnlyOption);
        assertEquals("--opt", formatter.getBothOpt(), "Should show only long option");

        // Test short option only
        Option shortOnlyOption = createOptionalShortOptionWithoutArg();
        formatter = OptionFormatter.from(shortOnlyOption);
        assertEquals("-o", formatter.getBothOpt(), "Should show only short option");
    }

    @Test
    void testDescriptionFormattingWithDeprecation() {
        final Option normalOption = createOptionWithDescription();
        final Option deprecatedOption = createDeprecatedOptionWithDescription();
        final Option complexDeprecatedOption = createComplexDeprecatedOptionWithDescription();

        // Test with no deprecation formatting
        assertEquals(SAMPLE_DESCRIPTION, OptionFormatter.from(normalOption).getDescription(), 
                "Normal option should show plain description");
        assertEquals(SAMPLE_DESCRIPTION, OptionFormatter.from(deprecatedOption).getDescription(), 
                "Deprecated option with no deprecation formatter should show plain description");
        assertEquals(SAMPLE_DESCRIPTION, OptionFormatter.from(complexDeprecatedOption).getDescription(), 
                "Complex deprecated option with no deprecation formatter should show plain description");

        // Test with simple deprecation formatting
        OptionFormatter.Builder simpleDeprecationBuilder = OptionFormatter.builder()
                .setDeprecatedFormatFunction(OptionFormatter.SIMPLE_DEPRECATED_FORMAT);

        assertEquals(SAMPLE_DESCRIPTION, simpleDeprecationBuilder.build(normalOption).getDescription(), 
                "Normal option with simple deprecation formatter should show plain description");
        assertEquals("[Deprecated] " + SAMPLE_DESCRIPTION, simpleDeprecationBuilder.build(deprecatedOption).getDescription(), 
                "Deprecated option with simple formatter should show deprecation prefix");
        assertEquals("[Deprecated] " + SAMPLE_DESCRIPTION, simpleDeprecationBuilder.build(complexDeprecatedOption).getDescription(), 
                "Complex deprecated option with simple formatter should show simple deprecation prefix");

        // Test with complex deprecation formatting
        OptionFormatter.Builder complexDeprecationBuilder = OptionFormatter.builder()
                .setDeprecatedFormatFunction(OptionFormatter.COMPLEX_DEPRECATED_FORMAT);

        assertEquals(SAMPLE_DESCRIPTION, complexDeprecationBuilder.build(normalOption).getDescription(), 
                "Normal option with complex deprecation formatter should show plain description");
        assertEquals("[Deprecated] " + SAMPLE_DESCRIPTION, complexDeprecationBuilder.build(deprecatedOption).getDescription(), 
                "Simple deprecated option with complex formatter should show simple deprecation");
        assertEquals("[Deprecated for removal since now. Use something else] " + SAMPLE_DESCRIPTION, 
                complexDeprecationBuilder.build(complexDeprecatedOption).getDescription(),
                "Complex deprecated option with complex formatter should show full deprecation info");
    }

    @Test
    void testArgumentNameDelimiterCustomization() {
        final Option optionWithArg = createBasicOptionWithArg();
        
        // Test custom delimiters
        OptionFormatter.Builder builder = OptionFormatter.builder()
                .setArgumentNameDelimiters("with argument named ", ".");
        assertEquals("with argument named arg.", builder.build(optionWithArg).getArgName(),
                "Should use custom argument delimiters");

        // Test null beginning delimiter
        builder = OptionFormatter.builder().setArgumentNameDelimiters(null, "");
        assertEquals("arg", builder.build(optionWithArg).getArgName(),
                "Should handle null beginning delimiter");

        // Test null ending delimiter
        builder = OptionFormatter.builder().setArgumentNameDelimiters("", null);
        assertEquals("arg", builder.build(optionWithArg).getArgName(),
                "Should handle null ending delimiter");
    }

    @Test
    void testDefaultArgumentNameCustomization() {
        final Option optionWithArg = createBasicOptionWithArg();
        
        // Test custom default argument name
        OptionFormatter.Builder builder = OptionFormatter.builder().setDefaultArgName("foo");
        assertEquals("<foo>", builder.build(optionWithArg).getArgName(),
                "Should use custom default argument name");

        // Test empty default argument name falls back to default
        builder = OptionFormatter.builder().setDefaultArgName("");
        assertEquals("<arg>", builder.build(optionWithArg).getArgName(),
                "Empty default argument name should fall back to 'arg'");

        // Test null default argument name falls back to default
        builder = OptionFormatter.builder().setDefaultArgName(null);
        assertEquals("<arg>", builder.build(optionWithArg).getArgName(),
                "Null default argument name should fall back to 'arg'");
    }

    @Test
    void testLongOptionPrefixCustomization() {
        final Option optionWithLongOpt = createBasicOptionWithArg();
        
        // Test custom long option prefix
        OptionFormatter.Builder builder = OptionFormatter.builder().setLongOptPrefix("fo");
        assertEquals("foopt", builder.build(optionWithLongOpt).getLongOpt(),
                "Should use custom long option prefix");

        // Test empty prefix
        builder = OptionFormatter.builder().setLongOptPrefix("");
        assertEquals("opt", builder.build(optionWithLongOpt).getLongOpt(),
                "Empty prefix should show bare long option");

        // Test null prefix
        builder = OptionFormatter.builder().setLongOptPrefix(null);
        assertEquals("opt", builder.build(optionWithLongOpt).getLongOpt(),
                "Null prefix should show bare long option");
    }

    @Test
    void testOptionArgumentSeparatorCustomization() {
        final Option optionWithArg = createBasicOptionWithArg();
        
        // Test custom separator
        OptionFormatter.Builder builder = OptionFormatter.builder()
                .setOptArgSeparator(" with argument named ");
        assertEquals("[-o with argument named <arg>]", builder.build(optionWithArg).toSyntaxOption(),
                "Should use custom option-argument separator");

        // Test null separator (no space)
        builder = OptionFormatter.builder().setOptArgSeparator(null);
        assertEquals("[-o<arg>]", builder.build(optionWithArg).toSyntaxOption(),
                "Null separator should concatenate option and argument");

        // Test equals separator
        builder = OptionFormatter.builder().setOptArgSeparator("=");
        assertEquals("[-o=<arg>]", builder.build(optionWithArg).toSyntaxOption(),
                "Should use equals as separator");
    }

    @Test
    void testOptionSeparatorCustomization() {
        final Option optionWithBoth = createBasicOptionWithArg();
        
        // Test custom separator
        OptionFormatter.Builder builder = OptionFormatter.builder().setOptSeparator(" and ");
        assertEquals("-o and --opt", builder.build(optionWithBoth).getBothOpt(),
                "Should use custom option separator");

        // Test empty separator falls back to concatenation
        builder = OptionFormatter.builder().setOptSeparator("");
        assertEquals("-o--opt", builder.build(optionWithBoth).getBothOpt(),
                "Empty separator should concatenate options");

        // Test null separator falls back to concatenation
        builder = OptionFormatter.builder().setOptSeparator(null);
        assertEquals("-o--opt", builder.build(optionWithBoth).getBothOpt(),
                "Null separator should concatenate options");
    }

    @Test
    void testCustomSyntaxFormatFunction() {
        final BiFunction<OptionFormatter, Boolean, String> customFunction = 
                (formatter, required) -> "Yep, it worked";
        final Option option = createBasicOptionWithArg();

        // Test custom syntax format function
        OptionFormatter.Builder builder = OptionFormatter.builder()
                .setSyntaxFormatFunction(customFunction);
        assertEquals("Yep, it worked", builder.build(option).toSyntaxOption(),
                "Should use custom syntax format function");

        // Test null function falls back to default
        builder = OptionFormatter.builder().setSyntaxFormatFunction(null);
        assertEquals("[-o <arg>]", builder.build(option).toSyntaxOption(),
                "Null function should use default formatting");
    }

    // Helper methods to create test options with clear, descriptive names

    private Option createBasicOptionWithArg() {
        return Option.builder().option(SHORT_OPTION).longOpt(LONG_OPTION).hasArg().get();
    }

    private Option createOptionWithCustomArgName() {
        return Option.builder().option(SHORT_OPTION).longOpt(LONG_OPTION).hasArg().argName(CUSTOM_ARG_NAME).get();
    }

    private Option createRequiredOptionWithCustomArgName() {
        return Option.builder().option(SHORT_OPTION).longOpt(LONG_OPTION).hasArg().required().argName(CUSTOM_ARG_NAME).get();
    }

    private Option createRequiredOptionWithoutArg() {
        return Option.builder().option(SHORT_OPTION).longOpt(LONG_OPTION).required().argName(CUSTOM_ARG_NAME).get();
    }

    private Option createOptionalShortOptionWithoutArg() {
        return Option.builder().option(SHORT_OPTION).argName(CUSTOM_ARG_NAME).get();
    }

    private Option createOptionalLongOptionWithArg() {
        return Option.builder().longOpt(LONG_OPTION).hasArg().argName(CUSTOM_ARG_NAME).get();
    }

    private Option createRequiredLongOptionWithArg() {
        return Option.builder().longOpt(LONG_OPTION).required().hasArg().argName(CUSTOM_ARG_NAME).get();
    }

    private Option createMultiCharShortOption() {
        return Option.builder().option("ot").longOpt(LONG_OPTION).hasArg().get();
    }

    private Option createOptionWithDescription() {
        return Option.builder().option(SHORT_OPTION).longOpt("one").hasArg().desc(SAMPLE_DESCRIPTION).get();
    }

    private Option createDeprecatedOptionWithDescription() {
        return Option.builder().option(SHORT_OPTION).longOpt("one").hasArg().desc(SAMPLE_DESCRIPTION).deprecated().get();
    }

    private Option createComplexDeprecatedOptionWithDescription() {
        return Option.builder().option(SHORT_OPTION).longOpt("one").hasArg().desc(SAMPLE_DESCRIPTION)
                .deprecated(DeprecatedAttributes.builder()
                        .setForRemoval(true)
                        .setSince("now")
                        .setDescription(DEPRECATION_DESCRIPTION)
                        .get())
                .get();
    }
}