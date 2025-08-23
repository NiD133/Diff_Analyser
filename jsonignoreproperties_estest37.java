package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link JsonIgnoreProperties.Value} class.
 */
public class JsonIgnorePropertiesValueTest {

    /**
     * Verifies that the {@code forIgnoreUnknown(true)} factory method correctly initializes
     * a {@link JsonIgnoreProperties.Value} instance. It should set {@code ignoreUnknown} to true
     * and retain the default values for all other properties.
     */
    @Test
    public void forIgnoreUnknownShouldCreateValueWithCorrectProperties() {
        // Arrange & Act
        // Create an instance using the factory method under test.
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.forIgnoreUnknown(true);

        // Assert
        // The primary property should be set to the specified value.
        assertTrue("ignoreUnknown should be true", value.getIgnoreUnknown());

        // Other properties should retain their default values.
        assertFalse("allowGetters should default to false", value.getAllowGetters());
        assertFalse("allowSetters should default to false", value.getAllowSetters());
        assertTrue("merge should default to true", value.getMerge());
        assertTrue("The set of ignored properties should be empty", value.getIgnored().isEmpty());
    }

    /**
     * Verifies that the {@code equals} method returns false when a {@link JsonIgnoreProperties.Value}
     * instance is compared with an object of a different, unrelated type.
     */
    @Test
    public void equalsShouldReturnFalseWhenComparedWithDifferentType() {
        // Arrange
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.forIgnoreUnknown(true);
        Object otherObject = new Object();

        // Act
        boolean result = value.equals(otherObject);

        // Assert
        assertFalse("equals() should return false for an object of a different type", result);
    }
}