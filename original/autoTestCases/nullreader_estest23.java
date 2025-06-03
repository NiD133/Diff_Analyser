package org.example;

import org.junit.jupiter.api.Test; // Using JUnit 5 for readability
import static org.junit.jupiter.api.Assertions.*; // Modern Assertions

/**
 * Test case for the NullReader class, specifically focusing on its reset() method.
 */
class NullReaderTest {

    /**
     * Tests the behavior of the `reset()` method in the `NullReader` class.
     * It verifies that calling `reset()` throws an `UnsupportedOperationException`
     * when the `NullReader` is configured to not support the `reset` operation.
     */
    @Test
    void testResetThrowsUnsupportedOperationException() {
        // Arrange: Create a NullReader instance that doesn't support reset.
        //          - The constructor arguments specify a length (247L),
        //            and that reset and mark are not supported (both false).
        NullReader nullReader = new NullReader(247L, false, false);

        // Act & Assert:  Call the reset() method and assert that it throws the expected exception.
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> {
            nullReader.reset();
        });

        // Assert: Verify the exception message.  This ensures we caught the *correct* exception.
        assertEquals("reset() not supported", exception.getMessage());
    }
}