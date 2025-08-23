package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readDouble() correctly interprets a byte array of all zeros as the double value 0.0.
     * <p>
     * According to the IEEE 754 standard for floating-point arithmetic, a 64-bit (8-byte)
     * representation with all bits set to zero corresponds to the value 0.0. This test ensures
     * that {@code readDouble} correctly handles this fundamental case.
     * </p>
     */
    @Test
    public void readDouble_givenAllZeroBytes_shouldReturnZero() throws IOException {
        // Arrange: Create a source containing 8 zero-bytes. A double is 8 bytes long.
        // A new byte array in Java is automatically initialized with zeros.
        byte[] allZeroBytes = new byte[8];
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(allZeroBytes);

        // Act: Read a double value from the beginning of the source.
        double actualValue = reader.readDouble();

        // Assert: The read value should be 0.0.
        double expectedValue = 0.0;
        assertEquals("Reading 8 zero-bytes should be interpreted as the double value 0.0",
                expectedValue, actualValue, 0.0);
    }
}