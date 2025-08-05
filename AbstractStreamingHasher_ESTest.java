package com.google.common.hash;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import com.google.common.hash.Crc32cHashFunction;
import com.google.common.hash.Hasher;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Tests for AbstractStreamingHasher functionality using Crc32cHasher as the concrete implementation.
 * Tests cover normal operations, error conditions, and state management.
 */
public class AbstractStreamingHasherTest {

    private Crc32cHashFunction.Crc32cHasher hasher;

    @Before
    public void setUp() {
        hasher = new Crc32cHashFunction.Crc32cHasher();
    }

    // ========== Normal Operation Tests ==========

    @Test
    public void testPutByte_ReturnsHasherInstance() {
        Hasher result = hasher.putByte((byte) -115);
        
        assertSame("putByte should return the same hasher instance", hasher, result);
    }

    @Test
    public void testPutBytes_WithByteArray_ReturnsHasherInstance() {
        byte[] data = new byte[2];
        
        Hasher result = hasher.putBytes(data, 1, 1);
        
        assertSame("putBytes should return the same hasher instance", hasher, result);
    }

    @Test
    public void testPutBytes_WithByteBuffer_ReturnsHasherInstance() {
        ByteBuffer buffer = ByteBuffer.allocate(23);
        
        Hasher result = hasher.putBytes(buffer);
        
        assertSame("putBytes should return the same hasher instance", hasher, result);
    }

    @Test
    public void testPutBytes_WithEncodedString_ReturnsHasherInstance() {
        Charset charset = Charset.defaultCharset();
        ByteBuffer buffer = charset.encode("test string");
        buffer.getShort(); // Modify buffer position
        
        Hasher result = hasher.putBytes(buffer);
        
        assertSame("putBytes should return the same hasher instance", hasher, result);
    }

    @Test
    public void testProcessRemaining_WithLargeBuffer_CompletesSuccessfully() {
        ByteBuffer largeBuffer = ByteBuffer.allocate(1317358741);
        
        // Should complete without throwing exception
        hasher.processRemaining(largeBuffer);
    }

    @Test
    public void testPutBytes_WithLargeDirectBuffer_CompletesSuccessfully() {
        ByteBuffer largeBuffer = ByteBuffer.allocateDirect(1905808397);
        
        // Should complete without throwing exception (may take time/memory)
        hasher.putBytes(largeBuffer);
    }

    // ========== Null Input Tests ==========

    @Test(expected = NullPointerException.class)
    public void testPutBytes_WithNullByteArray_ThrowsNullPointerException() {
        hasher.putBytes(null, 703, 703);
    }

    @Test(expected = NullPointerException.class)
    public void testPutBytes_WithNullByteBuffer_ThrowsNullPointerException() {
        hasher.putBytes((ByteBuffer) null);
    }

    @Test(expected = NullPointerException.class)
    public void testProcessRemaining_WithNullBuffer_ThrowsNullPointerException() {
        hasher.processRemaining(null);
    }

    // ========== Boundary Condition Tests ==========

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPutBytes_WithInvalidLength_ThrowsIndexOutOfBoundsException() {
        byte[] smallArray = new byte[3];
        
        hasher.putBytes(smallArray, 0, 67); // Length exceeds array size
    }

    // ========== State Management Tests ==========

    @Test(expected = BufferOverflowException.class)
    public void testPutShort_AfterHashCalled_ThrowsBufferOverflowException() {
        hasher.hash(); // Finalize the hasher
        
        hasher.putShort((short) 24366);
    }

    @Test(expected = BufferOverflowException.class)
    public void testPutLong_AfterHashCalled_ThrowsBufferOverflowException() {
        hasher.hash(); // Finalize the hasher
        
        hasher.putLong(-1479L);
    }

    @Test(expected = BufferOverflowException.class)
    public void testPutInt_AfterHashCalled_ThrowsBufferOverflowException() {
        hasher.hash(); // Finalize the hasher
        
        hasher.putInt(1101871998);
    }

