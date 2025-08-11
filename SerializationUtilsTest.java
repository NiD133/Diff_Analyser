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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link SerializationUtils}.
 *
 * The test suite is structured using nested classes for each public method,
 * improving organization and readability.
 */
class SerializationUtilsTest extends AbstractLangTest {

    // Test fixtures
    private String originalString;
    private Integer originalInteger;
    private HashMap<String, Object> originalMap;

    // Constants from the original test, kept for clarity
    static final String CLASS_NOT_FOUND_MESSAGE = "ClassNotFoundSerialization.readObject fake exception";
    protected static final String SERIALIZE_IO_EXCEPTION_MESSAGE = "Anonymous OutputStream I/O exception";

    @BeforeEach
    void setUp() {
        originalString = "foo";
        originalInteger = 7;
        originalMap = new HashMap<>();
        originalMap.put("FOO", originalString);
        originalMap.put("BAR", originalInteger);
    }

    // --- Helper Methods ---

    /**
     * Serializes an object using standard Java serialization for comparison.
     */
    private byte[] serializeWithStandardJava(final Serializable object) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
            return baos.toByteArray();
        }
    }

    /**
     * Asserts that a given object is a deep clone of the {@code originalMap}.
     */
    private void assertIsDeepCloneOfOriginalMap(final Object clone) {
        assertNotNull(clone);
        assertInstanceOf(HashMap.class, clone);
        assertNotSame(originalMap, clone, "Clone should be a different instance from the original map.");

        @SuppressWarnings("unchecked")
        final HashMap<String, Object> clonedMap = (HashMap<String, Object>) clone;

        // Check for deep clone behavior: map is equal, but internal references are not the same
        assertEquals(originalMap, clonedMap, "Cloned map should be equal to the original.");
        assertEquals(originalString, clonedMap.get("FOO"));
        assertNotSame(originalString, clonedMap.get("FOO"), "Cloned map's string value should be a different instance.");
        assertEquals(originalInteger, clonedMap.get("BAR"));
        assertNotSame(originalInteger, clonedMap.get("BAR"), "Cloned map's integer value should be a different instance.");
    }

    // --- Test Cases ---

    @Nested
    @DisplayName("clone()")
    class CloneTests {

        @Test
        void withSerializableObject_shouldReturnDeepClone() {
            // Act
            final HashMap<String, Object> clone = SerializationUtils.clone(originalMap);

            // Assert
            assertIsDeepCloneOfOriginalMap(clone);
        }

        @Test
        void withNull_shouldReturnNull() {
            assertNull(SerializationUtils.clone(null));
        }

        @Test
        void withUnserializableObject_shouldThrowSerializationException() {
            // Arrange
            originalMap.put("unserializable", new Object());

            // Act & Assert
            assertThrows(SerializationException.class, () -> SerializationUtils.clone(originalMap));
        }

        @Test
        void withSerializableLambda_shouldReturnWorkingClone() {
            // Arrange
            final SerializableSupplier<String> supplier = () -> "test";
            assertEquals("test", supplier.get());

            // Act
            final SerializableSupplier<String> clone = SerializationUtils.clone(supplier);

            // Assert
            assertNotNull(clone);
            assertEquals("test", clone.get());
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.SerializationUtilsTest#primitiveTypes")
        void withPrimitiveClass_shouldReturnSameClass(final Class<?> primitiveType) {
            // Act
            final Class<?> clone = SerializationUtils.clone(primitiveType);

            // Assert
            assertSame(primitiveType, clone, "Cloning a primitive class should return the same singleton instance.");
        }
    }

    @Nested
    @DisplayName("roundtrip()")
    class RoundtripTests {
        @Test
        void withSerializableObject_shouldReturnEqualObject() {
            // Act
            final HashMap<String, Object> roundtrippedMap = SerializationUtils.roundtrip(originalMap);

            // Assert
            assertIsDeepCloneOfOriginalMap(roundtrippedMap);
        }
    }

    @Nested
    @DisplayName("serialize()")
    class SerializeTests {

        @Test
        void toByteArray_withSerializableObject_shouldProduceCorrectBytes() throws IOException {
            // Arrange
            final byte[] expectedBytes = serializeWithStandardJava(originalMap);

            // Act
            final byte[] actualBytes = SerializationUtils.serialize(originalMap);

            // Assert
            assertArrayEquals(expectedBytes, actualBytes);
        }

        @Test
        void toByteArray_withNull_shouldProduceCorrectBytes() throws IOException {
            // Arrange
            final byte[] expectedBytes = serializeWithStandardJava(null);

            // Act
            final byte[] actualBytes = SerializationUtils.serialize(null);

            // Assert
            assertArrayEquals(expectedBytes, actualBytes);
        }

        @Test
        void toByteArray_withUnserializableObject_shouldThrowSerializationException() {
            // Arrange
            originalMap.put("unserializable", new Object());

            // Act & Assert
            assertThrows(SerializationException.class, () -> SerializationUtils.serialize(originalMap));
        }

        @Test
        void toStream_withSerializableObject_shouldWriteCorrectBytes() throws IOException {
            // Arrange
            final byte[] expectedBytes = serializeWithStandardJava(originalMap);
            final ByteArrayOutputStream actualByteStream = new ByteArrayOutputStream();

            // Act
            SerializationUtils.serialize(originalMap, actualByteStream);

            // Assert
            assertArrayEquals(expectedBytes, actualByteStream.toByteArray());
        }

        @Test
        void toStream_withNullObject_shouldWriteCorrectBytes() throws IOException {
            // Arrange
            final byte[] expectedBytes = serializeWithStandardJava(null);
            final ByteArrayOutputStream actualByteStream = new ByteArrayOutputStream();

            // Act
            SerializationUtils.serialize(null, actualByteStream);

            // Assert
            assertArrayEquals(expectedBytes, actualByteStream.toByteArray());
        }

        @Test
        void toStream_withUnserializableObject_shouldThrowSerializationException() {
            // Arrange
            originalMap.put("unserializable", new Object());
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // Act & Assert
            assertThrows(SerializationException.class, () -> SerializationUtils.serialize(originalMap, baos));
        }

        @Test
        void toStream_withNullStream_shouldThrowNullPointerException() {
            assertNullPointerException(() -> SerializationUtils.serialize(originalMap, null));
        }

        @Test
        void toStream_withNullObjectAndNullStream_shouldThrowNullPointerException() {
            assertNullPointerException(() -> SerializationUtils.serialize(null, null));
        }

        @Test
        void whenOutputStreamThrowsIOException_shouldWrapInSerializationException() {
            // Arrange
            final OutputStream failingStream = new OutputStream() {
                @Override
                public void write(final int b) throws IOException {
                    throw new IOException(SERIALIZE_IO_EXCEPTION_MESSAGE);
                }
            };

            // Act & Assert
            final SerializationException e =
                assertThrows(SerializationException.class, () -> SerializationUtils.serialize(originalMap, failingStream));
            assertEquals("java.io.IOException: " + SERIALIZE_IO_EXCEPTION_MESSAGE, e.getMessage());
        }
    }

    @Nested
    @DisplayName("deserialize()")
    class DeserializeTests {

        @Test
        void fromByteArray_withValidData_shouldReturnDeepClone() throws IOException {
            // Arrange
            final byte[] serializedBytes = serializeWithStandardJava(originalMap);

            // Act
            final Object deserializedObject = SerializationUtils.deserialize(serializedBytes);

            // Assert
            assertIsDeepCloneOfOriginalMap(deserializedObject);
        }

        @Test
        void fromByteArray_withSerializedNull_shouldReturnNull() throws IOException {
            // Arrange
            final byte[] serializedNull = serializeWithStandardJava(null);

            // Act
            final Object deserializedObject = SerializationUtils.deserialize(serializedNull);

            // Assert
            assertNull(deserializedObject);
        }

        @Test
        void fromByteArray_withNullData_shouldThrowNullPointerException() {
            assertNullPointerException(() -> SerializationUtils.deserialize((byte[]) null));
        }

        @Test
        void fromByteArray_withEmptyData_shouldThrowSerializationException() {
            assertThrows(SerializationException.class, () -> SerializationUtils.deserialize(new byte[0]));
        }

        @Test
        void fromStream_withValidData_shouldReturnDeepClone() throws IOException {
            // Arrange
            final byte[] serializedBytes = serializeWithStandardJava(originalMap);
            final ByteArrayInputStream bais = new ByteArrayInputStream(serializedBytes);

            // Act
            final Object deserializedObject = SerializationUtils.deserialize(bais);

            // Assert
            assertIsDeepCloneOfOriginalMap(deserializedObject);
        }

        @Test
        void fromStream_withSerializedNull_shouldReturnNull() throws IOException {
            // Arrange
            final byte[] serializedNull = serializeWithStandardJava(null);
            final ByteArrayInputStream bais = new ByteArrayInputStream(serializedNull);

            // Act
            final Object deserializedObject = SerializationUtils.deserialize(bais);

            // Assert
            assertNull(deserializedObject);
        }



        @Test
        void fromStream_withNullStream_shouldThrowNullPointerException() {
            assertNullPointerException(() -> SerializationUtils.deserialize((InputStream) null));
        }

        @Test
        void fromStream_withEmptyStream_shouldThrowSerializationException() {
            assertThrows(SerializationException.class, () -> SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0])));
        }

        @Test
        void whenClassNotFound_shouldWrapInSerializationException() throws IOException {
            // Arrange
            final byte[] serializedBytes = serializeWithStandardJava(new ClassNotFoundSerialization());
            final ByteArrayInputStream bais = new ByteArrayInputStream(serializedBytes);

            // Act & Assert
            final SerializationException ex = assertThrows(SerializationException.class, () -> SerializationUtils.deserialize(bais));
            assertEquals("java.lang.ClassNotFoundException: " + CLASS_NOT_FOUND_MESSAGE, ex.getMessage());
        }

        @Test
        void withCorruptedData_shouldThrowSerializationException() {
            // Arrange
            final byte[] corruptedData = {
                (byte) -84, (byte) -19, (byte) 0, (byte) 5, (byte) 125, (byte) -19, (byte) 0,
                (byte) 5, (byte) 115, (byte) 114, (byte) -1, (byte) 97, (byte) 122, (byte) -48, (byte) -65
            };

            // Act & Assert
            assertThrows(SerializationException.class, () -> SerializationUtils.deserialize(corruptedData));
        }

        @Test
        @DisplayName("when assigning to wrong type, should throw ClassCastException at call site")
        void whenAssigningToWrongType_throwsClassCastExceptionAtCallSite() {
            // Arrange
            final byte[] serializedString = SerializationUtils.serialize("Hello");

            // Act & Assert
            // This demonstrates that the ClassCastException is thrown by the JVM at the assignment,
            // not by the deserialize method itself, as documented.
            assertThrows(ClassCastException.class, () -> {
                @SuppressWarnings("unused") // The assignment is what causes the exception
                Integer incorrectlyTypedObject = SerializationUtils.deserialize(serializedString);
            });
        }
    }

    @Nested
    @DisplayName("SerializationException Class")
    class SerializationExceptionTests {

        private final Throwable cause = new IOException("root cause");

        @Test
        void shouldBeInstantiableWithNoArgs() {
            final SerializationException ex = new SerializationException();
            assertNull(ex.getMessage());
            assertNull(ex.getCause());
        }

        @Test
        void shouldBeInstantiableWithMessage() {
            final String message = "Custom message";
            final SerializationException ex = new SerializationException(message);
            assertEquals(message, ex.getMessage());
            assertNull(ex.getCause());
        }

        @Test
        void shouldBeInstantiableWithCause() {
            final SerializationException ex = new SerializationException(cause);
            assertEquals(cause.toString(), ex.getMessage());
            assertSame(cause, ex.getCause());
        }

        @Test
        void shouldBeInstantiableWithMessageAndCause() {
            final String message = "Custom message";
            final SerializationException ex = new SerializationException(message, cause);
            assertEquals(message, ex.getMessage());
            assertSame(cause, ex.getCause());
        }
    }

    @Test
    @DisplayName("Class should have a public constructor for bean compatibility")
    void class_shouldHavePublicConstructor() {
        // This test verifies the class's public API for reflection-based tools.
        // Arrange
        final Constructor<?>[] constructors = SerializationUtils.class.getDeclaredConstructors();

        // Assert
        assertNotNull(new SerializationUtils(), "Instantiation should be possible.");
        assertEquals(1, constructors.length, "Should have only one constructor.");
        assertTrue(Modifier.isPublic(constructors[0].getModifiers()), "Constructor should be public.");
        assertTrue(Modifier.isPublic(SerializationUtils.class.getModifiers()), "Class should be public.");
        assertFalse(Modifier.isFinal(SerializationUtils.class.getModifiers()), "Class should not be final.");
    }

    // --- Data Providers ---

    static Stream<Class<?>> primitiveTypes() {
        return Stream.of(byte.class, short.class, int.class, long.class, float.class, double.class,
            boolean.class, char.class, void.class);
    }
}

// --- Helper classes from original test ---

final class ClassNotFoundSerialization implements Serializable {
    private static final long serialVersionUID = 1L;
    private void readObject(final ObjectInputStream in) throws ClassNotFoundException {
        throw new ClassNotFoundException(SerializationUtilsTest.CLASS_NOT_FOUND_MESSAGE);
    }
}

interface SerializableSupplier<T> extends Supplier<T>, Serializable {
    // empty
}