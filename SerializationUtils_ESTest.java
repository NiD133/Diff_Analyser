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

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Serializable;

/**
 * Unit tests for {@link SerializationUtils}.
 */
public class SerializationUtilsTest {

    // A simple, non-serializable class for testing failure scenarios.
    private static class UnserializableObject {}

    // A serializable class that holds a reference to a non-serializable object.
    private static class SerializableContainer implements Serializable {
        private static final long serialVersionUID = 1L;
        // This field will cause serialization to fail.
        @SuppressWarnings("unused")
        private final UnserializableObject unserializableField;

        public SerializableContainer(UnserializableObject obj) {
            this.unserializableField = obj;
        }
    }

    // --- clone() tests ---

    @Test
    public void clone_withSerializableObject_returnsDeepClone() {
        // Arrange
        final Integer original = 12345;

        // Act
        final Integer cloned = SerializationUtils.clone(original);

        // Assert
        assertEquals(original, cloned);
        assertNotSame("The cloned object should be a new instance", original, cloned);
    }

    @Test
    public void clone_withNull_returnsNull() {
        // Act & Assert
        assertNull("Cloning a null object should return null", SerializationUtils.clone(null));
    }

    @Test(expected = SerializationException.class)
    public void clone_withNonSerializableContainedObject_throwsSerializationException() {
        // Arrange
        final SerializableContainer container = new SerializableContainer(new UnserializableObject());

        // Act: This should fail because the container holds a non-serializable field.
        SerializationUtils.clone(container);
    }

    // --- roundtrip() tests ---

    @Test
    public void roundtrip_withSerializableObject_returnsEqualObject() {
        // Arrange
        final String original = "test string";

        // Act
        final String roundtripped = SerializationUtils.roundtrip(original);

        // Assert
        assertEquals(original, roundtripped);
    }

    @Test
    public void roundtrip_withNull_returnsNull() {
        // Act & Assert
        assertNull("Roundtripping a null object should return null", SerializationUtils.roundtrip(null));
    }

    // --- serialize() and deserialize() tests ---

    @Test
    public void serializeAndDeserialize_withObject_restoresObject() {
        // Arrange
        final String original = "Hello, World!";

        // Act
        final byte[] serializedData = SerializationUtils.serialize(original);
        final String deserialized = SerializationUtils.deserialize(serializedData);

        // Assert
        assertNotNull(serializedData);
        assertEquals(original, deserialized);
    }

    @Test
    public void serializeAndDeserialize_withNull_restoresNull() {
        // Act
        final byte[] serializedData = SerializationUtils.serialize(null);
        final Object deserialized = SerializationUtils.deserialize(serializedData);

        // Assert
        assertNotNull(serializedData);
        assertNull("Deserializing a serialized null should result in null", deserialized);
    }

    @Test
    public void serializeAndDeserialize_viaStreams_restoresObject() {
        // Arrange
        final Integer original = 9876;
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Act
        SerializationUtils.serialize(original, baos);
        final byte[] data = baos.toByteArray();
        final InputStream bais = new ByteArrayInputStream(data);
        final Integer deserialized = SerializationUtils.deserialize(bais);

        // Assert
        assertEquals(original, deserialized);
    }

    // --- Exception handling tests ---

    @Test(expected = NullPointerException.class)
    public void serialize_toNullOutputStream_throwsNullPointerException() {
        SerializationUtils.serialize("some object", null);
    }

    @Test(expected = SerializationException.class)
    public void serialize_toClosedStream_throwsSerializationException() throws Exception {
        // An unconnected PipedOutputStream will throw an IOException on write.
        try (PipedOutputStream closedStream = new PipedOutputStream()) {
            SerializationUtils.serialize("some object", closedStream);
        }
    }

    @Test(expected = SerializationException.class)
    public void serialize_withNonSerializableObject_throwsSerializationException() {
        // Arrange
        final SerializableContainer container = new SerializableContainer(new UnserializableObject());
        // Act: This should fail because the container holds a non-serializable field.
        SerializationUtils.serialize(container);
    }

    @Test(expected = NullPointerException.class)
    public void deserialize_fromNullByteArray_throwsNullPointerException() {
        SerializationUtils.deserialize((byte[]) null);
    }

    @Test(expected = NullPointerException.class)
    public void deserialize_fromNullInputStream_throwsNullPointerException() {
        SerializationUtils.deserialize((InputStream) null);
    }



    @Test(expected = SerializationException.class)
    public void deserialize_fromCorruptedBytes_throwsSerializationException() {
        // Arrange: Create a byte array that is not a valid serialized object.
        final byte[] corruptedData = {0x00, 0x01, 0x02, 0x03};

        // Act
        SerializationUtils.deserialize(corruptedData);
    }

    @Test(expected = SerializationException.class)
    public void deserialize_fromClosedStream_throwsSerializationException() throws Exception {
        // An unconnected PipedInputStream will throw an IOException on read.
        try (PipedInputStream closedStream = new PipedInputStream()) {
            SerializationUtils.deserialize(closedStream);
        }
    }

    // --- Miscellaneous tests ---

    @Test
    public void serialize_nullObjectToStream_doesNotThrow() {
        // This test verifies that serializing a null object is a valid operation
        // and does not throw an exception.
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    @Test
    public void constructor_canBeInstantiated() {
        // The constructor is public for tools that require a JavaBean instance.
        // This test ensures it can be called for code coverage.
        assertNotNull(new SerializationUtils());
    }

    @Test(expected = NullPointerException.class)
    public void classLoaderAwareObjectInputStream_withNullStream_throwsNullPointerException() throws Exception {
        // The inner class constructor should reject a null input stream.
        new SerializationUtils.ClassLoaderAwareObjectInputStream(null, getClass().getClassLoader());
    }
}