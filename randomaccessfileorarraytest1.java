package com.itextpdf.text.pdf;

import com.itextpdf.text.io.RandomAccessSourceFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the RandomAccessFileOrArray class.
 */
public class RandomAccessFileOrArrayTest {

    private byte[] testData;
    private RandomAccessFileOrArray randomAccess;

    @Before
    public void setUp() {
        // Use a small, explicit byte array for clarity.
        // The original 10,000-byte array was unnecessarily large for this test's scope.
        testData = new byte[]{10, 20, 30, 40, 50};
        randomAccess = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(testData));
    }

    /**
     * Verifies that pushBack() places a byte at the head of the stream,
     * which is then returned by the next read() call. After that, reading
     * should continue from the original stream position.
     */
    @Test
    public void pushBack_shouldBeReturnedOnNextRead_beforeResumingStream() throws IOException {
        // Arrange: Read the first two bytes to advance the stream's position.
        // This sets up the state for testing the pushBack functionality.
        assertEquals("Pre-condition: First byte should be read correctly.", testData[0], (byte) randomAccess.read());
        assertEquals("Pre-condition: Second byte should be read correctly.", testData[1], (byte) randomAccess.read());

        byte valueToPushBack = 99;

        // Act: Push a new byte back into the stream.
        randomAccess.pushBack(valueToPushBack);

        // Assert: The next read should return the pushed-back byte,
        // and subsequent reads should continue from the original stream position.
        assertEquals("The pushed-back value should be read next.", valueToPushBack, (byte) randomAccess.read());
        assertEquals("Reading should resume from the third byte in the original data.", testData[2], (byte) randomAccess.read());
        assertEquals("Reading should continue sequentially.", testData[3], (byte) randomAccess.read());
    }
}