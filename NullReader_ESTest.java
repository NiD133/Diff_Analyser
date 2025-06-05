package org.apache.commons.io.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.EOFException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class NullReaderTest {

    private NullReader defaultNullReader;

    @BeforeEach
    void setUp() {
        defaultNullReader = new NullReader();
    }

    @Test
    void testReadAndMarkReset() throws IOException {
        // Given a NullReader with a negative size
        NullReader nullReader = new NullReader(-1L);

        // When we read a character and then mark the current position
        nullReader.read();
        nullReader.mark(0);

        // And then reset to the marked position
        nullReader.reset();

        // Then the position should be 1
        assertEquals(1L, nullReader.getPosition());
    }

    @Test
    void testReadWithCharArrayAndAdjustPosition() throws IOException {
        // Given a NullReader with a negative size and a char array
        NullReader nullReader = new NullReader(-1L);
        char[] charArray = new char[8];

        // When we read into the char array with negative offset and length, then read a single char
        nullReader.read(charArray, -1, -3484);
        int result = nullReader.read();

        // Then the position should be adjusted correctly and read() should return 0
        assertEquals(-3483L, nullReader.getPosition());
        assertEquals(0, result);
    }

    @Test
    void testProcessCharsDoesNothingByDefault() {
        // Given a NullReader and a char array
        NullReader nullReader = new NullReader();
        char[] charArray = new char[2];

        // When we call processChars
        nullReader.processChars(charArray, 1542, 1542);

        // Then markSupported should still return true (no side effects)
        assertTrue(nullReader.markSupported());
    }

    @Test
    void testSkipZeroCharacters() throws IOException {
        // Given a NullReader with a specified size
        NullReader nullReader = new NullReader(597L);

        // When we skip 0 characters
        long skipped = nullReader.skip(0);

        // Then the number of skipped characters should be 0, and mark should be supported
        assertEquals(0L, skipped);
        assertTrue(nullReader.markSupported());
    }

    @Test
    void testSkipCharactersAndAdjustPosition() throws IOException {
        // Given a NullReader with a specified size
        NullReader nullReader = new NullReader(1451L, true, true);

        // When we skip the entire size
        long skipped = nullReader.skip(1451L);

        // Then the position should be updated correctly and the skipped value equal to the size.
        assertEquals(1451L, nullReader.getPosition());
        assertEquals(1451L, skipped);
    }

    @Test
    void testReadCharArrayWithZeroLength() throws IOException {
        // Given a NullReader and an empty char array
        NullReader nullReader = new NullReader(1279L);
        char[] charArray = new char[4];

        // When we read 0 characters into the array
        int read = nullReader.read(charArray, 938, 0);

        // Then the number of characters read should be 0, markSupported is still true and position unchanged
        assertEquals(0, read);
        assertTrue(nullReader.markSupported());
        assertEquals(0L, nullReader.getPosition());
    }

    @Test
    void testReadCharArrayExceedingSize() throws IOException {
        // Given a NullReader with a size and a char array
        NullReader nullReader = new NullReader(1592L);
        char[] charArray = new char[5];

        // When we attempt to read more characters than the reader's size
        int read = nullReader.read(charArray, 2146694131, 2146694131);

        // Then the number of characters read should be limited to the reader's size and position is updated
        assertEquals(1592L, nullReader.getPosition());
        assertEquals(1592, read);
    }

    @Test
    void testReadFromEmptyCharArray() throws IOException {
        // Given a NullReader and an empty char array
        NullReader nullReader = new NullReader(1254L);
        char[] charArray = new char[0];

        // When we read from the empty array
        int read = nullReader.read(charArray);

        // Then the number of characters read should be 0 and mark is supported
        assertEquals(0, read);
        assertTrue(nullReader.markSupported());
    }

    @Test
    void testReadCharArrayWithinSize() throws IOException {
        // Given a NullReader with a size, markSupported and throwEofException enabled, and a char array
        NullReader nullReader = new NullReader(1480L, true, true);
        char[] charArray = new char[3];

        // When we read the char array
        int read = nullReader.read(charArray);

        // Then the number of characters read should equal to the array length and position is updated
        assertEquals(3L, nullReader.getPosition());
        assertEquals(3, read);
    }

    @Test
    void testProcessCharReturnsZeroByDefault() {
        // Given a NullReader with specific parameters
        NullReader nullReader = new NullReader(-1401L, true, false);

        // When we call processChar
        int result = nullReader.processChar();

        // Then the size should remain unchanged, markSupported is true and the return value is 0
        assertEquals(-1401L, nullReader.getSize());
        assertTrue(nullReader.markSupported());
        assertEquals(0, result);
    }

    @Test
    void testMarkNotSupportedByDefault() {
        // Given a NullReader that does not support mark and throws no exception on eof
        NullReader nullReader = new NullReader(0L, false, false);

        // When we check if mark is supported
        boolean supported = nullReader.markSupported();

        // Then it should return false
        assertFalse(supported);
    }

    @Test
    void testGetSizeReturnsCorrectSize() {
        // Given the INSTANCE NullReader
        NullReader nullReader = NullReader.INSTANCE;

        // When we get its size
        long size = nullReader.getSize();

        // Then it should return 0
        assertEquals(0L, size);
    }

    @Test
    void testGetSizeReturnsSetValue() {
        // Given a NullReader with a specified size
        NullReader nullReader = new NullReader(1279L);

        // When we get its size
        long size = nullReader.getSize();

        // Then it should return the specified size
        assertEquals(1279L, size);
    }

    @Test
    void testGetPositionAfterRead() throws IOException {
        // Given a NullReader with a negative size
        NullReader nullReader = new NullReader(-961L);

        // When we read a character
        nullReader.read();

        // Then the position should be incremented
        assertEquals(1L, nullReader.getPosition());
    }

    @Test
    void testReadZeroLengthCharArrayThrowsEofExceptionWhenSizeIsZero() {
        // Given a NullReader with size 0, mark supported and throwEofException enabled
        NullReader nullReader = new NullReader(0L, true, true);
        char[] charArray = new char[7];

        // When we try to read with offset and length equals to 0
        // Then an EOFException should be thrown
        assertThrows(EOFException.class, () -> nullReader.read(charArray, 0, 0));
    }

    @Test
    void testReadNullCharArrayThrowsNullPointerException() {
        // Given a NullReader
        NullReader nullReader = new NullReader(-955L);

        // When we try to read a null char array
        // Then a NullPointerException should be thrown
        assertThrows(NullPointerException.class, () -> NullReader.INSTANCE.read((char[]) null));
    }

    @Test
    void testReadZeroLengthCharArrayThrowsEofException() {
        // Given a NullReader with size 0 and throwEofException enabled
        NullReader nullReader = new NullReader(0L, false, true);
        char[] charArray = new char[2];

        // When we try to read the char array
        // Then an EOFException should be thrown
        assertThrows(EOFException.class, () -> nullReader.read(charArray));
    }

    @Test
    void testReadThrowsEofExceptionAfterSkippingPastEnd() {
        // Given a NullReader with a negative size, throwEofException enabled
        NullReader nullReader = new NullReader(-2271L, false, true);

        // When we skip past the end and attempt to read
        nullReader.skip(-2271L);

        // Then an EOFException should be thrown
        assertThrows(EOFException.class, nullReader::read);
    }

    @Test
    void testSkipThrowsIOExceptionAfterReadingAllChars() {
        // Given a NullReader
        NullReader nullReader = new NullReader();
        char[] charArray = new char[5];
        nullReader.read(charArray, 640, 0);
        // When we attempt to skip
        // Then an IOException should be thrown
        assertThrows(IOException.class, () -> nullReader.skip(0L));
    }

    @Test
    void testResetThrowsIOExceptionWhenMarkIsInvalid() {
        // Given a NullReader
        NullReader nullReader = new NullReader();

        // When we mark with a negative read limit and reset
        nullReader.mark(-1791);

        // Then an IOException should be thrown
        assertThrows(IOException.class, nullReader::reset);
    }

    @Test
    void testReadAndGetPositionForInstance() throws IOException {
        // Given the INSTANCE NullReader
        NullReader nullReader = NullReader.INSTANCE;

        // When we get the position and then read a char
        nullReader.getPosition();
        nullReader.read();

        // Then no exceptions are thrown
    }

    @Test
    void testSkipReturnsNegativeWhenSizeIsNegative() throws IOException {
        // Given a NullReader with negative size
        NullReader nullReader = new NullReader(-1324L);
        int int0 = 8;
        // When we skip
        long long0 = nullReader.skip(int0);

        // Then the position and return should be the size of the NullReader
        assertEquals((-1324L), nullReader.getPosition());
        assertEquals((-1324L), long0);
    }

    @Test
    void testResetThrowsUnsupportedOperationExceptionWhenMarkingNotSupported() {
        // Given a NullReader that doesn't support mark and doesn't throw exceptions on eof
        NullReader nullReader = new NullReader(247L, false, false);

        // When we try to reset
        // Then an UnsupportedOperationException should be thrown
        assertThrows(UnsupportedOperationException.class, nullReader::reset);
    }

    @Test
    void testResetThrowsIOExceptionWhenNoPositionMarked() {
        // Given a NullReader with size 0, mark supported and throwEofException disabled
        NullReader nullReader = new NullReader(0L);

        // When we try to reset
        // Then IOException should be thrown
        assertThrows(IOException.class, nullReader::reset);
    }

    @Test
    void testMarkAfterReadShouldNotThrowException() throws IOException {
        // Given a NullReader with negative size
        NullReader nullReader = new NullReader((-955L));

        // When we do operations as described in the original test case
        nullReader.INSTANCE.mark(1180);
        nullReader.getPosition();
        char[] charArray0 = new char[1];
        char char0 = 'U';
        charArray0[0] = 'U';
        nullReader.read(charArray0, 1180, 1180);
        nullReader.read();
        char[] charArray1 = new char[9];
        charArray1[0] = 'U';
        nullReader.getPosition();
        nullReader.INSTANCE.read(charArray1, (-1056), (-196));

        // Then no exception should be thrown
    }

    @Test
    void testMarkThrowsUnsupportedOperationExceptionWhenMarkingNotSupported() {
        // Given a NullReader that doesn't support mark and doesn't throw exceptions on eof
        NullReader nullReader = new NullReader(10L, false, false);

        // When we try to mark
        // Then an UnsupportedOperationException should be thrown
        assertThrows(UnsupportedOperationException.class, () -> nullReader.mark(1452));
    }

    @Test
    void testSkipThrowsEofExceptionAfterSkippingPastEndWhenEofExceptionEnabled() {
        // Given a NullReader with negative size, mark supported and throwEofException enabled
        NullReader nullReader = new NullReader((-348L), true, true);

        // When we skip and try to skip again
        nullReader.skip((-348L));

        // Then an EOFException should be thrown
        assertThrows(EOFException.class, () -> nullReader.skip((-348L)));
    }

    @Test
    void testCloseResetsPositionAndEof() throws IOException {
        // Given the INSTANCE NullReader
        NullReader nullReader = NullReader.INSTANCE;

        // When we close the reader
        nullReader.close();

        // Then the position should be reset
        assertEquals(0L, nullReader.getPosition());
    }

    @Test
    void testGetSizeReturnsSetValueEvenIfNegative() {
        // Given a NullReader with negative size
        NullReader nullReader = new NullReader((-1324L));

        // When we get its size
        long size = nullReader.getSize();

        // Then the returned value should be the provided one
        assertTrue(nullReader.markSupported());
        assertEquals((-1324L), size);
    }

    @Test
    void testReadReturnsNegativeOneAfterReadingAllChars() throws IOException {
        // Given a NullReader with negative size
        NullReader nullReader = new NullReader((-970L));

        // When we read all the chars
        char[] charArray0 = new char[14];
        nullReader.read(charArray0);
        int int0 = nullReader.read();

        // Then the position should be updated and the return should be -1
        assertEquals((-970L), nullReader.getPosition());
        assertEquals((-1), int0);
    }

    @Test
    void testMarkSupportedReturnsTrueByDefault() {
        // Given a NullReader with negative size
        NullReader nullReader = new NullReader((-1324L));

        // When we check if mark is supported
        boolean markSupported = nullReader.markSupported();

        // Then the size should be set and the returned value should be true
        assertEquals((-1324L), nullReader.getSize());
        assertTrue(markSupported);
    }
}