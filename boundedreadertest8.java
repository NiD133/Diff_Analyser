package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.file.TempFile;
import org.junit.jupiter.api.Test;

public class BoundedReaderTestTest8 {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private static final String STRING_END_NO_EOL = "0\n1\n2";

    private static final String STRING_END_EOL = "0\n1\n2\n";

    private final Reader sr = new BufferedReader(new StringReader("01234567890"));

    private final Reader shortReader = new BufferedReader(new StringReader("01"));

    private void testLineNumberReader(final Reader source) throws IOException {
        try (LineNumberReader reader = new LineNumberReader(new BoundedReader(source, 10_000_000))) {
            while (reader.readLine() != null) {
                // noop
            }
        }
    }

    void testLineNumberReaderAndFileReaderLastLine(final String data) throws IOException {
        try (TempFile path = TempFile.create(getClass().getSimpleName(), ".txt")) {
            final File file = path.toFile();
            FileUtils.write(file, data, StandardCharsets.ISO_8859_1);
            try (Reader source = Files.newBufferedReader(file.toPath())) {
                testLineNumberReader(source);
            }
        }
    }

    @Test
    void testMarkResetMarkMore() throws IOException {
        try (BoundedReader mr = new BoundedReader(sr, 3)) {
            mr.mark(4);
            mr.read();
            mr.read();
            mr.read();
            mr.reset();
            mr.read();
            mr.read();
            mr.read();
            assertEquals(-1, mr.read());
        }
    }
}
