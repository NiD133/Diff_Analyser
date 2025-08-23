package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

public class SequenceReaderTestTest3 {

    private static final char NUL = 0;

    private void checkArray(final char[] expected, final char[] actual) {
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], "Compare[" + i + "]");
        }
    }

    private void checkRead(final Reader reader, final String expected) throws IOException {
        for (int i = 0; i < expected.length(); i++) {
            assertEquals(expected.charAt(i), (char) reader.read(), "Read[" + i + "] of '" + expected + "'");
        }
    }

    private void checkReadEof(final Reader reader) throws IOException {
        for (int i = 0; i < 10; i++) {
            assertEquals(-1, reader.read());
        }
    }

    private static class CustomReader extends Reader {

        boolean closed;

        protected void checkOpen() throws IOException {
            if (closed) {
                throw new IOException("emptyReader already closed");
            }
        }

        @Override
        public void close() throws IOException {
            closed = true;
        }

        public boolean isClosed() {
            return closed;
        }

        @Override
        public int read(final char[] cbuf, final int off, final int len) throws IOException {
            checkOpen();
            close();
            return EOF;
        }
    }

    @Test
    void testCloseReaders() throws IOException {
        final CustomReader reader0 = new CustomReader();
        final CustomReader reader1 = new CustomReader() {

            private final char[] content = { 'A' };

            private int position;

            @Override
            public int read(final char[] cbuf, final int off, final int len) throws IOException {
                checkOpen();
                if (off < 0) {
                    throw new IndexOutOfBoundsException("off is negative");
                }
                if (len < 0) {
                    throw new IndexOutOfBoundsException("len is negative");
                }
                if (len > cbuf.length - off) {
                    throw new IndexOutOfBoundsException("len is greater than cbuf.length - off");
                }
                if (position > 0) {
                    return EOF;
                }
                cbuf[off] = content[0];
                position++;
                return 1;
            }
        };
        try (SequenceReader sequenceReader = new SequenceReader(reader1, reader0)) {
            assertEquals('A', sequenceReader.read());
            assertEquals(EOF, sequenceReader.read());
        } finally {
            assertTrue(reader1.isClosed());
            assertTrue(reader0.isClosed());
        }
        assertTrue(reader1.isClosed());
        assertTrue(reader0.isClosed());
    }
}
