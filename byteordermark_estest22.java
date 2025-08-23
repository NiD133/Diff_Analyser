package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link ByteOrderMark}.
 */
public class ByteOrderMarkTest {

    /**
     * Tests that the getCharsetName() method returns the exact string
     * that was passed to the constructor.
     */
    @Test
    public void testGetCharsetNameReturnsConstructorValue() {
        // Arrange: Create a ByteOrderMark with a specific charset name.
        // The actual byte values are irrelevant for this test, but the array cannot be empty.
        final String expectedCharsetName = "CUSTOM-CHARSET-123";
        final int[] anyNonEmptyBytes = {0x01, 0x02, 0x03};
        final ByteOrderMark bom = new ByteOrderMark(expectedCharsetName, anyNonEmptyBytes);

        // Act: Retrieve the charset name from the object.
        final String actualCharsetName = bom.getCharsetName();

        // Assert: Verify that the retrieved name matches the expected name.
        assertEquals("The charset name should match the one provided in the constructor.",
                expectedCharsetName, actualCharsetName);
    }
}