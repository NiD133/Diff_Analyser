package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.jupiter.api.Test;

/**
 * Tests the serialization of the {@link DayOfMonth} class.
 */
class DayOfMonthSerializationTest {

    @Test
    void deserializationReturnsCachedInstance() throws IOException, ClassNotFoundException {
        // Arrange: Create a DayOfMonth instance, which should be a cached singleton.
        DayOfMonth original = DayOfMonth.of(15);

        // Act: Serialize the instance to a byte stream.
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(byteStream)) {
            oos.writeObject(original);
        }

        // Act: Deserialize the instance from the byte stream.
        DayOfMonth deserialized;
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()))) {
            deserialized = (DayOfMonth) ois.readObject();
        }

        // Assert: The deserialized object should be the exact same instance as the original,
        // due to the readResolve() method returning the cached instance.
        assertSame(original, deserialized, "Deserialization should return the canonical, cached instance.");
    }
}