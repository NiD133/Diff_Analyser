package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on the behavior of the {@link JsonIgnoreProperties.Value} class.
 */
// The original test extended a scaffolding class, which is omitted here for clarity
// as it was not used in this specific test case.
// Original class name: JsonIgnoreProperties_ESTestTest36
public class JsonIgnorePropertiesValueTest {

    /**
     * Tests the reflexivity property of the equals() method.
     * An object must always be equal to itself.
     */
    @Test
    public void emptyValueShouldBeEqualToItself() {
        // Arrange
        // Get the singleton instance representing an empty configuration.
        final JsonIgnoreProperties.Value emptyValue = JsonIgnoreProperties.Value.EMPTY;

        // Act & Assert
        // An object should always be equal to itself. This test verifies
        // this basic property for the JsonIgnoreProperties.Value.EMPTY instance.
        assertEquals("The EMPTY instance should be equal to itself.", emptyValue, emptyValue);
    }
}