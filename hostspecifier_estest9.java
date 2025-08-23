package com.google.common.net;

import org.junit.Test;

/**
 * Tests for {@link HostSpecifier}.
 */
public class HostSpecifierTest {

    /**
     * Verifies that {@code fromValid()} rejects specifiers that are syntactically invalid,
     * such as a string that starts with a colon. This format is invalid because it
     * resembles a port number without a preceding host.
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValid_whenSpecifierStartsWithColon_throwsIllegalArgumentException() {
        HostSpecifier.fromValid(":7");
    }
}