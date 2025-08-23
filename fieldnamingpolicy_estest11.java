package com.google.gson;

import java.lang.reflect.Field;
import org.junit.Test;

/**
 * Tests for the {@link FieldNamingPolicy} enum.
 */
public class FieldNamingPolicyTest {

    /**
     * Verifies that calling translateName with a null Field object results in a NullPointerException.
     * This behavior is expected for all naming policies because they all attempt to access the
     * field's name before applying any transformation.
     */
    @Test(expected = NullPointerException.class)
    public void translateName_withNullField_throwsNullPointerException() {
        // Arrange: Select any policy; the null-check behavior is universal.
        FieldNamingPolicy policy = FieldNamingPolicy.UPPER_CAMEL_CASE;

        // Act: Call the method with a null Field.
        // The @Test(expected) annotation will assert that a NullPointerException is thrown.
        policy.translateName((Field) null);
    }
}