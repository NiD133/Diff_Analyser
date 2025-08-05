package com.itextpdf.text.pdf;

import org.junit.Assert;
import org.junit.Test;

public class DefaultSplitCharacterTest {

    @Test
    public void hyphensInsideDatePattern_ShouldNotBeSplitCharacters() {
        // Test string containing a date pattern
        String text = "anddate format2 01-01-1920";
        
        // Find positions of hyphens within the date pattern
        int dateStart = text.indexOf("01-01-1920");
        Assert.assertTrue("Date pattern should be present", dateStart != -1);
        
        int firstHyphenInDate = dateStart + 2;  // Position of '-' in "01-"
        int secondHyphenInDate = dateStart + 5; // Position of '-' in "01-01-"
        
        // Verify hyphens inside date pattern are not split characters
        Assert.assertFalse("First hyphen in date should not be split character", 
            isSplitCharacter(firstHyphenInDate, text));
        Assert.assertFalse("Second hyphen in date should not be split character", 
            isSplitCharacter(secondHyphenInDate, text));
    }

    @Test
    public void hyphenImmediatelyBeforeDatePattern_ShouldBeSplitCharacter() {
        // Test string with hyphen preceding a date pattern
        String text = "tha111-is one that should-be-splitted-right-herel-2018-12-18";
        
        // Find position of hyphen before date pattern
        int index = text.indexOf("-2018");
        Assert.assertTrue("Hyphen before date should be present", index != -1);
        
        // Verify hyphen before date is a split character
        Assert.assertTrue("Hyphen immediately before date should be split character", 
            isSplitCharacter(index, text));
    }

    @Test
    public void hyphenInRegularText_ShouldBeSplitCharacter() {
        // Test string with hyphen in non-date context
        String text = "tha111-is one that should-be-splitted-right-herel-2018-12-18";
        
        // Find first hyphen in the string
        int index = text.indexOf('-');
        Assert.assertTrue("Hyphen in text should be present", index != -1);
        
        // Verify hyphen in regular text is a split character
        Assert.assertTrue("Hyphen in regular text should be split character", 
            isSplitCharacter(index, text));
    }

    /**
     * Helper method to check if a character is a split character.
     * 
     * @param current character position to check
     * @param text the string containing the character
     * @return true if the character is a split character, false otherwise
     */
    private boolean isSplitCharacter(int current, String text) {
        return new DefaultSplitCharacter().isSplitCharacter(
            0,              // start position
            current,        // current character position
            text.length(),  // end position (exclusive)
            text.toCharArray(), 
            null
        );
    }
}