package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CaseFormat}.
 */
public class CaseFormatTest {

    @Test
    public void to_fromLowerHyphenToLowerUnderscore_convertsHyphenAndPreservesSpecialCharacters() {
        // Arrange
        CaseFormat sourceFormat = CaseFormat.LOWER_HYPHEN;
        CaseFormat targetFormat = CaseFormat.LOWER_UNDERSCORE;
        
        // The input string does not strictly conform to LOWER_HYPHEN, 
        // but contains a hyphen and other special characters.
        // The conversion should be a "best effort".
        String input = "S:^5jO-|]r";
        String expectedOutput = "S:^5jO_|]r";

        // Act
        String convertedString = sourceFormat.to(targetFormat, input);

        // Assert
        assertEquals(expectedOutput, convertedString);
    }
}