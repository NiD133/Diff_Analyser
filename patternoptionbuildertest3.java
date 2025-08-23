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

public class PatternOptionBuilderTestTest3 {

    @Test
    void testExistingFilePattern() throws Exception {
        final Options options = PatternOptionBuilder.parsePattern("g<");
        final CommandLineParser parser = new PosixParser();
        final CommandLine line = parser.parse(options, new String[] { "-g", "src/test/resources/org/apache/commons/cli/existing-readable.file" });
        final Object parsedReadableFileStream = line.getOptionObject("g");
        assertNotNull(parsedReadableFileStream, "option g not parsed");
        assertInstanceOf(FileInputStream.class, parsedReadableFileStream, "option g not FileInputStream");
    }
}
