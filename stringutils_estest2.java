package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link StringUtils} class.
 */
public class StringUtilsTest {

    /**
     * Verifies that converting an empty character array results in an empty byte array.
     * This tests the edge case of handling empty input.
     */
    @Test
    public void convertCharsToBytes_withEmptyArray_returnsEmptyByteArray() {
        // Arrange: Define an empty character array as input.
        char[] emptyInput = new char[0];

        // Act: Call the method under test.
        byte[] result = StringUtils.convertCharsToBytes(emptyInput);

        // Assert: The resulting byte array should be non-null and have a length of zero.
        assertNotNull("The result should not be null for an empty input.", result);
        assertEquals("The length of the resulting byte array should be 0.", 0, result.length);
    }
}