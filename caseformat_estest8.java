package com.google.common.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link CaseFormat}.
 */
public class CaseFormatTest {

    /**
     * Tests that converting an empty string from a format to itself
     * results in an empty string. This is an identity conversion.
     */
    @Test
    public void to_identityConversionWithEmptyString_returnsEmptyString() {
        // Arrange: Define the format, input, and expected output.
        // An identity conversion is where the source and target formats are the same.
        CaseFormat format = CaseFormat.UPPER_CAMEL;
        String input = "";
        String expected = "";

        // Act: Call the 'to' method to perform the conversion.
        String result = format.to(format, input);

        // Assert: Verify that the result matches the expected empty string.
        assertEquals(expected, result);
    }
}