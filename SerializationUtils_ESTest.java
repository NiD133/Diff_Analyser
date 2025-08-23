package org.apache.commons.lang3;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.NotSerializableException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for SerializationUtils.
 *
 * These tests avoid EvoSuite-specific constructs and use clear naming,
 * straightforward scenarios, and explicit assertions to improve maintainability.
 */
public class SerializationUtilsTest {

    // ---- Happy-path behavior ----

    @Test
    public void serializeNull_thenDeserialize_returnsNull() {
        byte[] bytes = SerializationUtils.serialize(null);
        Object obj = SerializationUtils.deserialize(bytes);
        assertNull("Deserializing a serialized null should return null", obj);
    }

    @Test
    public void serializeAndDeserialize_localeValue_preservesEquality() {
        Locale original = Locale.PRC;
        byte[] bytes = SerializationUtils.serialize(original);
        Locale copy = SerializationUtils.deserialize(bytes);

        assertEquals("Deserialized Locale should be equal to the original", original, copy);
        // Do not assertSame: serialization typically creates a new instance.
    }

    @Test
    public void roundtrip_null_returnsNull() {
        Integer result = SerializationUtils.roundtrip(null);
        assertNull(result);
    }

    @Test
    public void clone_null_returnsNull() {
        Integer result = SerializationUtils.clone(null);
        assertNull(result);
    }

    @Test
    public void clone_serializableImmutable_returnsEqualValue() {
        Integer original = 42;
        Integer cloned = SerializationUtils.clone(original);

        assertEquals("Cloned value should equal original", original, cloned);
        // For immutable types equality is sufficient; identity is not expected.
    }

    // ---- API contract: argument validation ----

    @Test
    public void serialize_withNullOutputStream_throwsNPEWithParameterName() {
        NullPointerException ex = assertThrows(
            NullPointerException.class,
            () -> SerializationUtils.serialize(123, null)
        );
        assertEquals("outputStream", ex.getMessage());
    }

    @Test
    public void deserialize_withNullByteArray_throwsNPEWithParameterName() {
        NullPointerException ex = assertThrows(
            NullPointerException.class,
            () -> SerializationUtils.deserialize((byte[]) null)
        );
        assertEquals("objectData", ex.getMessage());
    }

    @Test
    public void deserialize_withNullInputStream_throwsNPEWithParameterName() {
        NullPointerException ex = assertThrows(
            NullPointerException.class,
            () -> SerializationUtils.deserialize((InputStream) null)
        );
        assertEquals("inputStream", ex.getMessage());
    }

    // ---- Error handling: invalid data / non-serializable content ----

    @Test
    public void deserialize_withInvalidBytes_throwsSerializationExceptionWrappingIOException() {
        byte[] invalid = new byte[] { 1, 2, 3 };

        SerializationException ex = assertThrows(
            SerializationException.class,
            () -> SerializationUtils.deserialize(invalid)
        );
        assertNotNull("Cause should be an IOException", ex.getCause());
        assertTrue("Cause should be an IOException", ex.getCause() instanceof IOException);
    }

    @Test
    public void serialize_mapWithNonSerializableKey_throwsSerializationExceptionWrappingNotSerializableException() {
        Map<Object, Object> map = new HashMap<>();
        map.put(new Thread(), "value"); // Thread is not Serializable; HashMap is Serializable.

        SerializationException ex = assertThrows(
            SerializationException.class,
            () -> SerializationUtils.serialize((Serializable) map)
        );
        assertNotNull(ex.getCause());
        assertTrue("Cause should be NotSerializableException", ex.getCause() instanceof NotSerializableException);
    }

    // ---- Resource lifecycle: methods close the provided streams ----

    @Test
    public void serialize_closesProvidedOutputStream() {
        CloseTrackingOutputStream out = new CloseTrackingOutputStream();

        SerializationUtils.serialize("data", out);

        assertTrue("OutputStream should be closed by serialize()", out.isClosed());
    }

    @Test
    public void deserialize_closesProvidedInputStream() {
        byte[] bytes = SerializationUtils.serialize("hello");

        CloseTrackingInputStream in = new CloseTrackingInputStream(new ByteArrayInputStream(bytes));
        String result = SerializationUtils.deserialize(in);

        assertEquals("hello", result);
        assertTrue("InputStream should be closed by deserialize()", in.isClosed());
    }

    // ---- Test utilities ----

    private static final class CloseTrackingOutputStream extends OutputStream {
        private boolean closed;

        @Override
        public void write(int b) throws IOException {
            if (closed) {
                throw new IOException("Stream closed");
            }
            // Discard bytes; we only care about close behavior.
        }

        @Override
        public void close() throws IOException {
            closed = true;
            super.close();
        }

        boolean isClosed() {
            return closed;
        }
    }

    private static final class CloseTrackingInputStream extends InputStream {
        private final InputStream delegate;
        private boolean closed;

        private CloseTrackingInputStream(InputStream delegate) {
            this.delegate = delegate;
        }

        @Override
        public int read() throws IOException {
            if (closed) {
                throw new IOException("Stream closed");
            }
            return delegate.read();
        }

        @Override
        public void close() throws IOException {
            closed = true;
            delegate.close();
        }

        boolean isClosed() {
            return closed;
        }
    }
}