package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Test suite for the {@link JsonSetter.Value} class, focusing on its equals() method.
 */
public class JsonSetter_ESTestTest7 extends JsonSetter_ESTest_scaffolding {

    /**
     * Tests that two {@link JsonSetter.Value} instances are not considered equal
     * if they have different `contentNulls` settings.
     */
    @Test
    public void testEqualsReturnsFalseForDifferentContentNulls() {
        // Arrange
        // Create a default value instance, where both nulls and contentNulls are Nulls.DEFAULT.
        JsonSetter.Value defaultValue = JsonSetter.Value.empty();

        // Create another instance with a specific contentNulls setting (SKIP).
        JsonSetter.Value valueWithContentNullsSkip = JsonSetter.Value.forContentNulls(Nulls.SKIP);

        // Sanity check: Ensure the second value was created as expected.
        // It should have the default setting for valueNulls but the specific one for contentNulls.
        assertEquals(Nulls.DEFAULT, valueWithContentNullsSkip.getValueNulls());
        assertEquals(Nulls.SKIP, valueWithContentNullsSkip.getContentNulls());

        // Act & Assert
        // The core of the test: verify that the two instances are not equal.
        assertFalse("Values with different contentNulls settings should not be equal",
                defaultValue.equals(valueWithContentNullsSkip));

        // For completeness, also verify their hash codes are different, as per the equals/hashCode contract.
        assertNotEquals("Hash codes should differ for unequal objects",
                defaultValue.hashCode(), valueWithContentNullsSkip.hashCode());
    }
}