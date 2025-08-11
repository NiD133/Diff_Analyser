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

@SuppressWarnings("deprecation")
class PatternOptionBuilderTest {

    private static final String VALID_URL = "https://commons.apache.org";
    private static final String INVALID_URL = "foo://commons.apache.org";
    private static final String EXISTING_FILE = "src/test/resources/org/apache/commons/cli/existing-readable.file";
    private static final String NON_EXISTING_FILE = "non-existing.file";
    private static final String VALID_CLASS = "java.util.Calendar";
    private static final String INVALID_CLASS = "System.DateTime";

    @Test
    void testClassPattern_ValidClassParsed_InvalidClassReturnsNull() throws Exception {
        final Options options = PatternOptionBuilder.parsePattern("c+d+");
        final CommandLineParser parser = new PosixParser();
        final CommandLine line = parser.parse(options, new String[] {"-c", VALID_CLASS, "-d", INVALID_CLASS});
        
        assertEquals(Calendar.class, line.getOptionObject("c"), "Valid class should be loaded");
        assertNull(line.getOptionObject("d"), "Invalid class should return null");
    }

    @Test
    void testEmptyPattern_ReturnsEmptyOptions() {
        final Options options = PatternOptionBuilder.parsePattern("");
        assertTrue(options.getOptions().isEmpty(), "Empty pattern should produce no options");
    }

    @Test
    void testExistingFilePattern_WithExistingFile_ReturnsFileInputStream() throws Exception {
        final Options options = PatternOptionBuilder.parsePattern("g<");
        final CommandLineParser parser = new PosixParser();
        final CommandLine line = parser.parse(options, new String[] {"-g", EXISTING_FILE});
        
        final Object parsedValue = line.getOptionObject("g");
        assertNotNull(parsedValue, "FileInputStream should be created for existing file");
        assertInstanceOf(FileInputStream.class, parsedValue, "Parsed object should be FileInputStream");
    }

    @Test
    void testExistingFilePattern_WithNonExistingFile_ReturnsNull() throws Exception {
        final Options options = PatternOptionBuilder.parsePattern("f<");
        final CommandLineParser parser = new PosixParser();
        final CommandLine line = parser.parse(options, new String[] {"-f", NON_EXISTING_FILE});
        
        assertNull(line.getOptionObject("f"), "Non-existing file should return null");
    }

    @Test
    void testNumberPattern_ValidNumbersParsed_InvalidNumberReturnsNull() throws Exception {
        final Options options = PatternOptionBuilder.parsePattern("n%d%x%");
        final CommandLineParser parser = new PosixParser();
        final CommandLine line = parser.parse(options, new String[] {
            "-n", "1", 
            "-d", "2.1", 
            "-x", "3,5"  // Invalid number format
        });
        
        // Valid numbers
        assertEquals(Long.valueOf(1), line.getOptionObject("n"), "Integer should be parsed as Long");
        assertEquals(Double.valueOf(2.1), line.getOptionObject("d"), "Decimal should be parsed as Double");
        
        // Invalid number
        assertNull(line.getOptionObject("x"), "Invalid number format should return null");
    }

    @Test
    void testObjectPattern_ValidObjectCreated_InvalidClassesReturnNull() throws Exception {
        final Options options = PatternOptionBuilder.parsePattern("o@i@n@");
        final CommandLineParser parser = new PosixParser();
        final CommandLine line = parser.parse(options, new String[] {
            "-o", "java.lang.String", 
            "-i", VALID_CLASS, 
            "-n", INVALID_CLASS
        });
        
        assertEquals("", line.getOptionObject("o"), "String should be instantiated");
        assertNull(line.getOptionObject("i"), "Calendar should fail (no no-arg constructor)");
        assertNull(line.getOptionObject("n"), "Invalid class should return null");
    }

    @Test
    void testRequiredOption_MissingRequiredOption_ThrowsException() {
        final Options options = PatternOptionBuilder.parsePattern("!n%m%");
        final CommandLineParser parser = new PosixParser();
        
        final MissingOptionException e = assertThrows(MissingOptionException.class, 
            () -> parser.parse(options, new String[] {""}),
            "Missing required option should throw exception");
        
        assertEquals(1, e.getMissingOptions().size());
        assertTrue(e.getMissingOptions().contains("n"));
    }

