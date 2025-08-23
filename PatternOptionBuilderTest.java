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
 * Tests for PatternOptionBuilder.
 *
 * The pattern language maps single-letter options to value types:
 * - <letter>       : boolean flag (present/absent)
 * - <letter>:      : String value
 * - <letter>@      : Object created by class name
 * - <letter>+      : Class literal instantiated via empty constructor
 * - <letter>%      : Number (Long when integer, Double when decimal)
 * - <letter>/      : URL
 * - <letter>>      : File
 * - <letter><      : Existing readable file as FileInputStream
 * - !<letter>      : Marks option as required
 *
 * Notes:
 * - This test intentionally uses deprecated PosixParser to validate backward-compatibility behavior.
 * - Some inputs intentionally point to unknown classes or invalid values to verify "null on failure" behavior.
 */
@SuppressWarnings("deprecation") // exercises deprecated parser and behaviors on purpose
class PatternOptionBuilderTest {

    private static final String EXISTING_READABLE_FILE =
            "src/test/resources/org/apache/commons/cli/existing-readable.file";
    private static final String VALID_URL = "https://commons.apache.org";

    // Helpers

    private static Options options(String pattern) {
        return PatternOptionBuilder.parsePattern(pattern);
    }

    private static CommandLine parse(Options options, String... args) throws Exception {
        return new PosixParser().parse(options, args);
    }

    private static CommandLine parse(String pattern, String... args) throws Exception {
        return parse(options(pattern), args);
    }

    // Tests

    @Test
    void parsesClassAndIgnoresUnknownClass() throws Exception {
        // Pattern: c+ (instantiate class), d+ (instantiate class)
        final CommandLine line = parse("c+d+",
                "-c", "java.util.Calendar",
                "-d", "System.DateTime"); // intentionally unknown

        assertEquals(Calendar.class, line.getOptionObject("c"), "c should resolve to Calendar.class");
        assertNull(line.getOptionObject("d"), "d should be null for an unknown class");
    }

    @Test
    void parseEmptyPatternYieldsNoOptions() {
        final Options opts = options("");
        assertTrue(opts.getOptions().isEmpty(), "Empty pattern should produce no options");
    }

    @Test
    void existingReadableFileProducesInputStream() throws Exception {
        // Pattern: g< (existing file -> FileInputStream)
        final CommandLine line = parse("g<", "-g", EXISTING_READABLE_FILE);

        final Object value = line.getOptionObject("g");
        assertNotNull(value, "option g should be parsed");
        assertInstanceOf(FileInputStream.class, value, "g should be a FileInputStream");

        // Avoid resource leak in tests
        if (value instanceof FileInputStream fis) {
            fis.close();
        }
    }

    @Test
    void nonExistingFileYieldsNull() throws Exception {
        // Pattern: f< (existing file -> FileInputStream)
        final CommandLine line = parse("f<", "-f", "non-existing.file");
        assertNull(line.getOptionObject("f"), "f should be null when file does not exist");
    }

    @Test
    void parsesIntegralAndDoubleAndSkipsInvalidLocalizedNumber() throws Exception {
        // Pattern: n% (number), d% (number), x% (number)
        final CommandLine line = parse("n%d%x%",
                "-n", "1",
                "-d", "2.1",
                "-x", "3,5" // invalid in this parser; expects dot as decimal separator
        );

        assertEquals(Long.class, line.getOptionObject("n").getClass(), "n should be Long");
        assertEquals(Long.valueOf(1), line.getOptionObject("n"), "n value");

        assertEquals(Double.class, line.getOptionObject("d").getClass(), "d should be Double");
        assertEquals(Double.valueOf(2.1), line.getOptionObject("d"), "d value");

        assertNull(line.getOptionObject("x"), "x should be null for invalid number format");
    }

    @Test
    void resolvesObjectsAndIgnoresUnknownClasses() throws Exception {
        // Pattern: o@ (object by class name), i@ (object by class name), n@ (object by class name)
        final CommandLine line = parse("o@i@n@",
                "-o", "java.lang.String",
                "-i", "java.util.Calendar", // has no public no-args ctor for a concrete instance here
                "-n", "System.DateTime"     // unknown class
        );

        assertEquals("", line.getOptionObject("o"), "Should create a String instance");
        assertNull(line.getOptionObject("i"), "Calendar should not be instantiated as an object here");
        assertNull(line.getOptionObject("n"), "Unknown class should yield null");
    }

