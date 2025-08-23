package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link StringUtil#startsWithNewline(String)}.
 *
 * This improved test suite replaces a single, auto-generated test case.
 * It provides clear, focused tests for various scenarios, including edge cases,
 * to better document and verify the method's behavior.
 */
public class StringUtilTest {

    @Test
    public void shouldReturnFalseWhenStringDoesNotStartWithNewline() {
        // This test covers the scenario from the original, auto-generated test.
        // The specific string content is not important, only that it does not
        // begin with a newline character.
        assertFalse("Should return false for a string that does not start with a newline",
            StringUtil.startsWithNewline("^[a-zA-Z][a-zA-Z0-9+-.]*:"));
    }

    @Test
    public void shouldReturnTrueWhenStringStartsWithLineFeed() {
        assertTrue("Should return true for a string starting with '\\n'",
            StringUtil.startsWithNewline("\nThis starts with a line feed."));
    }

    @Test
    public void shouldReturnTrueWhenStringStartsWithCarriageReturn() {
        assertTrue("Should return true for a string starting with '\\r'",
            StringUtil.startsWithNewline("\rThis starts with a carriage return."));
    }

    @Test
    public void shouldReturnFalseWhenNewlineIsInTheMiddleOfString() {
        assertFalse("Should return false if the newline is not the first character",
            StringUtil.startsWithNewline("This has a\nnewline in the middle."));
    }

    @Test
    public void shouldReturnFalseForEmptyString() {
        assertFalse("Should return false for an empty string",
            StringUtil.startsWithNewline(""));
    }

    @Test
    public void shouldReturnFalseForNullString() {
        assertFalse("Should return false for a null input",
            StringUtil.startsWithNewline(null));
    }
}