    @Test(expected = BufferOverflowException.class)
    public void testPutChar_AfterHashCalled_ThrowsBufferOverflowException() {
        hasher.hash(); // Finalize the hasher
        
        hasher.putChar('7');
    }

    @Test(expected = BufferOverflowException.class)
    public void testPutByte_AfterHashCalled_ThrowsBufferOverflowException() {
        hasher.hash(); // Finalize the hasher
        
        hasher.putByte((byte) -115);
    }

    @Test(expected = BufferOverflowException.class)
    public void testPutBytes_WithByteArray_AfterHashCalled_ThrowsBufferOverflowException() {
        hasher.hash(); // Finalize the hasher
        byte[] data = new byte[2];
        
        hasher.putBytes(data, 1, 1);
    }

    @Test(expected = BufferOverflowException.class)
    public void testPutBytes_WithByteBuffer_AfterHashCalled_ThrowsBufferOverflowException() {
        hasher.hash(); // Finalize the hasher
        ByteBuffer buffer = ByteBuffer.allocate(80);
        
        hasher.putBytes(buffer);
    }

    // ========== IllegalStateException Tests (after processRemaining) ==========

    @Test(expected = IllegalStateException.class)
    public void testPutShort_AfterProcessRemaining_ThrowsIllegalStateException() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(2479);
        hasher.putBytes(buffer);
        hasher.processRemaining(buffer); // This finalizes the hasher
        
        hasher.putShort((short) -648);
    }

    @Test(expected = IllegalStateException.class)
    public void testPutLong_AfterMakeHashAndPutLong_ThrowsIllegalStateException() {
        hasher.makeHash(); // Finalize the hasher
        hasher.putLong(-4265267296055464877L); // This somehow works initially
        
        hasher.putLong(2304L); // But subsequent calls should fail
    }

    @Test(expected = IllegalStateException.class)
    public void testPutInt_AfterMakeHashAndPutBytes_ThrowsIllegalStateException() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(13);
        hasher.putBytes(buffer);
        hasher.makeHash(); // Finalize the hasher
        
        hasher.putInt(13);
    }

    @Test(expected = IllegalStateException.class)
    public void testPutChar_AfterProcessRemaining_ThrowsIllegalStateException() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(2479);
        hasher.putBytes(buffer);
        hasher.processRemaining(buffer); // This finalizes the hasher
        
        hasher.putChar('M');
    }

    @Test(expected = IllegalStateException.class)
    public void testPutBytes_AfterMakeHash_ThrowsIllegalStateException() {
        hasher.makeHash(); // Finalize the hasher
        ByteBuffer buffer = ByteBuffer.allocateDirect(185);
        
        hasher.putBytes(buffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testComplexOperationSequence_AfterHash_ThrowsIllegalStateException() {
        hasher.hash(); // Finalize the hasher
        
        // These operations work on the finalized hasher (unexpected behavior)
        byte[] emptyArray = new byte[0];
        hasher.putBytes(emptyArray);
        hasher.putBoolean(true);
        hasher.putChar('D');
        hasher.putInt(1347);
        hasher.putInt(7);
        hasher.putChar('D');
        hasher.putShort((short) -32506);
        
        // But this final operation should fail
        hasher.putByte((byte) 21);
    }

    @Test(expected = BufferOverflowException.class)
    public void testPutBytes_OnSameInstanceAfterHash_ThrowsBufferOverflowException() {
        Charset charset = Charset.defaultCharset();
        ByteBuffer buffer = charset.encode("test string");
        Crc32cHashFunction.Crc32cHasher sameHasher = (Crc32cHashFunction.Crc32cHasher) hasher.putBytes(buffer);
        
        hasher.hash(); // Finalize the original hasher
        byte[] data = new byte[2];
        
        // Using the same reference should fail
        sameHasher.putBytes(data, 1, 1);
    }
}