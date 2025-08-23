package com.google.common.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;
import org.junit.Test;

/**
 * Tests for {@link HostSpecifier}, focusing on specific edge cases.
 */
public class HostSpecifierTest {

    /**
     * Verifies that fromValid() throws an AssertionError for the unspecified IPv6 address "::".
     *
     * <p>This test documents an unexpected behavior in the implementation. The input "::" is a
     * syntactically valid IPv6 address, so {@code fromValid} should ideally succeed. However, it
     * was found to throw an {@link AssertionError} wrapping an {@link UnknownHostException}.
     *
     * <p>This test captures this specific failure mode. If this test begins to fail, it likely
     * means the underlying bug has been fixed. The test should then be updated to assert the new,
     * correct behavior (e.g., successful parsing).
     */
    @Test
    public void fromValid_withUnspecifiedIPv6Address_throwsAssertionError() {
        String unspecifiedIPv6Address = "::";

        // Assert that fromValid("::") throws an AssertionError, which is unexpected behavior.
        AssertionError error = assertThrows(
                AssertionError.class,
                () -> HostSpecifier.fromValid(unspecifiedIPv6Address));

        // Further inspect the exception to confirm it's the specific bug we're testing for.
        Throwable cause = error.getCause();
        assertNotNull("The AssertionError should have a cause.", cause);
        assertTrue("The cause should be an UnknownHostException.", cause instanceof UnknownHostException);
        assertEquals(
                "The exception message should indicate an issue with IPv4 parsing.",
                "Not IPv4: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]",
                cause.getMessage());
    }
}