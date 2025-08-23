package com.google.common.io;

import org.junit.Test;

/**
 * Tests for {@link CharStreams#asWriter(Appendable)}.
 */
public class CharStreamsTest {

    /**
     * Verifies that asWriter() throws a NullPointerException when given a null Appendable.
     */
    @Test(expected = NullPointerException.class)
    public void asWriter_withNullAppendable_throwsNullPointerException() {
        // The asWriter method should reject null inputs, as enforced by Preconditions.checkNotNull
        // within its implementation. This test confirms that passing null results in the expected
        // NullPointerException.
        CharStreams.asWriter(null);
    }
}