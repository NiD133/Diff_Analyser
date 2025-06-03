package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class ByteOrderMarkConstructorTest {

    @Test(timeout = 1000) // Reduced timeout for clarity
    public void testConstructor_EmptyCharsetName_ThrowsIllegalArgumentException() {
        // Arrange: Define an array of integers representing the byte sequence.
        int[] byteSequence = new int[7];

        // Act & Assert:  Attempt to create a ByteOrderMark with an empty charset name and the byte sequence.
        // Expect an IllegalArgumentException to be thrown because the charset name cannot be empty.
        try {
            new ByteOrderMark("", byteSequence);
            fail("Expected IllegalArgumentException for empty charset name.");
        } catch (IllegalArgumentException e) {
            // Assert that the exception message is as expected.
            assertEquals("No charsetName specified", e.getMessage());
        }
    }
}