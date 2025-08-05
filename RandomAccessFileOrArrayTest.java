package com.itextpdf.text.pdf;

import java.io.ByteArrayOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

/**
 * Test suite for the RandomAccessFileOrArray class.
 */
public class RandomAccessFileOrArrayTest {

    private static final int DATA_SIZE = 10000;
    private byte[] testData;
    private RandomAccessFileOrArray randomAccessFileOrArray;

    /**
     * Sets up the test environment by initializing test data and the RandomAccessFileOrArray instance.
     */
    @Before
    public void setUp() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int i = 0; i < DATA_SIZE; i++) {
            outputStream.write(i);
        }
        testData = outputStream.toByteArray();
        randomAccessFileOrArray = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(testData));
    }

    /**
     * Cleans up resources after each test.
     */
    @After
    public void tearDown() throws Exception {
        randomAccessFileOrArray.close();
    }

    /**
     * Tests the pushBack functionality by reading bytes, pushing a byte back, and verifying the sequence.
     */
    @Test
    public void testPushback_byteByByte() throws Exception {
        assertEquals("First byte should match", testData[0], (byte) randomAccessFileOrArray.read());
        assertEquals("Second byte should match", testData[1], (byte) randomAccessFileOrArray.read());

        byte pushBackValue = (byte) (testData[1] + 42);
        randomAccessFileOrArray.pushBack(pushBackValue);

        assertEquals("Pushed back byte should be read next", pushBackValue, (byte) randomAccessFileOrArray.read());
        assertEquals("Third byte should match", testData[2], (byte) randomAccessFileOrArray.read());
        assertEquals("Fourth byte should match", testData[3], (byte) randomAccessFileOrArray.read());
    }

    /**
     * Tests reading all bytes sequentially from the RandomAccessFileOrArray.
     */
    @Test
    public void testReadSequentially() throws Exception {
        for (int i = 0; i < testData.length; i++) {
            assertEquals("Byte at position " + i + " should match", testData[i], (byte) randomAccessFileOrArray.read());
        }
    }

    /**
     * Tests seeking to a specific position and reading from there.
     */
    @Test
    public void testSeekAndRead() throws Exception {
        int seekPosition = 72;
        randomAccessFileOrArray.seek(seekPosition);

        for (int i = seekPosition; i < testData.length; i++) {
            assertEquals("Byte at position " + i + " should match after seek", testData[i], (byte) randomAccessFileOrArray.read());
        }
    }

    /**
     * Tests the file pointer position after a pushback operation.
     */
    @Test
    public void testFilePositionWithPushback() throws Exception {
        int initialPosition = 72;
        randomAccessFileOrArray.seek(initialPosition);
        assertEquals("File pointer should be at initial position", initialPosition, randomAccessFileOrArray.getFilePointer());

        byte pushBackValue = 42;
        randomAccessFileOrArray.pushBack(pushBackValue);
        assertEquals("File pointer should be decremented by one after pushback", initialPosition - 1, randomAccessFileOrArray.getFilePointer());
    }
}