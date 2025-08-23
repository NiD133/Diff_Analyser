package com.itextpdf.text.pdf;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for DefaultSplitCharacter focusing on hyphen splitting behavior
 * in normal text versus dates. Hyphens inside dates (e.g., yyyy-MM-dd, dd-MM-yyyy)
 * should not be used as split points, while hyphens in regular text should.
 */
public class DefaultSplitCharacterTest {

    private static final char HYPHEN = '-';

    // Test fixtures
    private static final String TEXT_WITH_MIXED_HYPHENS =
            "tha111-is one that should-be-splitted-right-herel-2018-12-18";
    private static final String TEXT_WITH_DATE =
            "anddate format2 01-01-1920";

    private DefaultSplitCharacter sut;

    @Before
    public void setUp() {
        sut = new DefaultSplitCharacter();
    }

    @Test
    public void doesNotSplitAtHyphensInsideDate_dd_MM_yyyy() {
        // "01-01-1920" -> both hyphens are inside a date and must not split
        int firstHyphen = indexOf(TEXT_WITH_DATE, HYPHEN, 1);
        int secondHyphen = indexOf(TEXT_WITH_DATE, HYPHEN, 2);

        assertFalse(splitAllowedAt(TEXT_WITH_DATE, firstHyphen));
        assertFalse(splitAllowedAt(TEXT_WITH_DATE, secondHyphen));
    }

    @Test
    public void doesNotSplitAtHyphenInsideDate_yyyy_MM_dd() {
        // The trailing date "2018-12-18" -> hyphens are part of a date and must not split
        int startOfDate = TEXT_WITH_MIXED_HYPHENS.indexOf("2018-12-18"); // index of '2' in "2018"
        int firstHyphenInDate = startOfDate + "2018".length(); // position of the hyphen after "2018"

        assertFalse(splitAllowedAt(TEXT_WITH_MIXED_HYPHENS, firstHyphenInDate));
    }

    @Test
    public void splitsAtHyphenWithinPlainText() {
        int hyphenAfterTha111 = indexOf(TEXT_WITH_MIXED_HYPHENS, HYPHEN, 1);
        assertTrue(splitAllowedAt(TEXT_WITH_MIXED_HYPHENS, hyphenAfterTha111));
    }

    @Test
    public void splitsAtHyphenImmediatelyBeforeDate() {
        // The hyphen directly before "2018-12-18" is a normal text hyphen and may split
        int hyphenBeforeDate = TEXT_WITH_MIXED_HYPHENS.indexOf("-2018");
        assertTrue(splitAllowedAt(TEXT_WITH_MIXED_HYPHENS, hyphenBeforeDate));
    }

    private boolean splitAllowedAt(String text, int currentIndex) {
        // DefaultSplitCharacter.isSplitCharacter uses a [start, end) window of a char array.
        // Using end as length + 1 (exclusive) mirrors existing usage in legacy tests.
        return sut.isSplitCharacter(0, currentIndex, text.length() + 1, text.toCharArray(), null);
    }

    private static int indexOf(String text, char ch, int occurrence) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ch && ++count == occurrence) {
                return i;
            }
        }
        throw new AssertionError("Character '" + ch + "' not found " + occurrence + " times in: " + text);
    }
}