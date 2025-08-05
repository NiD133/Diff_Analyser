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

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.cli.DeprecatedAttributes;
import org.apache.commons.cli.Option;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link OptionFormatter}.
 */
class OptionFormatterTest {

    @Nested
    class SyntaxFormatting {

        @Test
        void toSyntaxOption_ShouldFormatOptionalShortOptionWithArg() {
            Option option = Option.builder("o").longOpt("opt").hasArg().build();
            OptionFormatter formatter = OptionFormatter.from(option);
            assertEquals("[-o <arg>]", formatter.toSyntaxOption());
        }

        @Test
        void toSyntaxOption_ShouldFormatOptionalShortOptionWithCustomArgName() {
            Option option = Option.builder("o").longOpt("opt").hasArg().argName("other").build();
            OptionFormatter formatter = OptionFormatter.from(option);
            assertEquals("[-o <other>]", formatter.toSyntaxOption());
        }

        @Test
        void toSyntaxOption_ShouldFormatRequiredShortOptionWithCustomArgName() {
            Option option = Option.builder("o").longOpt("opt").hasArg().required().argName("other").build();
            OptionFormatter formatter = OptionFormatter.from(option);
            assertEquals("-o <other>", formatter.toSyntaxOption());
        }

        @Test
        void toSyntaxOption_ShouldFormatRequiredShortOptionWithoutArg() {
            Option option = Option.builder("o").longOpt("opt").required().build();
            OptionFormatter formatter = OptionFormatter.from(option);
            assertEquals("-o", formatter.toSyntaxOption());
        }

        @Test
        void toSyntaxOption_ShouldFormatOptionalLongOptionWithArg() {
            Option option = Option.builder().longOpt("opt").hasArg().argName("other").build();
            OptionFormatter formatter = OptionFormatter.from(option);
            assertEquals("[--opt <other>]", formatter.toSyntaxOption());
        }

        @Test
        void toSyntaxOption_ShouldFormatMultiCharShortOptionWithArg() {
            Option option = Option.builder("ot").longOpt("opt").hasArg().build();
            OptionFormatter formatter = OptionFormatter.from(option);
            assertEquals("[-ot <arg>]", formatter.toSyntaxOption());
        }

        @Test
        void toSyntaxOption_ShouldFormatRequiredLongOptionWithArg() {
            Option option = Option.builder().longOpt("opt").required().hasArg().argName("other").build();
            OptionFormatter formatter = OptionFormatter.from(option);
            assertEquals("--opt <other>", formatter.toSyntaxOption());
        }

        @Test
        void toSyntaxOption_ShouldFormatOptionalShortOptionWithoutArg() {
            Option option = Option.builder("o").argName("other").build();
            OptionFormatter formatter = OptionFormatter.from(option);
            assertEquals("[-o]", formatter.toSyntaxOption());
        }

        @Test
        void toSyntaxOption_ShouldUseRequiredFormatWhenForced() {
            Option option = Option.builder("o").longOpt("opt").hasArg().build();
            OptionFormatter formatter = OptionFormatter.from(option);
            assertEquals("-o <arg>", formatter.toSyntaxOption(true));
        }

        @Test
        void toSyntaxOption_ShouldUseOptionalFormatWhenForced() {
            Option option = Option.builder("o").longOpt("opt").hasArg().required().build();
            OptionFormatter formatter = OptionFormatter.from(option);
            assertEquals("[-o <arg>]", formatter.toSyntaxOption(false));
        }

        @Test
        void toOptional_ShouldWrapTextInDelimiters() {
            Option option = Option.builder("o").longOpt("opt").build();
            OptionFormatter formatter = OptionFormatter.from(option);
            assertEquals("[what]", formatter.toOptional("what"));
        }

        @Test
        void toOptional_ShouldReturnEmptyStringForEmptyInput() {
            Option option = Option.builder("o").longOpt("opt").build();
            OptionFormatter formatter = OptionFormatter.from(option);
            assertEquals("", formatter.toOptional(""));
        }

        @Test
        void toOptional_ShouldReturnEmptyStringForNullInput() {
            Option option = Option.builder("o").longOpt("opt").build();
            OptionFormatter formatter = OptionFormatter.from(option);
            assertEquals("", formatter.toOptional(null));
        }

        @Test
        void toOptional_ShouldUseCustomDelimitersWhenConfigured() {
            Option option = Option.builder("o").longOpt("opt").build();
            OptionFormatter formatter = OptionFormatter.builder()
                .setOptionalDelimiters("-> ", " <-")
                .build(option);
            assertEquals("-> what <-", formatter.toOptional("what"));
        }
    }

