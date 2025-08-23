package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on the behavior of the {@link JsonIncludeProperties.Value} class.
 * The original test class name "JsonIncludeProperties_ESTestTest5" and its scaffolding
 * are artifacts of a test generation tool. In a real-world scenario, this test would be
 * part of a more descriptively named class like "JsonIncludePropertiesValueTest".
 */
public class JsonIncludeProperties_ESTestTest5 extends JsonIncludeProperties_ESTest_scaffolding {

    /**
     * Verifies that the equals() method of JsonIncludeProperties.Value returns false
     * when the object being compared is of a different type. This is a standard
     * part of the equals() contract.
     */
    @Test
    public void equalsShouldReturnFalseWhenComparedWithDifferentType() {
        // Arrange: Create a Value instance and an object of an incompatible type.
        final JsonIncludeProperties.Value value = JsonIncludeProperties.Value.all();
        final Object otherObject = new Object();

        // Act & Assert: The equals method should correctly identify the type mismatch.
        assertFalse("Value.equals(anotherType) should be false", value.equals(otherObject));
    }
}