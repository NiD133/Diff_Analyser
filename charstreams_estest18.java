package com.google.common.io;

import org.junit.Test;

/**
 * Tests for {@link CharStreams}.
 */
public class CharStreamsTest {

    /**
     * Verifies that calling exhaust() with a null Readable argument
     * results in a NullPointerException, as mandated by the method's contract.
     */
    @Test(expected = NullPointerException.class)
    public void exhaust_givenNullReadable_throwsNullPointerException() {
        CharStreams.exhaust(null);
    }
}