package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertNullPointerException;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link SerializationUtils}.
 */
class SerializationUtilsTest extends AbstractLangTest {

    private static final String CLASS_NOT_FOUND_MESSAGE = "ClassNotFoundSerialization.readObject fake exception";
    private static final String SERIALIZE_IO_EXCEPTION_MESSAGE = "Anonymous OutputStream I/O exception";

    private String testString;
    private Integer testInteger;
    private HashMap<Object, Object> testMap;

    @BeforeEach
    void setUp() {
        testString = "foo";
        testInteger = 7;
        testMap = new HashMap<>();
        testMap.put("FOO", testString);
        testMap.put("BAR", testInteger);
    }

    @Test
    void testClone() {
        final Object clonedMap = SerializationUtils.clone(testMap);
        assertNotNull(clonedMap);
        assertInstanceOf(HashMap.class, clonedMap);
        assertNotSame(clonedMap, testMap);

        final HashMap<?, ?> clonedTestMap = (HashMap<?, ?>) clonedMap;
        assertEquals(testString, clonedTestMap.get("FOO"));
        assertNotSame(testString, clonedTestMap.get("FOO"));
        assertEquals(testInteger, clonedTestMap.get("BAR"));
        assertNotSame(testInteger, clonedTestMap.get("BAR"));
        assertEquals(testMap, clonedTestMap);
    }

    @Test
    void testCloneNull() {
        final Object clonedObject = SerializationUtils.clone(null);
        assertNull(clonedObject);
    }

    @Test
    void testCloneSerializableSupplier() {
        final SerializableSupplier<String> supplier = () -> "test";
        assertEquals("test", supplier.get());

        final SerializableSupplier<String> clonedSupplier = SerializationUtils.clone(supplier);
        assertEquals("test", clonedSupplier.get());
    }

    @Test
    void testCloneUnserializable() {
        testMap.put(new Object(), new Object());
        assertThrows(SerializationException.class, () -> SerializationUtils.clone(testMap));
    }

    @Test
    void testConstructor() {
        assertNotNull(new SerializationUtils());
        final Constructor<?>[] constructors = SerializationUtils.class.getDeclaredConstructors();
        assertEquals(1, constructors.length);
        assertTrue(Modifier.isPublic(constructors[0].getModifiers()));
        assertTrue(Modifier.isPublic(SerializationUtils.class.getModifiers()));
        assertFalse(Modifier.isFinal(SerializationUtils.class.getModifiers()));
    }

    @Test
    void testDeserializeBytes() throws Exception {
        final byte[] serializedData = serializeObjectToBytes(testMap);

        final Object deserializedObject = SerializationUtils.deserialize(serializedData);
        assertDeserializedMap(deserializedObject);
    }

    @Test
    void testDeserializeBytesBadStream() {
        assertThrows(SerializationException.class, () -> SerializationUtils.deserialize(new byte[0]));
    }

    @Test
    void testDeserializeBytesNull() {
        assertNullPointerException(() -> SerializationUtils.deserialize((byte[]) null));
    }

    @Test
    void testDeserializeBytesOfNull() throws Exception {
        final byte[] serializedData = serializeObjectToBytes(null);

        final Object deserializedObject = SerializationUtils.deserialize(serializedData);
        assertNull(deserializedObject);
    }

    @Test
    void testDeserializeClassCastException() {
        final String value = "Hello";
        final byte[] serializedData = SerializationUtils.serialize(value);
        assertEquals(value, SerializationUtils.deserialize(serializedData));

        assertThrows(ClassCastException.class, () -> {
            final Integer deserializedInteger = SerializationUtils.deserialize(serializedData);
        });
    }

    @Test
    void testDeserializeStream() throws Exception {
        final byte[] serializedData = serializeObjectToBytes(testMap);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedData);

