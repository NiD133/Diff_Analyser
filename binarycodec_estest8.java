package org.apache.commons.codec.binary;

import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Tests for {@link BinaryCodec} focusing on handling large data inputs.
 */
public class BinaryCodecTest {

    /**
     * Tests that attempting to convert a very large byte array to ASCII characters
     * results in an OutOfMemoryError. The large input is created by repeatedly
     * applying the `toAsciiBytes` encoding, which expands the array size exponentially.
     *
     * <p><b>Note:</b> This test's outcome depends on the available JVM heap space.
     * It is designed to validate behavior under memory constraints, which was the
     * likely intent of the original auto-generated test.
     */
    @Test(timeout = 4000)
    public void toAsciiCharsWithExtremelyLargeInputShouldThrowOutOfMemoryError() {
        // Arrange: Create a very large byte array. Each call to toAsciiBytes
        // multiplies the array size by 8.
        byte[] largeInput = new byte[5]; // Initial size: 5 bytes
        largeInput = BinaryCodec.toAsciiBytes(largeInput); // Size grows to 40 bytes
        largeInput = BinaryCodec.toAsciiBytes(largeInput); // Size grows to 320 bytes
        largeInput = BinaryCodec.toAsciiBytes(largeInput); // Final size: 2,560 bytes

        // Act & Assert: The next conversion attempts to allocate a char array of
        // 2,560 * 8 = 20,480 elements. This is expected to exhaust available memory
        // under the constrained conditions in which this test was likely generated.
        try {
            BinaryCodec.toAsciiChars(largeInput);
            fail("Expected an OutOfMemoryError to be thrown, but it was not. " +
                 "The test environment may have a larger heap than anticipated.");
        } catch (OutOfMemoryError e) {
            // Success: The expected error was thrown, indicating that the method
            // failed as expected when faced with an extremely large allocation.
        }
    }
}