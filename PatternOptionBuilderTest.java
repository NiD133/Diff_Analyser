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

import org.junit.jupiter.api.Test;

/**
 * Tests for the PatternOptionBuilder class.
 *
 * <p>
 * The tests follow a standard Arrange-Act-Assert pattern. Each test focuses on a specific
 * pattern character and its expected behavior.
 * </p>
 */
@SuppressWarnings("deprecation") // The class uses PosixParser, which is deprecated.
class PatternOptionBuilderTest {

    @Test
    void parsePattern_shouldReturnEmptyOptions_forEmptyPattern() {
        // Act
        final Options options = PatternOptionBuilder.parsePattern("");

        // Assert
        assertTrue(options.getOptions().isEmpty(), "Expected no options to be created from an empty pattern.");
    }

    @Test
    void parsePattern_shouldCreateBooleanOptions_forSimpleChars() throws Exception {
        // Arrange
        final Options options = PatternOptionBuilder.parsePattern("abc");
        final CommandLineParser parser = new PosixParser();
        final String[] args = {"-abc"};

        // Act
        final CommandLine commandLine = parser.parse(options, args);

        // Assert
        assertTrue(commandLine.hasOption('a'), "Option 'a' should be present.");
        assertNull(commandLine.getOptionObject('a'), "A simple char option should not have a value.");
        assertTrue(commandLine.hasOption('b'), "Option 'b' should be present.");
        assertNull(commandLine.getOptionObject('b'), "A simple char option should not have a value.");
        assertTrue(commandLine.hasOption('c'), "Option 'c' should be present.");
        assertNull(commandLine.getOptionObject('c'), "A simple char option should not have a value.");
    }

    @Test
    void parsePattern_shouldCreateRequiredOption_forExclamationMark() {
        // Arrange
        // '!' makes the next option ('n') required. '%' means it's a Number.
        final Options options = PatternOptionBuilder.parsePattern("!n%");
        final CommandLineParser parser = new PosixParser();
        final String[] argsWithoutRequiredOption = {};

        // Act & Assert
        final MissingOptionException e = assertThrows(MissingOptionException.class,
            () -> parser.parse(options, argsWithoutRequiredOption),
            "A MissingOptionException should be thrown when a required option is missing.");

        assertEquals(1, e.getMissingOptions().size());
        assertTrue(e.getMissingOptions().contains("n"), "The missing option 'n' should be reported.");
    }

    @Test
    void parsePattern_shouldCreateStringOption_forColon() throws Exception {
        // Arrange
        // ':' indicates the option takes a string argument.
        final Options options = PatternOptionBuilder.parsePattern("a:");
        final CommandLineParser parser = new PosixParser();
        final String[] args = {"-a", "foo"};

        // Act
        final CommandLine commandLine = parser.parse(options, args);

        // Assert
        assertEquals("foo", commandLine.getOptionObject("a"), "getOptionObject should return the string value.");
    }

    @Test
    void parsePattern_shouldCreateObjectInstanceOption_forAtSymbol() throws Exception {
        // Arrange
        // '@' creates an instance of the class specified in the argument.
        final Options options = PatternOptionBuilder.parsePattern("o@i@n@");
        final CommandLineParser parser = new PosixParser();
        final String[] args = {
            "-o", "java.lang.String",      // Valid class with no-arg constructor
            "-i", "java.util.Calendar",    // Valid class but no public no-arg constructor
            "-n", "System.DateTime"        // Non-existent class
        };

        // Act
        final CommandLine commandLine = parser.parse(options, args);

        // Assert
        assertEquals("", commandLine.getOptionObject("o"), "Should create an instance for a valid class with a no-arg constructor.");
        assertNull(commandLine.getOptionObject("i"), "Should be null if class cannot be instantiated (e.g., no public no-arg constructor).");
        assertNull(commandLine.getOptionObject("n"), "Should be null for a non-existent class.");
    }

    @Test
    void parsePattern_shouldCreateFileOption_forGreaterThanSymbol() throws Exception {
        // Arrange
        // '>' creates a File object from the argument.
        final Options options = PatternOptionBuilder.parsePattern("e>");
        final CommandLineParser parser = new PosixParser();
        final String[] args = {"-e", "build.xml"};

        // Act
        final CommandLine commandLine = parser.parse(options, args);

        // Assert
        assertEquals(new File("build.xml"), commandLine.getOptionObject("e"), "Should create a File object.");
    }

    @Test
    void parsePattern_shouldCreateClassOption_forPlusSymbol() throws Exception {
        // Arrange
        // '+' creates a Class object from the argument.
        final Options options = PatternOptionBuilder.parsePattern("c+d+");
        final CommandLineParser parser = new PosixParser();
        final String[] args = {"-c", "java.util.Calendar", "-d", "System.DateTime"}; // Second class does not exist

        // Act
        final CommandLine commandLine = parser.parse(options, args);

        // Assert
        assertEquals(Calendar.class, commandLine.getOptionObject("c"), "Should create a Class object for a valid class name.");
        assertNull(commandLine.getOptionObject("d"), "Should return null for a non-existent class name.");
    }

