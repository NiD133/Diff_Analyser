package com.google.gson;

import org.junit.Test;

/**
 * Unit tests for the static helper methods in {@link FieldNamingPolicy}.
 */
public class FieldNamingPolicyTest {

    /**
     * Verifies that the {@code separateCamelCase} method throws a
     * {@code NullPointerException} when the input string is null.
     */
    @Test(expected = NullPointerException.class)
    public void separateCamelCase_givenNullInput_throwsNullPointerException() {
        // The method should reject null input before attempting any processing.
        // The separator character ('_') is arbitrary and does not affect this test's outcome.
        FieldNamingPolicy.separateCamelCase(null, '_');
    }
}