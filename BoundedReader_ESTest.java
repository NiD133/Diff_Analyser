package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.apache.commons.io.input.BoundedReader;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class BoundedReader_ESTest extends BoundedReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testReadLimitedCharacters() throws Throwable {
        StringReader stringReader = new StringReader("org.apache.commons.io.input.BoundedReader");
        BoundedReader boundedReader = new BoundedReader(stringReader, 3);
        char[] buffer = new char[7];
        int charsRead = boundedReader.read(buffer);
        assertEquals(3, charsRead);
        assertArrayEquals(new char[] {'o', 'r', 'g', '\u0000', '\u0000', '\u0000', '\u0000'}, buffer);
    }

    @Test(timeout = 4000)
    public void testReadEmptyString() throws Throwable {
        StringReader stringReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(stringReader, 1805);
        boundedReader.read();
        boundedReader.mark(0);
        int result = boundedReader.read();
        assertEquals(-1, result);
    }

    @Test(timeout = 4000)
    public void testReadWithNullReader() throws Throwable {
        BoundedReader boundedReader = new BoundedReader((Reader) null, -1);
        int result = boundedReader.read();
        assertEquals(-1, result);
    }

    @Test(timeout = 4000)
    public void testMarkAndReadEmptyString() throws Throwable {
        StringReader stringReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(stringReader, 1805);
        int firstRead = boundedReader.read();
        boundedReader.mark(1268);
        int secondRead = boundedReader.read();
        assertEquals(firstRead, secondRead);
    }

    @Test(timeout = 4000)
    public void testReadWithNegativeOffset() throws Throwable {
        StringReader stringReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(stringReader, 1);
        char[] buffer = new char[3];
        int result = boundedReader.read(buffer, -743, 0);
        assertEquals(0, result);
    }

    @Test(timeout = 4000)
    public void testReadSingleCharacter() throws Throwable {
        StringReader stringReader = new StringReader(".''L5DuTEy{jV3");
        BoundedReader boundedReader = new BoundedReader(stringReader, 1729);
        char[] buffer = new char[8];
        int charsRead = boundedReader.read(buffer, 1, 1);
        assertEquals(1, charsRead);
        assertArrayEquals(new char[] {'\u0000', '.', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000'}, buffer);
    }

    @Test(timeout = 4000)
    public void testReadFirstCharacter() throws Throwable {
        StringReader stringReader = new StringReader("v4]>?/Q;dj|.O1#4");
        BoundedReader boundedReader = new BoundedReader(stringReader, 1);
        int result = boundedReader.read();
        assertEquals('v', result);
    }

    @Test(timeout = 4000)
    public void testResetOnNullReader() throws Throwable {
        BoundedReader boundedReader = new BoundedReader((Reader) null, 1);
        try {
            boundedReader.reset();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.BoundedReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testResetOnClosedReader() throws Throwable {
        StringReader stringReader = new StringReader("org.apache.commons.io.input.BoundedReader");
        BoundedReader boundedReader = new BoundedReader(stringReader, 0);
        boundedReader.close();
        try {
            boundedReader.reset();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadWithNullCharArray() throws Throwable {
        BoundedReader boundedReader = new BoundedReader((Reader) null, 1);
        char[] buffer = new char[1];
        try {
            boundedReader.read(buffer, 1, 1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.BoundedReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadWithInvalidArrayBounds() throws Throwable {
        StringReader stringReader = new StringReader("4s");
        BoundedReader boundedReader = new BoundedReader(stringReader, 179);
        char[] buffer = new char[0];
        try {
            boundedReader.read(buffer, 179, 179);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.io.input.BoundedReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadOnClosedReader() throws Throwable {
        StringReader stringReader = new StringReader("");
        stringReader.close();
        BoundedReader boundedReader = new BoundedReader(stringReader, 214);
        char[] buffer = new char[0];
        try {
            boundedReader.read(buffer, 214, 214);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadOnNullReader() throws Throwable {
        BoundedReader boundedReader = new BoundedReader((Reader) null, 1);
        try {
            boundedReader.read();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.BoundedReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadOnClosedStringReader() throws Throwable {
        StringReader stringReader = new StringReader("");
        stringReader.close();
        BoundedReader boundedReader = new BoundedReader(stringReader, 202);
        try {
            boundedReader.read();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testMarkOnNullReader() throws Throwable {
        BoundedReader boundedReader = new BoundedReader((Reader) null, 4168);
        try {
            boundedReader.mark(4168);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.BoundedReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testMarkWithNegativeLimit() throws Throwable {
        StringReader stringReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(stringReader, -431);
        try {
            boundedReader.mark(-431);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testMarkOnClosedReader() throws Throwable {
        StringReader stringReader = new StringReader("");
        stringReader.close();
        BoundedReader boundedReader = new BoundedReader(stringReader, 198);
        try {
            boundedReader.mark(198);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testCloseOnNullReader() throws Throwable {
        BoundedReader boundedReader = new BoundedReader((Reader) null, -1235);
        try {
            boundedReader.close();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.BoundedReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadAfterMarkAndReset() throws Throwable {
        StringReader stringReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(stringReader, 1805);
        boundedReader.mark(1);
        boundedReader.read();
        int result = boundedReader.read();
        assertEquals(-1, result);
    }

    @Test(timeout = 4000)
    public void testReadWithNullBuffer() throws Throwable {
        StringReader stringReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(stringReader, 1805);
        int result = boundedReader.read((char[]) null, 1805, -1);
        assertEquals(-1, result);
    }

    @Test(timeout = 4000)
    public void testSkipCharacters() throws Throwable {
        StringReader stringReader = new StringReader("wa");
        BoundedReader boundedReader = new BoundedReader(stringReader, 10);
        boundedReader.mark(1);
        long skipped = boundedReader.skip(10);
        assertEquals(1L, skipped);
    }

    @Test(timeout = 4000)
    public void testResetOnEmptyReader() throws Throwable {
        StringReader stringReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(stringReader, 1);
        boundedReader.reset();
    }
}