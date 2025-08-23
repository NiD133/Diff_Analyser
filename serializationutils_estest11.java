package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.SerializationUtils}.
 */
public class SerializationUtilsTest {

    /**
     * Tests that calling {@link SerializationUtils#deserialize(byte[])} with a null
     * input array throws a NullPointerException.
     */
    @Test
    public void deserializeWithNullByteArrayShouldThrowNullPointerException() {
        try {
            SerializationUtils.deserialize((byte[]) null);
            fail("Expected a NullPointerException to be thrown for null input.");
        } catch (final NullPointerException e) {
            // The method's Javadoc guarantees a NullPointerException for null input.
            // We can also assert on the message for a more specific test, as the
            // underlying implementation uses Objects.requireNonNull with a message.
            assertEquals("objectData", e.getMessage());
        }
    }
}