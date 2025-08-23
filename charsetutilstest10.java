package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CharSetUtils#squeeze(String, String...)}.
 */
class CharSetUtilsTest extends AbstractLangTest {

    @Nested
    @DisplayName("squeeze(String, String...) tests")
    class SqueezeTests {

        @Test
        @DisplayName("should return null when the input string is null")
        void squeezeWithNullStringShouldReturnNull() {
            assertNull(CharSetUtils.squeeze(null, "a-z"));
            assertNull(CharSetUtils.squeeze(null, (String) null));
        }

        @Test
        @DisplayName("should return an empty string when the input string is empty")
        void squeezeWithEmptyStringShouldReturnEmptyString() {
            assertEquals("", CharSetUtils.squeeze("", "a-z"));
            assertEquals("", CharSetUtils.squeeze("", (String) null));
        }

        @Test
        @DisplayName("should return the original string when the set is null or empty")
        void squeezeWithNullOrEmptySetShouldReturnOriginalString() {
            assertEquals("hello", CharSetUtils.squeeze("hello", (String) null));
            assertEquals("hello", CharSetUtils.squeeze("hello", ""));
        }

        @Test
        @DisplayName("should not change the string if no characters in the set are repeated")
        void squeezeWithNonMatchingSetShouldNotChangeString() {
            // 'l' is repeated, but not in the set "a-e".
            assertEquals("hello", CharSetUtils.squeeze("hello", "a-e"));
        }

        @Test
        @DisplayName("should squeeze repeated characters that are in the specified character set")
        void squeezeWithSimpleSetShouldSqueezeMatchingChars() {
            // In "helloo", 'l' is repeated and in the set "l".
            // 'o' is also repeated, but not in the set.
            // Therefore, only 'll' should be squeezed to 'l'.
            assertEquals("heloo", CharSetUtils.squeeze("helloo", "l"));
        }

        @Test
        @DisplayName("should squeeze repeated characters that are in the specified range set")
        void squeezeWithRangeSetShouldSqueezeMatchingChars() {
            // In "hello", 'l' is repeated and falls within the range "l-p".
            assertEquals("helo", CharSetUtils.squeeze("hello", "l-p"));
        }

        @Test
        @DisplayName("should squeeze repeated characters that are NOT in the negated set")
        void squeezeWithNegatedSetShouldSqueezeOtherChars() {
            // The set "^l" means "any character except 'l'".
            // In "helloo", 'o' is repeated and is in the set.
            // 'l' is repeated but is NOT in the set.
            // Therefore, only "oo" is squeezed to "o".
            assertEquals("hello", CharSetUtils.squeeze("helloo", "^l"));
        }
    }
}