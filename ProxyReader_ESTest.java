package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PipedReader;
import java.io.StringReader;
import java.nio.CharBuffer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockIOException;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ProxyReader_ESTest extends ProxyReader_ESTest_scaffolding {

    // Test TeeReader's beforeRead method with a PipedReader and CharArrayWriter
    @Test(timeout = 4000)
    public void testTeeReaderBeforeRead() throws Throwable {
        PipedReader pipedReader = new PipedReader(1018);
        CharArrayWriter charArrayWriter = new CharArrayWriter(1018);
        TeeReader teeReader = new TeeReader(pipedReader, charArrayWriter);
        teeReader.beforeRead(0);
    }

    // Test CloseShieldReader's afterRead method with a StringReader
    @Test(timeout = 4000)
    public void testCloseShieldReaderAfterRead() throws Throwable {
        StringReader stringReader = new StringReader("");
        CloseShieldReader closeShieldReader = CloseShieldReader.wrap(stringReader);
        closeShieldReader.afterRead(1139);
    }

    // Test TaggedReader's reset method
    @Test(timeout = 4000)
    public void testTaggedReaderReset() throws Throwable {
        StringReader stringReader = new StringReader("*=jeP");
        TaggedReader taggedReader = new TaggedReader(stringReader);
        taggedReader.reset();
    }

    // Test TaggedReader's mark method
    @Test(timeout = 4000)
    public void testTaggedReaderMark() throws Throwable {
        StringReader stringReader = new StringReader("*=jeP");
        TaggedReader taggedReader = new TaggedReader(stringReader);
        taggedReader.mark(47);
    }

    // Test TaggedReader's skip method
    @Test(timeout = 4000)
    public void testTaggedReaderSkip() throws Throwable {
        StringReader stringReader = new StringReader("6+pe[XK?~jcz*N&o]");
        TaggedReader taggedReader = new TaggedReader(stringReader);
        long skipped = taggedReader.skip(75);
        assertEquals(17L, skipped);
    }

    // Test CloseShieldReader's close and ready methods
    @Test(timeout = 4000)
    public void testCloseShieldReaderCloseAndReady() throws Throwable {
        StringReader stringReader = new StringReader("$^pT");
        CloseShieldReader closeShieldReader = CloseShieldReader.wrap(stringReader);
        closeShieldReader.close();
        boolean isReady = closeShieldReader.ready();
        assertFalse(isReady);
    }

    // Test TaggedReader's read method with zero length
    @Test(timeout = 4000)
    public void testTaggedReaderReadZeroLength() throws Throwable {
        StringReader stringReader = new StringReader("6+pe[XK?~jcz*N&o]");
        TaggedReader taggedReader = new TaggedReader(stringReader);
        char[] buffer = new char[1];
        int read = taggedReader.read(buffer, 0, 0);
        assertEquals(0, read);
    }

    // Test CloseShieldReader's read method
    @Test(timeout = 4000)
    public void testCloseShieldReaderRead() throws Throwable {
        StringReader stringReader = new StringReader("6+pe[XK?~jcz*N&o]");
        TaggedReader taggedReader = new TaggedReader(stringReader);
        CloseShieldReader closeShieldReader = CloseShieldReader.wrap(taggedReader);
        char[] buffer = new char[4];
        int read = closeShieldReader.read(buffer, 1, 1);
        assertArrayEquals(new char[]{'\u0000', '6', '\u0000', '\u0000'}, buffer);
        assertEquals(1, read);
    }

    // Test CloseShieldReader's read method after close
    @Test(timeout = 4000)
    public void testCloseShieldReaderReadAfterClose() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        CloseShieldReader closeShieldReader = CloseShieldReader.wrap(pipedReader);
        char[] buffer = new char[4];
        closeShieldReader.close();
        int read = closeShieldReader.read(buffer, 0, 1041);
        assertEquals(-1, read);
    }

    // Test TaggedReader's read method with buffer
    @Test(timeout = 4000)
    public void testTaggedReaderReadWithBuffer() throws Throwable {
        StringReader stringReader = new StringReader("6+pe[XK?~jcz*N&o]");
        TaggedReader taggedReader = new TaggedReader(stringReader);
        char[] buffer = new char[6];
        int read = taggedReader.read(buffer);
        assertEquals(6, read);
    }

    // Test TaggedReader's read method with empty StringReader
    @Test(timeout = 4000)
    public void testTaggedReaderReadEmpty() throws Throwable {
        StringReader stringReader = new StringReader("");
        TaggedReader taggedReader = new TaggedReader(stringReader);
        char[] buffer = new char[3];
        int read = taggedReader.read(buffer);
        assertEquals(-1, read);
    }

    // Test TaggedReader's read method with CharBuffer
    @Test(timeout = 4000)
    public void testTaggedReaderReadCharBuffer() throws Throwable {
        StringReader stringReader = new StringReader("");
        TaggedReader taggedReader = new TaggedReader(stringReader);
        CharBuffer charBuffer = CharBuffer.wrap("");
        int read = taggedReader.read(charBuffer);
        assertEquals(0, read);
    }

    // Test TaggedReader's read method with CharBuffer and empty StringReader
    @Test(timeout = 4000)
    public void testTaggedReaderReadCharBufferEmpty() throws Throwable {
        StringReader stringReader = new StringReader("");
        TaggedReader taggedReader = new TaggedReader(stringReader);
        char[] buffer = new char[4];
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        int read = taggedReader.read(charBuffer);
        assertEquals(-1, read);
    }

    // Test TeeReader's markSupported method
    @Test(timeout = 4000)
    public void testTeeReaderMarkSupported() throws Throwable {
        StringReader stringReader = new StringReader("6+pe[XK?~jcz*N&o]");
        CharArrayWriter charArrayWriter = new CharArrayWriter(1018);
        TeeReader teeReader = new TeeReader(stringReader, charArrayWriter);
        boolean isMarkSupported = teeReader.markSupported();
        assertTrue(isMarkSupported);
    }

    // Test TaggedReader's skip method with IOException
    @Test(timeout = 4000)
    public void testTaggedReaderSkipIOException() throws Throwable {
        PipedReader pipedReader = new PipedReader(1018);
        TaggedReader taggedReader = new TaggedReader(pipedReader);
        try {
            taggedReader.skip(1018);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.io.input.TaggedReader", e);
        }
    }

    // Test TaggedReader's skip method with IllegalArgumentException
    @Test(timeout = 4000)
    public void testTaggedReaderSkipNegative() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        TaggedReader taggedReader = new TaggedReader(pipedReader);
        try {
            taggedReader.skip(-1L);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("java.io.Reader", e);
        }
    }

    // Test CloseShieldReader's skip method with IOException
    @Test(timeout = 4000)
    public void testCloseShieldReaderSkipIOException() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        CloseShieldReader closeShieldReader = CloseShieldReader.wrap(pipedReader);
        try {
            closeShieldReader.skip(2443L);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedReader", e);
        }
    }

    // Test TaggedReader's reset method with IOException
    @Test(timeout = 4000)
    public void testTaggedReaderResetIOException() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        TaggedReader taggedReader = new TaggedReader(pipedReader);
        try {
            taggedReader.reset();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.io.input.TaggedReader", e);
        }
    }

    // Test TaggedReader's ready method with IOException
    @Test(timeout = 4000)
    public void testTaggedReaderReadyIOException() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        TaggedReader taggedReader = new TaggedReader(pipedReader);
        try {
            taggedReader.ready();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.io.input.TaggedReader", e);
        }
    }

    // Test CloseShieldReader's ready method with IOException
    @Test(timeout = 4000)
    public void testCloseShieldReaderReadyIOException() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        CloseShieldReader closeShieldReader = CloseShieldReader.wrap(pipedReader);
        try {
            closeShieldReader.ready();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedReader", e);
        }
    }

    // Test TaggedReader's read method with null character array
    @Test(timeout = 4000)
    public void testTaggedReaderReadNullArray() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        CloseShieldReader closeShieldReader = CloseShieldReader.wrap(pipedReader);
        TaggedReader taggedReader = new TaggedReader(closeShieldReader);
        try {
            taggedReader.read((char[]) null, 477, 477);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.io.input.TaggedReader", e);
        }
    }

    // Test CloseShieldReader's read method with null character array
    @Test(timeout = 4000)
    public void testCloseShieldReaderReadNullArray() throws Throwable {
        StringReader stringReader = new StringReader("");
        CloseShieldReader closeShieldReader = CloseShieldReader.wrap(stringReader);
        try {
            closeShieldReader.read((char[]) null, 63, 63);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    // Test CloseShieldReader's read method with out of bounds index
    @Test(timeout = 4000)
    public void testCloseShieldReaderReadOutOfBounds() throws Throwable {
        StringReader stringReader = new StringReader("");
        CloseShieldReader closeShieldReader = CloseShieldReader.wrap(stringReader);
        char[] buffer = new char[0];
        try {
            closeShieldReader.read(buffer, 49, 49);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    // Test CloseShieldReader's read method with null character array
    @Test(timeout = 4000)
    public void testCloseShieldReaderReadNullArrayIOException() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        CloseShieldReader closeShieldReader = CloseShieldReader.wrap(pipedReader);
        try {
            closeShieldReader.read((char[]) null, 477, 477);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedReader", e);
        }
    }

    // Test CloseShieldReader's read method with empty character array
    @Test(timeout = 4000)
    public void testCloseShieldReaderReadEmptyArray() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        TaggedReader taggedReader = new TaggedReader(pipedReader);
        CloseShieldReader closeShieldReader = CloseShieldReader.wrap(taggedReader);
        char[] buffer = new char[0];
        try {
            closeShieldReader.read(buffer);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.io.input.TaggedReader", e);
        }
    }

    // Test CloseShieldReader's read method with null character array
    @Test(timeout = 4000)
    public void testCloseShieldReaderReadNullArrayException() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        CloseShieldReader closeShieldReader = CloseShieldReader.wrap(pipedReader);
        try {
            closeShieldReader.read((char[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.io.Reader", e);
        }
    }

    // Test CloseShieldReader's read method with empty character array
    @Test(timeout = 4000)
    public void testCloseShieldReaderReadEmptyArrayIOException() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        CloseShieldReader closeShieldReader = CloseShieldReader.wrap(pipedReader);
        char[] buffer = new char[0];
        try {
            closeShieldReader.read(buffer);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedReader", e);
        }
    }

    // Test TaggedReader's read method with CharBuffer and IOException
    @Test(timeout = 4000)
    public void testTaggedReaderReadCharBufferIOException() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        TaggedReader taggedReader = new TaggedReader(pipedReader);
        CharBuffer charBuffer = CharBuffer.allocate(1);
        try {
            taggedReader.read(charBuffer);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.io.input.TaggedReader", e);
        }
    }

    // Test TaggedReader's read method with null CharBuffer
    @Test(timeout = 4000)
    public void testTaggedReaderReadNullCharBuffer() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        TaggedReader taggedReader = new TaggedReader(pipedReader);
        try {
            taggedReader.read((CharBuffer) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.io.Reader", e);
        }
    }

    // Test CloseShieldReader's read method with CharBuffer and IOException
    @Test(timeout = 4000)
    public void testCloseShieldReaderReadCharBufferIOException() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        CloseShieldReader closeShieldReader = CloseShieldReader.wrap(pipedReader);
        CharBuffer charBuffer = CharBuffer.allocate(13);
        try {
            closeShieldReader.read(charBuffer);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedReader", e);
        }
    }

    // Test TaggedReader's read method with IOException
    @Test(timeout = 4000)
    public void testTaggedReaderReadIOException() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        TaggedReader taggedReader = new TaggedReader(pipedReader);
        try {
            taggedReader.read();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.io.input.TaggedReader", e);
        }
    }

    // Test CloseShieldReader's read method with IOException
    @Test(timeout = 4000)
    public void testCloseShieldReaderReadIOException() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        CloseShieldReader closeShieldReader = CloseShieldReader.wrap(pipedReader);
        try {
            closeShieldReader.read();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedReader", e);
        }
    }

    // Test TaggedReader's mark method with IOException
    @Test(timeout = 4000)
    public void testTaggedReaderMarkIOException() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        TaggedReader taggedReader = new TaggedReader(pipedReader);
        try {
            taggedReader.mark(1027);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.io.input.TaggedReader", e);
        }
    }

    // Test CloseShieldReader's mark method with IOException
    @Test(timeout = 4000)
    public void testCloseShieldReaderMarkIOException() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        CloseShieldReader closeShieldReader = CloseShieldReader.wrap(pipedReader);
        try {
            closeShieldReader.mark(308);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.Reader", e);
        }
    }

    // Test TaggedReader's handleIOException method
    @Test(timeout = 4000)
    public void testTaggedReaderHandleIOException() throws Throwable {
        PipedReader pipedReader = new PipedReader(2398);
        TaggedReader taggedReader = new TaggedReader(pipedReader);
        MockIOException mockIOException = new MockIOException("Y-XdQt@");
        try {
            taggedReader.handleIOException(mockIOException);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.io.input.TaggedReader", e);
        }
    }

    // Test CloseShieldReader's handleIOException method with null exception
    @Test(timeout = 4000)
    public void testCloseShieldReaderHandleNullIOException() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        CloseShieldReader closeShieldReader = CloseShieldReader.wrap(pipedReader);
        try {
            closeShieldReader.handleIOException(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.input.ProxyReader", e);
        }
    }

    // Test CloseShieldReader's handleIOException method
    @Test(timeout = 4000)
    public void testCloseShieldReaderHandleIOException() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        CloseShieldReader closeShieldReader = CloseShieldReader.wrap(pipedReader);
        MockIOException mockIOException = new MockIOException();
        try {
            closeShieldReader.handleIOException(mockIOException);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            // Expected exception
        }
    }

    // Test TaggedReader's read method with empty StringReader
    @Test(timeout = 4000)
    public void testTaggedReaderReadEmptyStringReader() throws Throwable {
        StringReader stringReader = new StringReader("");
        TaggedReader taggedReader = new TaggedReader(stringReader);
        int read = taggedReader.read();
        assertEquals(-1, read);
    }

    // Test TaggedReader's read method with CharBuffer and non-empty StringReader
    @Test(timeout = 4000)
    public void testTaggedReaderReadCharBufferNonEmpty() throws Throwable {
        StringReader stringReader = new StringReader("org.apache.commons.io.input.ProxyReader");
        TaggedReader taggedReader = new TaggedReader(stringReader);
        CharBuffer charBuffer = CharBuffer.allocate(1366);
        int read = taggedReader.read(charBuffer);
        assertEquals(39, read);
    }

    // Test TaggedReader's markSupported method
    @Test(timeout = 4000)
    public void testTaggedReaderMarkSupported() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        TaggedReader taggedReader = new TaggedReader(pipedReader);
        boolean isMarkSupported = taggedReader.markSupported();
        assertFalse(isMarkSupported);
    }

    // Test CloseShieldReader's ready method with non-empty StringReader
    @Test(timeout = 4000)
    public void testCloseShieldReaderReadyNonEmpty() throws Throwable {
        StringReader stringReader = new StringReader("$^pT");
        CloseShieldReader closeShieldReader = CloseShieldReader.wrap(stringReader);
        boolean isReady = closeShieldReader.ready();
        assertTrue(isReady);
    }

    // Test TaggedReader's close method
    @Test(timeout = 4000)
    public void testTaggedReaderClose() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        TaggedReader taggedReader = new TaggedReader(pipedReader);
        taggedReader.close();
    }

    // Test TaggedReader's read method with empty character array
    @Test(timeout = 4000)
    public void testTaggedReaderReadEmptyArray() throws Throwable {
        StringReader stringReader = new StringReader("");
        TaggedReader taggedReader = new TaggedReader(stringReader);
        char[] buffer = new char[0];
        int read = taggedReader.read(buffer);
        assertEquals(0, read);
    }

    // Test TaggedReader's mark method with negative read-ahead limit
    @Test(timeout = 4000)
    public void testTaggedReaderMarkNegative() throws Throwable {
        StringReader stringReader = new StringReader("6+pe[XK?~jcz*N&o]");
        TaggedReader taggedReader = new TaggedReader(stringReader);
        try {
            taggedReader.mark(-1520);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("java.io.StringReader", e);
        }
    }

    // Test CloseShieldReader's reset method with IOException
    @Test(timeout = 4000)
    public void testCloseShieldReaderResetIOException() throws Throwable {
        PipedReader pipedReader = new PipedReader();
        CloseShieldReader closeShieldReader = CloseShieldReader.wrap(pipedReader);
        try {
            closeShieldReader.reset();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.Reader", e);
        }
    }

    // Test CloseShieldReader's read method with non-empty StringReader
    @Test(timeout = 4000)
    public void testCloseShieldReaderReadNonEmpty() throws Throwable {
        StringReader stringReader = new StringReader("$^pT");
        CloseShieldReader closeShieldReader = CloseShieldReader.wrap(stringReader);
        int read = closeShieldReader.read();
        assertEquals(36, read);
    }

    // Test TaggedReader's skip method with negative value
    @Test(timeout = 4000)
    public void testTaggedReaderSkipNegativeValue() throws Throwable {
        StringReader stringReader = new StringReader("");
        TaggedReader taggedReader = new TaggedReader(stringReader);
        long skipped = taggedReader.skip(-1);
        assertEquals(0L, skipped);
    }
}