package com.itextpdf.text.pdf;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable, focused tests for DefaultSplitCharacter.
 *
 * These tests cover:
 * - Default splitting rules (space, hyphen, and non-control letters)
 * - Behavior when custom split characters are provided
 * - Accessing the current character
 * - Basic error handling (null arrays, out-of-bounds, negative indices)
 */
public class DefaultSplitCharacterTest {

    // -------- Default splitting behavior --------

    @Test
    public void default_splitsOnSpace() {
        DefaultSplitCharacter splitter = new DefaultSplitCharacter();
        char[] chars = "a b".toCharArray(); // index 1 is space

        boolean canSplit = splitter.isSplitCharacter(0, 1, chars.length - 1, chars, null);

        assertTrue("Default should split on space", canSplit);
    }

    @Test
    public void default_splitsOnHyphen() {
        DefaultSplitCharacter splitter = new DefaultSplitCharacter();
        char[] chars = "ab-c".toCharArray(); // index 2 is '-'

        boolean canSplit = splitter.isSplitCharacter(0, 2, chars.length - 1, chars, null);

        assertTrue("Default should split on hyphen '-'", canSplit);
    }

    @Test
    public void default_doesNotSplitOnLetter() {
        DefaultSplitCharacter splitter = new DefaultSplitCharacter();
        char[] chars = "abc".toCharArray(); // index 1 is 'b'

        boolean canSplit = splitter.isSplitCharacter(0, 1, chars.length - 1, chars, null);

        assertFalse("Default should not split on regular letters", canSplit);
    }

    @Test
    public void default_splitsOnNullCharacter() {
        // '\u0000' is <= space, should be splittable by default
        DefaultSplitCharacter splitter = (DefaultSplitCharacter) DefaultSplitCharacter.DEFAULT;
        char[] chars = new char[1]; // default-initialized to '\u0000'

        boolean canSplit = splitter.isSplitCharacter(0, 0, 0, chars, null);

        assertTrue("Default should split on null character (<= SPACE)", canSplit);
    }

    // -------- Custom splitting behavior --------

    @Test
    public void singleCharacterConstructor_onlyThatCharacterSplits() {
        DefaultSplitCharacter splitter = new DefaultSplitCharacter('w');
        char[] chars = "w-a".toCharArray();

        boolean splitOnW = splitter.isSplitCharacter(0, 0, chars.length - 1, chars, null); // 'w'
        boolean splitOnHyphen = splitter.isSplitCharacter(0, 1, chars.length - 1, chars, null); // '-'

        assertTrue("Custom splitter should split on 'w'", splitOnW);
        assertFalse("When custom chars are set, default hyphen should be ignored", splitOnHyphen);
    }

    @Test
    public void arrayConstructor_onlyCustomCharactersSplit_ignoresDefaults() {
        DefaultSplitCharacter splitter = new DefaultSplitCharacter(new char[] { ',' });
        char[] chars = "a,b-c".toCharArray();

        boolean splitOnComma = splitter.isSplitCharacter(0, 1, chars.length - 1, chars, null);   // ','
        boolean splitOnHyphen = splitter.isSplitCharacter(0, 3, chars.length - 1, chars, null);  // '-'

        assertTrue("Custom splitter should split on comma ','", splitOnComma);
        assertFalse("When custom chars are set, default hyphen should be ignored", splitOnHyphen);
    }

    // -------- getCurrentCharacter behavior --------

    @Test
    public void getCurrentCharacter_returnsExpectedChar() {
        DefaultSplitCharacter splitter = new DefaultSplitCharacter();
        char[] chars = "xyz".toCharArray();

        char current = splitter.getCurrentCharacter(1, chars, null);

        assertEquals("Should return the character at the current index", 'y', current);
    }

    @Test(expected = NullPointerException.class)
    public void getCurrentCharacter_throwsOnNullArray() {
        DefaultSplitCharacter splitter = new DefaultSplitCharacter();

        splitter.getCurrentCharacter(0, null, null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void getCurrentCharacter_throwsOnOutOfBoundsIndex() {
        DefaultSplitCharacter splitter = new DefaultSplitCharacter();
        char[] chars = new char[] { 'a', 'b' };

        splitter.getCurrentCharacter(5, chars, null);
    }

    // -------- isSplitCharacter error handling --------

    @Test(expected = NullPointerException.class)
    public void isSplitCharacter_throwsOnNullCharArray() {
        DefaultSplitCharacter splitter = new DefaultSplitCharacter();

        splitter.isSplitCharacter(0, 0, 0, null, null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void isSplitCharacter_throwsOnNegativeIndex() {
        DefaultSplitCharacter splitter = new DefaultSplitCharacter();
        char[] chars = new char[] { 'a' };

        splitter.isSplitCharacter(0, -1, 0, chars, null);
    }
}