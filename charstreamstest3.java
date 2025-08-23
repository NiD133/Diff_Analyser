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

public class CharStreamsTestTest3 extends IoTestCase {

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

    public void testReadLines_withLineProcessor() throws IOException {
        String text = "a\nb\nc";
        // Test a LineProcessor that always returns false.
        Reader r = new StringReader(text);
        LineProcessor<Integer> alwaysFalse = new LineProcessor<Integer>() {

            int seen;

            @Override
            public boolean processLine(String line) {
                seen++;
                return false;
            }

            @Override
            public Integer getResult() {
                return seen;
            }
        };
        assertEquals("processLine was called more than once", 1, CharStreams.readLines(r, alwaysFalse).intValue());
        // Test a LineProcessor that always returns true.
        r = new StringReader(text);
        LineProcessor<Integer> alwaysTrue = new LineProcessor<Integer>() {

            int seen;

            @Override
            public boolean processLine(String line) {
                seen++;
                return true;
            }

            @Override
            public Integer getResult() {
                return seen;
            }
        };
        assertEquals("processLine was not called for all the lines", 3, CharStreams.readLines(r, alwaysTrue).intValue());
        // Test a LineProcessor that is conditional.
        r = new StringReader(text);
        StringBuilder sb = new StringBuilder();
        LineProcessor<Integer> conditional = new LineProcessor<Integer>() {

            int seen;

            @Override
            public boolean processLine(String line) {
                seen++;
                sb.append(line);
                return seen < 2;
            }

            @Override
            public Integer getResult() {
                return seen;
            }
        };
        assertEquals(2, CharStreams.readLines(r, conditional).intValue());
        assertEquals("ab", sb.toString());
    }
}
