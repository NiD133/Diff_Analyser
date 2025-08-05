package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.EOFException;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class NullReader_ESTest extends NullReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testResetWithoutMarkThrowsIOException() throws Throwable {
        NullReader nullReader = NullReader.INSTANCE;
        try {
            nullReader.reset();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.io.input.NullReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadAfterMarkAndReset() throws Throwable {
        NullReader nullReader = new NullReader(959L, true, true);
        char[] buffer = new char[4];
        nullReader.read(buffer);
        nullReader.mark(0);
        nullReader.reset();
        assertEquals(4L, nullReader.getPosition());
    }

    @Test(timeout = 4000)
    public void testReadSingleCharacter() throws Throwable {
        NullReader nullReader = new NullReader(959L, true, true);
        int result = nullReader.read();
        assertEquals(1L, nullReader.getPosition());
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testSkipNegativeCharacters() throws Throwable {
        NullReader nullReader = new NullReader(-995L);
        nullReader.skip(-5663L);
        int result = nullReader.read((char[]) null, 0, 0);
        assertEquals(-5663L, nullReader.getPosition());
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testReadIntoArrayWithOffsetAndLength() throws Throwable {
        NullReader nullReader = new NullReader(959L, true, true);
        char[] buffer = new char[4];
        int result = nullReader.read(buffer, -2654, 616);
        assertEquals(616L, nullReader.getPosition());
        assertEquals(616, result);
    }

    @Test(timeout = 4000)
    public void testReadEmptyArray() throws Throwable {
        NullReader nullReader = new NullReader(3453L);
        char[] buffer = new char[0];
        int result = nullReader.read(buffer);
        assertTrue(nullReader.markSupported());
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testProcessCharReturnsZero() throws Throwable {
        NullReader nullReader = new NullReader(-476L);
        int result = nullReader.processChar();
        assertEquals(0, result);
        assertEquals(-476L, nullReader.getSize());
        assertTrue(nullReader.markSupported());
    }

    @Test(timeout = 4000)
    public void testGetSizeReturnsNegativeOne() throws Throwable {
        NullReader nullReader = new NullReader(-1L);
        long size = nullReader.getSize();
        assertTrue(nullReader.markSupported());
        assertEquals(-1L, size);
    }

    @Test(timeout = 4000)
    public void testReadWithNegativeLength() throws Throwable {
        NullReader nullReader = new NullReader(959L, true, true);
        char[] buffer = new char[4];
        nullReader.read(buffer, 616, -1073741823);
        long position = nullReader.getPosition();
        assertEquals(-1073741823L, position);
    }

    @Test(timeout = 4000)
    public void testSkipBeyondEndThrowsEOFException() throws Throwable {
        NullReader nullReader = new NullReader(0, true, true);
        try {
            nullReader.skip(512L);
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            verifyException("org.apache.commons.io.input.NullReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadBeyondEndThrowsEOFException() throws Throwable {
        NullReader nullReader = new NullReader(1L, false, true);
        char[] buffer = new char[1];
        nullReader.read(buffer);
        try {
            nullReader.read(buffer, 2146374983, 3917);
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            verifyException("org.apache.commons.io.input.NullReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadNullArrayThrowsNullPointerException() throws Throwable {
        NullReader nullReader = new NullReader();
        try {
            nullReader.read((char[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.NullReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadAfterSkipThrowsEOFException() throws Throwable {
        NullReader nullReader = new NullReader(7L, true, true);
        nullReader.skip(7L);
        try {
            nullReader.read();
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            verifyException("org.apache.commons.io.input.NullReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testSkipBeyondSize() throws Throwable {
        NullReader nullReader = new NullReader(1L);
        nullReader.skip(1695L);
        long position = nullReader.getPosition();
        assertEquals(1L, position);
    }

    @Test(timeout = 4000)
    public void testMarkAndReadBeyondEndThrowsIOException() throws Throwable {
        NullReader nullReader = new NullReader();
        nullReader.ready();
        nullReader.mark(2143);
        nullReader.read();
        char[] buffer = new char[0];
        try {
            nullReader.INSTANCE.read(buffer, 0, 2143);
        } catch (IOException e) {
            verifyException("org.apache.commons.io.input.NullReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testSkipZeroCharacters() throws Throwable {
        NullReader nullReader = new NullReader(959L, true, true);
        long skipped = nullReader.skip(0L);
        assertTrue(nullReader.markSupported());
        assertEquals(0L, skipped);
    }

    @Test(timeout = 4000)
    public void testSkipNegativeThrowsIOException() throws Throwable {
        NullReader nullReader = new NullReader();
        nullReader.read();
        try {
            nullReader.skip(-1);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.io.input.NullReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testResetUnsupportedThrowsException() throws Throwable {
        NullReader nullReader = new NullReader(-1388L, false, false);
        try {
            nullReader.reset();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.apache.commons.io.input.UnsupportedOperationExceptions", e);
        }
    }

    @Test(timeout = 4000)
    public void testResetWithoutMarkThrowsIOExceptionAgain() throws Throwable {
        NullReader nullReader = new NullReader(959L, true, true);
        try {
            nullReader.reset();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.io.input.NullReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadBeyondEndThrowsIOException() throws Throwable {
        NullReader nullReader = NullReader.INSTANCE;
        try {
            nullReader.read();
        } catch (IOException e) {
            verifyException("org.apache.commons.io.input.NullReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testMarkUnsupportedThrowsException() throws Throwable {
        NullReader nullReader = new NullReader(-329L, false, true);
        try {
            nullReader.mark(0);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.apache.commons.io.input.UnsupportedOperationExceptions", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadEmptyArrayTwiceThrowsEOFException() throws Throwable {
        NullReader nullReader = new NullReader(-1935L, true, true);
        char[] buffer = new char[0];
        nullReader.read(buffer, 1095, 1095);
        try {
            nullReader.read(buffer);
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            verifyException("org.apache.commons.io.input.NullReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testSkipNegativeReturnsNegativeOne() throws Throwable {
        NullReader nullReader = new NullReader(-3294L, false, false);
        char[] buffer = new char[7];
        nullReader.read(buffer, 2144545913, 0);
        long skipped = nullReader.skip(-2904L);
        assertEquals(-3294L, nullReader.getPosition());
        assertEquals(-1L, skipped);
    }

    @Test(timeout = 4000)
    public void testCloseResetsPosition() throws Throwable {
        NullReader nullReader = new NullReader(-995L);
        nullReader.close();
        assertEquals(0L, nullReader.getPosition());
        assertTrue(nullReader.markSupported());
        assertEquals(-995L, nullReader.getSize());
    }

    @Test(timeout = 4000)
    public void testReadEmptyArrayReturnsNegativeSize() throws Throwable {
        NullReader nullReader = new NullReader(-1935L, true, true);
        char[] buffer = new char[0];
        int result = nullReader.read(buffer);
        assertEquals(-1935L, nullReader.getPosition());
        assertEquals(-1935, result);
    }

    @Test(timeout = 4000)
    public void testMarkNotSupported() throws Throwable {
        NullReader nullReader = new NullReader(-3294L, false, false);
        boolean supported = nullReader.markSupported();
        assertEquals(-3294L, nullReader.getSize());
        assertFalse(supported);
    }

    @Test(timeout = 4000)
    public void testGetPositionAndSize() throws Throwable {
        NullReader nullReader = new NullReader(959L, true, true);
        nullReader.getPosition();
        assertTrue(nullReader.markSupported());
        assertEquals(959L, nullReader.getSize());
    }

    @Test(timeout = 4000)
    public void testReadIncrementsPosition() throws Throwable {
        NullReader nullReader = new NullReader(-3294L, false, false);
        int result = nullReader.read();
        assertEquals(1L, nullReader.getPosition());
        assertEquals(0, result);
    }
}