package com.google.gson;

import java.lang.reflect.Field;
import org.junit.Test;

/**
 * Test suite for {@link FieldNamingPolicy}.
 */
public class FieldNamingPolicyTest {

    /**
     * Verifies that the translateName method throws a NullPointerException when a null Field is provided.
     * This is a crucial check for input validation, as the method is expected to operate on the
     * Field object. This behavior should be consistent across all naming policies.
     */
    @Test(expected = NullPointerException.class)
    public void translateName_withNullField_shouldThrowNullPointerException() {
        // Arrange: Select any policy, as the null-check behavior is fundamental.
        FieldNamingPolicy policy = FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES;
        Field nullField = null;

        // Act & Assert: Call the method with a null input.
        // The @Test(expected=...) annotation asserts that a NullPointerException is thrown.
        policy.translateName(nullField);
    }
}