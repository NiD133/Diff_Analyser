package org.apache.commons.io.output;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.function.IOFunction;

/**
 * Test suite for ThresholdingOutputStream functionality.
 * Tests the core behavior of threshold detection and byte counting.
 */
public class ThresholdingOutputStream_ESTest {

    private static final int DEFAULT_THRESHOLD = 100;
    private static final int ZERO_THRESHOLD = 0;
    private static final int LARGE_THRESHOLD = 1000;

    // ========== Constructor and Basic Properties Tests ==========

    @Test
    public void shouldCreateStreamWithPositiveThreshold() {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(DEFAULT_THRESHOLD);
        
        assertEquals("Threshold should match constructor parameter", DEFAULT_THRESHOLD, stream.getThreshold());
        assertEquals("Initial byte count should be zero", 0L, stream.getByteCount());
        assertFalse("Threshold should not be exceeded initially", stream.isThresholdExceeded());
    }

    @Test
    public void shouldNormalizeNegativeThresholdToZero() {
        int negativeThreshold = -50;
        ThresholdingOutputStream stream = new ThresholdingOutputStream(negativeThreshold);
        
        assertEquals("Negative threshold should be normalized to zero", ZERO_THRESHOLD, stream.getThreshold());
    }

    @Test
    public void shouldCreateStreamWithCustomConsumerAndFunction() {
        IOConsumer<ThresholdingOutputStream> consumer = stream -> { /* no-op */ };
        IOFunction<ThresholdingOutputStream, OutputStream> function = stream -> null;
        
        ThresholdingOutputStream stream = new ThresholdingOutputStream(DEFAULT_THRESHOLD, consumer, function);
        
        assertEquals("Threshold should be set correctly with custom parameters", DEFAULT_THRESHOLD, stream.getThreshold());
    }

    // ========== Threshold Detection Tests ==========

    @Test
    public void shouldNotExceedThresholdWhenBelowLimit() throws IOException {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(DEFAULT_THRESHOLD);
        byte[] smallData = new byte[DEFAULT_THRESHOLD - 1];
        
        stream.write(smallData);
        
        assertFalse("Threshold should not be exceeded when below limit", stream.isThresholdExceeded());
        assertEquals("Byte count should match written data", smallData.length, stream.getByteCount());
    }

    @Test
    public void shouldExceedThresholdWhenAboveLimit() throws IOException {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(DEFAULT_THRESHOLD);
        byte[] largeData = new byte[DEFAULT_THRESHOLD + 1];
        
        stream.write(largeData);
        
        assertTrue("Threshold should be exceeded when above limit", stream.isThresholdExceeded());
        assertEquals("Byte count should match written data", largeData.length, stream.getByteCount());
    }

    @Test
    public void shouldExceedZeroThresholdImmediately() throws IOException {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(ZERO_THRESHOLD);
        
        stream.write(42); // Write single byte
        
        assertTrue("Zero threshold should be exceeded after any write", stream.isThresholdExceeded());
        assertEquals("Byte count should be 1", 1L, stream.getByteCount());
    }

    // ========== Write Operations Tests ==========

    @Test
    public void shouldWriteSingleByte() throws IOException {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(LARGE_THRESHOLD);
        int testByte = 123;
        
        stream.write(testByte);
        
        assertEquals("Byte count should increment by 1", 1L, stream.getByteCount());
        assertFalse("Threshold should not be exceeded", stream.isThresholdExceeded());
    }

    @Test
    public void shouldWriteByteArray() throws IOException {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(LARGE_THRESHOLD);
        byte[] data = {1, 2, 3, 4, 5};
        
        stream.write(data);
        
        assertEquals("Byte count should match array length", data.length, stream.getByteCount());
    }

    @Test
    public void shouldWriteByteArrayWithOffsetAndLength() throws IOException {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(LARGE_THRESHOLD);
        byte[] data = {1, 2, 3, 4, 5, 6, 7, 8};
        int offset = 2;
        int length = 3;
        
        stream.write(data, offset, length);
        
        assertEquals("Byte count should match specified length", length, stream.getByteCount());
    }

    @Test
    public void shouldHandleZeroLengthWrite() throws IOException {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(DEFAULT_THRESHOLD);
        byte[] data = {1, 2, 3};
        
        stream.write(data, 0, 0); // Write zero bytes
        
        assertEquals("Byte count should remain zero", 0L, stream.getByteCount());
        assertFalse("Threshold should not be exceeded", stream.isThresholdExceeded());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionForNullByteArray() throws IOException {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(DEFAULT_THRESHOLD);
        
        stream.write(null);
    }

    // ========== Byte Count Management Tests ==========

    @Test
    public void shouldResetByteCount() throws IOException {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(LARGE_THRESHOLD);
        stream.write(new byte[50]);
        
        stream.resetByteCount();
        
        assertEquals("Byte count should be reset to zero", 0L, stream.getByteCount());
    }

    @Test
    public void shouldSetByteCount() {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(LARGE_THRESHOLD);
        long newCount = 42L;
        
        stream.setByteCount(newCount);
        
        assertEquals("Byte count should be set to specified value", newCount, stream.getByteCount());
    }

    // ========== Stream Operations Tests ==========

    @Test
    public void shouldFlushWithoutError() throws IOException {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(DEFAULT_THRESHOLD);
        
        stream.flush(); // Should not throw exception
        
        assertEquals("Flush should not affect byte count", 0L, stream.getByteCount());
    }

    @Test
    public void shouldCloseWithoutError() throws IOException {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(DEFAULT_THRESHOLD);
        
        stream.close(); // Should not throw exception
        
        assertEquals("Close should not affect threshold", DEFAULT_THRESHOLD, stream.getThreshold());
    }

    @Test
    public void shouldCallThresholdReachedWithoutError() throws IOException {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(DEFAULT_THRESHOLD);
        
        stream.thresholdReached(); // Should not throw exception for base implementation
    }

    @Test
    public void shouldGetOutputStreamWithoutError() throws IOException {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(DEFAULT_THRESHOLD);
        
        OutputStream outputStream = stream.getOutputStream();
        
        assertNotNull("Output stream should not be null", outputStream);
    }

    @Test
    public void shouldGetStreamWithoutError() throws IOException {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(DEFAULT_THRESHOLD);
        
        OutputStream outputStream = stream.getStream(); // Deprecated method
        
        assertNotNull("Stream should not be null", outputStream);
    }

    // ========== Edge Cases and Error Conditions ==========

    @Test
    public void shouldHandleNegativeLengthWrite() throws IOException {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(DEFAULT_THRESHOLD);
        byte[] data = {1, 2, 3};
        
        // This tests the behavior with negative length - implementation dependent
        stream.write(data, 0, -5);
        
        // The actual behavior depends on implementation, but should not crash
        assertTrue("Method should complete without throwing exception", true);
    }

    @Test
    public void shouldAccumulateByteCountAcrossMultipleWrites() throws IOException {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(LARGE_THRESHOLD);
        byte[] chunk1 = new byte[30];
        byte[] chunk2 = new byte[40];
        
        stream.write(chunk1);
        stream.write(chunk2);
        
        long expectedTotal = chunk1.length + chunk2.length;
        assertEquals("Byte count should accumulate across writes", expectedTotal, stream.getByteCount());
    }

    @Test
    public void shouldCheckThresholdWithoutError() throws IOException {
        ThresholdingOutputStream stream = new ThresholdingOutputStream(DEFAULT_THRESHOLD);
        
        stream.checkThreshold(50); // Should not throw exception
        
        assertEquals("Check threshold should not affect byte count", 0L, stream.getByteCount());
    }
}