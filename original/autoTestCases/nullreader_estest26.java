package org.example;

import org.junit.jupiter.api.Test; // Updated import for JUnit 5
import static org.junit.jupiter.api.Assertions.*; // Updated import for JUnit 5

/**
 * Test case for the NullReader class, specifically focusing on the 'mark' method.
 * This test verifies that calling the 'mark' method on a NullReader instance configured
 * to not support marking throws an UnsupportedOperationException.
 */
class NullReaderMarkUnsupportedTest {

    @Test
    void testMarkThrowsUnsupportedOperationException() {
        // Arrange: Create a NullReader instance that does *not* support marking.
        // The constructor arguments are:
        //  - 10L:  The initial size (doesn't affect this test).
        //  - false: Indicates that marking is *not* supported.
        //  - false: Indicates that the reader does not throw EOFException at end.
        NullReader nullReader = new NullReader(10L, false, false);

        // Act and Assert:  Attempt to call the 'mark' method and assert that it throws
        // an UnsupportedOperationException with the expected message.
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> {
            nullReader.mark(1452); //  1452 is the read limit, which doesn't matter in this test.
        });

        // Optionally, verify the exception message for even more robust testing.
        assertEquals("mark() not supported", exception.getMessage());
    }
}