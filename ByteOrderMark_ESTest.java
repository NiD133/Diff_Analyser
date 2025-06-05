package org.apache.commons.io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.io.ByteOrderMark;

public class ByteOrderMarkTest {

    @Test
    void testUTF16BEMatchesIncorrectIntArray() {
        // Arrange
        ByteOrderMark utf16BE = ByteOrderMark.UTF_16BE;
        int[] incorrectArray = {0xFEFF, 0x00}; // Incorrect array, should start with 0xFE 0xFF

        // Act
        boolean matches = utf16BE.matches(incorrectArray);

        // Assert
        assertFalse(matches, "UTF-16BE should not match an array that doesn't start with its bytes.");
    }

    @Test
    void testUTF16BEEqualsUTF16LE() {
        // Arrange
        ByteOrderMark utf16BE = ByteOrderMark.UTF_16BE;
        ByteOrderMark utf16LE = ByteOrderMark.UTF_16LE;

        // Act
        boolean areEqual = utf16BE.equals(utf16LE);

        // Assert
        assertFalse(areEqual, "UTF-16BE and UTF-16LE should not be equal.");
    }

    @Test
    void testUTF16LEEqualsUTF32LE() {
        // Arrange
        ByteOrderMark utf16LE = ByteOrderMark.UTF_16LE;
        ByteOrderMark utf32LE = ByteOrderMark.UTF_32LE;

        // Act
        boolean areEqual = utf16LE.equals(utf32LE);

        // Assert
        assertFalse(areEqual, "UTF-16LE and UTF-32LE should not be equal.");
    }

    @Test
    void testUTF16LEEqualsItself() {
        // Arrange
        ByteOrderMark utf16LE = ByteOrderMark.UTF_16LE;

        // Act
        boolean areEqual = utf16LE.equals(utf16LE);

        // Assert
        assertTrue(areEqual, "An object should be equal to itself.");
    }

    @Test
    void testUTF32LEGetBytesAtIndex2() {
        // Arrange
        ByteOrderMark utf32LE = ByteOrderMark.UTF_32LE;

        // Act
        int byteAtIndex2 = utf32LE.get(2);

        // Assert
        assertEquals(0x00, byteAtIndex2, "The byte at index 2 of UTF-32LE should be 0x00.");
    }

    @Test
    void testUTF32LEGetBytesAtIndex1() {
        // Arrange
        ByteOrderMark utf32LE = ByteOrderMark.UTF_32LE;

        // Act
        int byteAtIndex1 = utf32LE.get(1);

        // Assert
        assertEquals(0xFE, byteAtIndex1, "The byte at index 1 of UTF-32LE should be 0xFE.");
    }

    @Test
    void testCustomBOMGetBytesAtIndex0() {
        // Arrange
        int[] bytes = {-8, 0, 0, 0};
        ByteOrderMark customBOM = new ByteOrderMark("+U", bytes);

        // Act
        int byteAtIndex0 = customBOM.get(0);

        // Assert
        assertEquals(-8, byteAtIndex0, "The byte at index 0 of the custom BOM should be -8.");
    }

