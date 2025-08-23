package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link JsonSetter.Value} class, focusing on its equality contract.
 */
public class JsonSetterValueTest {

    /**
     * Verifies that the equals() method of JsonSetter.Value returns false
     * when the object is compared with null, adhering to the Java Object contract.
     */
    @Test
    public void equalsShouldReturnFalseWhenComparedToNull() {
        // Arrange: Create a JsonSetter.Value instance with specific null handling settings.
        // This also implicitly tests the factory method's behavior.
        Nulls nullsHandling = Nulls.AS_EMPTY;
        JsonSetter.Value setterValue = JsonSetter.Value.forValueNulls(nullsHandling, nullsHandling);

        // Assert state before the main test to ensure our object is configured correctly.
        assertEquals("Value nulls should be configured as AS_EMPTY",
                Nulls.AS_EMPTY, setterValue.getValueNulls());
        assertEquals("Content nulls should be configured as AS_EMPTY",
                Nulls.AS_EMPTY, setterValue.getContentNulls());

        // Act & Assert: The primary goal is to test the equals() method against null.
        boolean isEqual = setterValue.equals(null);
        assertFalse("A JsonSetter.Value instance should never be equal to null.", isEqual);
    }
}