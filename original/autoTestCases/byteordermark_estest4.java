package org.apache.commons.io;

import org.junit.jupiter.api.Test; // Use JUnit 5 for improved features
import static org.junit.jupiter.api.Assertions.*; // Simplified assertions

/**
 * Test case for the ByteOrderMark class.  Focuses on self-equality.
 * This is a simple test to ensure that a ByteOrderMark object is equal to itself.
 */
public class ByteOrderMarkEqualityTest {

    @Test
    void testByteOrderMarkEqualsItself() {
        // Arrange: Create a ByteOrderMark instance (UTF-16LE in this case).
        ByteOrderMark byteOrderMark = ByteOrderMark.UTF_16LE;

        // Act: Check if the ByteOrderMark is equal to itself using the .equals() method.
        boolean isEqual = byteOrderMark.equals(byteOrderMark);

        // Assert: Verify that the ByteOrderMark is indeed equal to itself.
        assertTrue(isEqual, "A ByteOrderMark object should be equal to itself.");
    }
}