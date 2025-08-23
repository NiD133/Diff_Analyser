package com.google.common.net;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link HostSpecifier}.
 */
public class HostSpecifierTest {

    /**
     * Verifies that HostSpecifier.toString() returns the original string
     * when the specifier is a valid IPv4 address.
     */
    @Test
    public void toString_shouldReturnUnchangedString_whenInputIsIPv4Address() {
        // Arrange: Define the input and the expected outcome.
        String ipv4Address = "127.0.0.1";
        
        // Act: Create the HostSpecifier from the valid input.
        HostSpecifier hostSpecifier = HostSpecifier.fromValid(ipv4Address);
        String result = hostSpecifier.toString();
        
        // Assert: Verify that the actual result matches the expected outcome.
        assertEquals(ipv4Address, result);
    }
}