/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

import java.util.function.BiFunction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link OptionFormatter}.
 * This version has been refactored for clarity and maintainability.
 */
public class OptionFormatterTest {

    // ===================================================================================
    // Tests for Getter Methods
    // ===================================================================================

    @Test
    public void getOpt_whenOptionHasShortName_returnsPrefixedShortName() {
        // Arrange
        Option option = new Option("h", "help", false, "display help");
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        String shortOpt = formatter.getOpt();

        // Assert
        assertEquals("-h", shortOpt);
    }

    @Test
    public void getOpt_whenOptionHasNoShortName_returnsEmptyString() {
        // Arrange
        Option option = new Option(null, "help", false, "display help");
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        String shortOpt = formatter.getOpt();

        // Assert
        assertEquals("", shortOpt);
    }

    @Test
    public void getLongOpt_whenOptionHasLongName_returnsPrefixedLongName() {
        // Arrange
        Option option = new Option("h", "help", false, "display help");
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        String longOpt = formatter.getLongOpt();

        // Assert
        assertEquals("--help", longOpt);
    }

    @Test
    public void getLongOpt_whenOptionHasNoLongName_returnsEmptyString() {
        // Arrange
        Option option = new Option("h", null, false, "display help");
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        String longOpt = formatter.getLongOpt();

        // Assert
        assertEquals("", longOpt);
    }

    @Test
    public void getBothOpt_withBothNames_returnsBothNamesWithSeparator() {
        // Arrange
        Option option = new Option("h", "help", false, "display help");
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        String bothOpts = formatter.getBothOpt();

        // Assert
        assertEquals("-h, --help", bothOpts);
    }

    @Test
    public void getBothOpt_withOnlyShortName_returnsOnlyShortName() {
        // Arrange
        Option option = new Option("h", null, false, "display help");
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        String bothOpts = formatter.getBothOpt();

        // Assert
        assertEquals("-h", bothOpts);
    }
    
    @Test
    public void getBothOpt_withOnlyLongName_returnsOnlyLongName() {
        // Arrange
        Option option = new Option(null, "help", false, "display help");
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        String bothOpts = formatter.getBothOpt();

        // Assert
        assertEquals("--help", bothOpts);
    }

    @Test
    public void getBothOpt_withNoNames_returnsEmptyString() {
        // Arrange
        Option option = new Option(null, null, false, "no name option");
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        String bothOpts = formatter.getBothOpt();

        // Assert
        assertEquals("", bothOpts);
    }

    @Test
    public void getArgName_whenOptionHasArgAndName_returnsFormattedArgName() {
        // Arrange
        Option option = new Option("f", "file", true, "file to process");
        option.setArgName("path");
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        String argName = formatter.getArgName();

        // Assert
        assertEquals("<path>", argName);
    }

    @Test
    public void getArgName_whenOptionHasArgAndNoName_returnsDefaultFormattedArgName() {
        // Arrange
        Option option = new Option("f", "file", true, "file to process");
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        String argName = formatter.getArgName();

        // Assert
        assertEquals("<arg>", argName);
    }

    @Test
    public void getArgName_whenOptionHasNoArgs_returnsEmptyString() {
        // Arrange
        Option option = new Option("h", "help", false, "display help");
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        String argName = formatter.getArgName();

        // Assert
        assertEquals("", argName);
    }

    @Test
    public void getDescription_whenOptionHasDescription_returnsDescription() {
        // Arrange
        Option option = new Option("h", "help", false, "Show this help message");
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act & Assert
        assertEquals("Show this help message", formatter.getDescription());
    }

    @Test
    public void getDescription_whenOptionHasNoDescription_returnsEmptyString() {
        // Arrange
        Option option = new Option("v", "version", false, null);
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act & Assert
        assertEquals("", formatter.getDescription());
    }

    // ===================================================================================
    // Tests for State Checkers
    // ===================================================================================

    @Test
    public void isRequired_whenOptionIsRequired_returnsTrue() {
        // Arrange
        Option option = new Option("f", "file", true, "input file");
        option.setRequired(true);
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act & Assert
        assertTrue(formatter.isRequired());
    }

    @Test
    public void isRequired_whenOptionIsNotRequired_returnsFalse() {
        // Arrange
        Option option = new Option("h", "help", false, "display help");
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act & Assert
        assertFalse(formatter.isRequired());
    }

    // ===================================================================================
    // Tests for Syntax and Formatting
    // ===================================================================================

    @Test
    public void toOptional_withNonEmptyString_wrapsWithDefaultDelimiters() {
        // Arrange
        Option option = new Option("h", "help", false, "display help");
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        String optionalText = formatter.toOptional("-h, --help");

        // Assert
        assertEquals("[-h, --help]", optionalText);
    }

