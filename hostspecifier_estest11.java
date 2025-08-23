package com.google.common.net;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import org.junit.Test;

/**
 * Unit tests for {@link HostSpecifier}.
 */
public class HostSpecifierTest {

    /**
     * Verifies that two HostSpecifier instances created from the exact same
     * valid IP address string are considered equal.
     */
    @Test
    public void equals_whenCreatedFromSameIpAddress_shouldBeEqual() throws ParseException {
        // Arrange
        String ipAddress = "127.0.0.1";
        HostSpecifier specifier1 = HostSpecifier.from(ipAddress);
        HostSpecifier specifier2 = HostSpecifier.from(ipAddress);

        // Assert
        // The .equals() method should return true, and assertEquals uses this method.
        assertEquals(specifier1, specifier2);
    }
}