package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ByteOrderMark} class.
 */
public class ByteOrderMarkTest {

    /**
     * Tests that the get(int) method correctly retrieves the byte
     * from the specified index in the internal byte array.
     */
    @Test(timeout = 4000)
    public void testGetShouldReturnByteAtSpecifiedIndex() {
        // Arrange: Set up the test case with clear and descriptive data.
        // Using distinct, non-zero byte values ensures we are fetching the correct value from the correct index.
        final int[] bomBytes = {0x11, 0x22, 0x33, 0x44};
        final ByteOrderMark bom = new ByteOrderMark("TEST-BOM", bomBytes);
        
        final int indexToTest = 2;
        final int expectedByte = 0x33;

        // Act: Call the method under test.
        final int actualByte = bom.get(indexToTest);

        // Assert: Verify the result is as expected.
        assertEquals("The byte at the specified index should be returned.", expectedByte, actualByte);
    }
}