package com.google.common.util.concurrent;

import org.junit.Test;
import java.util.concurrent.Future;

/**
 * Contains tests for the FuturesGetChecked utility class.
 * Note: The original test was auto-generated. This version has been refactored for better
 * understandability and maintainability.
 */
public class FuturesGetChecked_ESTestTest6 {

    /**
     * Verifies that getChecked throws a NullPointerException when the provided Future is null.
     */
    @Test(expected = NullPointerException.class)
    public void getChecked_withNullFuture_throwsNullPointerException() throws Exception {
        // The method under test is expected to throw a NullPointerException
        // when its 'future' argument is null, as it will attempt to call .get() on it.
        FuturesGetChecked.getChecked((Future<Object>) null, Exception.class);
    }
}