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

    private static final String SAMPLE_DESCRIPTION = "option description";
    private static final char SAMPLE_CHAR_OPTION = 'o';
    private static final String SAMPLE_STRING_OPTION = "o";

    @Test
    void shouldCreateBasicOptionWithCharacterIdentifier() {
        // Given: An OptionBuilder with a description
        // When: Creating an option with a character identifier
        final Option option = OptionBuilder
                .withDescription(SAMPLE_DESCRIPTION)
                .create(SAMPLE_CHAR_OPTION);

        // Then: The option should have the correct properties
        assertEquals("o", option.getOpt());
        assertEquals(SAMPLE_DESCRIPTION, option.getDescription());
        assertFalse(option.hasArg(), "Basic option should not require arguments");
    }

    @Test
    void shouldCreateBasicOptionWithStringIdentifier() {
        // Given: An OptionBuilder with a description
        // When: Creating an option with a string identifier
        final Option option = OptionBuilder
                .withDescription(SAMPLE_DESCRIPTION)
                .create(SAMPLE_STRING_OPTION);

        // Then: The option should have the correct properties
        assertEquals("o", option.getOpt());
        assertEquals(SAMPLE_DESCRIPTION, option.getDescription());
        assertFalse(option.hasArg(), "Basic option should not require arguments");
    }

    @Test
    void shouldResetBuilderStateAfterEachOperation() {
        // Given: An invalid option creation that should fail
        // When: Attempting to create an option with invalid character
        assertThrows(IllegalArgumentException.class, 
                () -> OptionBuilder.withDescription("JUnit").create('"'),
                "Should reject invalid option character");

        // Then: Builder state should be reset - next option should not inherit description
        Option cleanOption = OptionBuilder.create('x');
        assertNull(cleanOption.getDescription(), 
                "Builder should be reset - no description should be inherited");

        // When: Attempting to create option without identifier
        assertThrows(IllegalStateException.class, 
                OptionBuilder::create,
                "Should require option identifier");

        // Then: Builder should still be reset for subsequent operations
        Option anotherCleanOption = OptionBuilder.create('x');
        assertNull(anotherCleanOption.getDescription(), 
                "Builder should remain reset after exception");
    }

    @Test
    void shouldCreateComplexOptionWithAllFeatures() {
        // Given: An OptionBuilder with all possible configurations
        // When: Creating a complex option with multiple features
        final Option complexOption = OptionBuilder
                .withLongOpt("simple option")
                .hasArg()
                .isRequired()
                .hasArgs()
                .withType(Float.class)
                .withDescription("this is a simple option")
                .create('s');

        // Then: All configured properties should be correctly set
        assertEquals("s", complexOption.getOpt());
        assertEquals("simple option", complexOption.getLongOpt());
        assertEquals("this is a simple option", complexOption.getDescription());
        assertEquals(Float.class, complexOption.getType());
        assertTrue(complexOption.hasArg(), "Option should accept arguments");
        assertTrue(complexOption.isRequired(), "Option should be required");
        assertTrue(complexOption.hasArgs(), "Option should accept multiple arguments");
    }

    @Test
    void shouldRejectIncompleteOptionCreation() {
        // When: Attempting to create an option without any configuration
        // Then: Should throw IllegalStateException
        assertThrows(IllegalStateException.class, 
                OptionBuilder::create,
                "Should require at least an option identifier");

        // Note: Builder is implicitly reset after the exception
        // This should work without issues
        OptionBuilder.create("opt");
    }

    @Test
    void shouldValidateOptionCharacters() {
        // When: Using invalid single character option (quote character)
        // Then: Should reject with IllegalArgumentException
        assertThrows(IllegalArgumentException.class, 
                () -> OptionBuilder.withDescription(SAMPLE_DESCRIPTION).create('"'),
                "Should reject quote character as option");

        // When: Using invalid character in option string (backtick)
        // Then: Should reject with IllegalArgumentException
        assertThrows(IllegalArgumentException.class, 
                () -> OptionBuilder.create("opt`"),
                "Should reject backtick in option string");

        // When: Using valid option string
        // Then: Should create successfully
        OptionBuilder.create("opt"); // Should not throw
    }

    @Test
    void shouldSetCorrectArgumentCount() {
        // Given: An OptionBuilder configured for specific argument count
        // When: Creating an option that accepts exactly 2 arguments
        final Option optionWithTwoArgs = OptionBuilder
                .withDescription(SAMPLE_DESCRIPTION)
                .hasArgs(2)
                .create('o');

        // Then: The argument count should be correctly set
        assertEquals(2, optionWithTwoArgs.getArgs());
    }

    @Test
    void shouldHandleSpecialOptionCharacters() throws Exception {
        // When: Creating option with '?' character
        final Option helpOption = OptionBuilder
                .withDescription("help options")
                .create('?');
        // Then: Should accept '?' as valid option character
        assertEquals("?", helpOption.getOpt());

        // When: Creating option with '@' character
        final Option stdinOption = OptionBuilder
                .withDescription("read from stdin")
                .create('@');
        // Then: Should accept '@' as valid option character
        assertEquals("@", stdinOption.getOpt());

        // When: Attempting to use space character as option
        // Then: Should reject space character
        assertThrows(IllegalArgumentException.class, 
                () -> OptionBuilder.create(' '),
                "Should reject space character as option");
    }

    @Test
    void shouldCreateMultipleIndependentOptions() {
        // Given: First complex option configuration
        // When: Creating first option with full configuration
        Option firstOption = OptionBuilder
                .withLongOpt("simple option")
                .hasArg()
                .isRequired()
                .hasArgs()
                .withType(Float.class)
                .withDescription("this is a simple option")
                .create('s');

        // Then: First option should have all specified properties
        assertComplexOptionProperties(firstOption, 's', "simple option", 
                "this is a simple option", Float.class, true, true, true);

        // Given: Second option with different configuration (builder should be reset)
        // When: Creating second option with simpler configuration
        Option secondOption = OptionBuilder
                .withLongOpt("dimple option")
                .hasArg()
                .withDescription("this is a dimple option")
                .create('d');

        // Then: Second option should have only its specified properties
        assertSimpleOptionProperties(secondOption, 'd', "dimple option", 
                "this is a dimple option", String.class, true, false, false);
    }

    /**
     * Helper method to assert properties of a complex option
     */
    private void assertComplexOptionProperties(Option option, char expectedOpt, 
            String expectedLongOpt, String expectedDescription, Class<?> expectedType,
            boolean shouldHaveArg, boolean shouldBeRequired, boolean shouldHaveArgs) {
        
        assertEquals(String.valueOf(expectedOpt), option.getOpt());
        assertEquals(expectedLongOpt, option.getLongOpt());
        assertEquals(expectedDescription, option.getDescription());
        assertEquals(expectedType, option.getType());
        assertEquals(shouldHaveArg, option.hasArg(), "hasArg() mismatch");
        assertEquals(shouldBeRequired, option.isRequired(), "isRequired() mismatch");
        assertEquals(shouldHaveArgs, option.hasArgs(), "hasArgs() mismatch");
    }

    /**
     * Helper method to assert properties of a simple option
     */
    private void assertSimpleOptionProperties(Option option, char expectedOpt, 
            String expectedLongOpt, String expectedDescription, Class<?> expectedType,
            boolean shouldHaveArg, boolean shouldBeRequired, boolean shouldHaveArgs) {
        
        assertEquals(String.valueOf(expectedOpt), option.getOpt());
        assertEquals(expectedLongOpt, option.getLongOpt());
        assertEquals(expectedDescription, option.getDescription());
        assertEquals(expectedType, option.getType());
        assertEquals(shouldHaveArg, option.hasArg(), "hasArg() mismatch");
        assertEquals(shouldBeRequired, option.isRequired(), "isRequired() mismatch");
        assertEquals(shouldHaveArgs, option.hasArgs(), "hasArgs() mismatch");
    }
}