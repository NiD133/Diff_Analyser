package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link IOCase#checkStartsWith(String, String)} method.
 * This suite focuses on the {@link IOCase#SENSITIVE} behavior.
 */
@DisplayName("IOCase.SENSITIVE.checkStartsWith()")
class IOCaseCheckStartsWithTest {

    @ParameterizedTest
    @CsvSource({
        "'ABC', 'A'",
        "'ABC', 'AB'",
        "'ABC', 'ABC'",  // A string starts with itself
        "'ABC', ''",     // An empty prefix is always a match
        "'', ''"         // An empty string starts with an empty prefix
    })
    @DisplayName("should return true for matching prefixes")
    void checkStartsWith_sensitive_whenPrefixMatches_shouldReturnTrue(final String str, final String prefix) {
        assertTrue(IOCase.SENSITIVE.checkStartsWith(str, prefix),
            () -> String.format("Expected '%s' to start with '%s'", str, prefix));
    }

    @ParameterizedTest
    @CsvSource({
        "'ABC', 'BC'",    // Not a prefix, just a substring
        "'ABC', 'C'",     // Not a prefix, just a substring
        "'ABC', 'ABCD'",  // Prefix cannot be longer than the string
        "'', 'A'"         // Empty string cannot start with a non-empty prefix
    })
    @DisplayName("should return false for non-matching prefixes")
    void checkStartsWith_sensitive_whenPrefixDoesNotMatch_shouldReturnFalse(final String str, final String prefix) {
        assertFalse(IOCase.SENSITIVE.checkStartsWith(str, prefix),
            () -> String.format("Expected '%s' NOT to start with '%s'", str, prefix));
    }

    @ParameterizedTest
    @MethodSource("nullArguments")
    @DisplayName("should return false for any null input")
    void checkStartsWith_sensitive_withNullInputs_shouldReturnFalse(final String str, final String prefix) {
        assertFalse(IOCase.SENSITIVE.checkStartsWith(str, prefix),
            () -> String.format("checkStartsWith with null inputs (str=%s, prefix=%s) should be false", str, prefix));
    }

    /**
     * Provides arguments for testing null inputs, as they are handled specially by the method.
     * @return A stream of arguments containing different combinations of null and non-null strings.
     */
    private static Stream<Arguments> nullArguments() {
        return Stream.of(
            Arguments.of("ABC", null),
            Arguments.of(null, "ABC"),
            Arguments.of(null, null)
        );
    }
}