    @Nested
    class DeprecationHandling {

        static Stream<Arguments> provideDeprecatedAttributesCases() {
            List<Arguments> cases = new ArrayList<>();
            cases.add(Arguments.of(
                "No attributes", 
                DeprecatedAttributes.builder().get(), 
                "[Deprecated]"
            ));
            cases.add(Arguments.of(
                "Since only", 
                DeprecatedAttributes.builder().setSince("now").get(), 
                "[Deprecated since now]"
            ));
            cases.add(Arguments.of(
                "For removal and since", 
                DeprecatedAttributes.builder().setSince("now").setForRemoval(true).get(), 
                "[Deprecated for removal since now]"
            ));
            cases.add(Arguments.of(
                "For removal only", 
                DeprecatedAttributes.builder().setForRemoval(true).get(), 
                "[Deprecated for removal]"
            ));
            cases.add(Arguments.of(
                "Description only", 
                DeprecatedAttributes.builder().setDescription("Use something else").get(), 
                "[Deprecated. Use something else]"
            ));
            cases.add(Arguments.of(
                "For removal and description", 
                DeprecatedAttributes.builder().setForRemoval(true).setDescription("Use something else").get(), 
                "[Deprecated for removal. Use something else]"
            ));
            cases.add(Arguments.of(
                "Since and description", 
                DeprecatedAttributes.builder().setSince("then").setDescription("Use something else").get(), 
                "[Deprecated since then. Use something else]"
            ));
            cases.add(Arguments.of(
                "All attributes", 
                DeprecatedAttributes.builder().setSince("then").setForRemoval(true).setDescription("Use something else").get(), 
                "[Deprecated for removal since then. Use something else]"
            ));
            return cases.stream();
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("provideDeprecatedAttributesCases")
        void complexDeprecatedFormat_ShouldGenerateCorrectMessage(
                String testCase, DeprecatedAttributes attributes, String expected) {
            Option option = Option.builder("o").deprecated(attributes).build();
            assertEquals(expected, OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(option));
        }

        @Test
        void complexDeprecatedFormat_ShouldAppendDescriptionWhenPresent() {
            DeprecatedAttributes attributes = DeprecatedAttributes.builder().get();
            Option option = Option.builder("o")
                .desc("The description")
                .deprecated(attributes)
                .build();
            assertEquals("[Deprecated] The description", 
                OptionFormatter.COMPLEX_DEPRECATED_FORMAT.apply(option));
        }

        @Test
        void getDescription_ShouldReturnOriginalDescriptionForNormalOption() {
            Option option = Option.builder("o")
                .desc("The description")
                .build();
            String description = OptionFormatter.from(option).getDescription();
            assertEquals("The description", description);
        }

        @Test
        void getDescription_ShouldIncludeSimpleDeprecationNotice() {
            Option option = Option.builder("o")
                .desc("The description")
                .deprecated()
                .build();
            OptionFormatter formatter = OptionFormatter.builder()
                .setDeprecatedFormatFunction(OptionFormatter.SIMPLE_DEPRECATED_FORMAT)
                .build(option);
            assertEquals("[Deprecated] The description", formatter.getDescription());
        }

        @Test
        void getDescription_ShouldIncludeComplexDeprecationNotice() {
            DeprecatedAttributes attributes = DeprecatedAttributes.builder()
                .setForRemoval(true)
                .setSince("now")
                .setDescription("Use something else")
                .get();
            Option option = Option.builder("o")
                .desc("The description")
                .deprecated(attributes)
                .build();
            OptionFormatter formatter = OptionFormatter.builder()
                .setDeprecatedFormatFunction(OptionFormatter.COMPLEX_DEPRECATED_FORMAT)
                .build(option);
            assertEquals(
                "[Deprecated for removal since now. Use something else] The description", 
                formatter.getDescription()
            );
        }
    }

    @Nested
    class BuilderBehavior {