    @Test
    void testConstructorWithNullBytesThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> new ByteOrderMark("pLt'", (int[]) null),
                "Constructor with null bytes should throw NullPointerException.");
    }

    @Test
    void testUTF16BELengthIs2() {
        // Arrange
        ByteOrderMark utf16BE = ByteOrderMark.UTF_16BE;

        // Act
        int length = utf16BE.length();

        // Assert
        assertEquals(2, length, "UTF-16BE should have a length of 2.");
    }

    @Test
    void testUTF16BEGetBytesAtIndexOutOfBoundsThrowsArrayIndexOutOfBoundsException() {
        // Arrange
        ByteOrderMark utf16BE = ByteOrderMark.UTF_16BE;

        // Act & Assert
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> utf16BE.get(2),
                "Accessing an index out of bounds should throw ArrayIndexOutOfBoundsException.");
    }

    @Test
    void testUTF8ToString() {
        // Arrange
        ByteOrderMark utf8 = ByteOrderMark.UTF_8;

        // Act
        String stringRepresentation = utf8.toString();

        // Assert
        assertEquals("ByteOrderMark[UTF-8: 0xEF,0xBB,0xBF]", stringRepresentation,
                "The toString() method should return the correct string representation.");
    }

    @Test
    void testUTF16LEMatchesShortIntArray() {
        // Arrange
        ByteOrderMark utf16LE = ByteOrderMark.UTF_16LE;
        int[] shortArray = {0, 0, 0};

        // Act
        boolean matches = utf16LE.matches(shortArray);

        // Assert
        assertFalse(matches, "UTF-16LE should not match a short array.");
    }

    @Test
    void testUTF8MatchesSingleElementIntArray() {
        // Arrange
        int[] singleElementArray = new int[1];
        ByteOrderMark utf8 = ByteOrderMark.UTF_8;

        // Act
        boolean matches = utf8.matches(singleElementArray);

        // Assert
        assertFalse(matches, "UTF-8 should not match a single element array.");
    }

    @Test
    void testUTF16BEMatchesNullIntArray() {
        // Arrange
        ByteOrderMark utf16BE = ByteOrderMark.UTF_16BE;

        // Act
        boolean matches = utf16BE.matches(null);

        // Assert
        assertFalse(matches, "UTF-16BE should not match a null array.");
    }

    @Test
    void testUTF8MatchesItsOwnRawBytes() {
        // Arrange
        ByteOrderMark utf8 = ByteOrderMark.UTF_8;
        int[] rawBytes = utf8.getRawBytes();

        // Act
        boolean matches = utf8.matches(rawBytes);

        // Assert
        assertTrue(matches, "UTF-8 should match its own raw bytes.");
    }

    @Test
    void testUTF8HashCodeIsConsistent() {
        // Arrange
        ByteOrderMark utf8 = ByteOrderMark.UTF_8;

        // Act
        int hashCode1 = utf8.hashCode();
        int hashCode2 = utf8.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2, "Hash code should be consistent for the same object.");
    }

    @Test
    void testUTF8GetBytesReturnsCorrectByteArray() {
        // Arrange
        ByteOrderMark utf8 = ByteOrderMark.UTF_8;

        // Act
        byte[] byteArray = utf8.getBytes();

        // Assert
        assertArrayEquals(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}, byteArray,
                "The getBytes() method should return the correct byte array for UTF-8.");
    }

    @Test
    void testUTF16LEEqualsUTF16BEAgain() {
       // Arrange
        ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_16LE;
        ByteOrderMark byteOrderMark1 = ByteOrderMark.UTF_16BE;

        // Act
        boolean isEqual = byteOrderMark0.equals(byteOrderMark1);

        //Assert
        assertFalse(isEqual);
    }

    @Test
    void testUTF32BEEqualsUTF16BE() {
        // Arrange
        ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_32BE;
        ByteOrderMark byteOrderMark1 = ByteOrderMark.UTF_16BE;

        // Act
        boolean isEqual = byteOrderMark0.equals(byteOrderMark1);

        //Assert
        assertFalse(isEqual);
    }

    @Test
    void testUTF8EqualsString() {
        // Arrange
        ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_8;

        // Act
        boolean isEqual = byteOrderMark0.equals("ByteOrderMark[UTF-8: 0xEF,0xBB,0xBF]");

        //Assert
        assertFalse(isEqual);
    }

    @Test
    void testConstructorWithEmptyByteArrayThrowsIllegalArgumentException() {
        // Arrange
        int[] intArray0 = new int[0];

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("N%W{9DrL", intArray0), "Should throw IllegalArgumentException");
    }

    @Test
    void testConstructorWithEmptyCharsetNameThrowsIllegalArgumentException() {
        // Arrange
        int[] intArray0 = new int[7];

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("", intArray0), "Should throw IllegalArgumentException");
    }

    @Test
    void testCustomBOMMatchesItsOwnBytes() {
        // Arrange
        ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_8;
        int[] intArray0 = byteOrderMark0.getRawBytes();
        ByteOrderMark byteOrderMark1 = new ByteOrderMark("ByteOrderMark[UTF-8: 0xEF,0xBB,0xBF]", intArray0);

        // Act
        boolean matches = byteOrderMark1.matches(intArray0);

        //Assert
        assertTrue(matches);
    }

    @Test
    void testGetCharsetNameReturnsCorrectCharset() {
        // Arrange
        ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_16BE;

        // Act
        String charsetName = byteOrderMark0.getCharsetName();

        //Assert
        assertEquals("UTF-16BE", charsetName);
    }
}