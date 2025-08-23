package com.google.gson;

import java.lang.reflect.Field;
import org.junit.Test;

/**
 * Tests for the {@link FieldNamingPolicy} enum.
 */
public class FieldNamingPolicyTest {

    /**
     * Verifies that calling {@code translateName} with a null {@code Field}
     * argument throws a {@code NullPointerException}.
     */
    @Test(expected = NullPointerException.class)
    public void translateName_whenFieldIsNull_throwsNullPointerException() {
        // An arbitrary policy is chosen here, as the null-check behavior
        // is expected to be consistent across all policies.
        FieldNamingPolicy policy = FieldNamingPolicy.LOWER_CASE_WITH_DASHES;

        // This call should fail because the input Field is null.
        policy.translateName(null);
    }
}