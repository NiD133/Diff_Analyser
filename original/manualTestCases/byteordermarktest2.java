package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * This test class focuses on validating the exception handling behavior
 * of the {@link ByteOrderMark} constructor. It ensures that the constructor
 * throws the appropriate exceptions when provided with invalid input.
 */
public class ByteOrderMarkConstructorExceptionTest {

    /**
     * Tests the {@link ByteOrderMark} constructor to ensure it throws
     * {@link NullPointerException} when a null charset name is provided.
     */
    @Test
    public void testConstructorThrowsNullPointerExceptionForNullCharsetName() {
        assertThrows(NullPointerException.class, () -> new ByteOrderMark(null, 1, 2, 3),
                "Should throw NullPointerException when charset name is null.");
    }

    /**
     * Tests the {@link ByteOrderMark} constructor to ensure it throws
     * {@link IllegalArgumentException} when an empty charset name is provided.
     */
    @Test
    public void testConstructorThrowsIllegalArgumentExceptionForEmptyCharsetName() {
        assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("", 1, 2, 3),
                "Should throw IllegalArgumentException when charset name is empty.");
    }

    /**
     * Tests the {@link ByteOrderMark} constructor to ensure it throws
     * {@link NullPointerException} when a null byte array is provided.
     */
    @Test
    public void testConstructorThrowsNullPointerExceptionForNullByteArray() {
        assertThrows(NullPointerException.class, () -> new ByteOrderMark("a", (int[]) null),
                "Should throw NullPointerException when byte array is null.");
    }

    /**
     * Tests the {@link ByteOrderMark} constructor to ensure it throws
     * {@link IllegalArgumentException} when no bytes are provided.
     * This indirectly tests the case where the varargs for the byte array are empty.
     */
    @Test
    public void testConstructorThrowsIllegalArgumentExceptionForNoBytesProvided() {
        assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("b"),
                "Should throw IllegalArgumentException when no bytes are provided.");
    }
}