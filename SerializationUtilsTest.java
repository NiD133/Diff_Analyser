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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;

/**
 * Test class that simulates ClassNotFoundException during deserialization
 */
final class ClassNotFoundSerialization implements Serializable {
    private static final long serialVersionUID = 1L;

    private void readObject(final ObjectInputStream in) throws ClassNotFoundException {
        throw new ClassNotFoundException(SerializationUtilsTest.CLASS_NOT_FOUND_MESSAGE);
    }
}

/**
 * Functional interface that extends both Supplier and Serializable for testing purposes
 */
interface SerializableSupplier<T> extends Supplier<T>, Serializable {
    // empty
}

/**
 * Tests {@link SerializationUtils}.
 */
@DisplayName("SerializationUtils Tests")
class SerializationUtilsTest extends AbstractLangTest {

    static final String CLASS_NOT_FOUND_MESSAGE = "ClassNotFoundSerialization.readObject fake exception";
    protected static final String SERIALIZE_IO_EXCEPTION_MESSAGE = "Anonymous OutputStream I/O exception";

    // Test data objects
    private String testString;
    private Integer testInteger;
    private HashMap<Object, Object> testMap;

    @BeforeEach
    void setUpTestData() {
        testString = "foo";
        testInteger = Integer.valueOf(7);
        testMap = new HashMap<>();
        testMap.put("FOO", testString);
        testMap.put("BAR", testInteger);
    }

    @Nested
    @DisplayName("Clone Tests")
    class CloneTests {

        @Test
        @DisplayName("Should create deep clone of serializable object")
        void shouldCreateDeepCloneOfSerializableObject() {
            // When
            Object clonedObject = SerializationUtils.clone(testMap);

            // Then
            assertNotNull(clonedObject);
            assertInstanceOf(HashMap.class, clonedObject);
            assertNotSame(clonedObject, testMap, "Clone should be different instance");
            
            HashMap<?, ?> clonedMap = (HashMap<?, ?>) clonedObject;
            assertEquals(testString, clonedMap.get("FOO"));
            assertNotSame(testString, clonedMap.get("FOO"), "String values should be different instances");
            assertEquals(testInteger, clonedMap.get("BAR"));
            assertNotSame(testInteger, clonedMap.get("BAR"), "Integer values should be different instances");
            assertEquals(testMap, clonedMap, "Maps should be equal in content");
        }

        @Test
        @DisplayName("Should return null when cloning null object")
        void shouldReturnNullWhenCloningNullObject() {
            // When
            Object clonedObject = SerializationUtils.clone(null);

            // Then
            assertNull(clonedObject);
        }

        @Test
        @DisplayName("Should clone serializable supplier successfully")
        void shouldCloneSerializableSupplierSuccessfully() {
            // Given
            SerializableSupplier<String> originalSupplier = () -> "test";
            assertEquals("test", originalSupplier.get());

            // When
            SerializableSupplier<String> clonedSupplier = SerializationUtils.clone(originalSupplier);

            // Then
            assertEquals("test", clonedSupplier.get());
        }

        @Test
        @DisplayName("Should throw exception when cloning unserializable object")
        void shouldThrowExceptionWhenCloningUnserializableObject() {
            // Given
            testMap.put(new Object(), new Object()); // Non-serializable objects

            // When & Then
            assertThrows(SerializationException.class, () -> SerializationUtils.clone(testMap));
        }
    }

    @Nested
    @DisplayName("Serialization to Bytes Tests")
    class SerializationToBytesTests {

        @Test
        @DisplayName("Should serialize object to byte array correctly")
        void shouldSerializeObjectToByteArrayCorrectly() throws Exception {
            // When
            byte[] serializedBytes = SerializationUtils.serialize(testMap);

            // Then - Compare with standard Java serialization
            byte[] expectedBytes = createExpectedSerializedBytes(testMap);
            assertEquals(expectedBytes.length, serializedBytes.length);
            assertArrayEquals(expectedBytes, serializedBytes);
        }

        @Test
        @DisplayName("Should serialize null to byte array correctly")
        void shouldSerializeNullToByteArrayCorrectly() throws Exception {
            // When
            byte[] serializedBytes = SerializationUtils.serialize(null);

            // Then - Compare with standard Java serialization
            byte[] expectedBytes = createExpectedSerializedBytes(null);
            assertEquals(expectedBytes.length, serializedBytes.length);
            assertArrayEquals(expectedBytes, serializedBytes);
        }

