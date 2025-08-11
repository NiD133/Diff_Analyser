package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertNullPointerException;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link SerializationUtils}.
 *
 * This version emphasizes readability by:
 * - Grouping related tests in @Nested classes.
 * - Using clear, intention-revealing test names.
 * - Extracting common setup and helper methods.
 * - Using constants for magic strings and keys.
 */
class SerializationUtilsTest extends AbstractLangTest {

    static final String CLASS_NOT_FOUND_MESSAGE = "ClassNotFoundSerialization.readObject fake exception";
    protected static final String SERIALIZE_IO_EXCEPTION_MESSAGE = "Anonymous OutputStream I/O exception";

    private static final String KEY_FOO = "FOO";
    private static final String KEY_BAR = "BAR";

    private String foo;
    private Integer seven;
    private HashMap<Object, Object> sampleMap;

    @BeforeEach
    void setUp() {
        foo = "foo";
        seven = Integer.valueOf(7);
        sampleMap = new HashMap<>();
        sampleMap.put(KEY_FOO, foo);
        sampleMap.put(KEY_BAR, seven);
    }

    // Test-only serializable Supplier
    private interface SerializableSupplier<T> extends Supplier<T>, Serializable {
        // no-op
    }

    // Used to trigger ClassNotFoundException during deserialization
    private static final class ClassNotFoundSerialization implements Serializable {
        private static final long serialVersionUID = 1L;
        @SuppressWarnings("unused")
        private void readObject(final ObjectInputStream in) throws ClassNotFoundException {
            throw new ClassNotFoundException(CLASS_NOT_FOUND_MESSAGE);
        }
    }

    // --- Helpers ----------------------------------------------------------------

