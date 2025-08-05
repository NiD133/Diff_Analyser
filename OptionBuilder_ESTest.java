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
package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;

/**
 * Tests for {@link OptionBuilder}.
 *
 * Note: {@link OptionBuilder} uses static fields to configure and build {@link Option} instances.
 * This is a deprecated, stateful design. Each test method in this suite is self-contained and
 * concludes with a call to a {@code create} method, which correctly builds an option and
 * resets the builder's static state for the next test.
 */
public class OptionBuilderTest {

    // --- Basic Option Creation ---

    @Test
    public void create_shouldBuildOptionWithLongName() {
        // Act
        Option option = OptionBuilder.withLongOpt("long-name").create();

        // Assert
        assertNull("Short name should be null", option.getOpt());
        assertEquals("long-name", option.getLongOpt());
        assertEquals("Number of arguments should be uninitialized", Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void createWithChar_shouldBuildOptionWithShortName() {
        // Act
        Option option = OptionBuilder.create('s');

        // Assert
        assertEquals("s", option.getOpt());
        assertNull("Long name should be null", option.getLongOpt());
    }

    @Test
    public void createWithString_shouldBuildOptionWithShortName() {
        // Act
        Option option = OptionBuilder.create("s");

        // Assert
        assertEquals("s", option.getOpt());
        assertNull("Long name should be null", option.getLongOpt());
    }

    @Test
    public void withLongOpt_shouldCreateOptionWithShortAndLongName() {
        // Act
        Option option = OptionBuilder.withLongOpt("long-name").create('s');

        // Assert
        assertEquals("s", option.getOpt());
        assertEquals("long-name", option.getLongOpt());
    }

    // --- Argument Handling ---

    @Test
    public void hasArg_shouldCreateOptionWithSingleArgument() {
        // Act
        Option option = OptionBuilder.hasArg().create('a');

        // Assert
        assertTrue("Option should have an argument", option.hasArg());
        assertEquals("Number of arguments should be 1", 1, option.getArgs());
    }

    @Test
    public void hasArgWithFalse_shouldCreateOptionWithNoArgument() {
        // Act
        Option option = OptionBuilder.hasArg(false).create('a');

        // Assert
        assertFalse("Option should not have an argument", option.hasArg());
        assertEquals("Number of arguments should be 0", 0, option.getArgs());
    }

    @Test
    public void hasArgs_shouldCreateOptionWithUnlimitedArguments() {
        // Act
        Option option = OptionBuilder.hasArgs().create('a');

        // Assert
        assertTrue("Option should have arguments", option.hasArgs());
        assertEquals("Number of arguments should be unlimited", Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void hasArgsWithNumber_shouldCreateOptionWithFixedNumberOfArguments() {
        // Act
        Option option = OptionBuilder.hasArgs(3).create('a');

        // Assert
        assertTrue("Option should have arguments", option.hasArgs());
        assertEquals("Number of arguments should be 3", 3, option.getArgs());
    }

    // --- Optional Argument Handling ---

    @Test
    public void hasOptionalArg_shouldCreateOptionWithSingleOptionalArgument() {
        // Act
        Option option = OptionBuilder.hasOptionalArg().create('o');

        // Assert
        assertTrue("Option should have an optional argument", option.hasOptionalArg());
        assertEquals("Number of arguments should be 1", 1, option.getArgs());
    }

    @Test
    public void hasOptionalArgs_shouldCreateOptionWithUnlimitedOptionalArguments() {
        // Act
        Option option = OptionBuilder.hasOptionalArgs().create('o');

        // Assert
        assertTrue("Option should have optional arguments", option.hasOptionalArg());
        assertEquals("Number of arguments should be unlimited", Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void hasOptionalArgsWithNumber_shouldCreateOptionWithFixedNumberOfOptionalArguments() {
        // Act
        Option option = OptionBuilder.hasOptionalArgs(2).create('o');

        // Assert
        assertTrue("Option should have optional arguments", option.hasOptionalArg());
        assertEquals("Number of arguments should be 2", 2, option.getArgs());
    }

    // --- Option Properties ---

    @Test
    public void isRequired_shouldMarkOptionAsRequired() {
        // Act
        Option option = OptionBuilder.isRequired().create("r");

        // Assert
        assertTrue("Option should be marked as required", option.isRequired());
    }

    @Test
    public void withArgName_shouldSetArgumentName() {
        // Act
        Option option = OptionBuilder.withArgName("file").hasArg().create('f');

        // Assert
        assertEquals("Argument name should be set", "file", option.getArgName());
    }

    @Test
    public void withValueSeparator_shouldSetSeparatorChar() {
        // Act
        Option option = OptionBuilder.withValueSeparator(':').create('s');

        // Assert
        assertTrue("Value separator should be enabled", option.hasValueSeparator());
        assertEquals("Value separator char should be ':'", ':', option.getValueSeparator());
    }

    @Test
    public void withDefaultValueSeparator_shouldSetSeparatorToEquals() {
        // Act
        Option option = OptionBuilder.withValueSeparator().create('s');

        // Assert
        assertTrue("Value separator should be enabled", option.hasValueSeparator());
        assertEquals("Value separator char should be '='", '=', option.getValueSeparator());
    }

    @Test
    public void withType_shouldSetOptionType() {
        // Act
        Option option = OptionBuilder.withType(File.class).create('f');

        // Assert
        assertEquals("Option type should be File.class", File.class, option.getType());
    }

    // --- Complex Builder Chain ---

    @Test
    public void buildComplexOption_shouldSetAllPropertiesCorrectly() {
        // Arrange
        OptionBuilder.withLongOpt("file");
        OptionBuilder.withDescription("The file to process");
        OptionBuilder.hasArg();
        OptionBuilder.withArgName("path");
        OptionBuilder.isRequired();
        OptionBuilder.withType(File.class);

        // Act
        Option option = OptionBuilder.create('f');

        // Assert
        assertEquals("f", option.getOpt());
        assertEquals("file", option.getLongOpt());
        assertEquals("The file to process", option.getDescription());
        assertEquals("path", option.getArgName());
        assertTrue(option.isRequired());
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
        assertEquals(File.class, option.getType());
    }

    // --- Exception Handling ---

    @Test(expected = IllegalArgumentException.class)
    public void create_shouldThrowException_whenLongOptionNotSet() {
        // This should fail because create() without a short-opt requires a long-opt to be set.
        OptionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void createWithInvalidShortName_shouldThrowException() {
        // ']' is not a valid character for a short option name.
        OptionBuilder.create(']');
    }

    @Test(expected = IllegalArgumentException.class)
    public void createWithEmptyStringName_shouldThrowException() {
        // An empty string is not a valid option name.
        OptionBuilder.create("");
    }

    @Test(expected = ClassCastException.class)
    public void withTypeWithNonClass_shouldThrowException() {
        // The deprecated withType(Object) method expects a Class object.
        // Providing a String should result in a ClassCastException.
        OptionBuilder.withType("java.lang.String");
    }
}