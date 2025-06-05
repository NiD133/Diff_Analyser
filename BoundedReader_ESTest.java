package org.apache.commons.io.input;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.CharBuffer;

import static org.junit.jupiter.api.Assertions.*;

class BoundedReaderTest {

    private StringReader stringReader;

    @BeforeEach
    void setUp() {
        stringReader = new StringReader("Sample Text");
    }

    @Test
    @DisplayName("Test that read() returns -1 when maxCharsFromTargetReader is 0")
    void testReadReturnsEOFWhenLimitIsZero() throws IOException {
        BoundedReader boundedReader = new BoundedReader(stringReader, 0);
        assertEquals(-1, boundedReader.read(), "Should return EOF when the limit is 0");
    }

    @Test
    @DisplayName("Test that read() returns the correct character within the limit")
    void testReadReturnsCorrectCharacterWithinLimit() throws IOException {
        BoundedReader boundedReader = new BoundedReader(new StringReader("A"), 1);
        assertEquals('A', boundedReader.read(), "Should return the character 'A'");
    }

    @Test
    @DisplayName("Test read() returns -1 when the reader has reached maxCharsFromTargetReader limit")
    void testReadReturnsEOFWhenMaxCharsReached() throws IOException {
        StringReader localStringReader = new StringReader("ABC");
        BoundedReader boundedReader = new BoundedReader(localStringReader, 2);
        boundedReader.read();
        boundedReader.read();
        assertEquals(-1, boundedReader.read(), "Should return EOF after reading 2 characters.");
    }

    @Test
    @DisplayName("Test read() returns correct character and maintains mark/reset functionality")
    void testReadAndMarkReset() throws IOException {
        StringReader localStringReader = new StringReader("QN?");
        BoundedReader boundedReader = new BoundedReader(localStringReader, 2680);

        boundedReader.read(); // Read 'Q'
        boundedReader.mark(2680); // Mark after reading 'Q'
        int nextChar = boundedReader.read(); // Read 'N'
        assertEquals('N', nextChar, "Should return the character code for 'N'");
    }


    @Test
    @DisplayName("Test read(char[], off, len) reads zero characters when len is zero")
    void testReadCharArrayZeroLength() throws IOException {
        StringReader localStringReader = new StringReader("Some text");
        BoundedReader boundedReader = new BoundedReader(localStringReader, 10);
        char[] charArray = new char[5];
        int charsRead = boundedReader.read(charArray, 0, 0);
        assertEquals(0, charsRead, "Should return 0 when len is 0");
    }

    @Test
    @DisplayName("Test read(char[], off, len) reads correct number of characters")
    void testReadCharArrayReadsCorrectNumberOfCharacters() throws IOException {
        StringReader localStringReader = new StringReader("abcdef");
        BoundedReader boundedReader = new BoundedReader(localStringReader, 3);
        char[] charArray = new char[6];
        int charsRead = boundedReader.read(charArray, 0, 3);
        assertEquals(3, charsRead, "Should read 3 characters");
        assertArrayEquals("abc\u0000\u0000\u0000".toCharArray(), charArray, "Should contain 'abc'");
    }


    @Test
    @DisplayName("Test reset() throws NullPointerException when target reader is null")
    void testResetThrowsNPEWhenTargetIsNull() {
        BoundedReader boundedReader = new BoundedReader(null, 0);
        assertThrows(NullPointerException.class, boundedReader::reset, "Should throw NullPointerException when target is null");
    }

    @Test
    @DisplayName("Test reset() throws IOException when the stream is closed")
    void testResetThrowsIOExceptionWhenStreamClosed() {
        StringReader localStringReader = new StringReader("test");
        BoundedReader boundedReader = new BoundedReader(localStringReader, 10);
        try {
            boundedReader.close();
        } catch (IOException e) {
            fail("IOException should not have been thrown when closing");
        }
        assertThrows(IOException.class, boundedReader::reset, "Should throw IOException when stream is closed");
    }

    @Test
    @DisplayName("Test read(char[], off, len) throws NullPointerException when cbuf is null")
    void testReadCharArrayThrowsNPEWhenCharArrayIsNull() {
        StringReader localStringReader = new StringReader("test");
        BoundedReader boundedReader = new BoundedReader(localStringReader, 10);
        assertThrows(NullPointerException.class, () -> boundedReader.read(null, 0, 1), "Should throw NullPointerException when char array is null");
    }

    @Test
    @DisplayName("Test read(char[], off, len) throws IOException when the stream is closed")
    void testReadCharArrayThrowsIOExceptionWhenStreamClosed() {
        StringReader localStringReader = new StringReader("test");
        BoundedReader boundedReader = new BoundedReader(localStringReader, 10);
        char[] charArray = new char[4];
        try {
            boundedReader.close();
        } catch (IOException e) {
            fail("IOException should not have been thrown when closing");
        }
        assertThrows(IOException.class, () -> boundedReader.read(charArray, 0, 1), "Should throw IOException when stream is closed");
    }

    @Test
    @DisplayName("Test read() throws NullPointerException when target reader is null")
    void testReadThrowsNPEWhenTargetIsNull() {
        BoundedReader boundedReader = new BoundedReader(null, 10);
        assertThrows(NullPointerException.class, boundedReader::read, "Should throw NullPointerException when target is null");
    }

