package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Test class for the serialization behavior of the Minutes class.
 */
public class MinutesTest {

    @Test
    public void serializingAndDeserializingAConstant_shouldReturnTheSameInstance() throws Exception {
        // Arrange
        Minutes originalMinutes = Minutes.THREE;

        // Act
        // Serialize the object to a byte array
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream)) {
            objectOutputStream.writeObject(originalMinutes);
        }
        byte[] serializedBytes = byteOutputStream.toByteArray();

        // Deserialize the byte array back to an object
        Minutes deserializedMinutes;
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(serializedBytes);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream)) {
            deserializedMinutes = (Minutes) objectInputStream.readObject();
        }

        // Assert
        // The Minutes class uses the readResolve method to return singleton instances
        // for its constants (ZERO, ONE, TWO, THREE). Therefore, the deserialized
        // object should be the exact same instance as the original constant, not just an
        // equal one.
        assertSame(
            "Deserialization should return the singleton instance",
            originalMinutes,
            deserializedMinutes
        );
    }
}