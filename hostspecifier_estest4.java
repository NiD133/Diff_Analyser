package com.google.common.net;

import org.junit.Test;

/**
 * Unit tests for {@link HostSpecifier}.
 */
public class HostSpecifierTest {

    /**
     * Verifies that fromValid() throws a NullPointerException when given a null input,
     * which is the expected behavior for Guava APIs.
     */
    @Test(expected = NullPointerException.class)
    public void fromValid_givenNullString_throwsNullPointerException() {
        HostSpecifier.fromValid(null);
    }
}