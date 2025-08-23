package com.google.common.io;

import static org.junit.Assert.assertThrows;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import java.io.EOFException;
import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.CharBuffer;
import java.util.List;
import org.jspecify.annotations.NullUnmarked;

public class CharStreamsTestTest12 extends IoTestCase {

    private static final String TEXT = "The quick brown fox jumped over the lazy dog.";

    /**
     * Returns a reader wrapping the given reader that only reads half of the maximum number of
     * characters that it could read in read(char[], int, int).
     */
    private static Reader newNonBufferFillingReader(Reader reader) {
        return new FilterReader(reader) {

            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                // if a buffer isn't being cleared correctly, this method will eventually start being called
                // with a len of 0 forever
                if (len <= 0) {
                    fail("read called with a len of " + len);
                }
                // read fewer than the max number of chars to read
                // shouldn't be a problem unless the buffer is shrinking each call
                return in.read(cbuf, off, Math.max(len - 1024, 0));
            }
        };
    }

    /**
     * Wrap an appendable in an appendable to defeat any type specific optimizations.
     */
    private static Appendable wrapAsGenericAppendable(Appendable a) {
        return new Appendable() {

            @Override
            public Appendable append(CharSequence csq) throws IOException {
                a.append(csq);
                return this;
            }

            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                a.append(csq, start, end);
                return this;
            }

            @Override
            public Appendable append(char c) throws IOException {
                a.append(c);
                return this;
            }
        };
    }

    /**
     * Wrap a readable in a readable to defeat any type specific optimizations.
     */
    private static Readable wrapAsGenericReadable(Readable a) {
        return new Readable() {

            @Override
            public int read(CharBuffer cb) throws IOException {
                return a.read(cb);
            }
        };
    }

    /**
     * Test for Guava issue 1061: https://github.com/google/guava/issues/1061
     *
     * <p>CharStreams.copy was failing to clear its CharBuffer after each read call, which effectively
     * reduced the available size of the buffer each time a call to read didn't fill up the available
     * space in the buffer completely. In general this is a performance problem since the buffer size
     * is permanently reduced, but with certain Reader implementations it could also cause the buffer
     * size to reach 0, causing an infinite loop.
     */
    // String.repeat unavailable under Java 8
    @SuppressWarnings("InlineMeInliner")
    public void testCopyWithReaderThatDoesNotFillBuffer() throws IOException {
        // need a long enough string for the buffer to hit 0 remaining before the copy completes
        String string = Strings.repeat("0123456789", 100);
        StringBuilder b = new StringBuilder();
        // the main assertion of this test is here... the copy will fail if the buffer size goes down
        // each time it is not filled completely
        long copied = CharStreams.copy(newNonBufferFillingReader(new StringReader(string)), b);
        assertEquals(string, b.toString());
        assertEquals(string.length(), copied);
    }
}
