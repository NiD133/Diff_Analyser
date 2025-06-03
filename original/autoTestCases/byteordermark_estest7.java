package org.apache.commons.io;

import org.junit.jupiter.api.Test; // Using JUnit 5 annotations for clarity
import static org.junit.jupiter.api.Assertions.*; // Using JUnit 5 assertions for clarity

/**
 * Test case for the ByteOrderMark class.  This test specifically focuses on
 * verifying that the {@link ByteOrderMark#get(int)} method correctly retrieves
 * the byte at a specified index from the byte sequence associated with the
 * ByteOrderMark.
 */
public class ByteOrderMarkGetAtIndexTest {

    @Test
    void testGetByteAtIndex() {
        // Arrange: Define a byte sequence as an integer array.  This simulates
        // the underlying byte representation of a potential ByteOrderMark.
        int[] byteSequence = new int[4];
        byteSequence[0] = -8; // Set the first byte to -8 (or 0xF8 in unsigned byte representation)

        // Act: Create a ByteOrderMark instance using the byte sequence and a dummy charset name.
        // The actual charset name doesn't matter for this specific test, as we're only testing
        // byte retrieval.
        ByteOrderMark byteOrderMark = new ByteOrderMark("+U", byteSequence);

        // Assert: Retrieve the byte at index 0 from the ByteOrderMark and assert that it matches
        // the value we initially set in the byte sequence.
        int retrievedByte = byteOrderMark.get(0);
        assertEquals(-8, retrievedByte, "The byte at index 0 should be -8.");
    }
}