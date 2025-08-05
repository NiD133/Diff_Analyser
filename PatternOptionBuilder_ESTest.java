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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link PatternOptionBuilder}.
 * This suite focuses on behavior-driven testing with clear, descriptive names
 * and consolidates repetitive tests into parameterized tests for better readability and maintenance.
 */
@DisplayName("PatternOptionBuilder Tests")
class PatternOptionBuilderTest {

    @Nested
    @DisplayName("isValueCode(char) tests")
    class IsValueCodeTests {

        @DisplayName("Should return true for valid type-defining characters")
        @ParameterizedTest(name = "Character ''{0}''")
        @ValueSource(chars = {'@', ':', '%', '+', '#', '<', '>', '/', '*', '!'})
        void isValueCode_shouldReturnTrue_forValidTypeCharacters(char code) {
            assertTrue(PatternOptionBuilder.isValueCode(code));
        }

        @DisplayName("Should return false for non-type-defining characters")
        @ParameterizedTest(name = "Character ''{0}''")
        @ValueSource(chars = {'a', 'z', '0', '9', '?', ',', ' '})
        void isValueCode_shouldReturnFalse_forNonValueCharacters(char code) {
            assertFalse(PatternOptionBuilder.isValueCode(code));
        }
    }

    @Nested
    @DisplayName("getValueType(char) tests")
    class GetValueTypeTests {

        static Stream<Arguments> typeCharacterProvider() {
            return Stream.of(
                Arguments.of('@', Object.class),
                Arguments.of(':', String.class),
                Arguments.of('%', Number.class),
                Arguments.of('+', Class.class),
                Arguments.of('#', Date.class),
                Arguments.of('<', FileInputStream.class),
                Arguments.of('>', File.class),
                Arguments.of('/', URL.class),
                Arguments.of('*', File[].class)
            );
        }

        @DisplayName("Should return the correct class for known type characters")
        @ParameterizedTest(name = "Type for ''{0}'' should be {1}")
        @MethodSource("typeCharacterProvider")
        void getValueType_shouldReturnCorrectClass_forKnownTypeCharacters(char code, Class<?> expectedType) {
            assertEquals(expectedType, PatternOptionBuilder.getValueType(code));
        }

        @DisplayName("Should return null for characters that do not define a type")
        @ParameterizedTest(name = "Character ''{0}''")
        @ValueSource(chars = {'a', 'z', '0', '9', '?', ',', ' ', ';', '='})
        void getValueType_shouldReturnNull_forUnknownTypeCharacters(char code) {
            assertNull(PatternOptionBuilder.getValueType(code));
        }
    }

    @Nested
    @DisplayName("parsePattern(String) tests")
    class ParsePatternTests {

        @Test
        @DisplayName("Should return empty Options for an empty pattern")
        void parsePattern_shouldReturnEmptyOptions_forEmptyPattern() {
            Options options = PatternOptionBuilder.parsePattern("");
            assertTrue(options.getOptions().isEmpty());
        }

        @Test
        @DisplayName("Should correctly parse a pattern with different option types")
        void parsePattern_shouldCreateOptionsWithCorrectTypes() {
            // Pattern: -v (flag), -s <string>, -f <file>, -n <number>
            Options options = PatternOptionBuilder.parsePattern("vs:f>n%");

            // -v is a simple flag
            Option optionV = options.getOption("v");
            assertNotNull(optionV);
            assertFalse(optionV.hasArg());

            // -s requires a String argument
            Option optionS = options.getOption("s");
            assertNotNull(optionS);
            assertTrue(optionS.hasArg());
            assertEquals(String.class, optionS.getType());

            // -f requires a File argument
            Option optionF = options.getOption("f");
            assertNotNull(optionF);
            assertTrue(optionF.hasArg());
            assertEquals(File.class, optionF.getType());

            // -n requires a Number argument
            Option optionN = options.getOption("n");
            assertNotNull(optionN);
            assertTrue(optionN.hasArg());
            assertEquals(Number.class, optionN.getType());
        }

        @Test
        @DisplayName("Should correctly parse required options marked with '!'")
        void parsePattern_shouldHandleRequiredOptions() {
            Options options = PatternOptionBuilder.parsePattern("!a:b");

            Option optionA = options.getOption("a");
            assertNotNull(optionA);
            assertTrue(optionA.isRequired());
            assertTrue(optionA.hasArg()); // The ':' makes it have an argument

            Option optionB = options.getOption("b");
            assertNotNull(optionB);
            assertFalse(optionB.isRequired()); // '!' only applies to the preceding option
            assertFalse(optionB.hasArg());
        }

        @Test
        @DisplayName("Should throw NullPointerException for a null pattern")
        void parsePattern_shouldThrowException_forNullPattern() {
            assertThrows(NullPointerException.class, () -> PatternOptionBuilder.parsePattern(null));
        }

        @DisplayName("Should throw IllegalArgumentException for patterns with invalid option names")
        @ParameterizedTest(name = "Pattern: \"{0}\"")
        @ValueSource(strings = {"=abc", "a'b", "a$c"})
        void parsePattern_shouldThrowException_forPatternWithInvalidOptionName(String invalidPattern) {
            assertThrows(IllegalArgumentException.class, () -> PatternOptionBuilder.parsePattern(invalidPattern));
        }
    }

    @Nested
    @DisplayName("Deprecated API tests")
    class DeprecatedApiTests {

        @Test
        @DisplayName("getValueClass(char) should return the correct class object")
        @SuppressWarnings("deprecation")
        void getValueClass_shouldReturnCorrectClass() {
            // This test ensures the deprecated method still works for backward compatibility.
            assertEquals(String.class, PatternOptionBuilder.getValueClass(':'));
            assertEquals(Object.class, PatternOptionBuilder.getValueClass('@'));
        }

        @Test
        @DisplayName("Constructor should be callable for backward compatibility")
        @SuppressWarnings("deprecation")
        void constructor_isPublic() {
            // The constructor is deprecated, but this test ensures it remains callable.
            new PatternOptionBuilder();
        }
    }
}