package org.apache.commons.lang3;

import org.junit.Test;
import java.io.OutputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for {@link org.apache.commons.lang3.SerializationUtils}.
 */
public class SerializationUtilsTest {

    /**
     * Tests that calling serialize() with a null OutputStream throws a NullPointerException,
     * as per the method's contract.
     */
    @Test
    public void serializeToNullOutputStreamThrowsNullPointerException() {
        // Arrange: Create a sample serializable object. The specific object doesn't matter.
        final Integer objectToSerialize = 1;

        // Act & Assert: Call the method with a null output stream and verify that
        // a NullPointerException is thrown.
        final NullPointerException exception = assertThrows(
            NullPointerException.class,
            () -> SerializationUtils.serialize(objectToSerialize, (OutputStream) null)
        );

        // Further Assert: Check the exception message for more precise validation.
        // The method is expected to use Objects.requireNonNull, which uses the
        // parameter name as the message.
        assertEquals("outputStream", exception.getMessage());
    }
}