        final Object deserializedObject = SerializationUtils.deserialize(inputStream);
        assertDeserializedMap(deserializedObject);
    }

    @Test
    void testDeserializeStreamBadStream() {
        assertThrows(SerializationException.class, () -> SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0])));
    }

    @Test
    void testDeserializeStreamClassNotFound() throws Exception {
        final byte[] serializedData = serializeObjectToBytes(new ClassNotFoundSerialization());
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedData);

        final SerializationException exception = assertThrows(SerializationException.class, () -> SerializationUtils.deserialize(inputStream));
        assertEquals("java.lang.ClassNotFoundException: " + CLASS_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    void testDeserializeStreamNull() {
        assertNullPointerException(() -> SerializationUtils.deserialize((InputStream) null));
    }

    @Test
    void testDeserializeStreamOfNull() throws Exception {
        final byte[] serializedData = serializeObjectToBytes(null);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedData);

        final Object deserializedObject = SerializationUtils.deserialize(inputStream);
        assertNull(deserializedObject);
    }

    @Test
    void testException() {
        final Exception cause = new Exception();

        SerializationException exception = new SerializationException();
        assertNull(exception.getMessage());
        assertNull(exception.getCause());

        exception = new SerializationException("Message");
        assertEquals("Message", exception.getMessage());
        assertNull(exception.getCause());

        exception = new SerializationException(cause);
        assertEquals("java.lang.Exception", exception.getMessage());
        assertSame(cause, exception.getCause());

        exception = new SerializationException("Message", cause);
        assertEquals("Message", exception.getMessage());
        assertSame(cause, exception.getCause());
    }

    @Test
    void testNegativeByteArray() {
        final byte[] invalidByteArray = {
            (byte) -84, (byte) -19, (byte) 0, (byte) 5, (byte) 125, (byte) -19, (byte) 0,
            (byte) 5, (byte) 115, (byte) 114, (byte) -1, (byte) 97, (byte) 122, (byte) -48, (byte) -65
        };

        assertThrows(SerializationException.class, () -> SerializationUtils.deserialize(new ByteArrayInputStream(invalidByteArray)));
    }

    @Test
    void testPrimitiveTypeClassSerialization() {
        final Class<?>[] primitiveTypes = { byte.class, short.class, int.class, long.class, float.class, double.class,
                boolean.class, char.class, void.class };

        for (final Class<?> primitiveType : primitiveTypes) {
            final Class<?> clonedType = SerializationUtils.clone(primitiveType);
            assertEquals(primitiveType, clonedType);
        }
    }

    @Test
    void testRoundtrip() {
        final HashMap<Object, Object> roundtripMap = SerializationUtils.roundtrip(testMap);
        assertEquals(testMap, roundtripMap);
    }

    @Test
    void testSerializeBytes() throws Exception {
        final byte[] serializedBytes = SerializationUtils.serialize(testMap);

        final byte[] expectedBytes = serializeObjectToBytes(testMap);
        assertArrayEquals(expectedBytes, serializedBytes);
    }

    @Test
    void testSerializeBytesNull() throws Exception {
        final byte[] serializedBytes = SerializationUtils.serialize(null);

        final byte[] expectedBytes = serializeObjectToBytes(null);
        assertArrayEquals(expectedBytes, serializedBytes);
    }

    @Test
    void testSerializeBytesUnserializable() {
        testMap.put(new Object(), new Object());
        assertThrows(SerializationException.class, () -> SerializationUtils.serialize(testMap));
    }

    @Test
    void testSerializeIOException() {
        final OutputStream failingStream = new OutputStream() {
            @Override
            public void write(final int b) throws IOException {
                throw new IOException(SERIALIZE_IO_EXCEPTION_MESSAGE);
            }
        };

        final SerializationException exception =
                assertThrows(SerializationException.class, () -> SerializationUtils.serialize(testMap, failingStream));
        assertEquals("java.io.IOException: " + SERIALIZE_IO_EXCEPTION_MESSAGE, exception.getMessage());
    }

    @Test
    void testSerializeStream() throws Exception {
        final ByteArrayOutputStream testStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(testMap, testStream);

        final byte[] expectedBytes = serializeObjectToBytes(testMap);
        assertArrayEquals(expectedBytes, testStream.toByteArray());
    }

    @Test
    void testSerializeStreamNullNull() {
        assertNullPointerException(() -> SerializationUtils.serialize(null, null));
    }

    @Test
    void testSerializeStreamNullObj() throws Exception {
        final ByteArrayOutputStream testStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(null, testStream);

        final byte[] expectedBytes = serializeObjectToBytes(null);
        assertArrayEquals(expectedBytes, testStream.toByteArray());
    }

    @Test
    void testSerializeStreamObjNull() {
        assertNullPointerException(() -> SerializationUtils.serialize(testMap, null));
    }

    @Test
    void testSerializeStreamUnserializable() {
        final ByteArrayOutputStream testStream = new ByteArrayOutputStream();
        testMap.put(new Object(), new Object());
        assertThrows(SerializationException.class, () -> SerializationUtils.serialize(testMap, testStream));
    }

    private byte[] serializeObjectToBytes(Object obj) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        }
    }

    private void assertDeserializedMap(Object deserializedObject) {
        assertNotNull(deserializedObject);
        assertInstanceOf(HashMap.class, deserializedObject);
        assertNotSame(deserializedObject, testMap);

        final HashMap<?, ?> deserializedMap = (HashMap<?, ?>) deserializedObject;
        assertEquals(testString, deserializedMap.get("FOO"));
        assertNotSame(testString, deserializedMap.get("FOO"));
        assertEquals(testInteger, deserializedMap.get("BAR"));
        assertNotSame(testInteger, deserializedMap.get("BAR"));
        assertEquals(testMap, deserializedMap);
    }
}