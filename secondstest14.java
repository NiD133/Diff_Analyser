package org.joda.time;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static org.junit.Assert.assertSame;

/**
 * Test suite for the serialization of the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that serializing and then deserializing one of the singleton constant
     * instances of {@link Seconds} returns the exact same instance, not just an
     * equal one. This verifies the correctness of the {@code readResolve()} method.
     */
    @Test
    public void serializingAConstant_whenDeserialized_thenReturnsTheSameInstance() throws IOException, ClassNotFoundException {
        // Arrange
        Seconds originalConstant = Seconds.THREE;

        // Act
        // Serialize the object to a byte array
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream)) {
            objectOutputStream.writeObject(originalConstant);
        }
        byte[] serializedBytes = byteOutputStream.toByteArray();

        // Deserialize the byte array back to an object
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(serializedBytes);
        Seconds deserializedConstant;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream)) {
            deserializedConstant = (Seconds) objectInputStream.readObject();
        }

        // Assert
        // The readResolve() method on Seconds ensures that deserializing a constant
        // returns the canonical singleton instance. We use assertSame to verify
        // that the deserialized object is the exact same instance in memory.
        assertSame("Deserialization should return the canonical constant instance",
                originalConstant, deserializedConstant);
    }
}