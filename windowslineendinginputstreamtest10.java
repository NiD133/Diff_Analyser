package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link WindowsLineEndingInputStream}.
 *
 * <p>
 * This class uses a nested structure to test the different read methods
 * ({@code read()}, {@code read(byte[])}, and {@code read(byte[], int, int)})
 * against the same set of line-ending conversion scenarios. An abstract base class,
 * {@code AbstractReadTest}, defines the test cases, and concrete nested classes
 * provide the specific read implementation.
 * </p>
 */
public class WindowsLineEndingInputStreamTest {

    /**
     * Creates a {@link WindowsLineEndingInputStream} from a String.
     *
     * @param data The string data to read from.
     * @param ensureLineFeedAtEos True to ensure the stream ends with CRLF.
     * @return A new instance of the stream.
     */
    private WindowsLineEndingInputStream createStream(final String data, final boolean ensureLineFeedAtEos) {
        return new WindowsLineEndingInputStream(
            CharSequenceInputStream.builder()
                .setCharSequence(data)
                .setCharset(StandardCharsets.UTF_8)
                .get(),
            ensureLineFeedAtEos);
    }

    @DisplayName("markSupported() should always return false")
    @ParameterizedTest(name = "when ensureLineFeedAtEos is {0}")
    @ValueSource(booleans = {false, true})
    void markSupportedShouldReturnFalse(final boolean ensureLineFeedAtEos) throws IOException {
        try (InputStream stream = createStream("", ensureLineFeedAtEos)) {
            assertFalse(stream.markSupported());
        }
    }

    @DisplayName("mark() should always throw UnsupportedOperationException")
    @ParameterizedTest(name = "when ensureLineFeedAtEos is {0}")
    @ValueSource(booleans = {false, true})
    void markShouldThrowUnsupportedOperationException(final boolean ensureLineFeedAtEos) throws IOException {
        try (InputStream stream = createStream("", ensureLineFeedAtEos)) {
            assertThrows(UnsupportedOperationException.class, () -> stream.mark(1));
        }
    }

    /**
     * Abstract base class for testing different read() method overloads.
     */
    abstract class AbstractReadTest {

        /**
         * Reads the entire stream content into a string using a specific read method.
         *
         * @param input The input string to be streamed.
         * @param ensureEofLf The "ensure line feed at end of stream" flag.
         * @return The resulting string after processing by WindowsLineEndingInputStream.
         * @throws IOException If an I/O error occurs.
         */
        abstract String readAll(String input, boolean ensureEofLf) throws IOException;

        @Test
        void shouldConvertLoneLfToCrLf() throws IOException {
            final String input = "a\nb";
            final String expected = "a\r\nb";
            assertEquals(expected, readAll(input, false));
        }

        @Test
        void shouldConvertLoneCrToCrLf() throws IOException {
            final String input = "a\rb";
            final String expected = "a\r\nb";
            assertEquals(expected, readAll(input, false));
        }

        @Test
        void shouldNotChangeExistingCrLf() throws IOException {
            final String input = "a\r\nb";
            assertEquals(input, readAll(input, false));
        }

        @Test
        void shouldAddCrLfToEofWhenEnsureEofIsTrueAndLfIsMissing() throws IOException {
            final String input = "abc";
            final String expected = "abc\r\n";
            assertEquals(expected, readAll(input, true));
        }


        @Test
        void shouldNotAddCrLfToEofWhenEnsureEofIsFalse() throws IOException {
            final String input = "abc";
            assertEquals(input, readAll(input, false));
        }

        @Test
        void shouldNotAddCrLfToEofWhenInputEndsWithCrLf() throws IOException {
            final String input = "abc\r\n";
            assertEquals(input, readAll(input, true));
        }

        @Test
        void shouldHandleMultipleBlankLinesAndEnsureEof() throws IOException {
            final String input = "a\r\n\r\nbc";
            final String expected = "a\r\n\r\nbc\r\n";
            assertEquals(expected, readAll(input, true));
        }

        @Test
        void shouldHandleEmptyStreamWhenEnsureEofIsTrue() throws IOException {
            assertEquals("\r\n", readAll("", true));
        }

        @Test
        void shouldHandleEmptyStreamWhenEnsureEofIsFalse() throws IOException {
            assertEquals("", readAll("", false));
        }
    }

    @Nested
    @DisplayName("using read()")
    class ReadByteByByteTest extends AbstractReadTest {
        @Override
        String readAll(final String input, final boolean ensureEofLf) throws IOException {
            try (InputStream in = createStream(input, ensureEofLf);
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                int ch;
                while ((ch = in.read()) != -1) {
                    out.write(ch);
                }
                return out.toString(StandardCharsets.UTF_8);
            }
        }
    }

    @Nested
    @DisplayName("using read(byte[])")
    class ReadIntoByteArrayTest extends AbstractReadTest {
        @Override
        String readAll(final String input, final boolean ensureEofLf) throws IOException {
            try (InputStream in = createStream(input, ensureEofLf);
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                final byte[] buffer = new byte[128];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                return out.toString(StandardCharsets.UTF_8);
            }
        }
    }

    @Nested
    @DisplayName("using read(byte[], int, int)")
    class ReadIntoByteArrayWithOffsetTest extends AbstractReadTest {
        @Override
        String readAll(final String input, final boolean ensureEofLf) throws IOException {
            try (InputStream in = createStream(input, ensureEofLf);
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                final byte[] buffer = new byte[128];
                int bytesRead;
                while ((bytesRead = in.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                return out.toString(StandardCharsets.UTF_8);
            }
        }
    }
}