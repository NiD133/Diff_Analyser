package com.google.common.net;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit tests for {@link HostSpecifier}.
 */
@RunWith(JUnit4.class)
public class HostSpecifierTest {

    @Test
    public void isValid_withValidIPv4Address_returnsTrue() {
        // The isValid() method should correctly identify a standard IPv4 address string
        // as a valid host specifier.
        assertTrue(
            "Expected '0.0.0.0' to be a valid host specifier",
            HostSpecifier.isValid("0.0.0.0"));
    }
}