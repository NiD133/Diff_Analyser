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

/**
 * Tests {@link BoundedReader}.
 */
public class BoundedReaderTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private static final String STRING_END_NO_EOL = "0\n1\n2";

    private static final String STRING_END_EOL = "0\n1\n2\n";

    private final Reader stringReader = new BufferedReader(new StringReader("01234567890"));

    private final Reader shortReader = new BufferedReader(new StringReader("01"));

    /**
     * Tests that the underlying reader is closed when the BoundedReader is closed.
     * 
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void testClose() throws IOException {
        final AtomicBoolean closed = new AtomicBoolean();
        try (Reader reader = new BufferedReader(new StringReader("01234567890")) {
            @Override
            public void close() throws IOException {
                closed.set(true);
                super.close();
            }
        }) {

            try (BoundedReader boundedReader = new BoundedReader(reader, 3)) {
                // nothing
            }
        }
        assertTrue(closed.get());
    }

    /**
     * Tests that the LineNumberReader works correctly with the BoundedReader.
     * 
     * @param source The source reader
     * @throws IOException If an I/O error occurs
     */
    private void testLineNumberReader(final Reader source) throws IOException {
        try (LineNumberReader reader = new LineNumberReader(new BoundedReader(source, 10_000_000))) {
            while (reader.readLine() != null) {
                // noop
            }
        }
    }

    /**
     * Tests that the LineNumberReader works correctly with the BoundedReader and a file reader.
     * 
     * @param data The data to write to the file
     * @throws IOException If an I/O error occurs
     */
    private void testLineNumberReaderAndFileReader(final String data) throws IOException {
        try (TempFile path = TempFile.create(getClass().getSimpleName(), ".txt")) {
            final File file = path.toFile();
            FileUtils.write(file, data, StandardCharsets.ISO_8859_1);
            try (Reader source = Files.newBufferedReader(file.toPath())) {
                testLineNumberReader(source);
            }
        }
    }

    /**
     * Tests that the LineNumberReader works correctly with the BoundedReader and a file reader, without a trailing EOL.
     */
    @Test
    public void testLineNumberReaderAndFileReaderNoEol() {
        assertTimeout(TIMEOUT, () -> testLineNumberReaderAndFileReader(STRING_END_NO_EOL));
    }

    /**
     * Tests that the LineNumberReader works correctly with the BoundedReader and a file reader, with a trailing EOL.
     */
    @Test
    public void testLineNumberReaderAndFileReaderEol() {
        assertTimeout(TIMEOUT, () -> testLineNumberReaderAndFileReader(STRING_END_EOL));
    }

    /**
     * Tests that the LineNumberReader works correctly with the BoundedReader and a string reader, without a trailing EOL.
     */
    @Test
    public void testLineNumberReaderAndStringReaderNoEol() {
        assertTimeout(TIMEOUT, () -> testLineNumberReader(new StringReader(STRING_END_NO_EOL)));
    }

    /**
     * Tests that the LineNumberReader works correctly with the BoundedReader and a string reader, with a trailing EOL.
     */
    @Test
    public void testLineNumberReaderAndStringReaderEol() {
        assertTimeout(TIMEOUT, () -> testLineNumberReader(new StringReader(STRING_END_EOL)));
    }

    /**
     * Tests that the mark and reset methods work correctly.
     * 
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void testMarkReset() throws IOException {
        try (BoundedReader boundedReader = new BoundedReader(stringReader, 3)) {
            boundedReader.mark(3);
            boundedReader.read();
            boundedReader.read();
            boundedReader.read();
            boundedReader.reset();
            boundedReader.read();
            boundedReader.read();
            boundedReader.read();
            assertEquals(-1, boundedReader.read());
        }
    }

    /**
     * Tests that the mark and reset methods work correctly, with an offset of 1.
     * 
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void testMarkResetFromOffset1() throws IOException {
        try (BoundedReader boundedReader = new BoundedReader(stringReader, 3)) {
            boundedReader.mark(3);
            boundedReader.read();
            boundedReader.read();
            boundedReader.read();
            assertEquals(-1, boundedReader.read());
            boundedReader.reset();
            boundedReader.mark(1);
            boundedReader.read();
            assertEquals(-1, boundedReader.read());
        }
    }

    /**
     * Tests that the mark and reset methods work correctly, with a mark value greater than the bounded reader's limit.
     * 
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void testMarkResetMarkMore() throws IOException {
        try (BoundedReader boundedReader = new BoundedReader(stringReader, 3)) {
            boundedReader.mark(4);
            boundedReader.read();
            boundedReader.read();
            boundedReader.read();
            boundedReader.reset();
            boundedReader.read();
            boundedReader.read();
            boundedReader.read();
            assertEquals(-1, boundedReader.read());
        }
    }

    /**
     * Tests that the mark and reset methods work correctly, with a mark value greater than the bounded reader's limit, and no initial offset.
     * 
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void testMarkResetWithMarkOutsideBoundedReaderMax() throws IOException {
        try (BoundedReader boundedReader = new BoundedReader(stringReader, 3)) {
            boundedReader.mark(4);
            boundedReader.read();
            boundedReader.read();
            boundedReader.read();
            assertEquals(-1, boundedReader.read());
        }
    }

    /**
     * Tests that the mark and reset methods work correctly, with a mark value greater than the bounded reader's limit, and an initial offset.
     * 
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void testMarkResetWithMarkOutsideBoundedReaderMaxAndInitialOffset() throws IOException {
        try (BoundedReader boundedReader = new BoundedReader(stringReader, 3)) {
            boundedReader.read();
            boundedReader.mark(3);
            boundedReader.read();
            boundedReader.read();
            assertEquals(-1, boundedReader.read());
        }
    }

    /**
     * Tests that reading bytes works correctly until EOF.
     */
    @Test
    public void testReadBytesEOF() {
        assertTimeout(TIMEOUT, () -> {
            final BoundedReader boundedReader = new BoundedReader(stringReader, 3);
            try (BufferedReader bufferedReader = new BufferedReader(boundedReader)) {
                bufferedReader.readLine();
                bufferedReader.readLine();
            }
        });
    }

    /**
     * Tests that reading multiple characters works correctly.
     * 
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void testReadMulti() throws IOException {
        try (BoundedReader boundedReader = new BoundedReader(stringReader, 3)) {
            final char[] cbuf = new char[4];
            Arrays.fill(cbuf, 'X');
            final int read = boundedReader.read(cbuf, 0, 4);
            assertEquals(3, read);
            assertEquals('0', cbuf[0]);
            assertEquals('1', cbuf[1]);
            assertEquals('2', cbuf[2]);
            assertEquals('X', cbuf[3]);
        }
    }

    /**
     * Tests that reading multiple characters works correctly, with an offset.
     * 
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void testReadMultiWithOffset() throws IOException {
        try (BoundedReader boundedReader = new BoundedReader(stringReader, 3)) {
            final char[] cbuf = new char[4];
            Arrays.fill(cbuf, 'X');
            final int read = boundedReader.read(cbuf, 1, 2);
            assertEquals(2, read);
            assertEquals('X', cbuf[0]);
            assertEquals('0', cbuf[1]);
            assertEquals('1', cbuf[2]);
            assertEquals('X', cbuf[3]);
        }
    }

    /**
     * Tests that reading until the end works correctly.
     * 
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void testReadTillEnd() throws IOException {
        try (BoundedReader boundedReader = new BoundedReader(stringReader, 3)) {
            boundedReader.read();
            boundedReader.read();
            boundedReader.read();
            assertEquals(-1, boundedReader.read());
        }
    }

    /**
     * Tests that reading from a short reader works correctly.
     * 
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void testShortReader() throws IOException {
        try (BoundedReader boundedReader = new BoundedReader(shortReader, 3)) {
            boundedReader.read();
            boundedReader.read();
            assertEquals(-1, boundedReader.read());
        }
    }

    /**
     * Tests that skipping characters works correctly.
     * 
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void testSkip() throws IOException {
        try (BoundedReader boundedReader = new BoundedReader(stringReader, 3)) {
            boundedReader.skip(2);
            boundedReader.read();
            assertEquals(-1, boundedReader.read());
        }
    }
}