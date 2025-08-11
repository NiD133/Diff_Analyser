package org.apache.commons.io.input;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

/**
 * Tests for WindowsLineEndingInputStream focusing on readability and intent.
 *
 * These tests verify:
 * - Conversion of LF and CR to CRLF
 * - Preservation of existing CRLF
 * - Optional injection of a trailing CRLF at end-of-stream
 * - Unsupported mark/reset behavior
 * - Delegation of close and propagation of IOExceptions
 */
public class WindowsLineEndingInputStreamTest {

    // Helpers

    private static byte[] readAll(final InputStream in) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buf = new byte[256];
        int n;
        while ((n = in.read(buf)) != -1) {
            out.write(buf, 0, n);
        }
        return out.toByteArray();
    }

    private static byte[] bytes(final String s) {
        return s.getBytes(US_ASCII);
    }

    private static String ascii(final byte[] b) {
        return new String(b, US_ASCII);
    }

    // Test cases

    @Test
    public void convertsLfToCrLf() throws IOException {
        // Arrange
        final byte[] input = bytes("a\nb\nc\n");
        // Act
        final byte[] output;
        try (WindowsLineEndingInputStream in =
                     new WindowsLineEndingInputStream(new ByteArrayInputStream(input), false)) {
            output = readAll(in);
        }
        // Assert
        assertEquals("a\r\nb\r\nc\r\n", ascii(output));
    }

    @Test
    public void preservesExistingCrLf() throws IOException {
        // Arrange
        final byte[] input = bytes("hello\r\nworld\r\n");
        // Act
        final byte[] output;
        try (WindowsLineEndingInputStream in =
                     new WindowsLineEndingInputStream(new ByteArrayInputStream(input), false)) {
            output = readAll(in);
        }
        // Assert
        assertEquals("hello\r\nworld\r\n", ascii(output));
    }

    @Test
    public void convertsLoneCrToCrLf() throws IOException {
        // Arrange
        final byte[] input = bytes("line1\rline2\r");
        // Act
        final byte[] output;
        try (WindowsLineEndingInputStream in =
                     new WindowsLineEndingInputStream(new ByteArrayInputStream(input), false)) {
            output = readAll(in);
        }
        // Assert
        assertEquals("line1\r\nline2\r\n", ascii(output));
    }

    @Test
    public void injectsCrLfAtEndWhenConfigured_true_emptyInput() throws IOException {
        // Arrange
        final byte[] input = bytes("");
        // Act
        final byte[] output;
        try (WindowsLineEndingInputStream in =
                     new WindowsLineEndingInputStream(new ByteArrayInputStream(input), true)) {
            output = readAll(in);
        }
        // Assert
        assertEquals("\r\n", ascii(output));
    }

    @Test
    public void injectsCrLfAtEndWhenConfigured_true_nonTerminatedLine() throws IOException {
        // Arrange: last line has no line ending
        final byte[] input = bytes("abc");
        // Act
        final byte[] output;
        try (WindowsLineEndingInputStream in =
                     new WindowsLineEndingInputStream(new ByteArrayInputStream(input), true)) {
            output = readAll(in);
        }
        // Assert
        assertEquals("abc\r\n", ascii(output));
    }

    @Test
    public void doesNotInjectCrLfAtEndWhenConfigured_false_emptyInput() throws IOException {
        // Arrange
        final byte[] input = bytes("");
        // Act
        final byte[] output;
        try (WindowsLineEndingInputStream in =
                     new WindowsLineEndingInputStream(new ByteArrayInputStream(input), false)) {
            output = readAll(in);
        }
        // Assert
        assertEquals("", ascii(output));
    }

    @Test
    public void markIsUnsupported() throws IOException {
        try (WindowsLineEndingInputStream in =
                     new WindowsLineEndingInputStream(new ByteArrayInputStream(new byte[0]), true)) {
            assertFalse("markSupported should be false", in.markSupported());
            try {
                in.mark(1);
                fail("mark should throw UnsupportedOperationException");
            } catch (UnsupportedOperationException expected) {
                // expected
            }
        }
    }

    @Test
    public void closeDelegatesToWrappedStream() throws IOException {
        // Arrange
        final TrackingInputStream wrapped = new TrackingInputStream(bytes("x\n"));
        // Act
        try (WindowsLineEndingInputStream in = new WindowsLineEndingInputStream(wrapped, false)) {
            // consume to ensure reading works through the wrapper
            readAll(in);
            assertFalse("wrapped should not be closed yet", wrapped.closed);
        }
        // Assert
        assertTrue("wrapped should be closed after outer close", wrapped.closed);
    }

    @Test
    public void readPropagatesIOExceptionFromWrappedStream() {
        // Arrange
        final InputStream throwing = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("boom");
            }
        };
        final WindowsLineEndingInputStream in =
                new WindowsLineEndingInputStream(throwing, false);

        // Act + Assert
        try {
            in.read();
            fail("Expected IOException to be propagated");
        } catch (IOException expected) {
            assertEquals("boom", expected.getMessage());
        }
    }

    @Test
    public void nullWrappedStream_throwsOnRead() {
        final WindowsLineEndingInputStream in =
                new WindowsLineEndingInputStream(null, true);
        try {
            in.read();
            fail("Expected NullPointerException when wrapped stream is null");
        } catch (NullPointerException expected) {
            // expected
        } catch (IOException e) {
            fail("Did not expect IOException: " + e.getMessage());
        }
    }

    @Test
    public void nullWrappedStream_throwsOnClose() {
        final WindowsLineEndingInputStream in =
                new WindowsLineEndingInputStream(null, false);
        try {
            in.close();
            fail("Expected NullPointerException when wrapped stream is null");
        } catch (NullPointerException expected) {
            // expected
        } catch (IOException e) {
            fail("Did not expect IOException: " + e.getMessage());
        }
    }

    // Simple tracking stream for close() verification
    private static final class TrackingInputStream extends InputStream {
        private final byte[] data;
        private int pos = 0;
        boolean closed = false;

        TrackingInputStream(final byte[] data) {
            this.data = data;
        }

        @Override
        public int read() {
            return pos < data.length ? (data[pos++] & 0xFF) : -1;
        }

        @Override
        public void close() {
            closed = true;
        }
    }
}