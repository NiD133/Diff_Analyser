package org.joda.time;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Tests the serialization of {@link DateTimeComparator}.
 * <p>
 * This class has been refactored to focus solely on its single responsibility,
 * removing extensive unused code from the original version.
 */
public class DateTimeComparatorSerializationTest extends TestCase {

    /**
     * Tests that a DateTimeComparator instance with specific field limits is serializable.
     * The test verifies that a deserialized comparator is equal to the original.
     */
    public void testSerialization() throws Exception {
        // Arrange: Create a comparator with a lower and upper limit.
        DateTimeComparator originalComparator = DateTimeComparator.getInstance(
                DateTimeFieldType.hourOfDay(), DateTimeFieldType.dayOfYear());

        // Act: Serialize and then deserialize the comparator.
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(originalComparator);
        }

        byte[] serializedData = byteStream.toByteArray();
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(serializedData);
        DateTimeComparator deserializedComparator;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream)) {
            deserializedComparator = (DateTimeComparator) objectInputStream.readObject();
        }

        // Assert: The deserialized comparator should be equal to the original.
        assertEquals(originalComparator, deserializedComparator);
    }
}