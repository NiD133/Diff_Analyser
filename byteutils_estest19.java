package org.apache.commons.compress.utils;

import java.io.IOException;
import org.junit.Test;

/**
 * Unit tests for the {@link ByteUtils} class, focusing on exception handling.
 */
public class ByteUtilsTest {

    /**
     * Verifies that {@link ByteUtils#toLittleEndian(ByteUtils.ByteConsumer, long, int)}
     * throws a {@link NullPointerException} when the provided consumer is null.
     * This ensures the method is null-safe with respect to its consumer argument.
     *
     * @throws IOException although not expected in this test, it's declared by the
     *                     method under test and thus required by the compiler.
     */
    @Test(expected = NullPointerException.class)
    public void toLittleEndianWithNullConsumerShouldThrowNullPointerException() throws IOException {
        // Arrange: The arguments for value and length are arbitrary as the method
        // should fail before they are used.
        final long anyValue = 42L;
        final int anyValidLength = 4;

        // Act & Assert: Call the method with a null consumer.
        // The @Test(expected=...) annotation handles the assertion that a
        // NullPointerException is thrown.
        ByteUtils.toLittleEndian(null, anyValue, anyValidLength);
    }
}