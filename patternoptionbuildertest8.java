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

public class PatternOptionBuilderTestTest8 {

    @Test
    void testSimplePattern() throws Exception {
        /*
         * Dates calculated from strings are dependent upon configuration and environment settings for the
         * machine on which the test is running.  To avoid this problem, convert the time into a string
         * and then unparse that using the converter.  This produces strings that always match the correct
         * time zone.
         */
        final Options options = PatternOptionBuilder.parsePattern("a:b@cde>f+n%t/m*z#");
        final Date expectedDate = new Date(1023400137000L);
        final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        final String[] args = { "-c", "-a", "foo", "-b", "java.util.Vector", "-e", "build.xml", "-f", "java.util.Calendar", "-n", "4.5", "-t", "https://commons.apache.org", "-z", dateFormat.format(expectedDate), "-m", "test*" };
        final CommandLineParser parser = new PosixParser();
        final CommandLine line = parser.parse(options, args);
        assertEquals("foo", line.getOptionValue("a"), "flag a");
        assertEquals("foo", line.getOptionObject("a"), "string flag a");
        assertEquals(new Vector<>(), line.getOptionObject("b"), "object flag b");
        assertTrue(line.hasOption("c"), "boolean true flag c");
        assertFalse(line.hasOption("d"), "boolean false flag d");
        assertEquals(new File("build.xml"), line.getOptionObject("e"), "file flag e");
        assertEquals(Calendar.class, line.getOptionObject("f"), "class flag f");
        assertEquals(Double.valueOf(4.5), line.getOptionObject("n"), "number flag n");
        assertEquals(new URL("https://commons.apache.org"), line.getOptionObject("t"), "url flag t");
        // tests the char methods of CommandLine that delegate to the String methods
        assertEquals("foo", line.getOptionValue('a'), "flag a");
        assertEquals("foo", line.getOptionObject('a'), "string flag a");
        assertEquals(new Vector<>(), line.getOptionObject('b'), "object flag b");
        assertTrue(line.hasOption('c'), "boolean true flag c");
        assertFalse(line.hasOption('d'), "boolean false flag d");
        assertEquals(new File("build.xml"), line.getOptionObject('e'), "file flag e");
        assertEquals(Calendar.class, line.getOptionObject('f'), "class flag f");
        assertEquals(Double.valueOf(4.5), line.getOptionObject('n'), "number flag n");
        assertEquals(new URL("https://commons.apache.org"), line.getOptionObject('t'), "url flag t");
        // FILES NOT SUPPORTED YET
        assertThrows(UnsupportedOperationException.class, () -> line.getOptionObject('m'));
        assertEquals(expectedDate, line.getOptionObject('z'), "date flag z");
    }
}
