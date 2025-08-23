package org.apache.commons.io.input;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for ObservableInputStream.
 *
 * Highlights:
 * - A RecordingObserver captures callbacks to assert on notifications.
 * - Tests use small, explicit input sources to avoid brittleness.
 * - Verifies observer registration/removal, EOF notifications, error propagation,
 *   and builder usage.
 */
public class ObservableInputStreamTest {

    /**
     * An observer that records all callbacks for assertions.
     */
    private static class RecordingObserver extends ObservableInputStream.Observer {
        int closedCount;
        int finishedCount;
        int dataIntCount;
        int dataBytesCount;
        int dataBytesTotalLen;
        IOException lastError;

        @Override
        public void closed() throws IOException {
            closedCount++;
        }

        @Override
        public void finished() throws IOException {
            finishedCount++;
        }

        @Override
        public void data(final int value) throws IOException {
            dataIntCount++;
        }

        @Override
        public void data(final byte[] buffer, final int offset, final int length) throws IOException {
            dataBytesCount++;
            if (length > 0) {
                dataBytesTotalLen += length;
            }
        }

        @Override
        public void error(final IOException exception) throws IOException {
            lastError = exception;
        }
    }

    /**
     * An observer that fails on data(int) to validate error propagation.
     */
    private static class FailingObserver extends ObservableInputStream.Observer {
        @Override
        public void data(final int value) throws IOException {
            throw new IOException("observer failed on data(int)");
        }
    }

    private static ObservableInputStream wrap(final byte[] bytes, final ObservableInputStream.Observer... observers) {
        return new ObservableInputStream(new ByteArrayInputStream(bytes), observers);
    }

    @Test
    public void readSingleByte_notifiesData_andFinishedAtEof() throws Exception {
        RecordingObserver o = new RecordingObserver();
        ObservableInputStream in = wrap(new byte[]{'A'}, o);

        assertEquals('A', in.read());
        assertEquals(1, o.dataIntCount);
        assertEquals(0, o.finishedCount);

        // First EOF read should notify finished().
        assertEquals(-1, in.read());
        assertTrue("finished() should be notified at EOF", o.finishedCount >= 1);

        in.close();
    }

    @Test
    public void readIntoArray_notifiesDataBytesWithExactLength() throws Exception {
        RecordingObserver o = new RecordingObserver();
        ObservableInputStream in = wrap("abc".getBytes("US-ASCII"), o);

        byte[] buf = new byte[8];
        int n = in.read(buf);

        assertEquals(3, n);
        assertTrue(o.dataBytesCount >= 1);
        assertEquals(3, o.dataBytesTotalLen);

        in.close();
    }

    @Test
    public void zeroLengthRead_returnsZero_andDoesNotNotify() throws Exception {
        RecordingObserver o = new RecordingObserver();
        ObservableInputStream in = wrap(new byte[]{1, 2, 3}, o);

        int n = in.read(new byte[0]);

        assertEquals(0, n);
        assertEquals(0, o.dataIntCount);
        assertEquals(0, o.dataBytesCount);

        in.close();
    }

    @Test
    public void consume_readsEverything_andNotifiesFinished() throws Exception {
        byte[] data = new byte[100];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }

        RecordingObserver o = new RecordingObserver();
        ObservableInputStream in = wrap(data, o);

        in.consume();

        // Depending on implementation, data may be delivered via data(int) or data(byte[],..).
        assertTrue(o.dataBytesTotalLen + o.dataIntCount >= 100);
        assertTrue("finished() should be called at least once", o.finishedCount >= 1);

        in.close();
    }

    @Test
    public void addRemoveObservers_andGetObserversReturnsCopy() {
        RecordingObserver a = new RecordingObserver();
        RecordingObserver b = new RecordingObserver();
        ObservableInputStream in = wrap(new byte[0], a, b);

        List<ObservableInputStream.Observer> snapshot = in.getObservers();
        assertEquals(2, snapshot.size());

        // Modifying the returned list must not affect the stream's internal list.
        snapshot.clear();
        assertEquals(2, in.getObservers().size());

        // Remove one observer.
        in.remove(a);
        assertEquals(1, in.getObservers().size());

        // Remove all.
        in.removeAllObservers();
        assertTrue(in.getObservers().isEmpty());
    }

    @Test
    public void close_notifiesClosed() throws Exception {
        RecordingObserver o = new RecordingObserver();
        ObservableInputStream in = wrap(new byte[]{1}, o);

        in.close();

        assertEquals(1, o.closedCount);
    }

    @Test
    public void underlyingIOException_isPropagated_andNotifiedToObservers() {
        InputStream faulty = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("boom");
            }
        };
        RecordingObserver o = new RecordingObserver();
        ObservableInputStream in = new ObservableInputStream(faulty, o);

        try {
            in.read();
            fail("Expected IOException");
        } catch (IOException ex) {
            assertEquals("boom", ex.getMessage());
        }

        assertNotNull("Observer should be notified about the error", o.lastError);
        assertEquals("boom", o.lastError.getMessage());
    }

    @Test
    public void observerException_duringData_isPropagated() throws Exception {
        ObservableInputStream in = wrap(new byte[]{42}, new FailingObserver());

        try {
            in.read();
            fail("Expected IOException from observer");
        } catch (IOException ex) {
            assertTrue(ex.getMessage().contains("observer failed"));
        } finally {
            in.close();
        }
    }

    @Test
    public void builder_withCharSequence_originReadsExpectedBytes() throws Exception {
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setCharSequence("ABC");

        RecordingObserver o = new RecordingObserver();
        List<ObservableInputStream.Observer> obs = new ArrayList<>();
        obs.add(o);
        builder.setObservers(obs);

        try (ObservableInputStream in = builder.get()) {
            assertEquals('A', in.read());
            assertEquals('B', in.read());
            assertEquals('C', in.read());
            assertEquals(-1, in.read());
        }

        assertTrue("Observer should have seen at least one data event", o.dataIntCount > 0 || o.dataBytesCount > 0);
    }

    @Test
    public void readByteArray_withOffsetLength_notifiesOnlyForBytesRead() throws Exception {
        RecordingObserver o = new RecordingObserver();
        ObservableInputStream in = wrap("hello".getBytes("US-ASCII"), o);

        byte[] buf = new byte[10];
        int n = in.read(buf, 2, 5);

        assertEquals(5, n);
        assertEquals(5, o.dataBytesTotalLen);
        assertTrue(o.dataBytesCount >= 1);

        in.close();
    }
}

What’s improved:
- Clear test names describing behavior under test.
- No EvoSuite runner, mocks, or unrelated exceptions from underlying JDK streams.
- A simple RecordingObserver explicitly asserts notifications.
- Focus on core behaviors: data notifications, EOF finished(), error propagation, observer management, and builder usage.
- Small utilities to avoid duplication and keep the tests concise and readable.