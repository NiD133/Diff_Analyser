package com.itextpdf.text.pdf;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test suite for the {@link DefaultSplitCharacter} class.
 */
// Renamed class for clarity and to follow standard conventions.
// Original name: DefaultSplitCharacterTestTest2
public class DefaultSplitCharacterTest {

    /**
     * Tests that a hyphen within a string that matches a date pattern (e.g., "DD-MM-YYYY")
     * is not considered a split character. This is important for preventing unwanted line breaks
     * in the middle of dates.
     */
    @Test
    // Renamed test for clarity and to describe the expected outcome.
    // Original name: hypenInsideDateTest
    public void isSplitCharacter_hyphenInsideDate_returnsFalse() {
        // 1. Arrange
        // The DefaultSplitCharacter is the class under test.
        DefaultSplitCharacter splitCharacter = new DefaultSplitCharacter();

        // The input string contains a date where we will test a hyphen.
        String textWithDate = "anddate format2 01-01-1920";

        // We are specifically testing the hyphen within the date part "01-01-1920".
        // "anddate format2 01-01-1920"
        //                     ^
        // Index: 21
        int hyphenInDatePosition = 21;

        // Sanity check to ensure our test setup is correct and the character is a hyphen.
        Assert.assertEquals("Test setup failed: The character at the target position should be a hyphen.",
                '-', textWithDate.charAt(hyphenInDatePosition));

        // 2. Act
        // The isSplitCharacter method determines if a character at a given position
        // can be used to split a line. We check the entire string for context.
        boolean isSplit = splitCharacter.isSplitCharacter(
                0, // start of the context
                hyphenInDatePosition, // the character to check
                textWithDate.length(), // end of the context
                textWithDate.toCharArray(),
                null // PdfChunk[] is not needed for this test
        );

        // 3. Assert
        // The hyphen inside the date should NOT be a split character.
        Assert.assertFalse("A hyphen within a date pattern should not be a split character.", isSplit);
    }
}