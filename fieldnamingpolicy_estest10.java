package com.google.gson;

import org.junit.Test;
import java.lang.reflect.Field;

/**
 * Unit tests for the {@link FieldNamingPolicy#IDENTITY} naming policy.
 */
public class FieldNamingPolicyIdentityTest {

    /**
     * Verifies that the IDENTITY policy's translateName method throws a
     * NullPointerException when given a null Field. This ensures the method
     * correctly handles invalid input.
     */
    @Test(expected = NullPointerException.class)
    public void translateName_withNullField_shouldThrowNullPointerException() {
        // Arrange
        FieldNamingPolicy identityPolicy = FieldNamingPolicy.IDENTITY;

        // Act & Assert
        // This call is expected to throw a NullPointerException, which is
        // handled and asserted by the @Test(expected=...) annotation.
        identityPolicy.translateName((Field) null);
    }
}