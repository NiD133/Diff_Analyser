package org.example;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.EOFException;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.example.NullReader;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true, useJEE = true)
public class NullReaderTest extends NullReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testReadSingleCharacterIncrementsPosition() throws Throwable {
        NullReader nullReader = new NullReader(-1L);
        nullReader.read();
        assertEquals(1L, nullReader.getPosition());
    }

    @Test(timeout = 4000)
    public void testReadWithNegativeOffsetAndLength() throws Throwable {
        NullReader nullReader = new NullReader(-1L);
        char[] charArray = new char[8];
        nullReader.read(charArray, -1, -3484);
        int result = nullReader.read();
        assertEquals(-3483L, nullReader.getPosition());
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testProcessCharsWithLargeOffsetAndLength() throws Throwable {
        NullReader nullReader = new NullReader();
        char[] charArray = new char[2];
        nullReader.processChars(charArray, 1542, 1542);
        assertTrue(nullReader.markSupported());
    }

    @Test(timeout = 4000)
    public void testSkipZeroCharacters() throws Throwable {
        NullReader nullReader = new NullReader(597L);
        long skipped = nullReader.skip(0);
        assertEquals(0L, skipped);
        assertTrue(nullReader.markSupported());
    }

    @Test(timeout = 4000)
    public void testSkipToEndOfReader() throws Throwable {
        NullReader nullReader = new NullReader(1451L, true, true);
        long skipped = nullReader.skip(1451L);
        assertEquals(1451L, nullReader.getPosition());
        assertEquals(1451L, skipped);
    }

    @Test(timeout = 4000)
    public void testReadWithZeroLength() throws Throwable {
        NullReader nullReader = new NullReader(1279L);
        char[] charArray = new char[4];
        int result = nullReader.read(charArray, 938, 0);
        assertTrue(nullReader.markSupported());
        assertEquals(0, result);
        assertEquals(0L, nullReader.getPosition());
    }

    @Test(timeout = 4000)
    public void testReadBeyondEndOfReader() throws Throwable {
        NullReader nullReader = new NullReader(1592L);
        char[] charArray = new char[5];
        int result = nullReader.read(charArray, 2146694131, 2146694131);
        assertEquals(1592L, nullReader.getPosition());
        assertEquals(1592, result);
    }

    @Test(timeout = 4000)
    public void testReadEmptyCharArray() throws Throwable {
        NullReader nullReader = new NullReader(1254L);
        char[] charArray = new char[0];
        int result = nullReader.read(charArray);
        assertEquals(0, result);
        assertTrue(nullReader.markSupported());
    }

    @Test(timeout = 4000)
    public void testReadCharArrayUpdatesPosition() throws Throwable {
        NullReader nullReader = new NullReader(1480L, true, true);
        char[] charArray = new char[3];
        int result = nullReader.read(charArray);
        assertEquals(3L, nullReader.getPosition());
        assertEquals(3, result);
    }

    @Test(timeout = 4000)
    public void testProcessCharWithNegativeSize() throws Throwable {
        NullReader nullReader = new NullReader(-1401L, true, false);
        int result = nullReader.processChar();
        assertEquals(-1401L, nullReader.getSize());
        assertTrue(nullReader.markSupported());
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testMarkNotSupported() throws Throwable {
        NullReader nullReader = new NullReader(0L, false, false);
        assertFalse(nullReader.markSupported());
    }

    @Test(timeout = 4000)
    public void testGetSizeOfInstance() throws Throwable {
        NullReader nullReader = NullReader.INSTANCE;
        long size = nullReader.getSize();
        assertEquals(0L, size);
    }

    @Test(timeout = 4000)
    public void testGetSizeOfReader() throws Throwable {
        NullReader nullReader = new NullReader(1279L);
        long size = nullReader.getSize();
        assertTrue(nullReader.markSupported());
        assertEquals(1279L, size);
    }

    @Test(timeout = 4000)
    public void testReadIncrementsPosition() throws Throwable {
        NullReader nullReader = new NullReader(-961L);
        nullReader.read();
        long position = nullReader.getPosition();
        assertEquals(1L, position);
    }

    @Test(timeout = 4000)
    public void testReadThrowsEOFException() throws Throwable {
        NullReader nullReader = new NullReader(0L, true, true);
        char[] charArray = new char[7];
        try {
            nullReader.read(charArray, 0, 0);
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            verifyException("org.example.NullReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadNullCharArrayThrowsNullPointerException() throws Throwable {
        NullReader nullReader = new NullReader(-955L);
        try {
            nullReader.INSTANCE.read((char[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.example.NullReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadThrowsEOFExceptionWhenEOFReached() throws Throwable {
        NullReader nullReader = new NullReader(0L, false, true);
        char[] charArray = new char[2];
        try {
            nullReader.read(charArray);
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            verifyException("org.example.NullReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testSkipThrowsEOFExceptionWhenEOFReached() throws Throwable {
        NullReader nullReader = new NullReader(-2271L, false, true);
        nullReader.skip(-2271L);
        try {
            nullReader.read();
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            verifyException("org.example.NullReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testSkipThrowsIOExceptionAfterEOF() throws Throwable {
        NullReader nullReader = new NullReader();
        char[] charArray = new char[5];
        nullReader.read(charArray, 640, 0);
        try {
            nullReader.skip(0L);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.example.NullReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testResetThrowsIOExceptionWhenNoMark() throws Throwable {
        NullReader nullReader = new NullReader();
        nullReader.mark(-1791);
        try {
            nullReader.reset();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.example.NullReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadFromInstance() throws Throwable {
        NullReader nullReader = NullReader.INSTANCE;
        nullReader.getPosition();
        nullReader.read();
    }

    @Test(timeout = 4000)
    public void testSkipNegativeSize() throws Throwable {
        NullReader nullReader = new NullReader(-1324L);
        long skipped = nullReader.skip(8);
        assertEquals(-1324L, nullReader.getPosition());
        assertEquals(-1324L, skipped);
    }

    @Test(timeout = 4000)
    public void testResetThrowsUnsupportedOperationException() throws Throwable {
        NullReader nullReader = new NullReader(247L, false, false);
        try {
            nullReader.reset();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.example.NullReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testResetThrowsIOExceptionWhenNoMarkSet() throws Throwable {
        NullReader nullReader = new NullReader(0L);
        try {
            nullReader.reset();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.example.NullReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testComplexReadOperations() throws Throwable {
        NullReader nullReader = new NullReader(-955L);
        nullReader.INSTANCE.mark(1180);
        nullReader.getPosition();
        char[] charArray = new char[1];
        charArray[0] = 'U';
        nullReader.read(charArray, 1180, 1180);
        nullReader.read();
        char[] charArray1 = new char[9];
        charArray1[0] = 'U';
        nullReader.getPosition();
        nullReader.INSTANCE.read(charArray1, -1056, -196);
    }

    @Test(timeout = 4000)
    public void testMarkThrowsUnsupportedOperationException() throws Throwable {
        NullReader nullReader = new NullReader(10L, false, false);
        try {
            nullReader.mark(1452);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.example.NullReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testSkipThrowsEOFExceptionOnSecondSkip() throws Throwable {
        NullReader nullReader = new NullReader(-348L, true, true);
        nullReader.skip(-348L);
        try {
            nullReader.skip(-348L);
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            verifyException("org.example.NullReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testCloseResetsPosition() throws Throwable {
        NullReader nullReader = NullReader.INSTANCE;
        nullReader.close();
        assertEquals(0L, nullReader.getPosition());
    }

    @Test(timeout = 4000)
    public void testGetSizeWithNegativeSize() throws Throwable {
        NullReader nullReader = new NullReader(-1324L);
        long size = nullReader.getSize();
        assertTrue(nullReader.markSupported());
        assertEquals(-1324L, size);
    }

    @Test(timeout = 4000)
    public void testReadReturnsEOFWhenSizeNegative() throws Throwable {
        NullReader nullReader = new NullReader(-970L);
        char[] charArray = new char[14];
        nullReader.read(charArray);
        int result = nullReader.read();
        assertEquals(-970L, nullReader.getPosition());
        assertEquals(-1, result);
    }

    @Test(timeout = 4000)
    public void testMarkSupportedWithNegativeSize() throws Throwable {
        NullReader nullReader = new NullReader(-1324L);
        boolean markSupported = nullReader.markSupported();
        assertEquals(-1324L, nullReader.getSize());
        assertTrue(markSupported);
    }
}