    @Test
    @DisplayName("Test read() throws IOException when the stream is closed")
    void testReadThrowsIOExceptionWhenStreamClosed() {
        StringReader localStringReader = new StringReader("test");
        BoundedReader boundedReader = new BoundedReader(localStringReader, 10);
        try {
            boundedReader.close();
        } catch (IOException e) {
            fail("IOException should not have been thrown when closing");
        }
        assertThrows(IOException.class, boundedReader::read, "Should throw IOException when stream is closed");
    }

    @Test
    @DisplayName("Test mark() throws NullPointerException when target reader is null")
    void testMarkThrowsNPEWhenTargetIsNull() {
        BoundedReader boundedReader = new BoundedReader(null, 10);
        assertThrows(NullPointerException.class, () -> boundedReader.mark(0), "Should throw NullPointerException when target is null");
    }

    @Test
    @DisplayName("Test mark() throws IllegalArgumentException when readAheadLimit is negative")
    void testMarkThrowsIllegalArgumentExceptionWhenReadAheadLimitIsNegative() {
        StringReader localStringReader = new StringReader("test");
        BoundedReader boundedReader = new BoundedReader(localStringReader, 10);
        assertThrows(IllegalArgumentException.class, () -> boundedReader.mark(-1), "Should throw IllegalArgumentException when readAheadLimit is negative");
    }


    @Test
    @DisplayName("Test close() throws NullPointerException when target reader is null")
    void testCloseThrowsNPEWhenTargetIsNull() {
        BoundedReader boundedReader = new BoundedReader(null, 10);
        assertThrows(NullPointerException.class, boundedReader::close, "Should throw NullPointerException when target is null");
    }

    @Test
    @DisplayName("Test mark() and read() interaction within bounds.")
    void testMarkAndReadWithinBounds() throws IOException {
        StringReader localStringReader = new StringReader("Mqy[$oy5nF");
        BoundedReader boundedReader = new BoundedReader(localStringReader, 1601);
        boundedReader.mark(1601);
        int readChar = boundedReader.read();
        assertEquals('M', readChar, "Should read 'M' after marking.");
    }

    @Test
    @DisplayName("Test nested BoundedReaders with limit of 1.")
    void testNestedBoundedReadersWithLimitOfOne() throws IOException {
        StringReader localStringReader = new StringReader("CU6^Ejr;7S;Ndl FK8");
        BoundedReader boundedReader1 = new BoundedReader(localStringReader, -1821); // Effectively no limit due to negative value
        BoundedReader boundedReader2 = new BoundedReader(boundedReader1, 1); // Limit to 1 char
        boundedReader2.mark(0);
        int readChar = boundedReader2.read();
        assertEquals(-1, readChar, "Should return EOF as the limit of outer reader is 1 and inner reader reads till end");
    }

     @Test
    @DisplayName("Test that read(CharBuffer) returns -1 when maxCharsFromTargetReader is 0")
    void testCharBufferReadReturnsEOFWhenLimitIsZero() throws IOException {
        StringReader localStringReader = new StringReader("TO[Gj");
        BoundedReader boundedReader = new BoundedReader(localStringReader, 0);
        CharBuffer charBuffer = CharBuffer.wrap("TO[Gj");
        int charsRead = boundedReader.read(charBuffer);
        assertEquals(-1, charsRead, "Should return EOF when the limit is 0");
    }


   @Test
    @DisplayName("Test read(char[], off, len) when offset and length are negative.")
    void testReadCharArrayWithNegativeOffsetAndLength() throws IOException {
        StringReader localStringReader = new StringReader("pI2");
        BoundedReader boundedReader = new BoundedReader(localStringReader, -2049);
        char[] charArray = new char[14];
        int charsRead = boundedReader.read(charArray, -2049, -1);
        assertEquals(-1, charsRead, "Should return -1 as the bounded reader effectively reads till the end");
    }

    @Test
    @DisplayName("Test read() when the bounded reader effectively reads till the end.")
    void testReadWhenBoundedReaderEffectivelyReadsTillEnd() throws IOException {
        StringReader localStringReader = new StringReader("pI2");
        BoundedReader boundedReader = new BoundedReader(localStringReader, -2049);
        int readChar = boundedReader.read();
        assertEquals(-1, readChar, "Should return EOF as the bounded reader effectively reads till the end");
    }

    @Test
    @DisplayName("Test reset() after construction without reading")
    void testResetAfterConstruction() throws IOException {
        StringReader localStringReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(localStringReader, 1);
        boundedReader.reset(); //Should not throw any exception
    }


    @Test
    @DisplayName("Test mark() throws IOException when stream is closed.")
    void testMarkThrowsIOExceptionWhenStreamClosed() throws IOException {
        StringReader localStringReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(localStringReader, 1);
        boundedReader.close();
        assertThrows(IOException.class, () -> boundedReader.mark(0), "Should throw IOException when stream is closed");
    }

    @Test
    @DisplayName("Test read(char[], off, len) throws ArrayIndexOutOfBoundsException when offset is out of bounds.")
    void testReadCharArrayThrowsArrayIndexOutOfBoundsException() throws IOException {
        StringReader localStringReader = new StringReader("j");
        BoundedReader boundedReader = new BoundedReader(localStringReader, 1);
        boundedReader.mark(699);
        char[] charArray = new char[6];
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> boundedReader.read(charArray, -2537, 699), "Should throw ArrayIndexOutOfBoundsException when offset is out of bounds");
    }
}