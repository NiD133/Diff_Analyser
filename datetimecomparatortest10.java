package org.joda.time;

import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.Test;

/**
 * Tests the serialization of the {@link DateTimeComparator} class.
 *
 * <p>This test focuses on verifying that the singleton pattern for standard
 * comparators is maintained across serialization and deserialization.
 */
public class DateTimeComparatorSerializationTest {

    /**
     * Verifies that serializing and then deserializing the default comparator
     * returns the same singleton instance, as is expected from the
     * {@code readResolve()} method.
     */
    @Test
    public void testSerializationOfDefaultInstancePreservesSingleton() throws IOException, ClassNotFoundException {
        // Arrange: Get the singleton instance of the default comparator.
        DateTimeComparator originalComparator = DateTimeComparator.getInstance();

        // Act: Serialize the comparator to a byte array.
        byte[] serializedBytes;
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(originalComparator);
            serializedBytes = byteStream.toByteArray();
        }

        // Act: Deserialize the comparator from the byte array.
        DateTimeComparator deserializedComparator;
        try (ByteArrayInputStream byteInputStream = new ByteArrayInputStream(serializedBytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream)) {
            deserializedComparator = (DateTimeComparator) objectInputStream.readObject();
        }

        // Assert: The deserialized object should be the exact same instance as the original,
        // confirming that the singleton pattern is correctly handled.
        assertSame("Deserialization should return the singleton instance", originalComparator, deserializedComparator);
    }
}