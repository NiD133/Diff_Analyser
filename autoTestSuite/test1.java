package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.CharBuffer;
import org.apache.commons.io.input.BoundedReader;

public class BoundedReaderTest {

    @Test
    public void testReadIntoCharBufferWhenLimitIsZero() throws IOException {
        StringReader stringReader = new StringReader("TO[Gj");
        BoundedReader boundedReader = new BoundedReader(stringReader, 0);
        CharBuffer charBuffer = CharBuffer.wrap("TO[Gj");
        // Since the limit is 0, no characters should be read, and -1 should be returned.
        int charsRead = boundedReader.read(charBuffer);
        assertEquals(-1, charsRead);
    }

    @Test
    public void testMarkAndReadSingleCharacter() throws IOException {
        StringReader stringReader = new StringReader("QN?");
        BoundedReader boundedReader = new BoundedReader(stringReader, 2680);
        // Read the first character ('Q')
        boundedReader.read();
        // Mark the current position (after 'Q')
        boundedReader.mark(2680);
        // Read the next character ('N')
        int nextChar = boundedReader.read();
        // Verify that the next character is 'N' (ASCII 78)
        assertEquals(78, nextChar);
    }

    @Test
    public void testReadZeroCharactersIntoCharArray() throws IOException {
        StringReader stringReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(stringReader, 1148);
        char[] charArray = new char[1];
        // Attempt to read 0 characters from the reader into the array.
        int charsRead = boundedReader.read(charArray, 0, 0);
        // Verify that 0 characters were read.
        assertEquals(0, charsRead);
    }

    @Test
    public void testReadOneCharacterIntoCharArrayWithOffset() throws IOException {
        StringReader stringReader = new StringReader("org.apache.commons.io.input.BoundedReader");
        BoundedReader boundedReader = new BoundedReader(stringReader, 1);
        char[] charArray = new char[6];
        // Read 1 character from the reader into the array, starting at index 1.
        int charsRead = boundedReader.read(charArray, 1, 1);
        // Verify that 1 character was read.
        assertEquals(1, charsRead);
        // Verify that the character 'o' was placed at index 1 of the array.
        assertArrayEquals(new char[] {'\u0000', 'o', '\u0000', '\u0000', '\u0000', '\u0000'}, charArray);
    }

    @Test(expected = NullPointerException.class)
    public void testResetWhenReaderIsNull() throws IOException {
        BoundedReader boundedReader = new BoundedReader(null, 0);
        // Resetting a BoundedReader with a null reader should throw a NullPointerException.
        boundedReader.reset();
    }

