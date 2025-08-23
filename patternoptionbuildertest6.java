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

public class PatternOptionBuilderTestTest6 {

    @Test
    void testObjectPattern() throws Exception {
        final Options options = PatternOptionBuilder.parsePattern("o@i@n@");
        final CommandLineParser parser = new PosixParser();
        final CommandLine line = parser.parse(options, new String[] { "-o", "java.lang.String", "-i", "java.util.Calendar", "-n", "System.DateTime" });
        assertEquals("", line.getOptionObject("o"), "o value");
        assertNull(line.getOptionObject("i"), "i value");
        assertNull(line.getOptionObject("n"), "n value");
    }
}
