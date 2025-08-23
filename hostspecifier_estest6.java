package com.google.common.net;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for {@link HostSpecifier}.
 */
public class HostSpecifierTest {

    @Test
    public void from_withNullInput_shouldThrowNullPointerException() {
        // The HostSpecifier.from() method is expected to reject null input
        // by throwing a NullPointerException, as is common practice in Guava.
        assertThrows(NullPointerException.class, () -> HostSpecifier.from(null));
    }
}