    @Test(expected = IOException.class)
    public void testResetAfterClose() throws IOException {
        StringReader stringReader = new StringReader("TO[Gj");
        BoundedReader boundedReader = new BoundedReader(stringReader, 0);
        boundedReader.close();
        // Resetting a BoundedReader after it has been closed should throw an IOException.
        boundedReader.reset();
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullCharArray() throws IOException {
        StringReader stringReader = new StringReader("g`8B;^5");
        BoundedReader boundedReader = new BoundedReader(stringReader, 820);
        // Reading into a null char array should throw a NullPointerException.
        boundedReader.read(null, 103, 78);
    }

    @Test(expected = IOException.class)
    public void testReadIntoCharArrayAfterClose() throws IOException {
        StringReader stringReader = new StringReader("j");
        BoundedReader boundedReader = new BoundedReader(stringReader, 1);
        boundedReader.close();
        char[] charArray = new char[6];
        // Reading from a closed BoundedReader should throw an IOException.
        boundedReader.read(charArray, (-2537), 699);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWhenReaderIsNull() throws IOException {
        BoundedReader boundedReader = new BoundedReader(null, 2482);
        // Reading from a BoundedReader with a null reader should throw a NullPointerException.
        boundedReader.read();
    }

    @Test(expected = IOException.class)
    public void testReadAfterClose() throws IOException {
        StringReader stringReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(stringReader, 2140);
        boundedReader.close();
        // Reading from a closed BoundedReader should throw an IOException.
        boundedReader.read();
    }

    @Test(expected = NullPointerException.class)
    public void testMarkWhenReaderIsNull() throws IOException {
        BoundedReader boundedReader = new BoundedReader(null, 0);
        // Marking a BoundedReader with a null reader should throw a NullPointerException.
        boundedReader.mark(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMarkWithNegativeReadAheadLimit() throws IOException {
        StringReader stringReader = new StringReader("SQ\"Rz~>o\"ggtg97eV");
        BoundedReader boundedReader = new BoundedReader(stringReader, (-585));
        // Marking a BoundedReader with a negative read-ahead limit should throw an IllegalArgumentException.
        boundedReader.mark((-585));
    }

    @Test(expected = NullPointerException.class)
    public void testCloseWhenReaderIsNull() throws IOException {
        BoundedReader boundedReader = new BoundedReader(null, 0);
        // Closing a BoundedReader with a null reader should throw a NullPointerException.
        boundedReader.close();
    }

    @Test
    public void testMarkAndRead() throws IOException {
        StringReader stringReader = new StringReader("Mqy[$oy5nF");
        BoundedReader boundedReader = new BoundedReader(stringReader, 1601);
        boundedReader.mark(1601);
        int charRead = boundedReader.read();
        assertEquals(77, charRead);
    }

    @Test
    public void testBoundedReaderWithNegativeLimitAndThenBoundedReaderWithLimitOne() throws IOException {
        StringReader stringReader = new StringReader("CU6^Ejr;7S;Ndl FK8");
        BoundedReader boundedReader = new BoundedReader(stringReader, (-1821));
        BoundedReader boundedReader1 = new BoundedReader(boundedReader, 1);
        boundedReader1.mark(0);
        int charRead = boundedReader1.read();
        assertEquals(-1, charRead);
    }

    @Test
    public void testBoundedReaderWithNegativeLimitAndThenBoundedReaderWithLimitOneWithoutMark() throws IOException {
        StringReader stringReader = new StringReader("CU6^Ejr;7S;Ndl FK8");
        BoundedReader boundedReader = new BoundedReader(stringReader, (-1821));
        BoundedReader boundedReader1 = new BoundedReader(boundedReader, 1);
        int charRead = boundedReader1.read();
        assertEquals(-1, charRead);
    }

    @Test
    public void testMarkAndReadEndOfStream() throws IOException {
        StringReader stringReader = new StringReader("QN?");
        BoundedReader boundedReader = new BoundedReader(stringReader, 2680);
        char[] charArray = new char[9];
        boundedReader.read(charArray);
        boundedReader.mark(1);
        int charRead = boundedReader.read();
        assertEquals(-1, charRead);
    }

    @Test
    public void testReadWithNegativeOffsetAndNegativeLength() throws IOException {
        StringReader stringReader = new StringReader("pI2");
        BoundedReader boundedReader = new BoundedReader(stringReader, (-2049));
        char[] charArray = new char[14];
        int charsRead = boundedReader.read(charArray, (-2049), (-1));
        assertEquals(-1, charsRead);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testReadWithArrayIndexOutOfBoundsException() throws IOException {
        StringReader stringReader = new StringReader("j");
        BoundedReader boundedReader = new BoundedReader(stringReader, 1);
        boundedReader.mark(699);
        char[] charArray = new char[6];
        boundedReader.read(charArray, (-2537), 699);
    }

    @Test
    public void testReadWithNegativeLimit() throws IOException {
        StringReader stringReader = new StringReader("pI2");
        BoundedReader boundedReader = new BoundedReader(stringReader, (-2049));
        int charRead = boundedReader.read();
        assertEquals(-1, charRead);
    }

    @Test
    public void testReset() throws IOException {
        StringReader stringReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(stringReader, 1);
        boundedReader.reset();
    }

    @Test(expected = IOException.class)
    public void testMarkAfterClose() throws IOException {
        StringReader stringReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(stringReader, 1);
        boundedReader.close();
        boundedReader.mark(0);
    }
}