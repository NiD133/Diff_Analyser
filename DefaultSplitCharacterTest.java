package com.itextpdf.text.pdf;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the DefaultSplitCharacter class.
 */
public class DefaultSplitCharacterTest {

    // Sample input texts for testing
    private final String[] sampleTexts = {
        "tha111-is one that should-be-splitted-right-herel-2018-12-18",
        "anddate format2 01-01-1920"
    };

    /**
     * Test to verify that a hyphen inside a date format is not considered a split character.
     */
    @Test
    public void testHyphenInsideDateFormat() {
        int position = 21; // Position of the hyphen in the date format "01-01-1920"
        Assert.assertFalse(isSplitCharacter(position, sampleTexts[1]));
    }

    /**
     * Test to verify that a hyphen before a date format is considered a split character.
     */
    @Test
    public void testHyphenBeforeDateFormat() {
        int position = 49; // Position of the hyphen before the date format "-2018-12-18"
        Assert.assertTrue(isSplitCharacter(position, sampleTexts[0]));
    }

    /**
     * Test to verify that a hyphen inside regular text is considered a split character.
     */
    @Test
    public void testHyphenInsideText() {
        int position = 6; // Position of the hyphen in "tha111-is"
        Assert.assertTrue(isSplitCharacter(position, sampleTexts[0]));
    }

    /**
     * Helper method to determine if a character at a given position is a split character.
     *
     * @param position The position of the character in the text.
     * @param text The text to be checked.
     * @return True if the character is a split character, false otherwise.
     */
    private boolean isSplitCharacter(int position, String text) {
        return new DefaultSplitCharacter().isSplitCharacter(
            75, position, text.length() + 1, text.toCharArray(), null
        );
    }
}