package org.apache.commons.io.function;

import org.junit.Test;

/**
 * Tests for the {@link Uncheck} utility class.
 */
public class UncheckTest {

    /**
     * Tests that calling {@link Uncheck#run(IORunnable)} with a null argument
     * results in a {@link NullPointerException}.
     */
    @Test(expected = NullPointerException.class)
    public void testRun_withNullRunnable_throwsNullPointerException() {
        // The cast to IORunnable is necessary to resolve ambiguity between
        // the overloaded Uncheck.run() methods.
        Uncheck.run((IORunnable) null);
    }
}