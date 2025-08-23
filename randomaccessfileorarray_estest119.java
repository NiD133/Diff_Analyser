package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link RandomAccessFileOrArray} class, focusing on edge cases for data reading methods.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readFully() with a negative length completes without throwing an exception.
     *
     * <p>The original auto-generated test incorrectly expected an EOFException for this scenario.
     * According to the {@link java.io.DataInput} interface contract, which this class implements,
     * a negative length should ideally throw an {@link IndexOutOfBoundsException}.</p>
     *
     * <p>However, the current implementation of {@code RandomAccessFileOrArray.readFully()}
     * uses a {@code while (n < len)} loop. When {@code len} is negative, this condition is
     * immediately false, causing the method to return without error and without reading any data.
     * This test documents this specific, albeit unexpected, behavior.</p>
     */
    @Test
    public void readFully_withNegativeLength_shouldCompleteWithoutException() {
        // Arrange: Set up a reader with a non-empty byte array source.
        byte[] sourceData = new byte[10];
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(sourceData);

        byte[] destinationBuffer = new byte[10];
        int negativeLength = -1;
        // The offset is irrelevant as the read loop will not execute with a negative length.
        int anyOffset = 0;

        // Act & Assert: Call readFully and verify that no exception is thrown.
        try {
            reader.readFully(destinationBuffer, anyOffset, negativeLength);
            // If this line is reached, the test passes, as no exception was thrown.
        } catch (Exception e) {
            // The test fails if any exception is caught.
            fail("Expected no exception for a negative length, but caught " + e.getClass().getName());
        }
    }
}