package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class ByteOrderMarkConstructorTest {

    @Test
    public void testByteOrderMarkConstructorWithEmptyByteArray() {
        try {
            // Attempt to create a ByteOrderMark with an empty byte array
            // which is not allowed according to the class's documentation and contract.
            new ByteOrderMark("SomeName", new int[0]);
            fail("Expected IllegalArgumentException: Byte array cannot be empty."); // Explicitly state the expectation

        } catch (IllegalArgumentException e) {
            // Verify that the correct exception is thrown with a meaningful message.
            assertEquals("No bytes specified", e.getMessage()); // Assert the error message for better clarity.
        }
    }
}