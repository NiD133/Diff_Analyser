package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

/**
 * Tests the serialization of the {@link Years} class.
 */
public class YearsTest {

    /**
     * This test verifies that when a singleton instance of {@link Years} (like {@code Years.THREE})
     * is serialized and then deserialized, the original singleton instance is returned.
     * <p>
     * The {@link Years} class uses the {@code readResolve()} method to ensure that deserialization
     * of its predefined constants ({@code ZERO}, {@code ONE}, etc.) does not create new objects,
     * thus preserving the singleton pattern.
     */
    @Test
    public void testSerialization_preservesSingletonInstances() throws IOException, ClassNotFoundException {
        // Arrange: Get a singleton instance to be tested.
        Years originalInstance = Years.THREE;
        Years deserializedInstance;

        // Act: Serialize the instance to a byte array, then deserialize it back into an object.
        byte[] serializedBytes;
        try (ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream)) {
            objectOutputStream.writeObject(originalInstance);
            serializedBytes = byteOutputStream.toByteArray();
        }

        try (ByteArrayInputStream byteInputStream = new ByteArrayInputStream(serializedBytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream)) {
            deserializedInstance = (Years) objectInputStream.readObject();
        }

        // Assert: Verify that the deserialized object is the *exact same instance* as the original.
        // We use assertSame() to check for object identity (i.e., they point to the same memory address),
        // which confirms that the readResolve() method is working correctly.
        assertSame(originalInstance, deserializedInstance);
    }
}