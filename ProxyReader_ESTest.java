package org.apache.commons.io.input;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.CharBuffer;

import org.junit.Test;

/**
 * Tests for ProxyReader focusing on its delegation behavior and hook methods.
 * These tests intentionally use small helper Reader implementations instead of
 * EvoSuite scaffolding to make intent clear and maintenance easy.
 */
public class ProxyReaderTest {

    /**
     * A ProxyReader that records calls to beforeRead, afterRead, and handleIOException.
     */
    private static class RecordingProxyReader extends ProxyReader {
        int beforeCalls;
        int afterCalls;
        int ioExceptionCalls;
        Integer lastBeforeN;
        Integer lastAfterN;

        RecordingProxyReader(final Reader delegate) {
            super(delegate);
        }

        @Override
        protected void beforeRead(final int n) throws IOException {
            beforeCalls++;
            lastBeforeN = n;
        }

        @Override
        protected void afterRead(final int n) throws IOException {
            afterCalls++;
            lastAfterN = n;
        }

        @Override
        protected void handleIOException(final IOException e) throws IOException {
            ioExceptionCalls++;
            throw e; // Keep default behavior (rethrow) but record the call.
        }
    }

    /**
     * A Reader that throws IOException from every read to exercise handleIOException().
     */
    private static class FailingReader extends Reader {
        @Override
        public int read(final char[] cbuf, final int off, final int len) throws IOException {
            throw new IOException("boom");
        }

        @Override
        public int read() throws IOException {
            throw new IOException("boom");
        }

        @Override
        public void close() throws IOException {
            // no-op
        }
    }

    /**
     * A Reader backed by StringReader that does not support mark/reset.
     */
    private static class UnmarkableStringReader extends Reader {
        private final StringReader delegate;

        UnmarkableStringReader(final String s) {
            this.delegate = new StringReader(s);
        }

        @Override
        public int read(final char[] cbuf, final int off, final int len) throws IOException {
            return delegate.read(cbuf, off, len);
        }

        @Override
        public int read() throws IOException {
            return delegate.read();
        }

        @Override
        public long skip(final long n) throws IOException {
            return delegate.skip(n);
        }

        @Override
        public boolean ready() throws IOException {
            return delegate.ready();
        }

        @Override
        public void close() throws IOException {
            delegate.close();
        }

        @Override
        public boolean markSupported() {
            return false;
        }

        @Override
        public void mark(final int readAheadLimit) throws IOException {
            throw new IOException("mark() not supported");
        }

        @Override
        public void reset() throws IOException {
            throw new IOException("reset() not supported");
        }
    }

    @Test
    public void read_singleChar_invokesHooksAndReturnsChar() throws Exception {
        RecordingProxyReader r = new RecordingProxyReader(new StringReader("ab"));
        int ch = r.read();

        assertEquals('a', ch);
        assertEquals(1, r.beforeCalls);
        assertEquals(Integer.valueOf(1), r.lastBeforeN);
        assertEquals(1, r.afterCalls);
        assertEquals(Integer.valueOf(1), r.lastAfterN);
    }

    @Test
    public void read_singleChar_atEof_callsAfterReadWithMinusOne() throws Exception {
        RecordingProxyReader r = new RecordingProxyReader(new StringReader(""));
        int ch = r.read();

        assertEquals(-1, ch);
        assertEquals(1, r.beforeCalls);
        assertEquals(Integer.valueOf(1), r.lastBeforeN);
        assertEquals(1, r.afterCalls);
        assertEquals(Integer.valueOf(-1), r.lastAfterN);
    }

    @Test
    public void read_intoArray_usesRequestedLengthInBeforeRead_andReportsActualCountInAfterRead() throws Exception {
        RecordingProxyReader r = new RecordingProxyReader(new StringReader("hello"));
        char[] buf = new char[8];

        int n = r.read(buf, 2, 3);

        assertEquals(3, n);
        assertArrayEquals(new char[] {'\0','\0','h','e','l','\0','\0','\0'}, buf);
        assertEquals(1, r.beforeCalls);
        assertEquals(Integer.valueOf(3), r.lastBeforeN);
        assertEquals(1, r.afterCalls);
        assertEquals(Integer.valueOf(3), r.lastAfterN);
    }

