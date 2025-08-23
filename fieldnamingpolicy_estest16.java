package com.google.gson;

import org.junit.Test;
import java.lang.reflect.Field;

/**
 * Tests for the {@link FieldNamingPolicy} enum.
 */
public class FieldNamingPolicyTest {

    /**
     * Verifies that the translateName method throws a NullPointerException
     * when a null Field is provided as input.
     */
    @Test(expected = NullPointerException.class)
    public void translateName_withNullField_throwsNullPointerException() {
        // Arrange
        // Any policy can be used here, as the null check should be common behavior.
        FieldNamingPolicy policy = FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES;

        // Act & Assert
        // The method call is expected to throw a NullPointerException,
        // which is asserted by the @Test(expected=...) annotation.
        policy.translateName((Field) null);
    }
}