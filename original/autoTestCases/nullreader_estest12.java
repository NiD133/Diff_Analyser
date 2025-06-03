package org.example;

import org.junit.jupiter.api.Test; // Changed to JUnit 5 for better readability

import static org.junit.jupiter.api.Assertions.assertEquals; // Streamlined import

/**
 * This test case focuses on verifying the behavior of the NullReader class.
 * Specifically, it tests the 'getSize()' method.
 */
class NullReaderTest { // Renamed class for clarity, implying a testing context

    /**
     * Tests that the 'getSize()' method of the NullReader instance returns 0.
     * This verifies the NullReader correctly reports its size as zero, 
     * as it represents a reader with no data.
     */
    @Test
    void testGetSizeReturnsZero() { // More descriptive method name
        // Arrange: Obtain an instance of NullReader.  We expect this to be a singleton
        //          according to the original test case.
        NullReader nullReader = NullReader.INSTANCE;

        // Act: Call the getSize() method.
        long size = nullReader.getSize();

        // Assert:  Verify that the returned size is 0.
        assertEquals(0L, size, "The size of the NullReader should be 0."); // Added a message to the assertion
    }
}