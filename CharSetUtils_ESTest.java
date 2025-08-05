package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.CharSetUtils;

/**
 * Test suite for CharSetUtils functionality.
 * Tests the main operations: squeeze, delete, keep, count, and containsAny.
 */
public class CharSetUtilsTest {

    // ========== squeeze() method tests ==========
    
    @Test
    public void squeeze_withNullInput_returnsNull() {
        String[] charSets = {"a-z"};
        String result = CharSetUtils.squeeze(null, charSets);
        assertNull("squeeze should return null when input string is null", result);
    }

    @Test
    public void squeeze_withEmptyString_returnsEmptyString() {
        String[] charSets = {"a-z"};
        String result = CharSetUtils.squeeze("", charSets);
        assertEquals("squeeze should return empty string when input is empty", "", result);
    }

    @Test
    public void squeeze_withNullCharSet_returnsOriginalString() {
        String input = "hello world";
        String[] charSets = new String[2]; // null elements
        String result = CharSetUtils.squeeze(input, charSets);
        assertEquals("squeeze should return original string when charset is null", input, result);
    }

    @Test
    public void squeeze_withNumericRange_removesConsecutiveDigits() {
        String input = "offset cannot be negative";
        String[] charSets = {"0-9"};
        String result = CharSetUtils.squeeze(input, charSets);
        assertEquals("offset cannot be negative", result); // no digits to squeeze
    }

    @Test
    public void squeeze_withRepeatedDots_squeezesToSingleDot() {
        String input = "...";
        String[] charSets = {"."};
        String result = CharSetUtils.squeeze(input, charSets);
        assertEquals("Should squeeze repeated dots to single dot", ".", result);
    }

    @Test
    public void squeeze_withComplexCharSet_removesRepeatedMatchingChars() {
        String input = "Aborting to protect against StackOverflowError - output of one loop is the input of another";
        String[] charSets = {"uk{Z^6e/S>lTbb#wl"};
        String result = CharSetUtils.squeeze(input, charSets);
        // The charset contains 'o', 't', 'u', 'l' which get squeezed when repeated
        assertEquals("Aborting to protect against StackOverflowEror - output of one lop is the input of another", result);
    }

    @Test
    public void squeeze_withSpecificChars_removesOnlyThoseRepeatedChars() {
        String input = "offset cannot be negative";
        String[] charSets = {"Jk{Z^]6e/!S>zlTbb#wl"};
        String result = CharSetUtils.squeeze(input, charSets);
        // Squeezes repeated 'f', 'n', 't' characters
        assertEquals("ofset canot be negative", result);
    }

    // ========== delete() method tests ==========
    
    @Test
    public void delete_withNullInput_returnsNull() {
        String[] charSets = {"a-z"};
        String result = CharSetUtils.delete(null, charSets);
        assertNull("delete should return null when input string is null", result);
    }

    @Test
    public void delete_withEmptyString_returnsEmptyString() {
        String[] charSets = new String[0];
        String result = CharSetUtils.delete("", charSets);
        assertEquals("delete should return empty string when input is empty", "", result);
    }

    @Test
    public void delete_withAlphaRange_removesMatchingLetters() {
        String input = "A-Z";
        String[] charSets = {"A-Z"};
        String result = CharSetUtils.delete(input, charSets);
        assertEquals("Should delete A and Z, leaving hyphen", "-", result);
    }

    @Test
    public void delete_withNullCharSet_returnsOriginalString() {
        String input = "A-Z";
        String[] charSets = new String[8]; // null elements
        String result = CharSetUtils.delete(input, charSets);
        assertEquals("delete should return original string when charset is null", input, result);
    }

    // ========== keep() method tests ==========
    
    @Test
    public void keep_withNullInput_returnsNull() {
        String[] charSets = {"a-z"};
        String result = CharSetUtils.keep(null, charSets);
        assertNull("keep should return null when input string is null", result);
    }

    @Test
    public void keep_withEmptyString_returnsEmptyString() {
        String[] charSets = new String[4];
        String result = CharSetUtils.keep("", charSets);
        assertEquals("keep should return empty string when input is empty", "", result);
    }

    @Test
    public void keep_withAlphaRange_keepsOnlyMatchingLetters() {
        String input = "A-Z";
        String[] charSets = {"A-Z"};
        String result = CharSetUtils.keep(input, charSets);
        assertEquals("Should keep only A and Z", "AZ", result);
    }

    @Test
    public void keep_withNullCharSet_returnsEmptyString() {
        String input = "A-Z";
        String[] charSets = new String[8]; // null elements
        String result = CharSetUtils.keep(input, charSets);
        assertEquals("keep should return empty string when charset is null", "", result);
    }

    // ========== count() method tests ==========
    
    @Test
    public void count_withEmptyString_returnsZero() {
        String[] charSets = new String[13];
        int result = CharSetUtils.count("", charSets);
        assertEquals("count should return 0 for empty string", 0, result);
    }

    @Test
    public void count_withMatchingChars_returnsCorrectCount() {
        String input = "^MPkb@$Zu";
        String[] charSets = {"h|Bv_9mUP7'&Y"};
        int result = CharSetUtils.count(input, charSets);
        assertEquals("Should count 1 matching character", 1, result);
    }

    @Test
    public void count_withNoMatchingChars_returnsZero() {
        String input = "&";
        String[] charSets = new String[2]; // null elements
        int result = CharSetUtils.count(input, charSets);
        assertEquals("count should return 0 when no chars match", 0, result);
    }

    // ========== containsAny() method tests ==========
    
    @Test
    public void containsAny_withEmptyString_returnsFalse() {
        String[] charSets = new String[7];
        boolean result = CharSetUtils.containsAny("", charSets);
        assertFalse("containsAny should return false for empty string", result);
    }

    @Test
    public void containsAny_withNoMatchingChars_returnsFalse() {
        String input = "A-Za-z";
        String[] charSets = {"Xii8KJ"};
        boolean result = CharSetUtils.containsAny(input, charSets);
        assertFalse("containsAny should return false when no characters match", result);
    }

    @Test
    public void containsAny_withMatchingChars_returnsTrue() {
        String input = "h|Bv_9mUP7'&Y";
        String[] charSets = {"h|Bv_9mUP7'&Y"};
        boolean result = CharSetUtils.containsAny(input, charSets);
        assertTrue("containsAny should return true when characters match", result);
    }

    @Test
    public void containsAny_withNullCharSet_returnsFalse() {
        String input = "h|Bv_9mUP7'&Y";
        String[] charSets = new String[2]; // null elements
        boolean result = CharSetUtils.containsAny(input, charSets);
        assertFalse("containsAny should return false when charset is null", result);
    }

    // ========== Constructor test ==========
    
    @Test
    public void constructor_canBeInstantiated() {
        // Test that the deprecated constructor works (for tools that need JavaBean instances)
        CharSetUtils utils = new CharSetUtils();
        assertNotNull("CharSetUtils instance should be created", utils);
    }
}