    @Test
    void mandatoryOptionEnforcedByExclamationMark() throws Exception {
        // Pattern: !n% (required number), m% (optional number)
        final MissingOptionException e = assertThrows(MissingOptionException.class,
                () -> parse("!n%m%", "")
        );

        assertEquals(1, e.getMissingOptions().size(), "Exactly one required option should be missing");
        assertTrue(e.getMissingOptions().contains("n"), "Option 'n' should be reported as missing");
    }

    @Test
    void parsesMixedPatternAndSupportsCharAccessorsToo() throws Exception {
        /*
         * The date string format is made deterministic by formatting a known epoch millisecond value using
         * the same formatter PatternOptionBuilder uses internally.
         */
        final String pattern = "a:b@cde>f+n%t/m*z#";
        final Date expectedDate = new Date(1023400137000L);
        final DateFormat fmt = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

        final CommandLine line = parse(pattern,
                "-c",
                "-a", "foo",                              // a: -> String
                "-b", "java.util.Vector",                 // b@ -> new instance of class
                "-e", "build.xml",                        // e> -> File
                "-f", "java.util.Calendar",               // f+ -> Class literal
                "-n", "4.5",                              // n% -> Number
                "-t", VALID_URL,                          // t/ -> URL
                "-z", fmt.format(expectedDate),           // z# -> Date
                "-m", "test*"                             // m* -> FILES (not supported yet)
        );

        // String accessors
        assertEquals("foo", line.getOptionValue("a"), "a value as string");
        assertEquals("foo", line.getOptionObject("a"), "a value as object");
        assertEquals(new Vector<>(), line.getOptionObject("b"), "b instantiated object");
        assertTrue(line.hasOption("c"), "c present (boolean)");
        assertFalse(line.hasOption("d"), "d not present (boolean)");
        assertEquals(new File("build.xml"), line.getOptionObject("e"), "e is File");
        assertEquals(Calendar.class, line.getOptionObject("f"), "f is Class");
        assertEquals(Double.valueOf(4.5), line.getOptionObject("n"), "n is Number");
        assertEquals(new URL(VALID_URL), line.getOptionObject("t"), "t is URL");

        // Char accessors delegate to String accessors
        assertEquals("foo", line.getOptionValue('a'), "a value as char accessor");
        assertEquals("foo", line.getOptionObject('a'), "a value as object via char accessor");
        assertEquals(new Vector<>(), line.getOptionObject('b'), "b instantiated via char accessor");
        assertTrue(line.hasOption('c'), "c present via char accessor");
        assertFalse(line.hasOption('d'), "d not present via char accessor");
        assertEquals(new File("build.xml"), line.getOptionObject('e'), "e via char accessor");
        assertEquals(Calendar.class, line.getOptionObject('f'), "f via char accessor");
        assertEquals(Double.valueOf(4.5), line.getOptionObject('n'), "n via char accessor");
        assertEquals(new URL(VALID_URL), line.getOptionObject('t'), "t via char accessor");

        // FILES ('*') not implemented
        assertThrows(UnsupportedOperationException.class, () -> line.getOptionObject('m'),
                "FILES ('*') not supported yet");

        assertEquals(expectedDate, line.getOptionObject('z'), "z is parsed Date");
    }

    @Test
    void untypedFlagsPresentWithoutValues() throws Exception {
        // Pattern: abc (all booleans)
        final CommandLine line = parse("abc", "-abc");

        assertTrue(line.hasOption('a'));
        assertNull(line.getOptionObject('a'), "a has no value");

        assertTrue(line.hasOption('b'));
        assertNull(line.getOptionObject('b'), "b has no value");

        assertTrue(line.hasOption('c'));
        assertNull(line.getOptionObject('c'), "c has no value");
    }

    @Test
    void parsesValidUrlAndRejectsUnknownProtocol() throws Exception {
        // Pattern: u/ (URL), v/ (URL)
        final CommandLine line = parse("u/v/",
                "-u", VALID_URL,
                "-v", "foo://commons.apache.org" // protocol likely unknown
        );

        assertEquals(new URL(VALID_URL), line.getOptionObject("u"), "u should parse to URL");
        assertNull(line.getOptionObject("v"), "v should be null for unknown protocol");
    }
}