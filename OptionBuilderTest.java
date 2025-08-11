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

package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * Tests for the deprecated {@link OptionBuilder}.
 * This test class acknowledges that OptionBuilder uses a static, mutable state,
 * and includes tests to verify that this state is properly reset between calls.
 */
@SuppressWarnings("deprecation") // OptionBuilder is marked deprecated
class OptionBuilderTest {

    @Test
    void testCreateSimpleOptionWithCharName() {
        // Act
        final Option option = OptionBuilder.withDescription("option description").create('o');

        // Assert
        assertEquals("o", option.getOpt());
        assertEquals("option description", option.getDescription());
        assertFalse(option.hasArg(), "Option should not have an argument by default");
    }

    @Test
    void testCreateSimpleOptionWithStringName() {
        // Act
        final Option option = OptionBuilder.withDescription("option description").create("o");

        // Assert
        assertEquals("o", option.getOpt());
        assertEquals("option description", option.getDescription());
        assertFalse(option.hasArg(), "Option should not have an argument by default");
    }

    @Test
    void testCreateWithAllProperties() {
        // Act
        //@formatter:off
        final Option option = OptionBuilder.withLongOpt("simple-option")
                                     .hasArg()
                                     .isRequired()
                                     .hasArgs()
                                     .withType(Float.class)
                                     .withDescription("this is a simple option")
                                     .create('s');
        //@formatter:on

        // Assert
        assertEquals("s", option.getOpt());
        assertEquals("simple-option", option.getLongOpt());
        assertEquals("this is a simple option", option.getDescription());
        assertEquals(Float.class, option.getType());
        assertTrue(option.hasArg());
        assertTrue(option.isRequired());
        assertTrue(option.hasArgs());
    }

    @Test
    void testCreateWithSpecificNumberOfArguments() {
        // Act
        final Option option = OptionBuilder.withDescription("option description")
                                           .hasArgs(2)
                                           .create('o');
        // Assert
        assertEquals(2, option.getArgs());
    }

    @Test
    void testCreateWithValidSpecialCharacters() {
        // Act
        final Option helpOpt = OptionBuilder.withDescription("help options").create('?');
        final Option atOpt = OptionBuilder.withDescription("read from stdin").create('@');

        // Assert
        assertEquals("?", helpOpt.getOpt());
        assertEquals("@", atOpt.getOpt());
    }

    @Test
    void testCreateFailsWithInvalidCharacters() {
        // Assert that creating an option with an invalid character name throws an exception.
        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.create(' '));
        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.create('"'));
        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.create("opt`"));
    }

    @Test
    void testCreateFailsWhenNoOptionNameIsProvided() {
        // Assert
        assertThrows(IllegalArgumentException.class,
            (Executable) OptionBuilder::create,
            "Creation should fail if no option name is provided.");
    }

    @Test
    void testBuilderIsResetAfterSuccessfulCreate() {
        // This test verifies that after successfully creating an option, the builder is
        // reset for the next option creation, and properties do not leak.

        // Arrange & Act: Create the first, complex option
        //@formatter:off
        final Option firstOption = OptionBuilder.withLongOpt("first-option")
                                                .hasArg()
                                                .isRequired()
                                                .hasArgs()
                                                .withType(Integer.class)
                                                .withDescription("a complex option")
                                                .create('f');
        //@formatter:on

        // Assert: Verify properties of the first option
        assertTrue(firstOption.isRequired());
        assertTrue(firstOption.hasArgs());
        assertEquals(Integer.class, firstOption.getType());

        // Arrange & Act: Create a second, simple option immediately after
        //@formatter:off
        final Option secondOption = OptionBuilder.withLongOpt("second-option")
                                                 .hasArg()
                                                 .withDescription("a simple option")
                                                 .create('s');
        //@formatter:on

        // Assert: Verify properties of the second option and confirm state was reset
        assertEquals("s", secondOption.getOpt());
        assertEquals("second-option", secondOption.getLongOpt());
        assertTrue(secondOption.hasArg());
        assertFalse(secondOption.isRequired(), "isRequired should have been reset to false");
        assertFalse(secondOption.hasArgs(), "hasArgs should have been reset to false");
        assertEquals(String.class, secondOption.getType(), "Type should have been reset to the default (String)");
    }

    @Test
    void testBuilderIsResetAfterFailedCreate() {
        // This test verifies that the builder's internal state is reset
        // even when a create() call fails.

        // Arrange: Set a description, then trigger a failure by using an invalid option character.
        assertThrows(IllegalArgumentException.class,
            () -> OptionBuilder.withDescription("should be reset").create('"'));

        // Act: Create a new, valid option.
        final Option option = OptionBuilder.create('x');

        // Assert: Verify the description was reset and not inherited from the failed call.
        assertNull(option.getDescription(), "Description should be null after a failed create call");
    }
}