    @Test
    void parsePattern_shouldCreateNumberOption_forPercentSymbol() throws Exception {
        // Arrange
        // '%' creates a Number object from the argument.
        final Options options = PatternOptionBuilder.parsePattern("n%d%");
        final CommandLineParser parser = new PosixParser();
        final String[] args = {"-n", "1", "-d", "2.1"};

        // Act
        final CommandLine commandLine = parser.parse(options, args);

        // Assert
        assertEquals(Long.valueOf(1), commandLine.getOptionObject("n"), "Should parse integer string as Long.");
        assertInstanceOf(Long.class, commandLine.getOptionObject("n"));

        assertEquals(Double.valueOf(2.1), commandLine.getOptionObject("d"), "Should parse decimal string as Double.");
        assertInstanceOf(Double.class, commandLine.getOptionObject("d"));
    }

    @Test
    void parsePattern_shouldReturnNull_forInvalidNumberWithPercentSymbol() throws Exception {
        // Arrange
        // '%' creates a Number object. "3,5" is not a valid format.
        final Options options = PatternOptionBuilder.parsePattern("x%");
        final CommandLineParser parser = new PosixParser();
        final String[] args = {"-x", "3,5"};

        // Act
        final CommandLine commandLine = parser.parse(options, args);

        // Assert
        assertNull(commandLine.getOptionObject("x"), "Should return null for a malformed number string.");
    }

    @Test
    void parsePattern_shouldCreateUrlOption_forSlashSymbol() throws Exception {
        // Arrange
        // '/' creates a URL object from the argument.
        final Options options = PatternOptionBuilder.parsePattern("u/v/");
        final CommandLineParser parser = new PosixParser();
        final String[] args = {"-u", "https://commons.apache.org", "-v", "foo://commons.apache.org"}; // Second URL is malformed

        // Act
        final CommandLine commandLine = parser.parse(options, args);

        // Assert
        assertEquals(new URL("https://commons.apache.org"), commandLine.getOptionObject("u"), "Should create a URL for a valid URL string.");
        assertNull(commandLine.getOptionObject("v"), "Should return null for a malformed URL string.");
    }

    @Test
    void parsePattern_shouldCreateDateOption_forHashSymbol() throws Exception {
        // Arrange
        // '#' creates a Date object from the argument.
        final Options options = PatternOptionBuilder.parsePattern("z#");
        final CommandLineParser parser = new PosixParser();
        final Date expectedDate = new Date(1023400137000L);

        // To make the test robust against locale differences, format the expected date
        // into a string and use that as the command-line input.
        final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        final String dateString = dateFormat.format(expectedDate);
        final String[] args = {"-z", dateString};

        // Act
        final CommandLine commandLine = parser.parse(options, args);

        // Assert
        assertEquals(expectedDate, commandLine.getOptionObject("z"), "Should create a Date object from the string argument.");
    }

    @Test
    void parsePattern_shouldCreateExistingFileOption_forLessThanSymbol() throws Exception {
        // Arrange
        // '<' creates a FileInputStream for an existing, readable file.
        final Options options = PatternOptionBuilder.parsePattern("g<");
        final CommandLineParser parser = new PosixParser();
        final String[] args = {"-g", "src/test/resources/org/apache/commons/cli/existing-readable.file"};

        // Act
        final CommandLine commandLine = parser.parse(options, args);

        // Assert
        final Object parsedObject = commandLine.getOptionObject("g");
        assertNotNull(parsedObject, "Should create an object for an existing file.");
        assertInstanceOf(FileInputStream.class, parsedObject, "The created object should be a FileInputStream.");
    }

    @Test
    void parsePattern_shouldReturnNull_forNonExistentFileWithLessThanSymbol() throws Exception {
        // Arrange
        // '<' requires an existing file.
        final Options options = PatternOptionBuilder.parsePattern("f<");
        final CommandLineParser parser = new PosixParser();
        final String[] args = {"-f", "non-existing.file"};

        // Act
        final CommandLine commandLine = parser.parse(options, args);

        // Assert
        assertNull(commandLine.getOptionObject("f"), "Should return null when the file does not exist.");
    }

    @Test
    void parsePattern_shouldThrowUnsupported_forFilesWithAsteriskSymbol() throws Exception {
        // Arrange
        // '*' is intended for multiple files, but is currently unsupported.
        final Options options = PatternOptionBuilder.parsePattern("m*");
        final CommandLineParser parser = new PosixParser();
        final String[] args = {"-m", "test*"};

        // Act
        final CommandLine commandLine = parser.parse(options, args);

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> commandLine.getOptionObject('m'),
            "The '*' pattern for multiple files should throw UnsupportedOperationException.");
    }
}