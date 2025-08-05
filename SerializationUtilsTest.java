/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertNullPointerException;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SerializationUtilsTest extends AbstractLangTest {

    static final String CLASS_NOT_FOUND_MESSAGE = "ClassNotFoundSerialization.readObject fake exception";
    protected static final String SERIALIZE_IO_EXCEPTION_MESSAGE = "Anonymous OutputStream I/O exception";

    private String iString;
    private Integer iInteger;
    private HashMap<Object, Object> iMap;

    @BeforeEach
    public void setUp() {
        iString = "foo";
        iInteger = Integer.valueOf(7);
        iMap = new HashMap<>();
        iMap.put("FOO", iString);
        iMap.put("BAR", iInteger);
    }

    @Nested
    class CloneTests {

        @Test
        void clone_serializableObject_createsEqualCopy() {
            final Object test = SerializationUtils.clone(iMap);
            assertNotNull(test);
            assertInstanceOf(HashMap.class, test);
            assertNotSame(test, iMap);
            final HashMap<?, ?> testMap = (HashMap<?, ?>) test;
            assertEquals(iString, testMap.get("FOO"));
            assertNotSame(iString, testMap.get("FOO"));
            assertEquals(iInteger, testMap.get("BAR"));
            assertNotSame(iInteger, testMap.get("BAR"));
            assertEquals(iMap, testMap);
        }

        @Test
        void clone_nullInput_returnsNull() {
            final Object test = SerializationUtils.clone(null);
            assertNull(test);
        }

        @Test
        void clone_serializableSupplier_maintainsFunctionality() {
            final SerializableSupplier<String> supplier = () -> "test";
            final SerializableSupplier<String> clone = SerializationUtils.clone(supplier);
            assertEquals("test", clone.get());
        }

        @Test
        void clone_unserializableObject_throwsSerializationException() {
            iMap.put(new Object(), new Object());
            assertThrows(SerializationException.class, () -> SerializationUtils.clone(iMap));
        }
    }

    @Nested
    class ConstructorTests {

        @Test
        void constructor_instantiationAndVisibility() {
            assertNotNull(new SerializationUtils());
            final Constructor<?>[] cons = SerializationUtils.class.getDeclaredConstructors();
            assertEquals(1, cons.length);
            assertTrue(Modifier.isPublic(cons[0].getModifiers()));
            assertTrue(Modifier.isPublic(SerializationUtils.class.getModifiers()));
            assertFalse(Modifier.isFinal(SerializationUtils.class.getModifiers()));
        }
    }

    @Nested
    class DeserializeBytesTests {

        @Test
        void deserialize_validByteArray_returnsDeserializedObject() throws Exception {
            final byte[] serializedData = serializeObject(iMap);
            final Object test = SerializationUtils.deserialize(serializedData);
            assertDeserializedObjectValid(test);
        }

        @Test
        void deserialize_emptyByteArray_throwsSerializationException() {
            assertThrows(SerializationException.class, () -> SerializationUtils.deserialize(new byte[0]));
        }

        @Test
        void deserialize_nullByteArray_throwsNullPointerException() {
            assertNullPointerException(() -> SerializationUtils.deserialize((byte[]) null));
        }

        @Test
        void deserialize_byteArrayContainingNull_returnsNull() throws Exception {
            final byte[] serializedData = serializeObject(null);
            final Object test = SerializationUtils.deserialize(serializedData);
            assertNull(test);
        }

        @Test
        void deserialize_incorrectTypeCast_throwsClassCastException() {
            final String value = "Hello";
            final byte[] serialized = SerializationUtils.serialize(value);
            // Valid deserialization
            assertEquals(value, SerializationUtils.deserialize(serialized));
            // Invalid cast attempt
            assertThrows(ClassCastException.class, () -> {
                @SuppressWarnings("unused")
                final Integer i = SerializationUtils.deserialize(serialized);
            });
        }
    }

    @Nested
    class DeserializeStreamTests {

        @Test
        void deserialize_validInputStream_returnsDeserializedObject() throws Exception {
            final byte[] serializedData = serializeObject(iMap);
            final Object test = SerializationUtils.deserialize(new ByteArrayInputStream(serializedData));
            assertDeserializedObjectValid(test);
        }

        @Test
        void deserialize_emptyInputStream_throwsSerializationException() {
            assertThrows(SerializationException.class, 
                () -> SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0])));
        }

        @Test
        void deserialize_classNotFoundInSerializedData_throwsSerializationExceptionWithCause() throws Exception {
            final byte[] serializedData = serializeObject(new ClassNotFoundSerialization());
            final InputStream inTest = new ByteArrayInputStream(serializedData);
            final SerializationException se = assertThrows(SerializationException.class, 
                () -> SerializationUtils.deserialize(inTest));
            assertEquals("java.lang.ClassNotFoundException: " + CLASS_NOT_FOUND_MESSAGE, se.getMessage());
        }

        @Test
        void deserialize_nullInputStream_throwsNullPointerException() {
            assertNullPointerException(() -> SerializationUtils.deserialize((InputStream) null));
        }

        @Test
        void deserialize_inputStreamContainingNull_returnsNull() throws Exception {
            final byte[] serializedData = serializeObject(null);
            final InputStream inTest = new ByteArrayInputStream(serializedData);
            final Object test = SerializationUtils.deserialize(inTest);
            assertNull(test);
        }
    }

    @Nested
    class SerializeBytesTests {

        @Test
        void serialize_validObject_producesExpectedByteArray() throws Exception {
            final byte[] testBytes = SerializationUtils.serialize(iMap);
            final byte[] expectedBytes = serializeUsingObjectOutputStream(iMap);
            assertArrayEquals(expectedBytes, testBytes);
        }

        @Test
        void serialize_nullObject_producesExpectedByteArray() throws Exception {
            final byte[] testBytes = SerializationUtils.serialize(null);
            final byte[] expectedBytes = serializeUsingObjectOutputStream(null);
            assertArrayEquals(expectedBytes, testBytes);
        }

        @Test
        void serialize_unserializableObject_throwsSerializationException() {
            iMap.put(new Object(), new Object());
            assertThrows(SerializationException.class, () -> SerializationUtils.serialize(iMap));
        }
    }

    @Nested
    class SerializeStreamTests {

        @Test
        void serialize_validObjectToStream_producesExpectedOutput() throws Exception {
            final ByteArrayOutputStream testStream = new ByteArrayOutputStream();
            SerializationUtils.serialize(iMap, testStream);
            final byte[] testBytes = testStream.toByteArray();
            final byte[] expectedBytes = serializeUsingObjectOutputStream(iMap);
            assertArrayEquals(expectedBytes, testBytes);
        }

        @Test
        void serialize_nullObjectToStream_producesExpectedOutput() throws Exception {
            final ByteArrayOutputStream testStream = new ByteArrayOutputStream();
            SerializationUtils.serialize(null, testStream);
            final byte[] testBytes = testStream.toByteArray();
            final byte[] expectedBytes = serializeUsingObjectOutputStream(null);
            assertArrayEquals(expectedBytes, testBytes);
        }

        @Test
        void serialize_outputStreamThrowsIOException_wrapsInSerializationException() {
            final OutputStream failingStream = new OutputStream() {
                @Override
                public void write(int arg0) throws IOException {
                    throw new IOException(SERIALIZE_IO_EXCEPTION_MESSAGE);
                }
            };
            final SerializationException e = assertThrows(SerializationException.class,
                () -> SerializationUtils.serialize(iMap, failingStream));
            assertEquals("java.io.IOException: " + SERIALIZE_IO_EXCEPTION_MESSAGE, e.getMessage());
        }

        @Test
        void serialize_nullObjectAndNullStream_throwsNullPointerException() {
            assertNullPointerException(() -> SerializationUtils.serialize(null, null));
        }

        @Test
        void serialize_validObjectWithNullStream_throwsNullPointerException() {
            assertNullPointerException(() -> SerializationUtils.serialize(iMap, null));
        }

        @Test
        void serialize_unserializableObjectToStream_throwsSerializationException() {
            iMap.put(new Object(), new Object());
            assertThrows(SerializationException.class,
                () -> SerializationUtils.serialize(iMap, new ByteArrayOutputStream()));
        }
    }

    @Nested
    class RoundtripTests {

        @Test
        void roundtrip_serializableObject_returnsEqualInstance() {
            final HashMap<Object, Object> newMap = SerializationUtils.roundtrip(iMap);
            assertEquals(iMap, newMap);
        }
    }

    @Nested
    class PrimitiveTypeTests {

        static Stream<Arguments> primitiveTypes() {
            return Stream.of(
                Arguments.of(byte.class),
                Arguments.of(short.class),
                Arguments.of(int.class),
                Arguments.of(long.class),
                Arguments.of(float.class),
                Arguments.of(double.class),
                Arguments.of(boolean.class),
                Arguments.of(char.class),
                Arguments.of(void.class)
            );
        }

        @ParameterizedTest
        @MethodSource("primitiveTypes")
        void clone_primitiveTypeClass_returnsSameClass(Class<?> primitiveType) {
            final Class<?> clone = SerializationUtils.clone(primitiveType);
            assertEquals(primitiveType, clone);
        }
    }

    @Nested
    class ExceptionTests {

        @Test
        void serializationException_noArgConstructor_hasNullMessageAndCause() {
            final SerializationException ex = new SerializationException();
            assertNull(ex.getMessage());
            assertNull(ex.getCause());
        }

        @Test
        void serializationException_messageConstructor_hasSpecifiedMessage() {
            final String message = "Test message";
            final SerializationException ex = new SerializationException(message);
            assertEquals(message, ex.getMessage());
            assertNull(ex.getCause());
        }

        @Test
        void serializationException_causeConstructor_hasSpecifiedCause() {
            final Exception cause = new Exception("Test cause");
            final SerializationException ex = new SerializationException(cause);
            assertEquals(cause.toString(), ex.getMessage());
            assertSame(cause, ex.getCause());
        }

        @Test
        void serializationException_messageAndCauseConstructor_hasBothProperties() {
            final String message = "Test message";
            final Exception cause = new Exception("Test cause");
            final SerializationException ex = new SerializationException(message, cause);
            assertEquals(message, ex.getMessage());
            assertSame(cause, ex.getCause());
        }
    }

    @Test
    void deserialize_invalidByteArray_throwsSerializationException() {
        final byte[] invalidData = {
            (byte) -84, (byte) -19, (byte) 0, (byte) 5, (byte) 125, (byte) -19, (byte) 0,
            (byte) 5, (byte) 115, (byte) 112, (byte) -1, (byte) 97, (byte) 122, (byte) -48, (byte) -65
        };
        assertThrows(SerializationException.class,
            () -> SerializationUtils.deserialize(new ByteArrayInputStream(invalidData)));
    }

    // Helper methods
    private byte[] serializeObject(Object obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.toByteArray();
        }
    }

    private byte[] serializeUsingObjectOutputStream(Serializable obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.toByteArray();
        }
    }

    private void assertDeserializedObjectValid(Object test) {
        assertNotNull(test);
        assertInstanceOf(HashMap.class, test);
        assertNotSame(test, iMap);
        final HashMap<?, ?> testMap = (HashMap<?, ?>) test;
        assertEquals(iString, testMap.get("FOO"));
        assertNotSame(iString, testMap.get("FOO"));
        assertEquals(iInteger, testMap.get("BAR"));
        assertNotSame(iInteger, testMap.get("BAR"));
        assertEquals(iMap, testMap);
    }

    // Test classes
    static final class ClassNotFoundSerialization implements Serializable {
        private static final long serialVersionUID = 1L;
        private void readObject(ObjectInputStream in) throws ClassNotFoundException {
            throw new ClassNotFoundException(SerializationUtilsTest.CLASS_NOT_FOUND_MESSAGE);
        }
    }

    interface SerializableSupplier<T> extends Supplier<T>, Serializable {
        // Marker interface
    }
}