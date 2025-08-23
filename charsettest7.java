package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the CharSet.getInstance(String) factory method with simple, single-token inputs.
 *
 * Note: The original class name 'CharSetTestTest7' has been kept for consistency,
 * but a more conventional name would be 'CharSetGetInstanceTest'.
 */
public class CharSetTestTest7 extends AbstractLangTest {

    @Nested
    @DisplayName("when input is null or empty")
    class WhenInputIsNullOrEmpty {

        @Test
        @DisplayName("getInstance(null) should return an empty CharSet")
        void getInstanceWithNullInputShouldReturnEmptySet() {
            // Arrange: The input is a null string. The cast is necessary to resolve
            // ambiguity with the varargs getInstance(String...) method.
            final String input = null;

            // Act
            final CharSet charSet = CharSet.getInstance(input);

            // Assert
            assertAll("Empty set for null input",
                () -> assertEquals("[]", charSet.toString(), "toString() should represent an empty set"),
                () -> assertEquals(0, charSet.getCharRanges().length, "Should contain no CharRanges")
            );
        }

        @Test
        @DisplayName("getInstance(\"\") should return an empty CharSet")
        void getInstanceWithEmptyStringShouldReturnEmptySet() {
            // Arrange
            final String input = "";

            // Act
            final CharSet charSet = CharSet.getInstance(input);

            // Assert
            assertAll("Empty set for empty string input",
                () -> assertEquals("[]", charSet.toString(), "toString() should represent an empty set"),
                () -> assertEquals(0, charSet.getCharRanges().length, "Should contain no CharRanges")
            );
        }
    }

    @Nested
    @DisplayName("when input defines a single character")
    class WhenInputIsSingleChar {

        @Test
        @DisplayName("getInstance(\"a\") should create a set with a single character")
        void getInstanceWithSingleCharacterShouldCreateCorrectSet() {
            // Arrange
            final String input = "a";

            // Act
            final CharSet charSet = CharSet.getInstance(input);

            // Assert
            assertAll("Set for single character 'a'",
                () -> assertEquals("[a]", charSet.toString()),
                () -> assertEquals(1, charSet.getCharRanges().length),
                () -> assertEquals("a", charSet.getCharRanges()[0].toString())
            );
        }

        @Test
        @DisplayName("getInstance(\"^a\") should create a set with a single negated character")
        void getInstanceWithNegatedSingleCharacterShouldCreateCorrectSet() {
            // Arrange
            final String input = "^a";

            // Act
            final CharSet charSet = CharSet.getInstance(input);

            // Assert
            assertAll("Set for negated single character '^a'",
                () -> assertEquals("[^a]", charSet.toString()),
                () -> assertEquals(1, charSet.getCharRanges().length),
                () -> assertEquals("^a", charSet.getCharRanges()[0].toString())
            );
        }
    }

    @Nested
    @DisplayName("when input defines a character range")
    class WhenInputIsCharRange {

        @Test
        @DisplayName("getInstance(\"a-e\") should create a set with a character range")
        void getInstanceWithCharacterRangeShouldCreateCorrectSet() {
            // Arrange
            final String input = "a-e";

            // Act
            final CharSet charSet = CharSet.getInstance(input);

            // Assert
            assertAll("Set for character range 'a-e'",
                () -> assertEquals("[a-e]", charSet.toString()),
                () -> assertEquals(1, charSet.getCharRanges().length),
                () -> assertEquals("a-e", charSet.getCharRanges()[0].toString())
            );
        }

        @Test
        @DisplayName("getInstance(\"^a-e\") should create a set with a negated character range")
        void getInstanceWithNegatedCharacterRangeShouldCreateCorrectSet() {
            // Arrange
            final String input = "^a-e";

            // Act
            final CharSet charSet = CharSet.getInstance(input);

            // Assert
            assertAll("Set for negated character range '^a-e'",
                () -> assertEquals("[^a-e]", charSet.toString()),
                () -> assertEquals(1, charSet.getCharRanges().length),
                () -> assertEquals("^a-e", charSet.getCharRanges()[0].toString())
            );
        }
    }
}