package com.google.common.net;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests for {@link HostSpecifier#isValid(String)}.
 *
 * This class provides a more understandable and maintainable version of the
 * original auto-generated test.
 */
public class HostSpecifierTest {

    /**
     * Verifies that isValid() correctly identifies the compressed IPv6 unspecified
     * address ("::") as a valid host specifier.
     *
     * The original auto-generated test incorrectly expected an AssertionError,
     * likely capturing a bug in the underlying implementation or its dependencies.
     * A robust test must verify the public contract, which states that a valid
     * IPv6 address string is a valid host specifier.
     */
    @Test
    public void isValid_withCompressedIpv6UnspecifiedAddress_shouldReturnTrue() {
        // GIVEN a string representing the compressed IPv6 unspecified address.
        String ipv6UnspecifiedAddress = "::";

        // WHEN the isValid method is called.
        boolean result = HostSpecifier.isValid(ipv6UnspecifiedAddress);

        // THEN the result should be true.
        assertTrue(
                "Expected '::' to be recognized as a valid host specifier.",
                result);
    }
}