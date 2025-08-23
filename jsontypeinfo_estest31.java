package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for the {@link JsonTypeInfo.Value} class.
 */
public class JsonTypeInfoValueTest {

    /**
     * This test verifies that the default EMPTY instance of {@link JsonTypeInfo.Value}
     * has a null value for the 'requireTypeIdForSubtypes' property, as it represents
     * a default, unconfigured state.
     */
    @Test
    public void emptyValueShouldHaveNullRequireTypeIdForSubtypes() {
        // Arrange
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;

        // Act
        Boolean requireTypeId = emptyValue.getRequireTypeIdForSubtypes();

        // Assert
        assertNull("The 'requireTypeIdForSubtypes' property for an EMPTY Value should be null.", requireTypeId);
    }
}