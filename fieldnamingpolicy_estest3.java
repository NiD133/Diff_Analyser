package com.google.gson;

import org.junit.Test;

/**
 * Tests for the static helper methods in {@link FieldNamingPolicy}.
 */
public class FieldNamingPolicyTest {

    /**
     * Verifies that calling upperCaseFirstLetter with a null input
     * correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void upperCaseFirstLetter_withNullInput_throwsNullPointerException() {
        FieldNamingPolicy.upperCaseFirstLetter(null);
    }
}