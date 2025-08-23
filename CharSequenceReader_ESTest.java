package com.google.common.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.io.CharSequenceReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
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
public class CharSequenceReaderTest extends CharSequenceReaderTestScaffolding {

    @Test(timeout = 4000)
    public void testMarkSupported() throws Throwable {
        char[] buffer = new char[6];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        reader.mark(666);
        assertTrue(reader.markSupported());
    }

    @Test(timeout = 4000)
    public void testReadSingleChar() throws Throwable {
        char[] buffer = new char[6];
        char[] singleCharBuffer = new char[1];
        CharBuffer charBuffer = CharBuffer.wrap(singleCharBuffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        int charsRead = reader.read(buffer);
        assertEquals(1, charsRead);
    }

    @Test(timeout = 4000)
    public void testSkipAllChars() throws Throwable {
        CharBuffer charBuffer = CharBuffer.allocate(1191);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        long charsSkipped = reader.skip(1191);
        assertEquals(1191L, charsSkipped);
    }

    @Test(timeout = 4000)
    public void testReadAndSkipZeroChars() throws Throwable {
        char[] buffer = new char[6];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        charBuffer.append('B');
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        charBuffer.flip();
        reader.read(charBuffer);
        long charsSkipped = reader.skip(0);
        assertEquals("", charBuffer.toString());
        assertEquals(-1L, charsSkipped);
    }

    @Test(timeout = 4000)
    public void testReadZeroChars() throws Throwable {
        char[] buffer = new char[2];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        int charsRead = reader.read(buffer, 0, 0);
        assertEquals(0, charsRead);
    }

    @Test(timeout = 4000)
    public void testReadPartialBuffer() throws Throwable {
        char[] buffer = new char[6];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        int charsRead = reader.read(buffer, 0, 4);
        assertEquals(4, charsRead);
    }

    @Test(timeout = 4000)
    public void testReadEmptyBuffer() throws Throwable {
        char[] buffer = new char[1];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharBuffer targetBuffer = CharBuffer.wrap(buffer);
        charBuffer.append('2');
        CharSequenceReader reader = new CharSequenceReader(targetBuffer);
        int charsRead = reader.read(charBuffer);
        assertEquals(0, charsRead);
    }

    @Test(timeout = 4000)
    public void testReadSingleCharacter() throws Throwable {
        char[] buffer = new char[6];
        buffer[0] = 'g';
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        int charRead = reader.read();
        assertEquals('g', charRead);
    }

    @Test(timeout = 4000)
    public void testSkipAfterClose() throws Throwable {
        char[] buffer = new char[1];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        reader.close();
        try {
            reader.skip(2031L);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("com.google.common.io.CharSequenceReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testResetAfterClose() throws Throwable {
        char[] buffer = new char[2];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        reader.close();
        try {
            reader.reset();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("com.google.common.io.CharSequenceReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadyAfterClose() throws Throwable {
        char[] buffer = new char[1];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        reader.close();
        try {
            reader.ready();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("com.google.common.io.CharSequenceReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadNullBuffer() throws Throwable {
        CharBuffer charBuffer = CharBuffer.allocate(93);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        try {
            reader.read((char[]) null, 93, 93);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.io.CharSequenceReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadNegativeIndices() throws Throwable {
        char[] buffer = new char[6];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        try {
            reader.read(buffer, -1, -1);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadReadOnlyBuffer() throws Throwable {
        char[] buffer = new char[1];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        CharBuffer readOnlyBuffer = CharBuffer.wrap((CharSequence) charBuffer);
        try {
            reader.read(readOnlyBuffer);
            fail("Expecting exception: ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
            verifyException("java.nio.StringCharBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadNullCharBuffer() throws Throwable {
        char[] buffer = new char[1];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        try {
            reader.read((CharBuffer) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadAfterClose() throws Throwable {
        char[] buffer = new char[1];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        reader.close();
        try {
            reader.read(charBuffer);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("com.google.common.io.CharSequenceReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadEmptyBufferAfterAppend() throws Throwable {
        char[] buffer = new char[6];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharBuffer appendedBuffer = CharBuffer.wrap((CharSequence) charBuffer);
        CharSequenceReader reader = new CharSequenceReader(appendedBuffer);
        charBuffer.append((CharSequence) appendedBuffer);
        try {
            reader.read();
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.nio.Buffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadAfterCloseSingleChar() throws Throwable {
        char[] buffer = new char[6];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        reader.close();
        try {
            reader.read();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("com.google.common.io.CharSequenceReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testMarkAfterClose() throws Throwable {
        char[] buffer = new char[1];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        reader.close();
        try {
            reader.mark(0);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("com.google.common.io.CharSequenceReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullCharSequence() throws Throwable {
        try {
            new CharSequenceReader(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testNegativeMarkLimit() throws Throwable {
        char[] buffer = new char[1];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        try {
            reader.mark(-1897);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testSkipZeroChars() throws Throwable {
        CharBuffer charBuffer = CharBuffer.allocate(0);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        long charsSkipped = reader.skip(0);
        assertEquals(0L, charsSkipped);
    }

    @Test(timeout = 4000)
    public void testSkipNegativeChars() throws Throwable {
        char[] buffer = new char[3];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        try {
            reader.skip(-1L);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadEmptyCharBuffer() throws Throwable {
        char[] buffer = new char[1];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        int charRead = reader.read();
        assertEquals(0, charRead);
    }

    @Test(timeout = 4000)
    public void testReadFromEmptyBuffer() throws Throwable {
        char[] buffer = new char[0];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        int charRead = reader.read();
        assertEquals(-1, charRead);
    }

    @Test(timeout = 4000)
    public void testReadAfterBufferExhaustion() throws Throwable {
        char[] buffer = new char[1];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        reader.read(charBuffer);
        int charRead = reader.read(charBuffer);
        assertEquals(0, charBuffer.length());
        assertEquals(-1, charRead);
    }

    @Test(timeout = 4000)
    public void testReadFullBuffer() throws Throwable {
        char[] buffer = new char[6];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        try {
            reader.read(charBuffer);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.nio.Buffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testMarkSupportedCheck() throws Throwable {
        char[] buffer = new char[1];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        assertTrue(reader.markSupported());
    }

    @Test(timeout = 4000)
    public void testReadAndReadZeroChars() throws Throwable {
        char[] buffer = new char[6];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        int charsRead = reader.read(buffer);
        assertEquals(6, charsRead);

        int zeroCharsRead = reader.read(buffer, 0, 0);
        assertEquals(-1, zeroCharsRead);
    }

    @Test(timeout = 4000)
    public void testReadAfterCloseWithZeroLength() throws Throwable {
        char[] buffer = new char[3];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        reader.close();
        try {
            reader.read(buffer, 0, 0);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("com.google.common.io.CharSequenceReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testReadyOnEmptyBuffer() throws Throwable {
        CharBuffer charBuffer = CharBuffer.allocate(0);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        assertTrue(reader.ready());
    }

    @Test(timeout = 4000)
    public void testResetOnBuffer() throws Throwable {
        char[] buffer = new char[2];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        CharSequenceReader reader = new CharSequenceReader(charBuffer);
        reader.reset();
        assertTrue(reader.markSupported());
    }
}