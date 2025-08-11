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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.junit.jupiter.api.Test;

/**
 * Test case for the PatternOptionBuilder class.
 * 
 * Pattern syntax reference:
 * - Plain letter (e.g., 'a') = boolean flag
 * - Letter + ':' (e.g., 'a:') = string value
 * - Letter + '@' (e.g., 'b@') = object instance
 * - Letter + '+' (e.g., 'c+') = class type
 * - Letter + '%' (e.g., 'd%') = number value
 * - Letter + '/' (e.g., 'e/') = URL value
 * - Letter + '>' (e.g., 'f>') = file value
 * - Letter + '<' (e.g., 'g<') = existing file input stream
 * - Letter + '#' (e.g., 'h#') = date value
 * - Letter + '*' (e.g., 'i*') = file array (unsupported)
 * - '!' prefix = required option
 */
@SuppressWarnings("deprecation") // tests some deprecated classes
class PatternOptionBuilderTest {

    private static final String EXISTING_TEST_FILE = "src/test/resources/org/apache/commons/cli/existing-readable.file";
    private static final String NON_EXISTING_FILE = "non-existing.file";
    private static final String VALID_URL = "https://commons.apache.org";
    private static final String INVALID_URL = "foo://commons.apache.org";
    private static final String VALID_CLASS_NAME = "java.util.Calendar";
    private static final String INVALID_CLASS_NAME = "System.DateTime";

    @Test
    void testEmptyPattern_ShouldCreateEmptyOptions() {
        // Given: An empty pattern string
        final String emptyPattern = "";
        
        // When: Parsing the empty pattern
        final Options optionsFromEmptyPattern = PatternOptionBuilder.parsePattern(emptyPattern);
        
        // Then: Should create options with no defined options
        assertTrue(optionsFromEmptyPattern.getOptions().isEmpty());
    }

    @Test
    void testUntypedPattern_ShouldCreateBooleanFlags() throws Exception {
        // Given: A pattern with plain letters (boolean flags)
        final String booleanFlagsPattern = "abc";
        final String[] commandLineArgs = {"-abc"};
        
        // When: Parsing options and command line
        final Options optionsWithBooleanFlags = PatternOptionBuilder.parsePattern(booleanFlagsPattern);
        final CommandLineParser parser = new PosixParser();
        final CommandLine parsedCommandLine = parser.parse(optionsWithBooleanFlags, commandLineArgs);
        
        // Then: All flags should be present but have no values
        assertTrue(parsedCommandLine.hasOption('a'));
        assertNull(parsedCommandLine.getOptionObject('a'), "Boolean flag 'a' should have no value");
        
        assertTrue(parsedCommandLine.hasOption('b'));
        assertNull(parsedCommandLine.getOptionObject('b'), "Boolean flag 'b' should have no value");
        
        assertTrue(parsedCommandLine.hasOption('c'));
        assertNull(parsedCommandLine.getOptionObject('c'), "Boolean flag 'c' should have no value");
    }

    @Test
    void testClassPattern_WithValidAndInvalidClassNames() throws Exception {
        // Given: A pattern expecting class types (+ suffix)
        final String classTypePattern = "c+d+";
        final String[] argsWithValidAndInvalidClasses = {
            "-c", VALID_CLASS_NAME,     // java.util.Calendar - valid class
            "-d", INVALID_CLASS_NAME    // System.DateTime - invalid class
        };
        
        // When: Parsing the command line
        final Options optionsExpectingClasses = PatternOptionBuilder.parsePattern(classTypePattern);
        final CommandLineParser parser = new PosixParser();
        final CommandLine parsedLine = parser.parse(optionsExpectingClasses, argsWithValidAndInvalidClasses);
        
        // Then: Valid class should be parsed, invalid should be null
        assertEquals(Calendar.class, parsedLine.getOptionObject("c"), "Valid class name should be parsed to Class object");
        assertNull(parsedLine.getOptionObject("d"), "Invalid class name should result in null");
    }

