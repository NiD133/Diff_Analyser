package org.joda.time;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static org.junit.Assert.assertSame;

/**
 * Test suite for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Verifies that deserializing a {@link Weeks} constant returns the canonical singleton instance.
     * <p>
     * The {@link Weeks} class pre-defines constants like {@link Weeks#ZERO}, {@link Weeks#ONE}, etc.
     * To preserve the singleton pattern across serialization, the class implements the
     * {@code readResolve()} method. This test ensures that when a constant like {@link Weeks#THREE}
     * is serialized and then deserialized, the resulting object is the *same instance* as the
     * original, not merely an equal one. This is confirmed using {@code assertSame}.
     */
    @Test
    public void serializationOfConstant_shouldReturnSameInstance() throws IOException, ClassNotFoundException {
        // Arrange: Use a pre-defined singleton constant from the Weeks class.
        final Weeks originalInstance = Weeks.THREE;

        // Act: Serialize the constant to a byte array.
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream)) {
            objectOutputStream.writeObject(originalInstance);
        }
        byte[] serializedBytes = byteStream.toByteArray();

        // Act: Deserialize the byte array back into an object.
        Weeks deserializedInstance;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedBytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            deserializedInstance = (Weeks) objectInputStream.readObject();
        }

        // Assert: The deserialized object should be the exact same instance as the original.
        assertSame(
            "Deserialization of a Weeks constant should return the canonical singleton instance.",
            originalInstance,
            deserializedInstance
        );
    }
}