package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.*;

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
 * Test suite for the PatternOptionBuilder class.
 */
@SuppressWarnings("deprecation") // tests some deprecated classes
class PatternOptionBuilderTest {

    @Test
    void testClassPattern() throws Exception {
        // Parse pattern with class options
        final Options options = PatternOptionBuilder.parsePattern("c+d+");
        final CommandLineParser parser = new PosixParser();
        final CommandLine line = parser.parse(options, new String[] {"-c", "java.util.Calendar", "-d", "System.DateTime"});

        // Verify that the 'c' option is parsed as Calendar class
        assertEquals(Calendar.class, line.getOptionObject("c"), "Expected 'c' to be parsed as Calendar class");

        // Verify that the 'd' option is not parsed
        assertNull(line.getOptionObject("d"), "Expected 'd' to be null");
    }

    @Test
    void testEmptyPattern() {
        // Parse an empty pattern
        final Options options = PatternOptionBuilder.parsePattern("");

        // Verify that no options are created
        assertTrue(options.getOptions().isEmpty(), "Expected no options for empty pattern");
    }

    @Test
    void testExistingFilePattern() throws Exception {
        // Parse pattern with existing file option
        final Options options = PatternOptionBuilder.parsePattern("g<");
        final CommandLineParser parser = new PosixParser();
        final CommandLine line = parser.parse(options, new String[] {"-g", "src/test/resources/org/apache/commons/cli/existing-readable.file"});

        // Verify that the 'g' option is parsed as a FileInputStream
        final Object parsedReadableFileStream = line.getOptionObject("g");
        assertNotNull(parsedReadableFileStream, "Expected 'g' option to be parsed");
        assertInstanceOf(FileInputStream.class, parsedReadableFileStream, "Expected 'g' to be a FileInputStream");
    }

    @Test
    void testExistingFilePatternFileNotExist() throws Exception {
        // Parse pattern with a non-existing file option
        final Options options = PatternOptionBuilder.parsePattern("f<");
        final CommandLineParser parser = new PosixParser();
        final CommandLine line = parser.parse(options, new String[] {"-f", "non-existing.file"});

        // Verify that the 'f' option is not parsed
        assertNull(line.getOptionObject("f"), "Expected 'f' to be null for non-existing file");
    }

    @Test
    void testNumberPattern() throws Exception {
        // Parse pattern with number options
        final Options options = PatternOptionBuilder.parsePattern("n%d%x%");
        final CommandLineParser parser = new PosixParser();
        final CommandLine line = parser.parse(options, new String[] {"-n", "1", "-d", "2.1", "-x", "3,5"});

        // Verify that the 'n' option is parsed as Long
        assertEquals(Long.class, line.getOptionObject("n").getClass(), "Expected 'n' to be Long class");
        assertEquals(Long.valueOf(1), line.getOptionObject("n"), "Expected 'n' value to be 1");

        // Verify that the 'd' option is parsed as Double
        assertEquals(Double.class, line.getOptionObject("d").getClass(), "Expected 'd' to be Double class");
        assertEquals(Double.valueOf(2.1), line.getOptionObject("d"), "Expected 'd' value to be 2.1");

        // Verify that the 'x' option is not parsed
        assertNull(line.getOptionObject("x"), "Expected 'x' to be null");
    }

    @Test
    void testObjectPattern() throws Exception {
        // Parse pattern with object options
        final Options options = PatternOptionBuilder.parsePattern("o@i@n@");
        final CommandLineParser parser = new PosixParser();
        final CommandLine line = parser.parse(options, new String[] {"-o", "java.lang.String", "-i", "java.util.Calendar", "-n", "System.DateTime"});

        // Verify that the 'o' option is parsed as an empty string
        assertEquals("", line.getOptionObject("o"), "Expected 'o' value to be an empty string");

        // Verify that 'i' and 'n' options are not parsed
        assertNull(line.getOptionObject("i"), "Expected 'i' to be null");
        assertNull(line.getOptionObject("n"), "Expected 'n' to be null");
    }

    @Test
    void testRequiredOption() throws Exception {
        // Parse pattern with a required option
        final Options options = PatternOptionBuilder.parsePattern("!n%m%");
        final CommandLineParser parser = new PosixParser();

        // Verify that a MissingOptionException is thrown for missing required option
        final MissingOptionException e = assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[] { "" }));
        assertEquals(1, e.getMissingOptions().size(), "Expected one missing option");
        assertTrue(e.getMissingOptions().contains("n"), "Expected 'n' to be missing");
    }

    @Test
    void testSimplePattern() throws Exception {
        // Parse a complex pattern with various option types
        final Options options = PatternOptionBuilder.parsePattern("a:b@cde>f+n%t/m*z#");
        final Date expectedDate = new Date(1023400137000L);
        final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        final String[] args = {"-c", "-a", "foo", "-b", "java.util.Vector", "-e", "build.xml", "-f", "java.util.Calendar", "-n", "4.5", "-t",
            "https://commons.apache.org", "-z", dateFormat.format(expectedDate), "-m", "test*"};
        final CommandLineParser parser = new PosixParser();
        final CommandLine line = parser.parse(options, args);

        // Verify various option values
        assertEquals("foo", line.getOptionValue("a"), "Expected 'a' value to be 'foo'");
        assertEquals(new Vector<>(), line.getOptionObject("b"), "Expected 'b' to be a Vector");
        assertTrue(line.hasOption("c"), "Expected 'c' to be true");
        assertFalse(line.hasOption("d"), "Expected 'd' to be false");
        assertEquals(new File("build.xml"), line.getOptionObject("e"), "Expected 'e' to be a File");
        assertEquals(Calendar.class, line.getOptionObject("f"), "Expected 'f' to be Calendar class");
        assertEquals(Double.valueOf(4.5), line.getOptionObject("n"), "Expected 'n' value to be 4.5");
        assertEquals(new URL("https://commons.apache.org"), line.getOptionObject("t"), "Expected 't' to be a URL");

        // Verify unsupported operation for 'm' option
        assertThrows(UnsupportedOperationException.class, () -> line.getOptionObject('m'));

        // Verify date option
        assertEquals(expectedDate, line.getOptionObject('z'), "Expected 'z' to be the expected date");
    }

    @Test
    void testUntypedPattern() throws Exception {
        // Parse pattern with untyped options
        final Options options = PatternOptionBuilder.parsePattern("abc");
        final CommandLineParser parser = new PosixParser();
        final CommandLine line = parser.parse(options, new String[] {"-abc"});

        // Verify that 'a', 'b', and 'c' options are present and have null values
        assertTrue(line.hasOption('a'), "Expected 'a' to be present");
        assertNull(line.getOptionObject('a'), "Expected 'a' value to be null");
        assertTrue(line.hasOption('b'), "Expected 'b' to be present");
        assertNull(line.getOptionObject('b'), "Expected 'b' value to be null");
        assertTrue(line.hasOption('c'), "Expected 'c' to be present");
        assertNull(line.getOptionObject('c'), "Expected 'c' value to be null");
    }

    @Test
    void testURLPattern() throws Exception {
        // Parse pattern with URL options
        final Options options = PatternOptionBuilder.parsePattern("u/v/");
        final CommandLineParser parser = new PosixParser();
        final CommandLine line = parser.parse(options, new String[] {"-u", "https://commons.apache.org", "-v", "foo://commons.apache.org"});

        // Verify that 'u' option is parsed as a URL and 'v' is not
        assertEquals(new URL("https://commons.apache.org"), line.getOptionObject("u"), "Expected 'u' to be a URL");
        assertNull(line.getOptionObject("v"), "Expected 'v' to be null");
    }
}