    @Test
    void testComplexPattern_MultipleOptionTypes_ParsedCorrectly() throws Exception {
        // Setup: Pattern includes all supported option types
        final String pattern = "a:b@cde>f+n%t/m*z#";
        final Options options = PatternOptionBuilder.parsePattern(pattern);
        
        // Test data
        final Date expectedDate = new Date(1023400137000L);
        final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        final String dateString = dateFormat.format(expectedDate);
        
        final String[] args = {
            "-c",                      // boolean flag
            "-a", "foo",               // string value
            "-b", "java.util.Vector",  // object instantiation
            "-e", "build.xml",         // file
            "-f", VALID_CLASS,         // class
            "-n", "4.5",               // number
            "-t", VALID_URL,           // URL
            "-z", dateString,          // date
            "-m", "test*"              // files (unsupported)
        };
        
        // Parse command line
        final CommandLineParser parser = new PosixParser();
        final CommandLine line = parser.parse(options, args);
        
        // Verify string-based access
        // -- String option (a:)
        assertEquals("foo", line.getOptionValue("a"), "String value mismatch");
        assertEquals("foo", line.getOptionObject("a"), "String object mismatch");
        
        // -- Object option (b@)
        assertEquals(new Vector<>(), line.getOptionObject("b"), "Vector object mismatch");
        
        // -- Boolean flags (c, d)
        assertTrue(line.hasOption("c"), "Flag c should be set");
        assertFalse(line.hasOption("d"), "Flag d should be unset");
        
        // -- File option (e>)
        assertEquals(new File("build.xml"), line.getOptionObject("e"), "File object mismatch");
        
        // -- Class option (f+)
        assertEquals(Calendar.class, line.getOptionObject("f"), "Class object mismatch");
        
        // -- Number option (n%)
        assertEquals(Double.valueOf(4.5), line.getOptionObject("n"), "Number value mismatch");
        
        // -- URL option (t/)
        assertEquals(new URL(VALID_URL), line.getOptionObject("t"), "URL mismatch");
        
        // -- Date option (z#)
        assertEquals(expectedDate, line.getOptionObject("z"), "Date value mismatch");
        
        // -- Files option (m*) - unsupported
        assertThrows(UnsupportedOperationException.class, 
            () -> line.getOptionObject("m"),
            "Files option should throw UnsupportedOperationException");

        // Verify char-based access (same expectations as string-based)
        assertEquals("foo", line.getOptionValue('a'), "char: string value");
        assertEquals("foo", line.getOptionObject('a'), "char: string object");
        assertEquals(new Vector<>(), line.getOptionObject('b'), "char: vector object");
        assertTrue(line.hasOption('c'), "char: boolean true");
        assertFalse(line.hasOption('d'), "char: boolean false");
        assertEquals(new File("build.xml"), line.getOptionObject('e'), "char: file object");
        assertEquals(Calendar.class, line.getOptionObject('f'), "char: class object");
        assertEquals(Double.valueOf(4.5), line.getOptionObject('n'), "char: number value");
        assertEquals(new URL(VALID_URL), line.getOptionObject('t'), "char: URL value");
        assertEquals(expectedDate, line.getOptionObject('z'), "char: date value");
        
        // Files option unsupported for char access
        assertThrows(UnsupportedOperationException.class, 
            () -> line.getOptionObject('m'),
            "char: files option should throw");
    }

    @Test
    void testUntypedPattern_FlagsSet_NoValuesAttached() throws Exception {
        final Options options = PatternOptionBuilder.parsePattern("abc");
        final CommandLineParser parser = new PosixParser();
        final CommandLine line = parser.parse(options, new String[] {"-abc"});
        
        assertTrue(line.hasOption('a'), "Flag a should be set");
        assertNull(line.getOptionObject('a'), "Flag a should have no value");
        assertTrue(line.hasOption('b'), "Flag b should be set");
        assertNull(line.getOptionObject('b'), "Flag b should have no value");
        assertTrue(line.hasOption('c'), "Flag c should be set");
        assertNull(line.getOptionObject('c'), "Flag c should have no value");
    }

    @Test
    void testURLPattern_ValidUrlParsed_InvalidUrlReturnsNull() throws Exception {
        final Options options = PatternOptionBuilder.parsePattern("u/v/");
        final CommandLineParser parser = new PosixParser();
        final CommandLine line = parser.parse(options, new String[] {
            "-u", VALID_URL, 
            "-v", INVALID_URL
        });
        
        assertEquals(new URL(VALID_URL), line.getOptionObject("u"), "Valid URL should be created");
        assertNull(line.getOptionObject("v"), "Invalid URL should return null");
    }
}