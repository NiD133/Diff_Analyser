package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the utility methods in {@link StringUtil}.
 * This suite focuses on the isHexDigit() method.
 */
public class StringUtilIsHexDigitTest {

    @Test
    public void isHexDigit_ShouldReturnTrue_ForLowercaseLetter() {
        // Arrange: A valid lowercase hexadecimal character.
        char lowercaseHexChar = 'f';

        // Act: Call the method under test.
        boolean isHex = StringUtil.isHexDigit(lowercaseHexChar);

        // Assert: Verify that the character is correctly identified as a hex digit.
        assertTrue("The character 'f' should be recognized as a valid hex digit.", isHex);
    }

    @Test
    public void isHexDigit_ShouldReturnTrue_ForUppercaseLetter() {
        // Arrange
        char uppercaseHexChar = 'B';

        // Act
        boolean isHex = StringUtil.isHexDigit(uppercaseHexChar);

        // Assert
        assertTrue("The character 'B' should be recognized as a valid hex digit.", isHex);
    }

    @Test
    public void isHexDigit_ShouldReturnTrue_ForDigit() {
        // Arrange
        char digitChar = '7';

        // Act
        boolean isHex = StringUtil.isHexDigit(digitChar);

        // Assert
        assertTrue("The digit '7' should be recognized as a valid hex digit.", isHex);
    }

    @Test
    public void isHexDigit_ShouldReturnFalse_ForNonHexLetter() {
        // Arrange: A letter that is not a valid hexadecimal character.
        char nonHexChar = 'g';

        // Act
        boolean isHex = StringUtil.isHexDigit(nonHexChar);

        // Assert
        assertFalse("The character 'g' should not be recognized as a hex digit.", isHex);
    }

    @Test
    public void isHexDigit_ShouldReturnFalse_ForSymbol() {
        // Arrange
        char symbolChar = '#';

        // Act
        boolean isHex = StringUtil.isHexDigit(symbolChar);

        // Assert
        assertFalse("The symbol '#' should not be recognized as a hex digit.", isHex);
    }
}