    @Test
    public void toOptional_withEmptyString_returnsEmptyString() {
        // Arrange
        Option option = new Option("h", "help", false, "display help");
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        String optionalText = formatter.toOptional("");

        // Assert
        assertEquals("", optionalText);
    }

    @Test
    public void toSyntaxOption_forOptionalOptionWithArg_isWrappedInBrackets() {
        // Arrange
        Option option = new Option("f", "file", true, "file to process");
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        String syntax = formatter.toSyntaxOption();

        // Assert
        assertEquals("[-f <arg>]", syntax);
    }

    @Test
    public void toSyntaxOption_whenForcedRequired_returnsSyntaxWithoutBrackets() {
        // Arrange
        Option option = new Option("f", "file", true, "file to process");
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        String syntax = formatter.toSyntaxOption(true);

        // Assert
        assertEquals("-f <arg>", syntax);
    }

    @Test
    public void toSyntaxOption_forRequiredOptionWhenForcedOptional_returnsSyntaxWithBrackets() {
        // Arrange
        Option option = new Option("f", "file", true, "file to process");
        option.setRequired(true);
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        String syntax = formatter.toSyntaxOption(false);

        // Assert
        assertEquals("[-f <arg>]", syntax);
    }

    @Test
    public void toSyntaxOption_forOptionWithNoNameAndNoArgs_returnsEmptyString() {
        // Arrange
        Option option = new Option(null, null, false, "no name option");
        OptionFormatter formatter = OptionFormatter.from(option);

        // Act
        String syntax = formatter.toSyntaxOption();

        // Assert
        assertEquals("", syntax);
    }

    // ===================================================================================
    // Tests for Builder Customizations
    // ===================================================================================

    @Test
    public void builder_withCustomLongOptPrefix_usesPrefixInOutput() {
        // Arrange
        Option option = new Option("f", "file", false, "file to process");
        OptionFormatter formatter = OptionFormatter.builder()
                .setLongOptPrefix("/")
                .build(option);

        // Act
        String longOpt = formatter.getLongOpt();

        // Assert
        assertEquals("/file", longOpt);
    }

    @Test
    public void builder_withCustomOptArgSeparator_usesSeparatorInOutput() {
        // Arrange
        Option option = new Option(null, "file", true, "file to process");
        OptionFormatter formatter = OptionFormatter.builder()
                .setOptArgSeparator("=")
                .build(option);

        // Act
        String syntax = formatter.toSyntaxOption();

        // Assert
        assertEquals("[--file=<arg>]", syntax);
    }

    @Test
    public void builder_withCustomDefaultArgName_usesNameForOptionsWithoutArgName() {
        // Arrange
        Option option = new Option(null, "file", true, "file to process");
        OptionFormatter formatter = OptionFormatter.builder()
                .setDefaultArgName("PATH")
                .build(option);

        // Act
        String syntax = formatter.toSyntaxOption();

        // Assert
        assertEquals("[--file <PATH>]", syntax);
    }

    @Test
    public void builder_withCustomSyntaxFunction_usesFunctionForSyntaxGeneration() {
        // Arrange
        Option option = new Option("v", "version", false, "show version");
        BiFunction<OptionFormatter, Boolean, String> customSyntax = (fmt, req) -> "VERSION_INFO";
        
        OptionFormatter formatter = OptionFormatter.builder()
                .setSyntaxFormatFunction(customSyntax)
                .build(option);

        // Act
        String syntax = formatter.toSyntaxOption();

        // Assert
        assertEquals("VERSION_INFO", syntax);
        assertNull(formatter.toSyntaxOption(false)); // Mockito mock in original returned null, replicating that behavior
    }

    // ===================================================================================
    // Tests for Null/Edge Cases
    // ===================================================================================

    @Test(expected = NullPointerException.class)
    public void from_nullOption_throwsNullPointerExceptionOnGetOpt() {
        OptionFormatter.from(null).getOpt();
    }

    @Test(expected = NullPointerException.class)
    public void from_nullOption_throwsNullPointerExceptionOnGetLongOpt() {
        OptionFormatter.from(null).getLongOpt();
    }

    @Test(expected = NullPointerException.class)
    public void from_nullOption_throwsNullPointerExceptionOnGetDescription() {
        OptionFormatter.from(null).getDescription();
    }

    @Test(expected = NullPointerException.class)
    public void from_nullOption_throwsNullPointerExceptionOnIsRequired() {
        OptionFormatter.from(null).isRequired();
    }

    @Test(expected = NullPointerException.class)
    public void from_nullOption_throwsNullPointerExceptionOnToSyntaxOption() {
        OptionFormatter.from(null).toSyntaxOption();
    }
}