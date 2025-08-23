package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.text.io.GetBufferedRandomAccessSource;
import com.itextpdf.text.io.IndependentRandomAccessSource;
import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.WindowRandomAccessSource;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.net.MockURL;
import org.evosuite.runtime.testdata.EvoSuiteFile;
import org.evosuite.runtime.testdata.FileSystemHandling;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class RandomAccessFileOrArray_ESTest extends RandomAccessFileOrArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testReadLongFromByteArray() throws Throwable {
        // Arrange
        byte[] byteArray = new byte[24];
        byteArray[4] = (byte) (-104);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        RandomAccessFileOrArray randomAccessFileOrArray = new RandomAccessFileOrArray(byteArrayInputStream);

        // Act
        long result = randomAccessFileOrArray.readLong();

        // Assert
        assertEquals(8L, randomAccessFileOrArray.getFilePointer());
        assertEquals(2550136832L, result);
    }

    @Test(timeout = 4000)
    public void testReadUnsignedIntLE() throws Throwable {
        // Arrange
        byte[] byteArray = new byte[6];
        byteArray[3] = (byte) (-99);
        RandomAccessFileOrArray randomAccessFileOrArray = new RandomAccessFileOrArray(byteArray);

        // Act
        long result = randomAccessFileOrArray.readUnsignedIntLE();

        // Assert
        assertEquals(4L, randomAccessFileOrArray.getFilePointer());
        assertEquals(2634022912L, result);
    }

    @Test(timeout = 4000)
    public void testReadShortLEAndUnsignedInt() throws Throwable {
        // Arrange
        byte[] byteArray = new byte[8];
        byteArray[2] = (byte) (-9);
        byteArray[3] = (byte) 52;
        RandomAccessFileOrArray randomAccessFileOrArray = new RandomAccessFileOrArray(byteArray);

        // Act
        short shortResult = randomAccessFileOrArray.readShortLE();
        long longResult = randomAccessFileOrArray.readUnsignedInt();

        // Assert
        assertEquals((short) 0, shortResult);
        assertEquals(4147380224L, longResult);
    }

    @Test(timeout = 4000)
    public void testReadFloatLE() throws Throwable {
        // Arrange
        byte[] byteArray = new byte[9];
        byteArray[1] = (byte) 16;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        RandomAccessFileOrArray randomAccessFileOrArray = new RandomAccessFileOrArray(byteArrayInputStream);

        // Act
        float result = randomAccessFileOrArray.readFloatLE();

        // Assert
        assertEquals(4L, randomAccessFileOrArray.getFilePointer());
        assertEquals(5.74E-42F, result, 0.01F);
    }

    @Test(timeout = 4000)
    public void testReadLineAndExpectEOFException() throws Throwable {
        // Arrange
        byte[] byteArray = new byte[5];
        RandomAccessFileOrArray randomAccessFileOrArray = new RandomAccessFileOrArray(byteArray);
        randomAccessFileOrArray.readLine();

        // Act & Assert
        try {
            randomAccessFileOrArray.readLongLE();
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            // Expected exception
            verifyException("com.itextpdf.text.pdf.RandomAccessFileOrArray", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadCharLE() throws Throwable {
        // Arrange
        byte[] byteArray = new byte[6];
        byteArray[0] = (byte) 119;
        byteArray[1] = (byte) 30;
        RandomAccessFileOrArray randomAccessFileOrArray = new RandomAccessFileOrArray(byteArray);

        // Act
        char result = randomAccessFileOrArray.readCharLE();

        // Assert
        assertEquals(2L, randomAccessFileOrArray.getFilePointer());
        assertEquals('\u1E77', result);
    }

    @Test(timeout = 4000)
    public void testReadUnsignedShortLE() throws Throwable {
        // Arrange
        byte[] byteArray = new byte[7];
        byteArray[0] = (byte) (-7);
        byteArray[1] = (byte) 13;
        RandomAccessFileOrArray randomAccessFileOrArray = new RandomAccessFileOrArray(byteArray);

        // Act
        int result = randomAccessFileOrArray.readUnsignedShortLE();

        // Assert
        assertEquals(2L, randomAccessFileOrArray.getFilePointer());
        assertEquals(3577, result);
    }

    @Test(timeout = 4000)
    public void testReadDouble() throws Throwable {
        // Arrange
        byte[] byteArray = new byte[8];
        byteArray[2] = (byte) (-9);
        RandomAccessFileOrArray randomAccessFileOrArray = new RandomAccessFileOrArray(byteArray);

        // Act
        double result = randomAccessFileOrArray.readDouble();

        // Assert
        assertEquals(8L, randomAccessFileOrArray.getFilePointer());
        assertEquals(1.34178037854316E-309, result, 0.01);
    }

    @Test(timeout = 4000)
    public void testReadBooleanWithPushBack() throws Throwable {
        // Arrange
        byte[] byteArray = new byte[9];
        RandomAccessFileOrArray randomAccessFileOrArray = new RandomAccessFileOrArray(byteArray);
        randomAccessFileOrArray.pushBack((byte) (-63));

        // Act
        boolean result = randomAccessFileOrArray.readBoolean();

        // Assert
        assertEquals(0L, randomAccessFileOrArray.getFilePointer());
        assertTrue(result);
    }

    // Additional tests can be added here following the same pattern

}