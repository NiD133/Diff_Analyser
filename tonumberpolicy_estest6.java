package com.google.gson;

import com.google.gson.stream.JsonReader;
import org.junit.Test;

/**
 * Tests for {@link ToNumberPolicy} focusing on invalid or edge-case inputs.
 */
public class ToNumberPolicyTest {

    /**
     * Verifies that calling readNumber with a null JsonReader throws a NullPointerException.
     * This is a fundamental contract test to ensure the method handles null inputs gracefully.
     */
    @Test(expected = NullPointerException.class)
    public void readNumber_whenReaderIsNull_throwsNullPointerException() {
        // The specific policy instance (DOUBLE) is arbitrary; any policy should exhibit
        // the same behavior for a null reader.
        ToNumberPolicy policy = ToNumberPolicy.DOUBLE;

        // This call is expected to throw a NullPointerException.
        policy.readNumber(null);
    }
}