package com.google.gson;

import java.lang.reflect.Field;
import org.junit.Test;

/**
 * Tests for the {@link FieldNamingPolicy} enum, focusing on edge cases.
 */
public class FieldNamingPolicyTest {

    /**
     * Verifies that calling translateName with a null Field argument
     * results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void translateName_whenFieldIsNull_shouldThrowNullPointerException() {
        FieldNamingPolicy policy = FieldNamingPolicy.LOWER_CASE_WITH_DOTS;
        
        // This call is expected to throw a NullPointerException because the input Field is null.
        policy.translateName(null);
    }
}