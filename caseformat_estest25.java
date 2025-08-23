package com.google.common.base;

import com.google.common.base.Converter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link CaseFormat}.
 */
public class CaseFormatTest {

    /**
     * Verifies that creating a converter for the same source and target format
     * results in an "identity" converter, which returns the input string unchanged.
     */
    @Test
    public void converterTo_withSameSourceAndTargetFormat_returnsIdentityConverter() {
        // Arrange
        CaseFormat format = CaseFormat.LOWER_CAMEL;
        String originalString = "thisIsLowerCamelCase";

        // Act
        Converter<String, String> identityConverter = format.converterTo(format);
        String convertedString = identityConverter.convert(originalString);

        // Assert
        assertNotNull("The converter should never be null.", identityConverter);
        assertEquals(
            "Expected the string to be unchanged when converting to the same format.",
            originalString,
            convertedString
        );
    }
}