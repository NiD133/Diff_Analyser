package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests WindowsLineEndingInputStream by exercising its three read paths:
 * - Single-byte reads.
 * - Read into a full byte[].
 * - Read into a byte[] with offset/length.
 *
 * Each functional scenario is validated across all read modes to ensure consistent behavior.
 */
class WindowsLineEndingInputStreamTest {

    private static final int BUFFER_SIZE = 128;

    /**
     * How the stream is read in the test. This lets us run the same scenarios
     * for different read methods without duplicating test code.
     */
    private enum ReadMode {
        SINGLE_BYTE {
            @Override
            String readAll(final WindowsLineEndingInputStream in) throws IOException {
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                int ch;
                while ((ch = in.read()) != -1) {
                    out.write(ch);
                }
                return out.toString(StandardCharsets.UTF_8);
            }
        },
        BUFFER {
            @Override
            String readAll(final WindowsLineEndingInputStream in) throws IOException {
                final byte[] buf = new byte[BUFFER_SIZE];
                final int read = in.read(buf);
                if (read < 0) {
                    return "";
                }
                return new String(buf, 0, read, StandardCharsets.UTF_8);
            }
        },
        BUFFER_WITH_OFFSET {
            @Override
            String readAll(final WindowsLineEndingInputStream in) throws IOException {
                final byte[] buf = new byte[BUFFER_SIZE];
                final int read = in.read(buf, 0, buf.length);
                if (read < 0) {
                    return "";
                }
                return new String(buf, 0, read, StandardCharsets.UTF_8);
            }
        };

        abstract String readAll(WindowsLineEndingInputStream in) throws IOException;
    }

    // Data-driven scenarios: input, ensureLineFeedAtEos flag, expected output.
    // We generate the cross-product with all read modes.
    private static Stream<Arguments> scenariosForAllModes() {
        final Stream.Builder<Arguments> b = Stream.builder();
        for (final ReadMode mode : ReadMode.values()) {
            // In the middle of the line, ensure final CRLF
            b.add(Arguments.of(mode, "a\r\nbc", true, "a\r\nbc\r\n"));

            // Linux line feeds, do not ensure final CRLF
            b.add(Arguments.of(mode, "ab\nc", false, "ab\r\nc"));

            // Malformed input with lone CR, do not ensure final CRLF
            b.add(Arguments.of(mode, "a\rbc", false, "a\rbc"));

            // Multiple blank lines, ensure final CRLF
            b.add(Arguments.of(mode, "a\r\n\r\nbc", true, "a\r\n\r\nbc\r\n"));

            // Retain existing line feed at end when not ensuring CRLF
            b.add(Arguments.of(mode, "a\r\n\r\n", false, "a\r\n\r\n"));

            // No trailing CRLF when not ensuring
            b.add(Arguments.of(mode, "a", false, "a"));

            // Simple string, ensure final CRLF
            b.add(Arguments.of(mode, "abc", true, "abc\r\n"));

            // Already ends with CRLF twice, ensure should not add more
            b.add(Arguments.of(mode, "a\r\n\r\n", true, "a\r\n\r\n"));
        }
        return b.build();
    }

    @ParameterizedTest(name = "{index}: mode={0}, ensure={2}, input=\"{1}\"")
    @MethodSource("scenariosForAllModes")
    void convertsLineEndings(final ReadMode mode, final String input, final boolean ensure, final String expected) throws IOException {
        // Arrange + Act
        final String actual = roundTrip(mode, input, ensure);
        // Assert
        assertEquals(expected, actual);
    }

    @ParameterizedTest(name = "mark() is unsupported (ensure={0})")
    @ValueSource(booleans = { false, true })
    void markIsUnsupported(final boolean ensure) throws IOException {
        try (WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensure)) {
            assertThrows(UnsupportedOperationException.class, () -> stream.mark(1));
        }
    }

    @ParameterizedTest(name = "markSupported() is false (ensure={0})")
    @ValueSource(booleans = { false, true })
    void markSupportedIsFalse(final boolean ensure) throws IOException {
        try (WindowsLineEndingInputStream stream = new WindowsLineEndingInputStream(new NullInputStream(), ensure)) {
            assertFalse(stream.markSupported());
        }
    }

    // Helpers

    private static InputStream inputFrom(final String s) {
        return CharSequenceInputStream.builder()
                .setCharSequence(s)
                .setCharset(StandardCharsets.UTF_8)
                .get();
    }

    private static String roundTrip(final ReadMode mode, final String input, final boolean ensure) throws IOException {
        try (WindowsLineEndingInputStream in = new WindowsLineEndingInputStream(inputFrom(input), ensure)) {
            return mode.readAll(in);
        }
    }
}