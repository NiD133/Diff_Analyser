package com.google.common.net;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link HostSpecifier}.
 */
public class HostSpecifierTest {

    /**
     * Verifies the reflexive property of the equals() method,
     * ensuring that an object is always equal to itself.
     */
    @Test
    public void equals_givenSameInstance_returnsTrue() {
        // Arrange: Create a HostSpecifier instance from a valid IP address.
        // Using fromValid() is suitable here as the input is a known-valid constant.
        HostSpecifier hostSpecifier = HostSpecifier.fromValid("127.0.0.1");

        // Act & Assert: An object must be equal to itself.
        assertEquals(hostSpecifier, hostSpecifier);
    }
}