    @Test
    void testNumberPattern_WithValidAndInvalidNumbers() throws Exception {
        // Given: A pattern expecting number values (% suffix)
        final String numberValuesPattern = "n%d%x%";
        final String[] argsWithMixedNumbers = {
            "-n", "1",      // Valid integer
            "-d", "2.1",    // Valid decimal
            "-x", "3,5"     // Invalid number format (comma instead of dot)
        };
        
        // When: Parsing the command line
        final Options optionsExpectingNumbers = PatternOptionBuilder.parsePattern(numberValuesPattern);
        final CommandLineParser parser = new PosixParser();
        final CommandLine parsedNumbers = parser.parse(optionsExpectingNumbers, argsWithMixedNumbers);
        
        // Then: Valid numbers should be parsed with correct types, invalid should be null
        assertEquals(Long.class, parsedNumbers.getOptionObject("n").getClass(), "Integer should be parsed as Long");
        assertEquals(Long.valueOf(1), parsedNumbers.getOptionObject("n"), "Integer value should be correct");
        
        assertEquals(Double.class, parsedNumbers.getOptionObject("d").getClass(), "Decimal should be parsed as Double");
        assertEquals(Double.valueOf(2.1), parsedNumbers.getOptionObject("d"), "Decimal value should be correct");
        
        assertNull(parsedNumbers.getOptionObject("x"), "Invalid number format should result in null");
    }

    @Test
    void testObjectPattern_WithClassInstantiation() throws Exception {
        // Given: A pattern expecting object instances (@ suffix)
        final String objectInstancePattern = "o@i@n@";
        final String[] argsWithClassNames = {
            "-o", "java.lang.String",   // Should create empty String instance
            "-i", VALID_CLASS_NAME,     // Should fail to create Calendar instance (no empty constructor)
            "-n", INVALID_CLASS_NAME    // Should fail with invalid class name
        };
        
        // When: Parsing the command line
        final Options optionsExpectingObjects = PatternOptionBuilder.parsePattern(objectInstancePattern);
        final CommandLineParser parser = new PosixParser();
        final CommandLine parsedObjects = parser.parse(optionsExpectingObjects, argsWithClassNames);
        
        // Then: Only classes with empty constructors should be instantiated
        assertEquals("", parsedObjects.getOptionObject("o"), "String should be instantiated as empty string");
        assertNull(parsedObjects.getOptionObject("i"), "Calendar cannot be instantiated via empty constructor");
        assertNull(parsedObjects.getOptionObject("n"), "Invalid class name should result in null");
    }

    @Test
    void testURLPattern_WithValidAndInvalidURLs() throws Exception {
        // Given: A pattern expecting URL values (/ suffix)
        final String urlValuesPattern = "u/v/";
        final String[] argsWithMixedUrls = {
            "-u", VALID_URL,    // Valid HTTPS URL
            "-v", INVALID_URL   // Invalid protocol
        };
        
        // When: Parsing the command line
        final Options optionsExpectingUrls = PatternOptionBuilder.parsePattern(urlValuesPattern);
        final CommandLineParser parser = new PosixParser();
        final CommandLine parsedUrls = parser.parse(optionsExpectingUrls, argsWithMixedUrls);
        
        // Then: Valid URL should be parsed, invalid should be null
        assertEquals(new URL(VALID_URL), parsedUrls.getOptionObject("u"), "Valid URL should be parsed correctly");
        assertNull(parsedUrls.getOptionObject("v"), "Invalid URL should result in null");
    }

    @Test
    void testExistingFilePattern_WithExistingFile() throws Exception {
        // Given: A pattern expecting existing file input streams (< suffix)
        final String existingFilePattern = "g<";
        final String[] argsWithExistingFile = {"-g", EXISTING_TEST_FILE};
        
        // When: Parsing the command line
        final Options optionsExpectingExistingFile = PatternOptionBuilder.parsePattern(existingFilePattern);
        final CommandLineParser parser = new PosixParser();
        final CommandLine parsedExistingFile = parser.parse(optionsExpectingExistingFile, argsWithExistingFile);
        
        // Then: Should create FileInputStream for existing file
        final Object parsedFileStream = parsedExistingFile.getOptionObject("g");
        assertNotNull(parsedFileStream, "Existing file should be parsed");
        assertInstanceOf(FileInputStream.class, parsedFileStream, "Should create FileInputStream for existing file");
    }

    @Test
    void testExistingFilePattern_WithNonExistentFile() throws Exception {
        // Given: A pattern expecting existing file but file doesn't exist
        final String existingFilePattern = "f<";
        final String[] argsWithNonExistentFile = {"-f", NON_EXISTING_FILE};
        
        // When: Parsing the command line
        final Options optionsExpectingExistingFile = PatternOptionBuilder.parsePattern(existingFilePattern);
        final CommandLineParser parser = new PosixParser();
        final CommandLine parsedNonExistentFile = parser.parse(optionsExpectingExistingFile, argsWithNonExistentFile);
        
        // Then: Should return null for non-existent file
        assertNull(parsedNonExistentFile.getOptionObject("f"), "Non-existent file should result in null");
    }

