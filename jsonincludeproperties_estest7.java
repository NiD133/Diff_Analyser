package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonIncludeProperties.Value} class.
 */
public class JsonIncludePropertiesValueTest {

    /**
     * Verifies the reflexive property of the equals() method.
     * An object must always be equal to itself.
     */
    @Test
    public void shouldBeEqualToItself() {
        // Arrange
        JsonIncludeProperties.Value allPropertiesValue = JsonIncludeProperties.Value.all();

        // Act & Assert
        // According to the equals() contract, an object must be equal to itself.
        assertEquals(allPropertiesValue, allPropertiesValue);
    }
}