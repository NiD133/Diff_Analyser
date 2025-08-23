package com.google.common.io;

import org.junit.Test;

/**
 * Tests for {@link MultiInputStream}.
 */
public class MultiInputStreamTest {

    @Test(expected = NullPointerException.class)
    public void constructor_whenIteratorIsNull_throwsNullPointerException() {
        // Attempt to construct a MultiInputStream with a null iterator.
        // The constructor is expected to fail fast with a NullPointerException,
        // which is verified by the @Test(expected=...) annotation.
        new MultiInputStream(null);
    }
}