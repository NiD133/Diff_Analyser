package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class WindowsLineEndingInputStreamTestTest10 {

    private String roundtripReadByte(final String msg) throws IOException {
        return roundtripReadByte(msg, true);
    }

    private String roundtripReadByte(final String msg, final boolean ensure) throws IOException {
        // read(byte[])
        try (WindowsLineEndingInputStream lf = new WindowsLineEndingInputStream(CharSequenceInputStream.builder().setCharSequence(msg).setCharset(StandardCharsets.UTF_8).get(), ensure)) {
            final byte[] buf = new byte[100];
            int i = 0;
            while (i < buf.length) {
                final int read = lf.read();
                if (read < 0) {
                    break;
                }
                buf[i++] = (byte) read;
            }
            return new String(buf, 0, i, StandardCharsets.UTF_8);
        }
    }

    private String roundtripReadByteArray(final String msg) throws IOException {
        return roundtripReadByteArray(msg, true);
    }

    private String roundtripReadByteArray(final String msg, final boolean ensure) throws IOException {
        // read(byte[])
        try (WindowsLineEndingInputStream lf = new WindowsLineEndingInputStream(CharSequenceInputStream.builder().setCharSequence(msg).setCharset(StandardCharsets.UTF_8).get(), ensure)) {
            final byte[] buf = new byte[100];
            final int read = lf.read(buf);
            return new String(buf, 0, read, StandardCharsets.UTF_8);
        }
    }

    private String roundtripReadByteArrayIndex(final String msg) throws IOException {
        return roundtripReadByteArrayIndex(msg, true);
    }

    private String roundtripReadByteArrayIndex(final String msg, final boolean ensure) throws IOException {
        // read(byte[])
        try (WindowsLineEndingInputStream lf = new WindowsLineEndingInputStream(CharSequenceInputStream.builder().setCharSequence(msg).setCharset(StandardCharsets.UTF_8).get(), ensure)) {
            final byte[] buf = new byte[100];
            final int read = lf.read(buf, 0, 100);
            return new String(buf, 0, read, StandardCharsets.UTF_8);
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void testMark(final boolean ensureLineFeedAtEndOfFile) {
        assertThrows(UnsupportedOperationException.class, () -> new WindowsLineEndingInputStream(new NullInputStream(), true).mark(1));
    }

    @SuppressWarnings("resource")
    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void testMarkSupported(final boolean ensureLineFeedAtEndOfFile) {
        assertFalse(new WindowsLineEndingInputStream(new NullInputStream(), true).markSupported());
    }

    @Test
    void testMultipleBlankLines_Byte() throws Exception {
        assertEquals("a\r\n\r\nbc\r\n", roundtripReadByte("a\r\n\r\nbc"));
    }
}
