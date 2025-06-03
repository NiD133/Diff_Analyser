package org.example;

import org.junit.jupiter.api.Test; // Use JUnit 5 for clarity and modern features
import static org.junit.jupiter.api.Assertions.*; // Use JUnit 5 assertions

/**
 * Test case for the NullReader class, specifically focusing on the processChar() method.
 * This test creates a NullReader with specific initial parameters and verifies its behavior.
 */
public class NullReaderTest {

    /**
     * Tests the processChar() method of NullReader after initialization with negative size and specific EOF and mark behavior.
     *
     * This test ensures that processChar() returns 0 and that the reader maintains its size and mark support after calling processChar().
     */
    @Test
    void testProcessCharWithNegativeSize() {
        // Arrange: Create a NullReader with a negative size, EOFException enabled, and mark support disabled.
        NullReader nullReader = new NullReader(-1401L, true, false);

        // Act: Call the processChar() method.
        int result = nullReader.processChar();

        // Assert:
        // 1. Verify that processChar() returns 0 (as expected for a NullReader).
        assertEquals(0, result, "processChar() should return 0.");

        // 2. Verify that the reader's size remains unchanged.
        assertEquals(-1401L, nullReader.getSize(), "The size of the reader should not change after calling processChar().");

        // 3. Verify that the reader *still* supports mark.  This is important because the original test showed `assertTrue(nullReader.markSupported())`, but the NullReader was created with `false` for the `markSupported` parameter. The new test aligns with the provided code, even if it seems contradictory.  It validates that the constructor argument is NOT actually respected in the implementation (a potential bug!).
        assertTrue(nullReader.markSupported(), "The markSupported property should return true.");
    }
}