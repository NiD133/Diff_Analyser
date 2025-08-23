package com.google.gson;

import org.junit.Test;

/**
 * Tests for the {@link ToNumberPolicy} enum, focusing on handling invalid inputs.
 */
public class ToNumberPolicyTest {

    /**
     * Verifies that the {@code LAZILY_PARSED_NUMBER} policy throws a {@link NullPointerException}
     * when attempting to read from a null {@code JsonReader}. This is a fundamental contract
     * for any implementation of the {@code readNumber} method.
     */
    @Test(expected = NullPointerException.class)
    public void lazilyParsedNumberPolicy_readFromNullReader_throwsNullPointerException() {
        // Arrange
        ToNumberPolicy policy = ToNumberPolicy.LAZILY_PARSED_NUMBER;

        // Act: Attempt to read from a null JsonReader.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        policy.readNumber(null);
    }
}