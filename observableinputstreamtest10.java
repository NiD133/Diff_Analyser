package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ObservableInputStream.Observer;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.io.test.CustomIOException;
import org.junit.jupiter.api.Test;

public class ObservableInputStreamTestTest10 {

    private ObservableInputStream brokenObservableInputStream() {
        return new ObservableInputStream(BrokenInputStream.INSTANCE);
    }

    private InputStream createInputStream() {
        final byte[] buffer = MessageDigestInputStreamTest.generateRandomByteStream(IOUtils.DEFAULT_BUFFER_SIZE);
        return createInputStream(new ByteArrayInputStream(buffer));
    }

    private ObservableInputStream createInputStream(final InputStream origin) {
        return new ObservableInputStream(origin);
    }

    private void testNotificationCallbacks(final int bufferSize) throws IOException {
        final byte[] buffer = IOUtils.byteArray();
        final LengthObserver lengthObserver = new LengthObserver();
        final MethodCountObserver methodCountObserver = new MethodCountObserver();
        try (ObservableInputStream ois = new ObservableInputStream(new ByteArrayInputStream(buffer), lengthObserver, methodCountObserver)) {
            assertEquals(IOUtils.DEFAULT_BUFFER_SIZE, IOUtils.copy(ois, NullOutputStream.INSTANCE, bufferSize));
        }
        assertEquals(IOUtils.DEFAULT_BUFFER_SIZE, lengthObserver.getTotal());
        assertEquals(1, methodCountObserver.getClosedCount());
        assertEquals(1, methodCountObserver.getFinishedCount());
        assertEquals(0, methodCountObserver.getErrorCount());
        assertEquals(0, methodCountObserver.getDataCount());
        assertEquals(buffer.length / bufferSize, methodCountObserver.getDataBufferCount());
    }

    private static final class DataViewObserver extends MethodCountObserver {

        private byte[] buffer;

        private int lastValue = -1;

        private int length = -1;

        private int offset = -1;

        @Override
        public void data(final byte[] buffer, final int offset, final int length) throws IOException {
            this.buffer = buffer;
            this.offset = offset;
            this.length = length;
        }

        @Override
        public void data(final int value) throws IOException {
            super.data(value);
            lastValue = value;
        }
    }

    private static final class LengthObserver extends Observer {

        private long total;

        @Override
        public void data(final byte[] buffer, final int offset, final int length) throws IOException {
            this.total += length;
        }

        @Override
        public void data(final int value) throws IOException {
            total++;
        }

        public long getTotal() {
            return total;
        }
    }

    private static class MethodCountObserver extends Observer {

        private long closedCount;

        private long dataBufferCount;

        private long dataCount;

        private long errorCount;

        private long finishedCount;

        @Override
        public void closed() throws IOException {
            closedCount++;
        }

        @Override
        public void data(final byte[] buffer, final int offset, final int length) throws IOException {
            dataBufferCount++;
        }

        @Override
        public void data(final int value) throws IOException {
            dataCount++;
        }

        @Override
        public void error(final IOException exception) throws IOException {
            errorCount++;
        }

        @Override
        public void finished() throws IOException {
            finishedCount++;
        }

        public long getClosedCount() {
            return closedCount;
        }

        public long getDataBufferCount() {
            return dataBufferCount;
        }

        public long getDataCount() {
            return dataCount;
        }

        public long getErrorCount() {
            return errorCount;
        }

        public long getFinishedCount() {
            return finishedCount;
        }
    }

    @Test
    void testGetObservers0() throws IOException {
        try (ObservableInputStream ois = new ObservableInputStream(new NullInputStream())) {
            assertTrue(ois.getObservers().isEmpty());
        }
    }
}
