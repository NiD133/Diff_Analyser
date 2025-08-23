package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the isInAlphabet(byte) method of the Base16 class.
 */
@DisplayName("Base16.isInAlphabet")
class Base16IsInAlphabetTest {

    @Nested
    @DisplayName("when configured for lower-case")
    class WhenLowerCase {

        private final Base16 base16 = new Base16(true); // lower-case alphabet

        @Test
        void shouldReturnTrueForDigits() {
            for (char c = '0'; c <= '9'; c++) {
                assertTrue(base16.isInAlphabet((byte) c), () -> "Digit '" + c + "' should be in the alphabet");
            }
        }

        @Test
        void shouldReturnTrueForLowerCaseHexLetters() {
            for (char c = 'a'; c <= 'f'; c++) {
                assertTrue(base16.isInAlphabet((byte) c), () -> "Lower-case letter '" + c + "' should be in the alphabet");
            }
        }

        @Test
        void shouldReturnFalseForUpperCaseHexLetters() {
            for (char c = 'A'; c <= 'F'; c++) {
                assertFalse(base16.isInAlphabet((byte) c), () -> "Upper-case letter '" + c + "' should not be in the lower-case alphabet");
            }
        }

        @Test
        void shouldReturnFalseForCharactersAdjacentToValidRanges() {
            assertFalse(base16.isInAlphabet((byte) ('0' - 1)), "Character before '0' should be invalid");
            assertFalse(base16.isInAlphabet((byte) ('9' + 1)), "Character after '9' should be invalid");
            assertFalse(base16.isInAlphabet((byte) ('a' - 1)), "Character before 'a' should be invalid");
            assertFalse(base16.isInAlphabet((byte) ('f' + 1)), "Character after 'f' should be invalid");
        }
    }

    @Nested
    @DisplayName("when configured for upper-case")
    class WhenUpperCase {

        private final Base16 base16 = new Base16(false); // upper-case alphabet

        @Test
        void shouldReturnTrueForDigits() {
            for (char c = '0'; c <= '9'; c++) {
                assertTrue(base16.isInAlphabet((byte) c), () -> "Digit '" + c + "' should be in the alphabet");
            }
        }

        @Test
        void shouldReturnTrueForUpperCaseHexLetters() {
            for (char c = 'A'; c <= 'F'; c++) {
                assertTrue(base16.isInAlphabet((byte) c), () -> "Upper-case letter '" + c + "' should be in the alphabet");
            }
        }

        @Test
        void shouldReturnFalseForLowerCaseHexLetters() {
            for (char c = 'a'; c <= 'f'; c++) {
                assertFalse(base16.isInAlphabet((byte) c), () -> "Lower-case letter '" + c + "' should not be in the upper-case alphabet");
            }
        }

        @Test
        void shouldReturnFalseForCharactersAdjacentToValidRanges() {
            assertFalse(base16.isInAlphabet((byte) ('0' - 1)), "Character before '0' should be invalid");
            assertFalse(base16.isInAlphabet((byte) ('9' + 1)), "Character after '9' should be invalid");
            assertFalse(base16.isInAlphabet((byte) ('A' - 1)), "Character before 'A' should be invalid");
            assertFalse(base16.isInAlphabet((byte) ('F' + 1)), "Character after 'F' should be invalid");
        }
    }

    /**
     * Provides Base16 instances (lower-case and upper-case) for the parameterized test.
     */
    static Stream<Base16> base16Instances() {
        return Stream.of(new Base16(true), new Base16(false));
    }

    @DisplayName("should return false for values outside the hex character set")
    @ParameterizedTest
    @MethodSource("base16Instances")
    void shouldReturnFalseForNonHexValues(final Base16 base16) {
        // Test byte values that are never part of the Base16 alphabet
        assertFalse(base16.isInAlphabet((byte) -128), "Min byte value should be invalid");
        assertFalse(base16.isInAlphabet((byte) -1), "Byte value -1 should be invalid");
        assertFalse(base16.isInAlphabet((byte) 0), "NUL character should be invalid");
        assertFalse(base16.isInAlphabet((byte) 'G'), "Character 'G' should be invalid");
        assertFalse(base16.isInAlphabet((byte) 'g'), "Character 'g' should be invalid");
        assertFalse(base16.isInAlphabet((byte) 127), "DEL character should be invalid");
    }
}