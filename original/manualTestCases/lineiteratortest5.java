package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class GeneratedTestCase {

    @Test
    public void testInvalidEncoding() throws Exception {
        final String encoding = "XXXXXXXX";
        final File testFile = new File(temporaryFolder, "LineIterator-invalidEncoding.txt");
        createLinesFile(testFile, UTF_8, 3);
        assertThrows(UnsupportedCharsetException.class, () -> FileUtils.lineIterator(testFile, encoding));
    }
}
