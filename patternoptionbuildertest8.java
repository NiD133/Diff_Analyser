package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PatternOptionBuilder}.
 * This suite focuses on verifying that command-line arguments are correctly parsed
 * into their expected types based on the pattern string.
 */
@DisplayName("PatternOptionBuilder Tests")
class PatternOptionBuilderTest {

    private CommandLineParser parser;

    @BeforeEach
    void setUp() {
        // Using a common parser for all tests
        parser = new PosixParser();
    }

    @Test
    @DisplayName("Simple flags should be correctly identified as present or absent")
    void parsePattern_withSimpleFlags_shouldCorrectlyIdentifyPresence() throws ParseException {
        // Arrange
        final Options options = PatternOptionBuilder.parsePattern("cd");
        final String[] args = {"-c"};

        // Act
        final CommandLine line = parser.parse(options, args);

        // Assert
        assertTrue(line.hasOption("c"), "Flag 'c' should be present");
        assertTrue(line.hasOption('c'), "Flag 'c' should be present (char access)");

        assertFalse(line.hasOption("d"), "Flag 'd' should be absent");
        assertFalse(line.hasOption('d'), "Flag 'd' should be absent (char access)");
    }

    @Test
    @DisplayName("Pattern ':' should parse argument as a String")
    void parsePattern_withStringArgument_shouldReturnCorrectValue() throws ParseException {
        // Arrange
        final Options options = PatternOptionBuilder.parsePattern("a:");
        final String[] args = {"-a", "foo"};
        final String expectedValue = "foo";

        // Act
        final CommandLine line = parser.parse(options, args);

        // Assert
        assertEquals(expectedValue, line.getOptionValue("a"));
        assertEquals(expectedValue, line.getOptionValue('a'));
        assertEquals(expectedValue, line.getOptionObject("a"));
        assertEquals(expectedValue, line.getOptionObject('a'));
    }

    @Test
    @DisplayName("Pattern '@' should parse argument as an existing Object")
    void parsePattern_withObjectArgument_shouldReturnInstantiatedObject() throws ParseException {
        // Arrange
        final Options options = PatternOptionBuilder.parsePattern("b@");
        final String[] args = {"-b", "java.util.Vector"};

        // Act
        final CommandLine line = parser.parse(options, args);

        // Assert
        assertEquals(new Vector<>(), line.getOptionObject("b"));
        assertEquals(new Vector<>(), line.getOptionObject('b'));
    }

    @Test
    @DisplayName("Pattern '>' should parse argument as a File")
    void parsePattern_withFileArgument_shouldReturnFileObject() throws ParseException {
        // Arrange
        final Options options = PatternOptionBuilder.parsePattern("e>");
        final String[] args = {"-e", "build.xml"};

        // Act
        final CommandLine line = parser.parse(options, args);

        // Assert
        assertEquals(new File("build.xml"), line.getOptionObject("e"));
        assertEquals(new File("build.xml"), line.getOptionObject('e'));
    }

    @Test
    @DisplayName("Pattern '+' should parse argument as a Class")
    void parsePattern_withClassArgument_shouldReturnClassObject() throws ParseException {
        // Arrange
        final Options options = PatternOptionBuilder.parsePattern("f+");
        final String[] args = {"-f", "java.util.Calendar"};

        // Act
        final CommandLine line = parser.parse(options, args);

        // Assert
        assertEquals(Calendar.class, line.getOptionObject("f"));
        assertEquals(Calendar.class, line.getOptionObject('f'));
    }

    @Test
    @DisplayName("Pattern '%' should parse argument as a Number")
    void parsePattern_withNumberArgument_shouldReturnNumberObject() throws ParseException {
        // Arrange
        final Options options = PatternOptionBuilder.parsePattern("n%");
        final String[] args = {"-n", "4.5"};

        // Act
        final CommandLine line = parser.parse(options, args);

        // Assert
        assertEquals(Double.valueOf(4.5), line.getOptionObject("n"));
        assertEquals(Double.valueOf(4.5), line.getOptionObject('n'));
    }

    @Test
    @DisplayName("Pattern '/' should parse argument as a URL")
    void parsePattern_withUrlArgument_shouldReturnUrlObject() throws Exception {
        // Arrange
        final Options options = PatternOptionBuilder.parsePattern("t/");
        final String[] args = {"-t", "https://commons.apache.org"};

        // Act
        final CommandLine line = parser.parse(options, args);

        // Assert
        assertEquals(new URL("https://commons.apache.org"), line.getOptionObject("t"));
        assertEquals(new URL("https://commons.apache.org"), line.getOptionObject('t'));
    }

    @Test
    @DisplayName("Pattern '#' should parse argument as a Date")
    void parsePattern_withDateArgument_shouldReturnDateObject() throws ParseException {
        // Arrange
        /*
         * Dates calculated from strings are dependent upon configuration and environment settings for the
         * machine on which the test is running. To avoid this problem, convert a fixed time into a
         * string and then parse that string. This produces strings that always match the correct
         * time zone for the test environment.
         */
        final Options options = PatternOptionBuilder.parsePattern("z#");
        final Date expectedDate = new Date(1023400137000L);
        final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        final String dateString = dateFormat.format(expectedDate);
        final String[] args = {"-z", dateString};

        // Act
        final CommandLine line = parser.parse(options, args);

        // Assert
        assertEquals(expectedDate, line.getOptionObject("z"));
        assertEquals(expectedDate, line.getOptionObject('z'));
    }

    @Test
    @DisplayName("Pattern '*' for multiple files should throw UnsupportedOperationException on access")
    void parsePattern_withUnsupportedFilesType_shouldThrowExceptionOnAccess() throws ParseException {
        // Arrange
        // The '*' pattern is for multiple files (File[]), which is not yet supported.
        final Options options = PatternOptionBuilder.parsePattern("m*");
        final String[] args = {"-m", "test.txt"}; // Argument is required, but access will fail
        final CommandLine line = parser.parse(options, args);

        // Act & Assert
        assertThrows(UnsupportedOperationException.class,
            () -> line.getOptionObject("m"),
            "Accessing an unsupported type should throw an exception.");

        assertThrows(UnsupportedOperationException.class,
            () -> line.getOptionObject('m'),
            "Accessing an unsupported type should throw an exception (char access).");
    }
}