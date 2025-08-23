package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class SeekableInMemoryByteChannel_ESTest extends SeekableInMemoryByteChannel_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testWriteEmptyBuffer() throws Throwable {
        byte[] byteArray = new byte[5];
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(byteArray);
        ByteBuffer buffer = ByteBuffer.allocate(0);
        
        int bytesWritten = channel.write(buffer);
        
        assertEquals(0, bytesWritten);
        assertEquals(0L, channel.position());
        assertEquals(5L, channel.size());
    }

    @Test(timeout = 4000)
    public void testTruncateToCurrentSize() throws Throwable {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(122);
        SeekableByteChannel truncatedChannel = channel.truncate(122);
        
        assertEquals(0L, truncatedChannel.position());
    }

    @Test(timeout = 4000)
    public void testTruncateBeyondCurrentSize() throws Throwable {
        byte[] byteArray = new byte[3];
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(byteArray);
        SeekableByteChannel truncatedChannel = channel.truncate(Integer.MAX_VALUE);
        
        assertEquals(3L, truncatedChannel.size());
        assertEquals(0L, truncatedChannel.position());
    }

    @Test(timeout = 4000)
    public void testWriteBeyondCapacity() throws Throwable {
        byte[] byteArray = new byte[1];
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(byteArray);
        ByteBuffer buffer = ByteBuffer.allocate(86);
        
        int bytesWritten = channel.write(buffer);
        
        assertEquals("java.nio.HeapByteBuffer[pos=86 lim=86 cap=86]", buffer.toString());
        assertEquals(86, bytesWritten);
    }

    @Test(timeout = 4000)
    public void testReadFromPositionBeyondSize() throws Throwable {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1098);
        
        channel.position(Integer.MAX_VALUE);
        int bytesRead = channel.read(buffer);
        
        assertEquals(-1, bytesRead);
    }

    @Test(timeout = 4000)
    public void testReadEmptyBufferAtNonZeroPosition() throws Throwable {
        byte[] byteArray = new byte[5];
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(byteArray);
        channel.position(1L);
        ByteBuffer buffer = ByteBuffer.allocate(0);
        
        int bytesRead = channel.read(buffer);
        
        assertEquals(1L, channel.position());
        assertEquals(0, bytesRead);
    }

    @Test(timeout = 4000)
    public void testPositionAtZero() throws Throwable {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        SeekableByteChannel positionedChannel = channel.position(0L);
        
        assertEquals(0L, positionedChannel.size());
    }

    @Test(timeout = 4000)
    public void testWriteAndReadDirectBuffer() throws Throwable {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(1);
        ByteBuffer buffer = ByteBuffer.allocateDirect(1);
        
        channel.read(buffer);
        int bytesWritten = channel.write(buffer);
        
        assertEquals(1L, channel.position());
        assertEquals(0, bytesWritten);
    }

    @Test(timeout = 4000)
    public void testCloseAndTruncate() throws Throwable {
        byte[] byteArray = new byte[0];
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(byteArray);
        
        channel.close();
        channel.truncate(6051L);
        
        assertFalse(channel.isOpen());
    }

    @Test(timeout = 4000)
    public void testSizeAfterConstruction() throws Throwable {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(1);
        
        long size = channel.size();
        
        assertEquals(1L, size);
    }

    @Test(timeout = 4000)
    public void testPositionAfterRead() throws Throwable {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(1);
        byte[] byteArray = new byte[3];
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        
        channel.read(buffer);
        long position = channel.position();
        
        assertEquals(1L, position);
    }

    @Test(timeout = 4000)
    public void testArrayContentAfterConstruction() throws Throwable {
        byte[] byteArray = new byte[8];
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(byteArray);
        
        byte[] internalArray = channel.array();
        
        assertArrayEquals(new byte[] {0, 0, 0, 0, 0, 0, 0, 0}, internalArray);
    }

    @Test(timeout = 4000)
    public void testWriteNullBufferThrowsException() throws Throwable {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(958);
        
        try {
            channel.write(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.utils.SeekableInMemoryByteChannel", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadAfterCloseThrowsException() throws Throwable {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        channel.close();
        ByteBuffer buffer = ByteBuffer.allocateDirect(1169);
        
        try {
            channel.read(buffer);
            fail("Expecting exception: ClosedChannelException");
        } catch (ClosedChannelException e) {
            verifyException("org.apache.commons.compress.utils.SeekableInMemoryByteChannel", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadNullBufferThrowsException() throws Throwable {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        
        try {
            channel.read(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.utils.SeekableInMemoryByteChannel", e);
        }
    }

    @Test(timeout = 4000)
    public void testPositionAfterCloseThrowsException() throws Throwable {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        channel.close();
        
        try {
            channel.position(1073741828L);
            fail("Expecting exception: ClosedChannelException");
        } catch (ClosedChannelException e) {
            verifyException("org.apache.commons.compress.utils.SeekableInMemoryByteChannel", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullArrayThrowsException() throws Throwable {
        try {
            new SeekableInMemoryByteChannel((byte[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.utils.SeekableInMemoryByteChannel", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNegativeSizeThrowsException() throws Throwable {
        try {
            new SeekableInMemoryByteChannel(-1);
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            verifyException("org.apache.commons.compress.utils.SeekableInMemoryByteChannel", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsOpenAfterClose() throws Throwable {
        byte[] byteArray = new byte[1];
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(byteArray);
        
        channel.close();
        boolean isOpen = channel.isOpen();
        
        assertFalse(isOpen);
    }

    @Test(timeout = 4000)
    public void testIsOpenAfterConstruction() throws Throwable {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(1);
        
        boolean isOpen = channel.isOpen();
        
        assertTrue(isOpen);
    }

    @Test(timeout = 4000)
    public void testTruncateToZero() throws Throwable {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(1);
        ByteBuffer buffer = ByteBuffer.allocateDirect(1);
        
        channel.read(buffer);
        channel.truncate(0);
        
        assertEquals(0L, channel.size());
    }

    @Test(timeout = 4000)
    public void testTruncateWithLargeSizeThrowsException() throws Throwable {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        
        try {
            channel.truncate(2147483648L);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.compress.utils.SeekableInMemoryByteChannel", e);
        }
    }

    @Test(timeout = 4000)
    public void testTruncateWithNegativeSizeThrowsException() throws Throwable {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        
        try {
            channel.truncate(-469L);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.compress.utils.SeekableInMemoryByteChannel", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadFromEmptyChannel() throws Throwable {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1098);
        
        int bytesRead = channel.read(buffer);
        
        assertEquals(-1, bytesRead);
    }

    @Test(timeout = 4000)
    public void testWriteAndClearDirectBuffer() throws Throwable {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(1);
        ByteBuffer buffer = ByteBuffer.allocateDirect(1);
        
        channel.read(buffer);
        buffer.clear();
        int bytesWritten = channel.write(buffer);
        
        assertEquals(0, buffer.remaining());
        assertEquals(1, bytesWritten);
    }

    @Test(timeout = 4000)
    public void testPositionWithLargeValueThrowsException() throws Throwable {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        
        try {
            channel.position(2147483669L);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.compress.utils.SeekableInMemoryByteChannel", e);
        }
    }

    @Test(timeout = 4000)
    public void testPositionWithNegativeValueThrowsException() throws Throwable {
        byte[] byteArray = new byte[2];
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(byteArray);
        
        try {
            channel.position(-429L);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.compress.utils.SeekableInMemoryByteChannel", e);
        }
    }

    @Test(timeout = 4000)
    public void testWriteLargeDirectBuffer() throws Throwable {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        ByteBuffer buffer = ByteBuffer.allocateDirect(1073741823);
        
        int bytesWritten = channel.write(buffer);
        
        assertEquals(1073741823, buffer.position());
        assertEquals(1073741823, bytesWritten);
    }

    @Test(timeout = 4000)
    public void testArrayContentAfterEmptyConstruction() throws Throwable {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        
        byte[] internalArray = channel.array();
        
        assertArrayEquals(new byte[] {}, internalArray);
    }

    @Test(timeout = 4000)
    public void testSizeAfterEmptyConstruction() throws Throwable {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        
        long size = channel.size();
        
        assertEquals(0L, size);
    }

    @Test(timeout = 4000)
    public void testWriteAfterCloseThrowsException() throws Throwable {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        channel.close();
        ByteBuffer buffer = ByteBuffer.allocate(1);
        
        try {
            channel.write(buffer);
            fail("Expecting exception: ClosedChannelException");
        } catch (ClosedChannelException e) {
            verifyException("org.apache.commons.compress.utils.SeekableInMemoryByteChannel", e);
        }
    }

    @Test(timeout = 4000)
    public void testPositionAfterEmptyConstruction() throws Throwable {
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        
        long position = channel.position();
        
        assertEquals(0L, position);
    }
}