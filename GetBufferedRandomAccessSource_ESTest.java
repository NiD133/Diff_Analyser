package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.text.io.ArrayRandomAccessSource;
import com.itextpdf.text.io.ByteBufferRandomAccessSource;
import com.itextpdf.text.io.GetBufferedRandomAccessSource;
import com.itextpdf.text.io.MappedChannelRandomAccessSource;
import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.WindowRandomAccessSource;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class GetBufferedRandomAccessSourceTest extends GetBufferedRandomAccessSource_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testGetFromEmptySourceReturnsZero() throws Throwable {
        byte[] emptyArray = new byte[0];
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(emptyArray);
        WindowRandomAccessSource windowSource = new WindowRandomAccessSource(arraySource, -782L, -782L);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(windowSource);
        
        int result = bufferedSource.get(-1L);
        
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testLengthOfNullSourceIsZero() throws Throwable {
        WindowRandomAccessSource windowSource = new WindowRandomAccessSource(null, 0L, 0L);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(windowSource);
        
        long length = bufferedSource.length();
        
        assertEquals(0L, length);
    }

    @Test(timeout = 4000)
    public void testLengthOfNegativeWindowSource() throws Throwable {
        byte[] emptyArray = new byte[0];
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(emptyArray);
        WindowRandomAccessSource windowSource = new WindowRandomAccessSource(arraySource, -782L, -782L);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(windowSource);
        
        long length = bufferedSource.length();
        
        assertEquals(-782L, length);
    }

    @Test(timeout = 4000)
    public void testGetFromNonEmptySource() throws Throwable {
        byte[] dataArray = new byte[16];
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(dataArray);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
        
        int bytesRead = bufferedSource.get(0L, dataArray, 0, 610);
        
        assertEquals(16, bytesRead);
    }

    @Test(timeout = 4000)
    public void testGetWithInvalidParametersReturnsMinusOne() throws Throwable {
        byte[] emptyArray = new byte[0];
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(emptyArray);
        WindowRandomAccessSource windowSource = new WindowRandomAccessSource(arraySource, -782L, -782L);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(windowSource);
        
        int result = bufferedSource.get(0L, emptyArray, 465, -1134);
        
        assertEquals(-1, result);
    }

    @Test(timeout = 4000)
    public void testGetByteValueFromSource() throws Throwable {
        byte[] dataArray = new byte[8];
        dataArray[0] = (byte) -56;
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(dataArray);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
        
        int byteValue = bufferedSource.get(0L);
        
        assertEquals(200, byteValue);
    }

    @Test(timeout = 4000)
    public void testCloseSourceAndAccessLengthThrowsException() throws Throwable {
        byte[] dataArray = new byte[1];
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(dataArray);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
        
        bufferedSource.close();
        
        try {
            bufferedSource.length();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.io.ArrayRandomAccessSource", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetWithNullArrayThrowsException() throws Throwable {
        byte[] dataArray = new byte[4];
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(dataArray);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
        
        try {
            bufferedSource.get(-1780L, null, -213, -213);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetWithInvalidByteBufferThrowsException() throws Throwable {
        byte[] dataArray = new byte[1];
        ByteBuffer byteBuffer = ByteBuffer.wrap(dataArray);
        ByteBufferRandomAccessSource byteBufferSource = new ByteBufferRandomAccessSource(byteBuffer);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(byteBufferSource);
        
        try {
            bufferedSource.get(0L, dataArray, -89, -89);
            fail("Expecting exception: NoSuchMethodError");
        } catch (NoSuchMethodError e) {
            verifyException("com.itextpdf.text.io.ByteBufferRandomAccessSource", e);
        }
    }

    @Test(timeout = 4000)
    public void testAccessClosedSourceThrowsException() throws Throwable {
        byte[] emptyArray = new byte[0];
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(emptyArray);
        WindowRandomAccessSource windowSource = new WindowRandomAccessSource(arraySource, -782L, -782L);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(windowSource);
        
        windowSource.close();
        
        try {
            bufferedSource.get(-2836L, emptyArray, -1, -1);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.itextpdf.text.io.ArrayRandomAccessSource", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetWithInvalidPositionThrowsException() throws Throwable {
        byte[] dataArray = new byte[2];
        ByteBuffer byteBuffer = ByteBuffer.wrap(dataArray);
        ByteBufferRandomAccessSource byteBufferSource = new ByteBufferRandomAccessSource(byteBuffer);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(byteBufferSource);
        
        try {
            bufferedSource.get(2147483651L, dataArray, 1617, 1617);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("com.itextpdf.text.io.ByteBufferRandomAccessSource", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetWithInvalidArrayIndexThrowsException() throws Throwable {
        byte[] dataArray = new byte[1];
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(dataArray);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
        
        try {
            bufferedSource.get(0L, dataArray, -328, 762);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testMappedChannelSourceThrowsIOException() throws Throwable {
        MappedChannelRandomAccessSource mappedSource = new MappedChannelRandomAccessSource(null, 2782L, 2782L);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(mappedSource);
        byte[] dataArray = new byte[1];
        
        try {
            bufferedSource.get(2782L, dataArray, -27, 0);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("com.itextpdf.text.io.MappedChannelRandomAccessSource", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetWithNullWindowSourceThrowsException() throws Throwable {
        WindowRandomAccessSource windowSource = new WindowRandomAccessSource(null, -685L, -685L);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(windowSource);
        
        try {
            bufferedSource.get(-960L);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.io.WindowRandomAccessSource", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetWithDirectByteBufferThrowsException() throws Throwable {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1);
        ByteBufferRandomAccessSource byteBufferSource = new ByteBufferRandomAccessSource(byteBuffer);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(byteBufferSource);
        
        try {
            bufferedSource.get(-1269L);
            fail("Expecting exception: NoSuchMethodError");
        } catch (NoSuchMethodError e) {
            verifyException("com.itextpdf.text.io.ByteBufferRandomAccessSource", e);
        }
    }

    @Test(timeout = 4000)
    public void testAccessClosedSourceThrowsIllegalStateException() throws Throwable {
        byte[] dataArray = new byte[8];
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(dataArray);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
        
        bufferedSource.close();
        
        try {
            bufferedSource.get(-155L);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.itextpdf.text.io.ArrayRandomAccessSource", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetWithInvalidLongPositionThrowsException() throws Throwable {
        byte[] dataArray = new byte[2];
        ByteBuffer byteBuffer = ByteBuffer.wrap(dataArray);
        ByteBufferRandomAccessSource byteBufferSource = new ByteBufferRandomAccessSource(byteBuffer);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(byteBufferSource);
        
        try {
            bufferedSource.get(2147483651L);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("com.itextpdf.text.io.ByteBufferRandomAccessSource", e);
        }
    }

    @Test(timeout = 4000)
    public void testMappedChannelSourceGetThrowsIOException() throws Throwable {
        MappedChannelRandomAccessSource mappedSource = new MappedChannelRandomAccessSource(null, 1760L, 1760L);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(mappedSource);
        
        try {
            bufferedSource.get(1760L);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("com.itextpdf.text.io.MappedChannelRandomAccessSource", e);
        }
    }

    @Test(timeout = 4000)
    public void testCloseNullWindowSourceThrowsException() throws Throwable {
        WindowRandomAccessSource windowSource = new WindowRandomAccessSource(null, 0L, 0L);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(windowSource);
        
        try {
            bufferedSource.close();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.io.WindowRandomAccessSource", e);
        }
    }

    @Test(timeout = 4000)
    public void testInitializeWithNullSourceThrowsException() throws Throwable {
        try {
            new GetBufferedRandomAccessSource(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.itextpdf.text.io.GetBufferedRandomAccessSource", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetBeyondEndOfEmptySourceReturnsMinusOne() throws Throwable {
        byte[] emptyArray = new byte[0];
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(emptyArray);
        WindowRandomAccessSource windowSource = new WindowRandomAccessSource(arraySource, -782L, -782L);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(windowSource);
        
        int result = bufferedSource.get(3322L);
        
        assertEquals(-1, result);
    }

    @Test(timeout = 4000)
    public void testGetByteTwiceFromSource() throws Throwable {
        byte[] dataArray = new byte[15];
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(dataArray);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
        
        bufferedSource.get(0L);
        int result = bufferedSource.get(0L);
        
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testGetNegativePositionThrowsException() throws Throwable {
        byte[] dataArray = new byte[8];
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(dataArray);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
        
        bufferedSource.get(0L);
        
        try {
            bufferedSource.get(-1L);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testLengthOfNonEmptySource() throws Throwable {
        byte[] dataArray = new byte[8];
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(dataArray);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
        
        long length = bufferedSource.length();
        
        assertEquals(8L, length);
    }

    @Test(timeout = 4000)
    public void testGetWithZeroLengthReturnsZero() throws Throwable {
        byte[] dataArray = new byte[15];
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(dataArray);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
        
        int result = bufferedSource.get(0L, dataArray, 0, 0);
        
        assertEquals(0, result);
    }
}