    @Test
    void testRequiredOption_ShouldThrowExceptionWhenMissing() throws Exception {
        // Given: A pattern with required options (! prefix)
        final String requiredOptionsPattern = "!n%m%";  // 'n' is required, 'm' is optional
        final String[] argsWithoutRequiredOption = {""};
        
        // When: Parsing without providing required option
        final Options optionsWithRequiredOption = PatternOptionBuilder.parsePattern(requiredOptionsPattern);
        final CommandLineParser parser = new PosixParser();
        
        // Then: Should throw MissingOptionException
        final MissingOptionException exception = assertThrows(MissingOptionException.class, 
            () -> parser.parse(optionsWithRequiredOption, argsWithoutRequiredOption));
        
        assertEquals(1, exception.getMissingOptions().size());
        assertTrue(exception.getMissingOptions().contains("n"), "Should report 'n' as missing required option");
    }

    @Test
    void testComplexPattern_WithAllOptionTypes() throws Exception {
        // Given: A complex pattern with multiple option types
        /*
         * Pattern breakdown:
         * a: = string value
         * b@ = object instance  
         * c = boolean flag
         * d = boolean flag
         * e> = file value
         * f+ = class type
         * n% = number value
         * t/ = URL value
         * m* = file array (unsupported)
         * z# = date value
         */
        final String complexPattern = "a:b@cde>f+n%t/m*z#";
        
        // Prepare date for testing (avoid timezone issues by formatting and parsing)
        final Date expectedDate = new Date(1023400137000L);
        final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        
        final String[] complexArgs = {
            "-c",                                           // Boolean flag
            "-a", "foo",                                    // String value
            "-b", "java.util.Vector",                       // Object instance
            "-e", "build.xml",                              // File value
            "-f", VALID_CLASS_NAME,                         // Class type
            "-n", "4.5",                                    // Number value
            "-t", VALID_URL,                                // URL value
            "-z", dateFormat.format(expectedDate),         // Date value
            "-m", "test*"                                   // File array (unsupported)
        };
        
        // When: Parsing the complex command line
        final Options complexOptions = PatternOptionBuilder.parsePattern(complexPattern);
        final CommandLineParser parser = new PosixParser();
        final CommandLine parsedComplexLine = parser.parse(complexOptions, complexArgs);
        
        // Then: Verify all option types are parsed correctly
        
        // String option
        assertEquals("foo", parsedComplexLine.getOptionValue("a"), "String option value");
        assertEquals("foo", parsedComplexLine.getOptionObject("a"), "String option object");
        
        // Object instance option
        assertEquals(new Vector<>(), parsedComplexLine.getOptionObject("b"), "Object instance should be empty Vector");
        
        // Boolean flags
        assertTrue(parsedComplexLine.hasOption("c"), "Boolean flag 'c' should be present");
        assertFalse(parsedComplexLine.hasOption("d"), "Boolean flag 'd' should be absent");
        
        // File option
        assertEquals(new File("build.xml"), parsedComplexLine.getOptionObject("e"), "File option value");
        
        // Class type option
        assertEquals(Calendar.class, parsedComplexLine.getOptionObject("f"), "Class type option value");
        
        // Number option
        assertEquals(Double.valueOf(4.5), parsedComplexLine.getOptionObject("n"), "Number option value");
        
        // URL option
        assertEquals(new URL(VALID_URL), parsedComplexLine.getOptionObject("t"), "URL option value");
        
        // Date option
        assertEquals(expectedDate, parsedComplexLine.getOptionObject('z'), "Date option value");
        
        // Test char-based access methods (should delegate to String methods)
        assertEquals("foo", parsedComplexLine.getOptionValue('a'), "Char-based string access");
        assertEquals("foo", parsedComplexLine.getOptionObject('a'), "Char-based string object access");
        assertEquals(new Vector<>(), parsedComplexLine.getOptionObject('b'), "Char-based object access");
        assertTrue(parsedComplexLine.hasOption('c'), "Char-based boolean flag access");
        assertFalse(parsedComplexLine.hasOption('d'), "Char-based boolean flag access (false)");
        assertEquals(new File("build.xml"), parsedComplexLine.getOptionObject('e'), "Char-based file access");
        assertEquals(Calendar.class, parsedComplexLine.getOptionObject('f'), "Char-based class access");
        assertEquals(Double.valueOf(4.5), parsedComplexLine.getOptionObject('n'), "Char-based number access");
        assertEquals(new URL(VALID_URL), parsedComplexLine.getOptionObject('t'), "Char-based URL access");
        
        // File array option (unsupported - should throw exception)
        assertThrows(UnsupportedOperationException.class, 
            () -> parsedComplexLine.getOptionObject('m'), 
            "File array option should throw UnsupportedOperationException");
    }
}