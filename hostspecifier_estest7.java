package com.google.common.net;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HostSpecifier#from(String)}.
 */
class HostSpecifierTest {

    @Test
    @DisplayName("from() should throw ParseException for the unspecified IPv6 address '::'")
    void from_withUnspecifiedIpv6Address_throwsParseException() {
        // The "::" string represents the "unspecified" IPv6 address.
        // This address is not considered a valid host specifier for use in a URI,
        // so the from() method is expected to reject it.
        String unspecifiedIpv6Address = "::";

        // Assert that calling from() with this input throws the documented exception.
        ParseException exception = assertThrows(
            ParseException.class,
            () -> HostSpecifier.from(unspecifiedIpv6Address)
        );

        // For better diagnostics, verify the exception message contains the invalid input.
        assertTrue(
            exception.getMessage().contains(unspecifiedIpv6Address),
            "Exception message should contain the invalid specifier."
        );
    }
}