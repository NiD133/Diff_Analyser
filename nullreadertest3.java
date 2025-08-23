package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import org.junit.jupiter.api.Test;

public class NullReaderTestTest3 {

    // Use the same message as in java.io.InputStream.reset() in OpenJDK 8.0.275-1.
    private static final String MARK_RESET_NOT_SUPPORTED = "mark/reset not supported";

    private static final class TestNullReader extends NullReader {

        TestNullReader(final int size) {
            super(size);
        }

        TestNullReader(final int size, final boolean markSupported, final boolean throwEofException) {
            super(size, markSupported, throwEofException);
        }

        @Override
        protected int processChar() {
            return (int) getPosition() - 1;
        }

        @Override
        protected void processChars(final char[] chars, final int offset, final int length) {
            final int startPos = (int) getPosition() - length;
            for (int i = offset; i < length; i++) {
                chars[i] = (char) (startPos + i);
            }
        }
    }

    @Test
    void testMarkNotSupported() throws Exception {
        final Reader reader = new TestNullReader(100, false, true);
        assertFalse(reader.markSupported(), "Mark Should NOT be Supported");
        try {
            reader.mark(5);
            fail("mark() should throw UnsupportedOperationException");
        } catch (final UnsupportedOperationException e) {
            assertEquals(MARK_RESET_NOT_SUPPORTED, e.getMessage(), "mark() error message");
        }
        try {
            reader.reset();
            fail("reset() should throw UnsupportedOperationException");
        } catch (final UnsupportedOperationException e) {
            assertEquals(MARK_RESET_NOT_SUPPORTED, e.getMessage(), "reset() error message");
        }
        reader.close();
    }
}
