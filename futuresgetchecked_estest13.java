package com.google.common.util.concurrent;

import org.junit.Test;

/**
 * Tests for the static helper methods in {@link FuturesGetChecked}.
 */
public class FuturesGetCheckedTest {

    /**
     * Verifies that {@code checkExceptionClassValidity} does not throw an exception
     * when given a valid exception class like {@code java.lang.Exception}.
     *
     * <p>A class is considered valid if it is a checked exception and has a constructor
     * that can be used by {@code Futures.getChecked} (e.g., one that accepts a {@code Throwable}
     * cause).
     */
    @Test
    public void checkExceptionClassValidity_withValidExceptionType_completesSuccessfully() {
        // The method under test should not throw any exception for a valid input.
        // This test passes if the following line executes without throwing.
        FuturesGetChecked.checkExceptionClassValidity(Exception.class);
    }
}