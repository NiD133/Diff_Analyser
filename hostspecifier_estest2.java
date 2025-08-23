package com.google.common.net;

import org.junit.Test;

/**
 * Unit tests for {@link HostSpecifier}.
 */
public class HostSpecifierTest {

    /**
     * Verifies that the isValid() method adheres to the common Guava convention
     * of throwing a NullPointerException when a null argument is provided.
     */
    @Test(expected = NullPointerException.class)
    public void isValid_whenInputIsNull_throwsNullPointerException() {
        HostSpecifier.isValid(null);
    }
}