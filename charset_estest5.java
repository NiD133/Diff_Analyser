package org.apache.commons.lang3;

import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharSet}.
 */
public class CharSetTest {

    /**
     * Tests that the CharSet constructor throws a NullPointerException
     * when passed a null array, as is expected by its contract.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullArrayThrowsNullPointerException() {
        // The constructor is protected, but this test class, being in the same package,
        // can invoke it directly to test this edge case.
        // The cast to (String[]) is necessary to distinguish the call from
        // a call with a single null String.
        new CharSet((String[]) null);
    }
}