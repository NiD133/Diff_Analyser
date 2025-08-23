package com.google.common.net;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for invalid inputs to {@link HostSpecifier}.
 */
class HostSpecifierInvalidInputTest {

    private static Stream<String> invalidIpAddresses() {
        return Stream.of(
                "1.2.3",             // Not a valid IPv4 address
                "2001:db8::1::::::0",  // Too many parts in IPv6 address
                "[2001:db8::1",       // Mismatched brackets
                "[::]:80"            // Port is not allowed
        );
    }

    /**
     * Verifies that fromValid() rejects invalid IP address specifiers.
     */
    @ParameterizedTest
    @MethodSource("invalidIpAddresses")
    void fromValid_withInvalidIpAddress_throwsIllegalArgumentException(String invalidSpecifier) {
        assertThrows(
                IllegalArgumentException.class,
                () -> HostSpecifier.fromValid(invalidSpecifier));
    }

    /**
     * Verifies that from() rejects invalid IP address specifiers.
     */
    @ParameterizedTest
    @MethodSource("invalidIpAddresses")
    void from_withInvalidIpAddress_throwsParseException(String invalidSpecifier) {
        ParseException exception = assertThrows(
                ParseException.class,
                () -> HostSpecifier.from(invalidSpecifier));

        // The ParseException should be caused by an IllegalArgumentException.
        assertThat(exception).hasCauseThat().isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Verifies that isValid() returns false for invalid IP address specifiers.
     */
    @ParameterizedTest
    @MethodSource("invalidIpAddresses")
    void isValid_withInvalidIpAddress_returnsFalse(String invalidSpecifier) {
        assertFalse(HostSpecifier.isValid(invalidSpecifier));
    }
}