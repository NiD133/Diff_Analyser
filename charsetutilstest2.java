package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link CharSetUtils#containsAny(String, String...)}.
 */
// Renamed from CharSetUtilsTestTest2 for clarity and convention.
class CharSetUtilsTest extends AbstractLangTest {

    /**
     * Provides arguments for testing null and empty inputs, which should always return false.
     * The cast to (String) is necessary to distinguish the null from the String... varargs.
     */
    private static Stream<Arguments> nullAndEmptyInputs() {
        return Stream.of(
            Arguments.of(null, (String) null), // Both inputs null
            Arguments.of(null, ""),            // Null string, empty set
            Arguments.of(null, "a-z"),         // Null string, valid set
            Arguments.of("", (String) null),   // Empty string, null set
            Arguments.of("", ""),              // Empty string, empty set
            Arguments.of("", "a-z"),           // Empty string, valid set
            Arguments.of("hello", (String) null), // Valid string, null set
            Arguments.of("hello", "")             // Valid string, empty set
        );
    }

    @ParameterizedTest
    @MethodSource("nullAndEmptyInputs")
    @DisplayName("containsAny should return false for any null or empty input")
    void containsAny_withNullOrEmptyInput_shouldReturnFalse(final String str, final String set) {
        // As per Javadoc, any null or empty input should result in false.
        assertFalse(CharSetUtils.containsAny(str, set));
    }

    @Test
    @DisplayName("containsAny should return true when a character from the string is in the set")
    void containsAny_whenMatchIsFound_shouldReturnTrue() {
        // The character 'e' in "hello" is within the character set range "a-e".
        assertTrue(CharSetUtils.containsAny("hello", "a-e"),
            "Should find a match because 'e' is in the set.");

        // The characters 'l' and 'o' in "hello" are within the character set range "l-p".
        assertTrue(CharSetUtils.containsAny("hello", "l-p"),
            "Should find a match because 'l' and 'o' are in the set.");
    }

    @Test
    @DisplayName("containsAny should return false when no character from the string is in the set")
    void containsAny_whenNoMatchIsFound_shouldReturnFalse() {
        // No characters in "hello" are within the character set range "a-d".
        // This case is documented in the SUT Javadoc and is important for completeness.
        assertFalse(CharSetUtils.containsAny("hello", "a-d"),
            "Should not find a match as no characters are in the set.");
    }
}