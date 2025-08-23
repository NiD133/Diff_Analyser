package com.google.common.util.concurrent;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit tests for {@link FuturesGetChecked}.
 */
public class FuturesGetCheckedTest {

    /**
     * Verifies that isCheckedException() correctly identifies a standard checked exception.
     */
    @Test
    public void isCheckedException_whenGivenCheckedException_shouldReturnTrue() {
        // The method should return true for Exception.class, as it is a checked exception
        // (i.e., not a subclass of RuntimeException).
        assertTrue(
            "Exception.class should be identified as a checked exception",
            FuturesGetChecked.isCheckedException(Exception.class));
    }

    /**
     * Verifies that isCheckedException() correctly identifies a runtime (unchecked) exception.
     */
    @Test
    public void isCheckedException_whenGivenRuntimeException_shouldReturnFalse() {
        // The method should return false for RuntimeException.class, as it is an
        // unchecked exception.
        assertFalse(
            "RuntimeException.class should be identified as an unchecked exception",
            FuturesGetChecked.isCheckedException(RuntimeException.class));
    }

    /**
     * Verifies that isCheckedException() correctly identifies a subclass of a runtime exception.
     */
    @Test
    public void isCheckedException_whenGivenSubclassOfRuntimeException_shouldReturnFalse() {
        // Subclasses of RuntimeException, like IllegalArgumentException, are also unchecked.
        assertFalse(
            "IllegalArgumentException should be identified as an unchecked exception",
            FuturesGetChecked.isCheckedException(IllegalArgumentException.class));
    }
}