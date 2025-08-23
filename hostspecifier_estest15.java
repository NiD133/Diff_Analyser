package com.google.common.net;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Tests for {@link HostSpecifier}.
 */
public class HostSpecifierTest {

    @Test
    public void isValid_returnsFalse_forStringWithInvalidCharacters() {
        // The HostSpecifier.isValid() method should return false for a string
        // containing characters that are not permitted in domain names or IP addresses,
        // such as '$', '|', or ';'.
        String specifierWithInvalidChars = "E$U|;qI:3";

        assertFalse(
                "Specifier with invalid characters should be considered invalid",
                HostSpecifier.isValid(specifierWithInvalidChars));
    }
}