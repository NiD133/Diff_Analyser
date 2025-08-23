package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link ByteOrderMark} class.
 */
public class ByteOrderMarkTest {

    /**
     * Tests that the toString() method produces a correctly formatted string,
     * including the charset name and a hexadecimal representation of the BOM bytes.
     */
    @Test
    public void testToStringFormat() {
        // Arrange: Set up the test data and expected outcome.
        final String charsetName = "CUSTOM-ENCODING";
        final int[] bomBytes = {0xCA, 0xFE, 0xBA, 0xBE}; // Use distinct, non-zero values for a clear test.
        final ByteOrderMark bom = new ByteOrderMark(charsetName, bomBytes);

        // The expected format is "ByteOrderMark[<charsetName>: <hexBytes>]".
        final String expectedString = "ByteOrderMark[CUSTOM-ENCODING: 0xCA,0xFE,0xBA,0xBE]";

        // Act: Call the method under test.
        final String actualString = bom.toString();

        // Assert: Verify the result is as expected.
        assertEquals("The toString() output should match the expected format.", expectedString, actualString);
    }
}