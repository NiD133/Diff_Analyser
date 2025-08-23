package com.google.common.net;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * Unit tests for {@link HostSpecifier}.
 */
class HostSpecifierTest {

    /**
     * Verifies that the from() method correctly throws a ParseException when given an
     * invalid specifier that ends with a colon, which is not a valid format for a
     * domain name or an IP address.
     */
    @Test
    @DisplayName("from() should throw ParseException for an invalid specifier ending with a colon")
    void from_invalidSpecifierEndingWithColon_throwsParseException() {
        // Arrange: Define an input string that is not a valid host specifier.
        // A valid specifier cannot end with a colon unless it's part of an IPv6 address
        // in brackets, which this is not.
        final String invalidSpecifier = "jR:";

        // Act & Assert: Verify that calling from() with the invalid input throws the
        // expected exception.
        ParseException exception = assertThrows(
            ParseException.class,
            () -> HostSpecifier.from(invalidSpecifier)
        );

        // Assert: Further verify that the exception message is correct, providing
        // clear feedback on failure.
        assertEquals("Invalid host specifier: " + invalidSpecifier, exception.getMessage());
    }
}