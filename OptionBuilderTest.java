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

@SuppressWarnings("deprecation") // OptionBuilder is marked deprecated
class OptionBuilderTest {

    // Constants for repeated strings
    private static final String OPTION_DESCRIPTION = "option description";
    private static final String LONG_OPT_SIMPLE = "simple option";
    private static final String DESC_SIMPLE = "this is a simple option";
    private static final String LONG_OPT_DIMPLE = "dimple option";
    private static final String DESC_DIMPLE = "this is a dimple option";

    // Tests for basic Option creation
    @Test
    void createOption_withCharOpt_setsBasicProperties() {
        final Option option = OptionBuilder.withDescription(OPTION_DESCRIPTION).create('o');

        assertEquals("o", option.getOpt());
        assertEquals(OPTION_DESCRIPTION, option.getDescription());
        assertFalse(option.hasArg());
    }

    @Test
    void createOption_withStringOpt_setsBasicProperties() {
        final Option option = OptionBuilder.withDescription(OPTION_DESCRIPTION).create("o");

        assertEquals("o", option.getOpt());
        assertEquals(OPTION_DESCRIPTION, option.getDescription());
        assertFalse(option.hasArg());
    }

    // Tests for OptionBuilder reset behavior
    @Test
    void builderState_resetsAfterEachCreate_call() {
        // Failed creation due to invalid char should reset builder
        assertThrows(IllegalArgumentException.class, () -> 
            OptionBuilder.withDescription("JUnit").create('"')
        );
        // Verify reset by creating a valid option without description
        assertNull(OptionBuilder.create('x').getDescription(), "Builder should reset description after failure");

        // Failed creation due to missing option should reset builder
        assertThrows(IllegalArgumentException.class, () -> 
            OptionBuilder.create()
        );
        // Verify reset by creating a valid option without description
        assertNull(OptionBuilder.create('y').getDescription(), "Builder should reset description after missing option");
    }

    @Test
    void createOption_withoutRequiredProperties_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            OptionBuilder.create()
        );
        // Verify builder reset by creating a valid option afterward
        OptionBuilder.create("opt");
    }

    // Tests for complete option configuration
    @Test
    void createOption_withAllProperties_setsCorrectAttributes() {
        final Option option = OptionBuilder.withLongOpt(LONG_OPT_SIMPLE)
                .hasArg()
                .isRequired()
                .hasArgs()
                .withType(Float.class)
                .withDescription(DESC_SIMPLE)
                .create('s');

        assertEquals("s", option.getOpt());
        assertEquals(LONG_OPT_SIMPLE, option.getLongOpt());
        assertEquals(DESC_SIMPLE, option.getDescription());
        assertEquals(Float.class, option.getType());
        assertTrue(option.hasArg());
        assertTrue(option.isRequired());
        assertTrue(option.hasArgs());
    }

    @Test
    void createTwoOptionsSequentially_maintainsIndependentConfigurations() {
        // Create first option with multiple properties
        final Option firstOption = OptionBuilder.withLongOpt(LONG_OPT_SIMPLE)
                .hasArg()
                .isRequired()
                .hasArgs()
                .withType(Float.class)
                .withDescription(DESC_SIMPLE)
                .create('s');

        // Create second option with different properties
        final Option secondOption = OptionBuilder.withLongOpt(LONG_OPT_DIMPLE)
                .hasArg()
                .withDescription(DESC_DIMPLE)
                .create('d');

        // Verify first option retains its configuration
        assertEquals("s", firstOption.getOpt());
        assertEquals(LONG_OPT_SIMPLE, firstOption.getLongOpt());
        assertEquals(DESC_SIMPLE, firstOption.getDescription());
        assertEquals(Float.class, firstOption.getType());
        assertTrue(firstOption.hasArg());
        assertTrue(firstOption.isRequired());
        assertTrue(firstOption.hasArgs());

        // Verify second option has independent configuration
        assertEquals("d", secondOption.getOpt());
        assertEquals(LONG_OPT_DIMPLE, secondOption.getLongOpt());
        assertEquals(DESC_DIMPLE, secondOption.getDescription());
        assertEquals(String.class, secondOption.getType());
        assertTrue(secondOption.hasArg());
        assertFalse(secondOption.isRequired());
        assertFalse(secondOption.hasArgs());
    }

    // Tests for argument count configuration
    @Test
    void createOption_withFixedArgCount_setsCorrectNumberOfArgs() {
        final Option option = OptionBuilder.withDescription(OPTION_DESCRIPTION)
                .hasArgs(2)
                .create('o');

        assertEquals(2, option.getArgs());
    }

    // Tests for special/illegal option characters
    @Test
    void createOption_withSpecialCharacters_handlesCorrectly() {
        // Valid special characters
        Option questionMarkOpt = OptionBuilder.withDescription("help options").create('?');
        assertEquals("?", questionMarkOpt.getOpt());

        Option atSignOpt = OptionBuilder.withDescription("read from stdin").create('@');
        assertEquals("@", atSignOpt.getOpt());

        // Invalid character (space)
        assertThrows(IllegalArgumentException.class, () -> 
            OptionBuilder.create(' ')
        );
    }

    @Test
    void createOption_withIllegalCharacters_throwsException() {
        // Invalid char option
        assertThrows(IllegalArgumentException.class, () -> 
            OptionBuilder.withDescription(OPTION_DESCRIPTION).create('"')
        );

        // Invalid string option
        assertThrows(IllegalArgumentException.class, () -> 
            OptionBuilder.create("opt`")
        );

        // Valid option should not throw
        OptionBuilder.create("valid");
    }
}