package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.Locale;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.SerializationUtils}.
 */
public class SerializationUtilsTest {

    /**
     * Tests that an object is equivalent to the original after a serialization
     * and deserialization round trip. This verifies the basic functionality of
     * the serialize() and deserialize() methods.
     */
    @Test
    public void testSerializationRoundTrip() {
        // Arrange: Create a serializable object to be tested.
        // Locale is a standard, simple serializable class suitable for this test.
        final Locale originalLocale = Locale.PRC;

        // Act: Serialize the object to a byte array, then deserialize it back into a new object.
        final byte[] serializedData = SerializationUtils.serialize(originalLocale);
        final Locale deserializedLocale = SerializationUtils.deserialize(serializedData);

        // Assert: The deserialized object should be equal to the original,
        // but it should be a different instance in memory.
        assertEquals(originalLocale, deserializedLocale);
        assertNotSame(originalLocale, deserializedLocale);
    }
}