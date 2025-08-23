package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CaseFormat}.
 */
public class CaseFormatTest {

    @Test
    public void to_convertingEmptyString_returnsEmptyString() {
        // Arrange: Define the source and target formats for the conversion.
        CaseFormat sourceFormat = CaseFormat.LOWER_HYPHEN;
        CaseFormat targetFormat = CaseFormat.UPPER_CAMEL;
        String emptyInput = "";

        // Act: Convert the empty string from the source to the target format.
        String result = sourceFormat.to(targetFormat, emptyInput);

        // Assert: The result should also be an empty string.
        assertEquals(emptyInput, result);
    }
}