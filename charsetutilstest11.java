package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CharSetUtils#squeeze(String, String...)}.
 */
@DisplayName("CharSetUtils Tests")
class CharSetUtilsTest {

    @Nested
    @DisplayName("squeeze(String, String...)")
    class SqueezeTest {

        @Test
        @DisplayName("should return null when the input string is null")
        void squeezeWithNullStringShouldReturnNull() {
            assertNull(CharSetUtils.squeeze(null, (String[]) null), "With null set");
            assertNull(CharSetUtils.squeeze(null), "With no set (varargs)");
            assertNull(CharSetUtils.squeeze(null, "el"), "With a valid set");
        }

        @Test
        @DisplayName("should return an empty string when the input string is empty")
        void squeezeWithEmptyStringShouldReturnEmptyString() {
            assertEquals("", CharSetUtils.squeeze("", (String[]) null), "With null set");
            assertEquals("", CharSetUtils.squeeze(""), "With no set (varargs)");
            assertEquals("", CharSetUtils.squeeze("", "a-e"), "With a valid set");
        }

        @Test
        @DisplayName("should not change the string when the set is null or empty")
        void squeezeWithNullOrEmptySetShouldReturnOriginalString() {
            // The method should be robust and treat a null or empty set as a no-op.
            assertEquals("hello", CharSetUtils.squeeze("hello", (String) null));
            assertEquals("hello", CharSetUtils.squeeze("hello", (String[]) null));
            assertEquals("hello", CharSetUtils.squeeze("hello")); // Varargs version
        }

        @Test
        @DisplayName("should squeeze only repeated characters that are in the specified set")
        void squeezeShouldOnlySqueezeCharsInSet() {
            // In "hello", 'l' is repeated. The set "el" contains 'l'.
            // Expected: "ll" -> "l", resulting in "helo".
            assertEquals("helo", CharSetUtils.squeeze("hello", "el"));

            // In "fooooff", 'o' and 'f' are repeated and are in the set "fo".
            // Expected: "oooo" -> "o", "ff" -> "f", resulting in "fof".
            assertEquals("fof", CharSetUtils.squeeze("fooooff", "fo"));

            // In "fooffooff", 'o' and 'f' are repeated and in the set "of".
            // Expected: "oo" -> "o", "ff" -> "f", resulting in "fofof".
            assertEquals("fofof", CharSetUtils.squeeze("fooffooff", "of"));
        }

        @Test
        @DisplayName("should not squeeze repeated characters that are not in the specified set")
        void squeezeShouldNotSqueezeCharsNotInSet() {
            // In "hello", 'l' is repeated, but the set "e" does not contain 'l'.
            // Expected: No change.
            assertEquals("hello", CharSetUtils.squeeze("hello", "e"));

            // In "hello", 'l' is repeated, but the set "a-e" does not contain 'l'.
            // Expected: No change.
            assertEquals("hello", CharSetUtils.squeeze("hello", "a-e"));
        }
    }
}