    @Test
    public void read_intoArray_zeroLength_returnsZero_andCallsHooksWithZero() throws Exception {
        RecordingProxyReader r = new RecordingProxyReader(new StringReader("data"));
        char[] buf = new char[4];

        int n = r.read(buf, 0, 0);

        assertEquals(0, n);
        assertEquals(1, r.beforeCalls);
        assertEquals(Integer.valueOf(0), r.lastBeforeN);
        assertEquals(1, r.afterCalls);
        assertEquals(Integer.valueOf(0), r.lastAfterN);
    }

    @Test
    public void read_intoArray_fullArrayAtEof_returnsMinusOne_andAfterReadSeesMinusOne() throws Exception {
        RecordingProxyReader r = new RecordingProxyReader(new StringReader(""));
        char[] buf = new char[5];

        int n = r.read(buf);

        assertEquals(-1, n);
        assertEquals(1, r.beforeCalls);
        assertEquals(Integer.valueOf(buf.length), r.lastBeforeN);
        assertEquals(1, r.afterCalls);
        assertEquals(Integer.valueOf(-1), r.lastAfterN);
    }

    @Test
    public void read_intoCharBuffer_delegates_andInvokesHooks() throws Exception {
        RecordingProxyReader r = new RecordingProxyReader(new StringReader("abc"));
        CharBuffer target = CharBuffer.allocate(5);

        int n = r.read(target);

        assertEquals(3, n);
        assertEquals(1, r.beforeCalls);
        assertEquals(Integer.valueOf(5), r.lastBeforeN); // requested remaining
        assertEquals(1, r.afterCalls);
        assertEquals(Integer.valueOf(3), r.lastAfterN);

        target.flip();
        char[] out = new char[3];
        target.get(out);
        assertArrayEquals(new char[] {'a','b','c'}, out);
    }

    @Test
    public void skip_doesNotInvokeHooks_andSkipsCharacters() throws Exception {
        RecordingProxyReader r = new RecordingProxyReader(new StringReader("abcdef"));
        assertEquals(0, r.beforeCalls);
        assertEquals(0, r.afterCalls);

        long skipped = r.skip(3);
        assertEquals(3L, skipped);

        // Hooks are not called by skip/reset per contract.
        assertEquals(0, r.beforeCalls);
        assertEquals(0, r.afterCalls);

        assertEquals('d', r.read());
    }

    @Test
    public void markSupported_and_markReset_delegateToUnderlying() throws Exception {
        RecordingProxyReader r = new RecordingProxyReader(new StringReader("xyz"));
        assertTrue(r.markSupported());

        r.mark(100);
        assertEquals('x', r.read());

        // reset() should not trigger hooks
        int before = r.beforeCalls;
        int after = r.afterCalls;

        r.reset();

        assertEquals(before, r.beforeCalls);
        assertEquals(after, r.afterCalls);

        assertEquals('x', r.read()); // back to the marked position
    }

    @Test
    public void negative_markLimit_throwsIllegalArgumentExceptionFromDelegate() throws Exception {
        RecordingProxyReader r = new RecordingProxyReader(new StringReader("data"));
        try {
            r.mark(-1);
            fail("Expected IllegalArgumentException for negative readAheadLimit");
        } catch (IllegalArgumentException expected) {
            // StringReader throws IllegalArgumentException("Read-ahead limit < 0")
        }
    }

    @Test
    public void markNotSupported_throwsIOException() throws Exception {
        RecordingProxyReader r = new RecordingProxyReader(new UnmarkableStringReader("abc"));
        assertFalse(r.markSupported());
        try {
            r.mark(1);
            fail("Expected IOException when mark() not supported");
        } catch (IOException expected) {
            // expected
        }
        try {
            r.reset();
            fail("Expected IOException when reset() not supported");
        } catch (IOException expected) {
            // expected
        }
    }

    @Test
    public void ready_delegatesToUnderlying() throws Exception {
        RecordingProxyReader r = new RecordingProxyReader(new StringReader("ready"));
        assertTrue(r.ready());
    }

    @Test
    public void handleIOException_isInvoked_andExceptionIsRethrown() {
        RecordingProxyReader r = new RecordingProxyReader(new FailingReader());
        try {
            r.read();
            fail("Expected IOException");
        } catch (IOException expected) {
            // rethrown by handleIOException
        }
        assertEquals(1, r.ioExceptionCalls);
    }
}