package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for the BoundedReader class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true, useJEE = true)
public class BoundedReader_ESTest extends BoundedReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testReadWithZeroLimit() throws Throwable {
        StringReader input = new StringReader("TO[Gj");
        BoundedReader reader = new BoundedReader(input, 0);
        CharBuffer buffer = CharBuffer.wrap("TO[Gj");
        int result = reader.read(buffer);
        assertEquals("Expected EOF when limit is zero", -1, result);
    }

    @Test(timeout = 4000)
    public void testReadSingleCharacter() throws Throwable {
        StringReader input = new StringReader("QN?");
        BoundedReader reader = new BoundedReader(input, 2680);
        reader.read(); // Read first character
        reader.mark(2680);
        int result = reader.read(); // Read second character
        assertEquals("Expected to read character 'N'", 'N', result);
    }

    @Test(timeout = 4000)
    public void testReadEmptyString() throws Throwable {
        StringReader input = new StringReader("");
        BoundedReader reader = new BoundedReader(input, 1148);
        char[] buffer = new char[1];
        int result = reader.read(buffer, 0, 0);
        assertEquals("Expected to read zero characters from empty string", 0, result);
    }

    @Test(timeout = 4000)
    public void testReadWithOffset() throws Throwable {
        StringReader input = new StringReader("org.apache.commons.io.input.BoundedReader");
        BoundedReader reader = new BoundedReader(input, 1);
        char[] buffer = new char[6];
        int result = reader.read(buffer, 1, 1);
        assertEquals("Expected to read one character", 1, result);
        assertArrayEquals("Expected buffer to contain 'o' at index 1", new char[] {'\u0000', 'o', '\u0000', '\u0000', '\u0000', '\u0000'}, buffer);
    }

    @Test(timeout = 4000)
    public void testResetOnNullReader() throws Throwable {
        BoundedReader reader = new BoundedReader((Reader) null, 0);
        try {
            reader.reset();
            fail("Expected NullPointerException when resetting null reader");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.BoundedReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testResetAfterClose() throws Throwable {
        StringReader input = new StringReader("TO[Gj");
        BoundedReader reader = new BoundedReader(input, 0);
        reader.close();
        try {
            reader.reset();
            fail("Expected IOException when resetting closed reader");
        } catch (IOException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadNullBuffer() throws Throwable {
        StringReader input = new StringReader("g`8B;^5");
        BoundedReader reader = new BoundedReader(input, 820);
        try {
            reader.read((char[]) null, 103, 78);
            fail("Expected NullPointerException when reading into null buffer");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.BoundedReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadAfterClose() throws Throwable {
        StringReader input = new StringReader("j");
        BoundedReader reader = new BoundedReader(input, 1);
        reader.close();
        char[] buffer = new char[6];
        try {
            reader.read(buffer, -2537, 699);
            fail("Expected IOException when reading from closed reader");
        } catch (IOException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadOnNullReader() throws Throwable {
        BoundedReader reader = new BoundedReader((Reader) null, 2482);
        try {
            reader.read();
            fail("Expected NullPointerException when reading from null reader");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.BoundedReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadAfterCloseOnEmptyString() throws Throwable {
        StringReader input = new StringReader("");
        BoundedReader reader = new BoundedReader(input, 2140);
        reader.close();
        try {
            reader.read();
            fail("Expected IOException when reading from closed reader");
        } catch (IOException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testMarkOnNullReader() throws Throwable {
        BoundedReader reader = new BoundedReader((Reader) null, 0);
        try {
            reader.mark(0);
            fail("Expected NullPointerException when marking null reader");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.BoundedReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testMarkWithNegativeLimit() throws Throwable {
        StringReader input = new StringReader("SQ\"Rz~>o\"ggtg97eV");
        BoundedReader reader = new BoundedReader(input, -585);
        try {
            reader.mark(-585);
            fail("Expected IllegalArgumentException when marking with negative limit");
        } catch (IllegalArgumentException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testCloseOnNullReader() throws Throwable {
        BoundedReader reader = new BoundedReader((Reader) null, 0);
        try {
            reader.close();
            fail("Expected NullPointerException when closing null reader");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.BoundedReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadAfterMark() throws Throwable {
        StringReader input = new StringReader("Mqy[$oy5nF");
        BoundedReader reader = new BoundedReader(input, 1601);
        reader.mark(1601);
        int result = reader.read();
        assertEquals("Expected to read character 'M'", 'M', result);
    }

    @Test(timeout = 4000)
    public void testReadWithNegativeLimit() throws Throwable {
        StringReader input = new StringReader("CU6^Ejr;7S;Ndl FK8");
        BoundedReader reader = new BoundedReader(input, -1821);
        BoundedReader nestedReader = new BoundedReader(reader, 1);
        nestedReader.mark(0);
        int result = nestedReader.read();
        assertEquals("Expected EOF when limit is negative", -1, result);
    }

    @Test(timeout = 4000)
    public void testReadWithNegativeLimitDirectly() throws Throwable {
        StringReader input = new StringReader("CU6^Ejr;7S;Ndl FK8");
        BoundedReader reader = new BoundedReader(input, -1821);
        BoundedReader nestedReader = new BoundedReader(reader, 1);
        int result = nestedReader.read();
        assertEquals("Expected EOF when limit is negative", -1, result);
    }

    @Test(timeout = 4000)
    public void testReadIntoBuffer() throws Throwable {
        StringReader input = new StringReader("QN?");
        BoundedReader reader = new BoundedReader(input, 2680);
        char[] buffer = new char[9];
        int result = reader.read(buffer);
        assertArrayEquals("Expected buffer to contain 'QN?'", new char[] {'Q', 'N', '?', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000'}, buffer);
        assertEquals("Expected to read 3 characters", 3, result);

        reader.mark(1);
        int eofResult = reader.read();
        assertEquals("Expected EOF after reading all characters", -1, eofResult);
    }

    @Test(timeout = 4000)
    public void testReadWithInvalidOffsetAndLength() throws Throwable {
        StringReader input = new StringReader("pI2");
        BoundedReader reader = new BoundedReader(input, -2049);
        char[] buffer = new char[14];
        int result = reader.read(buffer, -2049, -1);
        assertEquals("Expected EOF when offset and length are invalid", -1, result);
    }

    @Test(timeout = 4000)
    public void testReadSingleCharacterWithNegativeLimit() throws Throwable {
        StringReader input = new StringReader("pI2");
        BoundedReader reader = new BoundedReader(input, -2049);
        int result = reader.read();
        assertEquals("Expected EOF when limit is negative", -1, result);
    }

    @Test(timeout = 4000)
    public void testResetOnEmptyString() throws Throwable {
        StringReader input = new StringReader("");
        BoundedReader reader = new BoundedReader(input, 1);
        reader.reset();
    }

    @Test(timeout = 4000)
    public void testMarkAfterClose() throws Throwable {
        StringReader input = new StringReader("");
        BoundedReader reader = new BoundedReader(input, 1);
        reader.close();
        try {
            reader.mark(0);
            fail("Expected IOException when marking closed reader");
        } catch (IOException e) {
            verifyException("java.io.StringReader", e);
        }
    }
}