    private static byte[] jdkSerialize(final Object obj) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(out)) {
            oos.writeObject(obj);
            oos.flush();
        }
        return out.toByteArray();
    }

    private static ByteArrayInputStream toInputStream(final byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    private void assertDeepClonedMapEquals(final Map<Object, Object> original, final Map<?, ?> clone) {
        assertNotNull(clone);
        assertInstanceOf(HashMap.class, clone);
        assertNotSame(original, clone);
        assertEquals(original, clone);

        // Verify deep clone for known entries
        assertEquals(foo, clone.get(KEY_FOO));
        assertNotSame(foo, clone.get(KEY_FOO));
        assertEquals(seven, clone.get(KEY_BAR));
        assertNotSame(seven, clone.get(KEY_BAR));
    }

    // --- Tests ------------------------------------------------------------------

    @Nested
    class CloneTests {

        @Test
        void deepClone_map_returnsEqualButIndependentCopy() {
            final HashMap<?, ?> cloned = SerializationUtils.clone(sampleMap);
            assertDeepClonedMapEquals(sampleMap, cloned);
        }

        @Test
        void clone_null_returnsNull() {
            assertNull(SerializationUtils.clone(null));
        }

        @Test
        void clone_serializableFunctionalInterface_works() {
            final SerializableSupplier<String> supplier = () -> "test";
            assertEquals("test", supplier.get());
            final SerializableSupplier<String> clone = SerializationUtils.clone(supplier);
            assertEquals("test", clone.get());
        }

        @Test
        void clone_withUnserializableContent_throwsSerializationException() {
            sampleMap.put(new Object(), new Object());
            assertThrows(SerializationException.class, () -> SerializationUtils.clone(sampleMap));
        }

        @Test
        void clone_primitiveTypeClasses_returnsSameClassObject() {
            final Class<?>[] primitiveTypes = {
                byte.class, short.class, int.class, long.class,
                float.class, double.class, boolean.class, char.class, void.class
            };
            for (final Class<?> primitiveType : primitiveTypes) {
                final Class<?> clone = SerializationUtils.clone(primitiveType);
                assertEquals(primitiveType, clone);
            }
        }
    }

    @Nested
    class DeserializeTests {

        @Test
        void fromBytes_roundtripsMap() throws Exception {
            final byte[] bytes = jdkSerialize(sampleMap);
            final Object obj = SerializationUtils.deserialize(bytes);
            assertDeepClonedMapEquals(sampleMap, (HashMap<?, ?>) obj);
        }

        @Test
        void fromEmptyBytes_throwsSerializationException() {
            assertThrows(SerializationException.class, () -> SerializationUtils.deserialize(new byte[0]));
        }

        @Test
        void fromNullBytes_throwsNullPointerException() {
            assertNullPointerException(() -> SerializationUtils.deserialize((byte[]) null));
        }

        @Test
        void fromBytesOfNull_returnsNull() throws Exception {
            final byte[] bytes = jdkSerialize(null);
            assertNull(SerializationUtils.deserialize(bytes));
        }

        @Test
        void fromStream_roundtripsMap() throws Exception {
            final byte[] bytes = jdkSerialize(sampleMap);
            final Object obj = SerializationUtils.deserialize(toInputStream(bytes));
            assertDeepClonedMapEquals(sampleMap, (HashMap<?, ?>) obj);
        }

        @Test
        void fromEmptyStream_throwsSerializationException() {
            assertThrows(SerializationException.class, () -> SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0])));
        }

        @Test
        void fromStream_classNotFound_wrapsMessage() throws Exception {
            final byte[] bytes = jdkSerialize(new ClassNotFoundSerialization());
            final SerializationException se =
                assertThrows(SerializationException.class, () -> SerializationUtils.deserialize(toInputStream(bytes)));
            assertEquals("java.lang.ClassNotFoundException: " + CLASS_NOT_FOUND_MESSAGE, se.getMessage());
        }

        @Test
        void fromNullStream_throwsNullPointerException() {
            assertNullPointerException(() -> SerializationUtils.deserialize((InputStream) null));
        }

        @Test
        void fromStreamOfNull_returnsNull() throws Exception {
            final byte[] bytes = jdkSerialize(null);
            assertNull(SerializationUtils.deserialize(toInputStream(bytes)));
        }

        @Test
        void negativeByteArray_throwsSerializationException() {
            final byte[] bytes = {
                (byte) -84, (byte) -19, (byte) 0, (byte) 5, (byte) 125, (byte) -19, (byte) 0,
                (byte) 5, (byte) 115, (byte) 114, (byte) -1, (byte) 97, (byte) 122, (byte) -48, (byte) -65
            };
            assertThrows(SerializationException.class, () -> SerializationUtils.deserialize(new ByteArrayInputStream(bytes)));
        }

        @Test
        void classCastException_occursAtCallSite_notInDeserialize() {
            final String value = "Hello";
            final byte[] serialized = SerializationUtils.serialize(value);

            // Correctly typed call site works
            assertEquals(value, SerializationUtils.deserialize(serialized));

            // Incorrectly typed call site throws ClassCastException at call site
            assertThrows(ClassCastException.class, () -> {
                @SuppressWarnings("unused") // provoke CCE at assignment site
                final Integer i = SerializationUtils.deserialize(serialized);
            });
        }
    }

    @Nested
    class SerializeTests {

        @Test
        void toByteArray_matchesJdkSerialization() throws Exception {
            final byte[] testBytes = SerializationUtils.serialize(sampleMap);
            final byte[] expected = jdkSerialize(sampleMap);
            assertEquals(expected.length, testBytes.length);
            assertArrayEquals(expected, testBytes);
        }

        @Test
        void nullToByteArray_matchesJdkSerialization() throws Exception {
            final byte[] testBytes = SerializationUtils.serialize(null);
            final byte[] expected = jdkSerialize(null);
            assertEquals(expected.length, testBytes.length);
            assertArrayEquals(expected, testBytes);
        }

        @Test
        void unserializableToBytes_throwsSerializationException() {
            sampleMap.put(new Object(), new Object());
            assertThrows(SerializationException.class, () -> SerializationUtils.serialize(sampleMap));
        }

        @Test
        void toStream_matchesJdkSerialization() throws Exception {
            final ByteArrayOutputStream testOut = new ByteArrayOutputStream();
            SerializationUtils.serialize(sampleMap, testOut);
            final byte[] testBytes = testOut.toByteArray();

            final byte[] expected = jdkSerialize(sampleMap);

            assertEquals(expected.length, testBytes.length);
            assertArrayEquals(expected, testBytes);
        }

        @Test
        void nullObjectToStream_matchesJdkSerialization() throws Exception {
            final ByteArrayOutputStream testOut = new ByteArrayOutputStream();
            SerializationUtils.serialize(null, testOut);
            final byte[] testBytes = testOut.toByteArray();

            final byte[] expected = jdkSerialize(null);

            assertEquals(expected.length, testBytes.length);
            assertArrayEquals(expected, testBytes);
        }

        @Test
        void toStream_withNullStream_throwsNullPointerException() {
            assertNullPointerException(() -> SerializationUtils.serialize(sampleMap, null));
        }

        @Test
        void toStream_withBothArgsNull_throwsNullPointerException() {
            assertNullPointerException(() -> SerializationUtils.serialize(null, null));
        }

        @Test
        void toStream_unserializable_throwsSerializationException() {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            sampleMap.put(new Object(), new Object());
            assertThrows(SerializationException.class, () -> SerializationUtils.serialize(sampleMap, out));
        }

        @Test
        void toStream_whenIOException_wrapsAndPreservesMessage() {
            final OutputStream throwingStream = new OutputStream() {
                @Override
                public void write(final int b) throws IOException {
                    throw new IOException(SERIALIZE_IO_EXCEPTION_MESSAGE);
                }
            };
            final SerializationException e =
                assertThrows(SerializationException.class, () -> SerializationUtils.serialize(sampleMap, throwingStream));
            assertEquals("java.io.IOException: " + SERIALIZE_IO_EXCEPTION_MESSAGE, e.getMessage());
        }
    }

    @Nested
    class MiscTests {

        @Test
        void roundtrip_returnsDeepCopy() {
            final HashMap<Object, Object> newMap = SerializationUtils.roundtrip(sampleMap);
            assertEquals(sampleMap, newMap);
        }

        @Test
        void utilityClassConstructor_isPublicAndClassIsNonFinal_singlePublicCtor() {
            assertNotNull(new SerializationUtils());
            final Constructor<?>[] ctors = SerializationUtils.class.getDeclaredConstructors();
            assertEquals(1, ctors.length);
            assertTrue(Modifier.isPublic(ctors[0].getModifiers()));
            assertTrue(Modifier.isPublic(SerializationUtils.class.getModifiers()));
            assertFalse(Modifier.isFinal(SerializationUtils.class.getModifiers()));
        }

        @Test
        void serializationException_constructors_behaveAsExpected() {
            SerializationException serEx;
            final Exception cause = new Exception();

            serEx = new SerializationException();
            assertNull(serEx.getMessage());
            assertNull(serEx.getCause());

            serEx = new SerializationException("Message");
            assertEquals("Message", serEx.getMessage());
            assertNull(serEx.getCause());

            serEx = new SerializationException(cause);
            assertEquals("java.lang.Exception", serEx.getMessage());
            assertSame(cause, serEx.getCause());

            serEx = new SerializationException("Message", cause);
            assertEquals("Message", serEx.getMessage());
            assertSame(cause, serEx.getCause());
        }
    }
}