package com.itextpdf.text.pdf;

import com.itextpdf.text.io.RandomAccessSourceFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Readability-focused tests for RandomAccessFileOrArray.
 *
 * Test data:
 * - A deterministic byte array of length TEST_DATA_LENGTH where value[i] == (byte) i.
 *
 * What we verify:
 * - Sequential reads return the same bytes as the backing array.
 * - Seeking moves the internal pointer to the requested offset.
 * - Pushing back a byte affects the next read and updates the file pointer accordingly.
 */
public class RandomAccessFileOrArrayTest {

    private static final int TEST_DATA_LENGTH = 10_000;
    private static final int SAMPLE_SEEK_OFFSET = 72;

    private byte[] data;
    private RandomAccessFileOrArray randomAccess;

    @Before
    public void setUp() {
        // Build a simple, predictable byte array: value[i] == (byte) i
        data = new byte[TEST_DATA_LENGTH];
        for (int i = 0; i < TEST_DATA_LENGTH; i++) {
            data[i] = (byte) i;
        }
        randomAccess = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(data));
    }

    @Test
    public void sequentialRead_returnsAllBytesInOrder() throws Exception {
        for (int i = 0; i < data.length; i++) {
            byte actual = (byte) randomAccess.read();
            assertEquals("Byte mismatch at index " + i, data[i], actual);
        }
    }

    @Test
    public void seek_movesPointerToRequestedOffset() throws Exception {
        // Arrange
        RandomAccessFileOrArray ra = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(data));

        // Act
        ra.seek(SAMPLE_SEEK_OFFSET);

        // Assert
        for (int i = SAMPLE_SEEK_OFFSET; i < data.length; i++) {
            byte actual = (byte) ra.read();
            assertEquals("Byte mismatch after seek at index " + i, data[i], actual);
        }
    }

    @Test
    public void pushBack_nextReadReturnsPushedValue_thenContinuesWithUnderlyingStream() throws Exception {
        // Arrange: consume first two bytes
        assertEquals(data[0], (byte) randomAccess.read());
        assertEquals(data[1], (byte) randomAccess.read());

        // Act: push back a custom value (not necessarily equal to any next byte)
        byte pushed = (byte) (data[1] + 42);
        randomAccess.pushBack(pushed);

        // Assert: first read returns the pushed value, then resumes with underlying data
        assertEquals("First read should return pushed-back byte", pushed, (byte) randomAccess.read());
        assertEquals("Stream should continue from original position", data[2], (byte) randomAccess.read());
        assertEquals(data[3], (byte) randomAccess.read());
    }

    @Test
    public void getFilePointer_reflectsPushback() throws Exception {
        // Arrange
        RandomAccessFileOrArray ra = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(data));
        long offset = SAMPLE_SEEK_OFFSET;

        // Act
        ra.seek(offset);
        long positionAfterSeek = ra.getFilePointer();

        // Push back a byte; file pointer should move one position back
        ra.pushBack((byte) 42);
        long positionAfterPushback = ra.getFilePointer();

        // Assert
        assertEquals("File pointer should equal seek offset", offset, positionAfterSeek);
        assertEquals("File pointer should move back by one after pushBack()", offset - 1, positionAfterPushback);
    }
}