        @Test
        void copyConstructor_ShouldCreateEquivalentFormatter() {
            // Setup original formatter with custom settings
            Option option = Option.builder("o").build();
            OptionFormatter original = OptionFormatter.builder()
                .setLongOptPrefix("l")
                .setOptPrefix("s")
                .setArgumentNameDelimiters("{", "}")
                .setDefaultArgName("foo")
                .setOptSeparator(" and ")
                .setOptionalDelimiters("?>", "<?")
                .build(option);

            // Create copy
            OptionFormatter copy = new OptionFormatter.Builder(original).build(option);

            // Verify equivalence
            assertEquals(original.toSyntaxOption(), copy.toSyntaxOption());
            assertEquals(original.toSyntaxOption(true), copy.toSyntaxOption(true));
            assertEquals(original.toSyntaxOption(false), copy.toSyntaxOption(false));
            assertEquals(original.getOpt(), copy.getOpt());
            assertEquals(original.getLongOpt(), copy.getLongOpt());
            assertEquals(original.getBothOpt(), copy.getBothOpt());
            assertEquals(original.getDescription(), copy.getDescription());
            assertEquals(original.getArgName(), copy.getArgName());
        }

        @Test
        void setArgumentNameDelimiters_ShouldAffectArgNameFormatting() {
            Option option = Option.builder("o").longOpt("opt").hasArg().build();
            OptionFormatter formatter = OptionFormatter.builder()
                .setArgumentNameDelimiters("with argument named ", ".")
                .build(option);
            assertEquals("with argument named arg.", formatter.getArgName());
        }

        @Test
        void setDefaultArgName_ShouldOverrideDefaultArgumentName() {
            Option option = Option.builder("o").longOpt("opt").hasArg().build();
            OptionFormatter formatter = OptionFormatter.builder()
                .setDefaultArgName("foo")
                .build(option);
            assertEquals("<foo>", formatter.getArgName());
        }

        @Test
        void setLongOptPrefix_ShouldAffectLongOptionFormatting() {
            Option option = Option.builder("o").longOpt("opt").build();
            OptionFormatter formatter = OptionFormatter.builder()
                .setLongOptPrefix("fo")
                .build(option);
            assertEquals("foopt", formatter.getLongOpt());
        }

        @Test
        void setOptArgSeparator_ShouldAffectSyntaxFormatting() {
            Option option = Option.builder("o").longOpt("opt").hasArg().build();
            OptionFormatter formatter = OptionFormatter.builder()
                .setOptArgSeparator("=")
                .build(option);
            assertEquals("[-o=<arg>]", formatter.toSyntaxOption());
        }

        @Test
        void setOptSeparator_ShouldAffectBothOptFormatting() {
            Option option = Option.builder("o").longOpt("opt").build();
            OptionFormatter formatter = OptionFormatter.builder()
                .setOptSeparator(" and ")
                .build(option);
            assertEquals("-o and --opt", formatter.getBothOpt());
        }

        @Test
        void setSyntaxFormatFunction_ShouldOverrideDefaultFormatting() {
            Option option = Option.builder("o").longOpt("opt").build();
            BiFunction<OptionFormatter, Boolean, String> customFormat = 
                (o, required) -> "Custom format";
            OptionFormatter formatter = OptionFormatter.builder()
                .setSyntaxFormatFunction(customFormat)
                .build(option);
            assertEquals("Custom format", formatter.toSyntaxOption());
        }
    }

    @Nested
    class GetterMethods {

        @Test
        void getBothOpt_ShouldCombineShortAndLongOptions() {
            Option option = Option.builder("o").longOpt("opt").build();
            OptionFormatter formatter = OptionFormatter.from(option);
            assertEquals("-o, --opt", formatter.getBothOpt());
        }

        @Test
        void getBothOpt_ShouldReturnOnlyLongOptionWhenShortMissing() {
            Option option = Option.builder().longOpt("opt").build();
            OptionFormatter formatter = OptionFormatter.from(option);
            assertEquals("--opt", formatter.getBothOpt());
        }

        @Test
        void getBothOpt_ShouldReturnOnlyShortOptionWhenLongMissing() {
            Option option = Option.builder("o").build();
            OptionFormatter formatter = OptionFormatter.from(option);
            assertEquals("-o", formatter.getBothOpt());
        }

        @Test
        void getArgName_ShouldFormatArgumentName() {
            Option option = Option.builder("o").longOpt("opt").hasArg().argName("file").build();
            OptionFormatter formatter = OptionFormatter.from(option);
            assertEquals("<file>", formatter.getArgName());
        }

        @Test
        void getArgName_ShouldUseDefaultWhenArgNameMissing() {
            Option option = Option.builder("o").longOpt("opt").hasArg().build();
            OptionFormatter formatter = OptionFormatter.from(option);
            assertEquals("<arg>", formatter.getArgName());
        }
    }
}