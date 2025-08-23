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

public class PatternOptionBuilderTestTest5 {

    @Test
    void testNumberPattern() throws Exception {
        final Options options = PatternOptionBuilder.parsePattern("n%d%x%");
        final CommandLineParser parser = new PosixParser();
        // 3,5 fails validation.
        //assertThrows(ParseException.class, () -> parser.parse(options, new String[] {"-n", "1", "-d", "2.1", "-x", "3,5"}));
        final CommandLine line = parser.parse(options, new String[] { "-n", "1", "-d", "2.1", "-x", "3,5" });
        assertEquals(Long.class, line.getOptionObject("n").getClass(), "n object class");
        assertEquals(Long.valueOf(1), line.getOptionObject("n"), "n value");
        assertEquals(Double.class, line.getOptionObject("d").getClass(), "d object class");
        assertEquals(Double.valueOf(2.1), line.getOptionObject("d"), "d value");
        assertNull(line.getOptionObject("x"), "x object");
    }
}
