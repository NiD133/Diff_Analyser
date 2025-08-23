package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CaseFormat}.
 */
public class CaseFormatTest {

    @Test
    public void to_whenTargetFormatIsSameAsSource_returnsOriginalStringUnchanged() {
        // Arrange
        // The input string is intentionally chosen to not conform to the LOWER_CAMEL format.
        // This verifies that an identity conversion (source format == target format)
        // is a no-op that returns the original string, regardless of its content.
        CaseFormat format = CaseFormat.LOWER_CAMEL;
        String nonConformingInput = "0P|{HG$S{ax$v|r_  ";

        // Act
        String result = format.to(format, nonConformingInput);

        // Assert
        assertEquals(nonConformingInput, result);
    }
}