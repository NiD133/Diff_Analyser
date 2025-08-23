package com.google.common.net;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import org.junit.Test;

/**
 * Tests for {@link HostSpecifier}.
 */
public class HostSpecifierTest {

    @Test
    public void toString_forIPv4Address_returnsOriginalString() throws ParseException {
        // Arrange
        String ipv4Address = "127.0.0.1";
        HostSpecifier hostSpecifier = HostSpecifier.from(ipv4Address);

        // Act
        String result = hostSpecifier.toString();

        // Assert
        assertEquals(ipv4Address, result);
    }
}