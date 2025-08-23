package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link CharSequenceUtils}.
 * This class focuses on the {@code lastIndexOf} and {@code toCharArray} methods.
 */
@DisplayName("Tests for CharSequenceUtils")
public class CharSequenceUtilsTest extends AbstractLangTest {

    // --- Tests for lastIndexOf(CharSequence, CharSequence, int) ---

    /**
     * Provides test cases for {@code lastIndexOf} with various {@link CharSequence} implementations.
     * The method is expected to behave like {@link String#lastIndexOf(String, int)}.
     *
     * @return a stream of arguments for the parameterized test.
     */
    static Stream<Arguments> lastIndexOfSource() {
        // @formatter:off
        return Stream.of(
            // Arguments: CharSequence, search sequence, start index, expected result
            arguments("abc", "b", 2, 1),
            arguments(new StringBuilder("abc"), "b", 2, 1),
            arguments(new StringBuffer("abc"), "b", 2, 1),
            arguments("abc", new StringBuilder("b"), 2, 1),
            arguments(new StringBuilder("abc"), new StringBuilder("b"), 2, 1),
            arguments(new StringBuffer("abc"), new StringBuffer("b"), 2, 1),
            arguments(new StringBuilder("abc"), new StringBuffer("b"), 2, 1)
        );
        // @formatter:on
    }

    @ParameterizedTest(name = "lastIndexOf({0}, {1}, {2}) should be {3}")
    @MethodSource("lastIndexOfSource")
    void lastIndexOf_withVariousCharSequenceTypes_returnsCorrectIndex(
            final CharSequence cs, final CharSequence search, final int start, final int expected) {
        // Act
        final int actual = CharSequenceUtils.lastIndexOf(cs, search, start);

        // Assert
        assertEquals(expected, actual, "Failed for CharSequence of type " + cs.getClass().getSimpleName());
    }

    // --- Tests for toCharArray(CharSequence) ---

    @Nested
    @DisplayName("toCharArray(CharSequence)")
    class ToCharArrayTests {

        @Test
        @DisplayName("should convert a StringBuilder to a char array")
        void toCharArray_withStringBuilder_returnsCorrectArray() {
            // Arrange
            final CharSequence cs = new StringBuilder("abcdefg");
            final char[] expected = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};

            // Act
            final char[] actual = CharSequenceUtils.toCharArray(cs);

            // Assert
            assertArrayEquals(expected, actual);
        }

        @Test
        @DisplayName("should convert a String to a char array")
        void toCharArray_withString_returnsCorrectArray() {
            // Arrange
            final CharSequence cs = "abcdefg";
            final char[] expected = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};

            // Act
            final char[] actual = CharSequenceUtils.toCharArray(cs);

            // Assert
            assertArrayEquals(expected, actual);
        }

        @Test
        @DisplayName("should return an empty array for null input")
        void toCharArray_withNull_returnsEmptyArray() {
            // Act
            final char[] actual = CharSequenceUtils.toCharArray(null);

            // Assert
            assertArrayEquals(ArrayUtils.EMPTY_CHAR_ARRAY, actual);
        }
    }
}