        @Test
        @DisplayName("Should throw exception when serializing unserializable object to bytes")
        void shouldThrowExceptionWhenSerializingUnserializableObjectToBytes() {
            // Given
            testMap.put(new Object(), new Object()); // Non-serializable objects

            // When & Then
            assertThrows(SerializationException.class, () -> SerializationUtils.serialize(testMap));
        }

        private byte[] createExpectedSerializedBytes(Object obj) throws Exception {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(outputStream)) {
                oos.writeObject(obj);
                oos.flush();
            }
            return outputStream.toByteArray();
        }
    }

    @Nested
    @DisplayName("Serialization to Stream Tests")
    class SerializationToStreamTests {

        @Test
        @DisplayName("Should serialize object to output stream correctly")
        void shouldSerializeObjectToOutputStreamCorrectly() throws Exception {
            // Given
            ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();

            // When
            SerializationUtils.serialize(testMap, testOutputStream);

            // Then - Compare with standard Java serialization
            byte[] expectedBytes = createExpectedSerializedBytes(testMap);
            byte[] actualBytes = testOutputStream.toByteArray();
            assertEquals(expectedBytes.length, actualBytes.length);
            assertArrayEquals(expectedBytes, actualBytes);
        }

        @Test
        @DisplayName("Should serialize null object to output stream correctly")
        void shouldSerializeNullObjectToOutputStreamCorrectly() throws Exception {
            // Given
            ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();

            // When
            SerializationUtils.serialize(null, testOutputStream);

            // Then - Compare with standard Java serialization
            byte[] expectedBytes = createExpectedSerializedBytes(null);
            byte[] actualBytes = testOutputStream.toByteArray();
            assertEquals(expectedBytes.length, actualBytes.length);
            assertArrayEquals(expectedBytes, actualBytes);
        }

        @Test
        @DisplayName("Should throw NPE when both object and stream are null")
        void shouldThrowNPEWhenBothObjectAndStreamAreNull() {
            assertNullPointerException(() -> SerializationUtils.serialize(null, null));
        }

        @Test
        @DisplayName("Should throw NPE when output stream is null")
        void shouldThrowNPEWhenOutputStreamIsNull() {
            assertNullPointerException(() -> SerializationUtils.serialize(testMap, null));
        }

        @Test
        @DisplayName("Should throw exception when serializing unserializable object to stream")
        void shouldThrowExceptionWhenSerializingUnserializableObjectToStream() {
            // Given
            ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
            testMap.put(new Object(), new Object()); // Non-serializable objects

            // When & Then
            assertThrows(SerializationException.class, () -> SerializationUtils.serialize(testMap, testOutputStream));
        }

        @Test
        @DisplayName("Should handle IOException during serialization")
        void shouldHandleIOExceptionDuringSerialization() {
            // Given - OutputStream that always throws IOException
            OutputStream faultyOutputStream = new OutputStream() {
                @Override
                public void write(final int arg0) throws IOException {
                    throw new IOException(SERIALIZE_IO_EXCEPTION_MESSAGE);
                }
            };

            // When & Then
            SerializationException exception = assertThrows(SerializationException.class, 
                () -> SerializationUtils.serialize(testMap, faultyOutputStream));
            assertEquals("java.io.IOException: " + SERIALIZE_IO_EXCEPTION_MESSAGE, exception.getMessage());
        }

        private byte[] createExpectedSerializedBytes(Object obj) throws Exception {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(outputStream)) {
                oos.writeObject(obj);
                oos.flush();
            }
            return outputStream.toByteArray();
        }
    }

    @Nested
    @DisplayName("Deserialization from Bytes Tests")
    class DeserializationFromBytesTests {

        @Test
        @DisplayName("Should deserialize object from byte array correctly")
        void shouldDeserializeObjectFromByteArrayCorrectly() throws Exception {
            // Given
            byte[] serializedBytes = createSerializedBytes(testMap);

            // When
            Object deserializedObject = SerializationUtils.deserialize(serializedBytes);

            // Then
            assertDeserializedMapIsCorrect(deserializedObject);
        }

        @Test
        @DisplayName("Should deserialize null from byte array correctly")
        void shouldDeserializeNullFromByteArrayCorrectly() throws Exception {
            // Given
            byte[] serializedNullBytes = createSerializedBytes(null);

            // When
            Object deserializedObject = SerializationUtils.deserialize(serializedNullBytes);

            // Then
            assertNull(deserializedObject);
        }

        @Test
        @DisplayName("Should throw NPE when deserializing null byte array")
        void shouldThrowNPEWhenDeserializingNullByteArray() {
            assertNullPointerException(() -> SerializationUtils.deserialize((byte[]) null));
        }

        @Test
        @DisplayName("Should throw exception when deserializing invalid byte array")
        void shouldThrowExceptionWhenDeserializingInvalidByteArray() {
            assertThrows(SerializationException.class, () -> SerializationUtils.deserialize(new byte[0]));
        }

        @Test
        @DisplayName("Should handle ClassCastException at call site")
        void shouldHandleClassCastExceptionAtCallSite() {
            // Given
            String originalValue = "Hello";
            byte[] serializedString = SerializationUtils.serialize(originalValue);
            
            // Verify normal deserialization works
            assertEquals(originalValue, SerializationUtils.deserialize(serializedString));

            // When & Then - ClassCastException should occur at call site, not in deserialize method
            assertThrows(ClassCastException.class, () -> {
                @SuppressWarnings("unused") // Needed to cause the exception
                Integer incorrectlyTypedResult = SerializationUtils.deserialize(serializedString);
            });
        }

        private byte[] createSerializedBytes(Object obj) throws Exception {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(outputStream)) {
                oos.writeObject(obj);
                oos.flush();
            }
            return outputStream.toByteArray();
        }
    }

    @Nested
    @DisplayName("Deserialization from Stream Tests")
    class DeserializationFromStreamTests {

        @Test
        @DisplayName("Should deserialize object from input stream correctly")
        void shouldDeserializeObjectFromInputStreamCorrectly() throws Exception {
            // Given
            ByteArrayInputStream inputStream = createInputStreamWithSerializedObject(testMap);

            // When
            Object deserializedObject = SerializationUtils.deserialize(inputStream);

            // Then
            assertDeserializedMapIsCorrect(deserializedObject);
        }

        @Test
        @DisplayName("Should deserialize null from input stream correctly")
        void shouldDeserializeNullFromInputStreamCorrectly() throws Exception {
            // Given
            ByteArrayInputStream inputStream = createInputStreamWithSerializedObject(null);

            // When
            Object deserializedObject = SerializationUtils.deserialize(inputStream);

            // Then
            assertNull(deserializedObject);
        }

        @Test
        @DisplayName("Should throw NPE when deserializing from null input stream")
        void shouldThrowNPEWhenDeserializingFromNullInputStream() {
            assertNullPointerException(() -> SerializationUtils.deserialize((InputStream) null));
        }

        @Test
        @DisplayName("Should throw exception when deserializing from invalid input stream")
        void shouldThrowExceptionWhenDeserializingFromInvalidInputStream() {
            // Given
            ByteArrayInputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);

            // When & Then
            assertThrows(SerializationException.class, () -> SerializationUtils.deserialize(emptyInputStream));
        }

        @Test
        @DisplayName("Should handle ClassNotFoundException during deserialization")
        void shouldHandleClassNotFoundExceptionDuringDeserialization() throws Exception {
            // Given
            ByteArrayInputStream inputStream = createInputStreamWithSerializedObject(new ClassNotFoundSerialization());

            // When & Then
            SerializationException exception = assertThrows(SerializationException.class, 
                () -> SerializationUtils.deserialize(inputStream));
            assertEquals("java.lang.ClassNotFoundException: " + CLASS_NOT_FOUND_MESSAGE, exception.getMessage());
        }

        @Test
        @DisplayName("Should handle corrupted byte array during deserialization")
        void shouldHandleCorruptedByteArrayDuringDeserialization() {
            // Given - Corrupted byte array that looks like serialized data but isn't valid
            byte[] corruptedByteArray = {
                (byte) -84, (byte) -19, (byte) 0, (byte) 5, (byte) 125, (byte) -19, (byte) 0,
                (byte) 5, (byte) 115, (byte) 114, (byte) -1, (byte) 97, (byte) 122, (byte) -48, (byte) -65
            };
            ByteArrayInputStream corruptedInputStream = new ByteArrayInputStream(corruptedByteArray);

            // When & Then
            assertThrows(SerializationException.class, () -> SerializationUtils.deserialize(corruptedInputStream));
        }

        private ByteArrayInputStream createInputStreamWithSerializedObject(Object obj) throws Exception {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(outputStream)) {
                oos.writeObject(obj);
                oos.flush();
            }
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
    }

    @Nested
    @DisplayName("Roundtrip Tests")
    class RoundtripTests {

        @Test
        @DisplayName("Should perform successful serialization roundtrip")
        void shouldPerformSuccessfulSerializationRoundtrip() {
            // When
            HashMap<Object, Object> roundtrippedMap = SerializationUtils.roundtrip(testMap);

            // Then
            assertEquals(testMap, roundtrippedMap);
        }
    }

    @Nested
    @DisplayName("Special Cases Tests")
    class SpecialCasesTests {

        @Test
        @DisplayName("Should handle primitive type class serialization")
        void shouldHandlePrimitiveTypeClassSerialization() {
            // Given
            Class<?>[] primitiveTypes = { 
                byte.class, short.class, int.class, long.class, 
                float.class, double.class, boolean.class, char.class, void.class 
            };

            // When & Then
            for (Class<?> primitiveType : primitiveTypes) {
                Class<?> clonedType = SerializationUtils.clone(primitiveType);
                assertEquals(primitiveType, clonedType, 
                    "Cloned primitive type should equal original: " + primitiveType.getName());
            }
        }
    }

    @Nested
    @DisplayName("SerializationException Tests")
    class SerializationExceptionTests {

        @Test
        @DisplayName("Should create SerializationException with different constructors")
        void shouldCreateSerializationExceptionWithDifferentConstructors() {
            Exception causedException = new Exception();

            // Test default constructor
            SerializationException defaultException = new SerializationException();
            assertNull(defaultException.getMessage());
            assertNull(defaultException.getCause());

            // Test message constructor
            SerializationException messageException = new SerializationException("Test Message");
            assertEquals("Test Message", messageException.getMessage());
            assertNull(messageException.getCause());

            // Test cause constructor
            SerializationException causeException = new SerializationException(causedException);
            assertEquals("java.lang.Exception", causeException.getMessage());
            assertSame(causedException, causeException.getCause());

            // Test message and cause constructor
            SerializationException messageAndCauseException = new SerializationException("Test Message", causedException);
            assertEquals("Test Message", messageAndCauseException.getMessage());
            assertSame(causedException, messageAndCauseException.getCause());
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should verify SerializationUtils constructor properties")
        void shouldVerifySerializationUtilsConstructorProperties() {
            // When
            SerializationUtils instance = new SerializationUtils();

            // Then
            assertNotNull(instance);
            
            Constructor<?>[] constructors = SerializationUtils.class.getDeclaredConstructors();
            assertEquals(1, constructors.length, "Should have exactly one constructor");
            assertTrue(Modifier.isPublic(constructors[0].getModifiers()), "Constructor should be public");
            assertTrue(Modifier.isPublic(SerializationUtils.class.getModifiers()), "Class should be public");
            assertFalse(Modifier.isFinal(SerializationUtils.class.getModifiers()), "Class should not be final");
        }
    }

    // Helper method used by multiple test classes
    private void assertDeserializedMapIsCorrect(Object deserializedObject) {
        assertNotNull(deserializedObject);
        assertInstanceOf(HashMap.class, deserializedObject);
        assertNotSame(deserializedObject, testMap, "Deserialized object should be different instance");
        
        HashMap<?, ?> deserializedMap = (HashMap<?, ?>) deserializedObject;
        assertEquals(testString, deserializedMap.get("FOO"));
        assertNotSame(testString, deserializedMap.get("FOO"), "String values should be different instances");
        assertEquals(testInteger, deserializedMap.get("BAR"));
        assertNotSame(testInteger, deserializedMap.get("BAR"), "Integer values should be different instances");
        assertEquals(testMap, deserializedMap, "Maps should be equal in content");
    }
}