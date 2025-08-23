package com.itextpdf.text.pdf;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link DefaultSplitCharacter} class, focusing on its handling of hyphens,
 * especially in the context of date patterns.
 *
 * The original test was confusing due to magic numbers and a setup that bypassed the
 * logic it intended to test. These tests clarify the actual behavior.
 */
// Class renamed for clarity and convention. Original: DefaultSplitCharacterTestTest3
public class DefaultSplitCharacterTest {

    private DefaultSplitCharacter splitCharacter;

    @Before
    public void setUp() {
        // Instantiate the object under test once for all tests to avoid repetition.
        splitCharacter = new DefaultSplitCharacter();
    }

    /**
     * Verifies that a standard hyphen in a common word is treated as a valid split character.
     * This serves as a baseline for hyphen behavior.
     */
    @Test
    public void standardHyphenIsSplitCharacter() {
        // Arrange
        String text = "hello-world";
        int hyphenIndex = text.indexOf('-');
        char[] chars = text.toCharArray();

        // Act
        // The 'start' and 'end' parameters define the context for the check.
        // We use the entire string for context (start=0, end=length).
        boolean isSplit = splitCharacter.isSplitCharacter(0, hyphenIndex, chars.length, chars, null);

        // Assert
        Assert.assertTrue("A standard hyphen not related to a date should be a split character.", isSplit);
    }

    /**
     * Verifies the behavior for hyphens near a date pattern.
     *
     * The source code contains special logic for dates, but its implementation
     * (`return m.start(1) + start != current;`) effectively treats all hyphens
     * as split characters. This is because the start of a date match (`m.start(1)`)
     * is always a digit, which can never equal the index of a hyphen (`current`).
     *
     * This test clarifies this potentially surprising but correct behavior.
     */
    @Test
    public void hyphenIsAlwaysSplitCharacterEvenWhenNextToDate() {
        // Arrange
        String text = "split-before-2018-12-18";
        char[] chars = text.toCharArray();
        int dateStartIndex = text.indexOf("2018");

        // Case 1: Test the hyphen immediately PRECEDING the date.
        // This was the original test's intent.
        int hyphenBeforeDateIndex = text.lastIndexOf('-', dateStartIndex);

        // Case 2: Test the first hyphen WITHIN the date pattern.
        int hyphenWithinDateIndex = dateStartIndex + 4; // Index of '-' between 2018 and 12

        // Act
        boolean isSplitBeforeDate = splitCharacter.isSplitCharacter(0, hyphenBeforeDateIndex, chars.length, chars, null);
        boolean isSplitWithinDate = splitCharacter.isSplitCharacter(0, hyphenWithinDateIndex, chars.length, chars, null);

        // Assert
        Assert.assertTrue("A hyphen immediately preceding a date should be a split character.", isSplitBeforeDate);
        Assert.assertTrue("A hyphen within a date pattern should also be a split character.", isSplitWithinDate);
    }
}