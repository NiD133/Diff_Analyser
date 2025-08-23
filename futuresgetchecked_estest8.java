package com.google.common.util.concurrent;

import org.junit.Test;

/**
 * Tests for the static helper methods in {@link FuturesGetChecked}.
 */
public class FuturesGetCheckedTest {

    /**
     * Verifies that checkExceptionClassValidity throws a NullPointerException
     * when given a null class reference. This ensures the method correctly
     * handles invalid, null input as per standard contract expectations.
     */
    @Test(expected = NullPointerException.class)
    public void checkExceptionClassValidity_withNullClass_throwsNullPointerException() {
        FuturesGetChecked.checkExceptionClassValidity(null);
    }
}