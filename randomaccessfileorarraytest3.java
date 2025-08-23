package com.itextpdf.text.pdf;

import com.itextpdf.text.io.RandomAccessSourceFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the seek functionality of {@link RandomAccessFileOrArray}.
 */
// 1. Renamed class for clarity. "TestTest" is redundant, and the new name
//    focuses on the functionality being tested.
public class RandomAccessFileOrArraySeekTest {

    // 2. Use constants for magic numbers to improve readability and maintainability.
    private static final int DATA_SIZE = 10000;
    private static final int SEEK_POSITION = 72;

    private byte[] testData;
    // 3. Renamed 'rafoa' to a more descriptive name that reflects the class.
    private RandomAccessFileOrArray randomAccessData;

    @Before
    public void setUp() throws IOException {
        // The test data consists of bytes with values 0, 1, 2, ..., 255, 0, 1, ...
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(DATA_SIZE);
        for (int i = 0; i < DATA_SIZE; i++) {
            // The write(int) method writes the low-order byte of the integer.
            outputStream.write(i);
        }
        testData = outputStream.toByteArray();

        randomAccessData = new RandomAccessFileOrArray(
                new RandomAccessSourceFactory().createSource(testData));
    }

    // 4. The original tearDown() was empty and has been removed for conciseness.

    /**
     * Verifies that after seeking to a specific position, the internal file pointer
     * is updated, and subsequent reads start correctly from that new position.
     */
    @Test
    // 5. Renamed test method to follow the "when-then" convention for clarity.
    //    It clearly states the action and the expected outcome.
    public void seek_whenPositionIsSet_thenReadsFromThatPosition() throws IOException {
        // Arrange
        // 6. Use the Arrange-Act-Assert pattern for a clear test structure.
        //    The setup is done in the @Before method. We define our expectations here.
        int remainingByteCount = DATA_SIZE - SEEK_POSITION;
        byte[] expectedBytes = Arrays.copyOfRange(testData, SEEK_POSITION, DATA_SIZE);
        byte[] actualBytes = new byte[remainingByteCount];

        // Act
        randomAccessData.seek(SEEK_POSITION);

        // Assert: First, verify the direct effect of seek() on the file pointer.
        // 7. This assertion directly confirms the state change caused by the 'seek' operation.
        assertEquals("File pointer should be updated to the seek position.",
                SEEK_POSITION, randomAccessData.getFilePointer());

        // Act (continued): Now, perform the read operation from the new position.
        int bytesRead = randomAccessData.read(actualBytes);

        // Assert: Then, verify the result of the subsequent read operation.
        // 8. Assertions are more comprehensive and expressive, checking both the
        //    number of bytes read and their content in a single, clear block.
        assertEquals("Number of bytes read should match the remaining data size.",
                remainingByteCount, bytesRead);
        assertArrayEquals("Data read after seek should match the expected byte sequence.",
                expectedBytes, actualBytes);
    }
}