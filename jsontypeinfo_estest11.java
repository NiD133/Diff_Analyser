package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link JsonTypeInfo.Value} class, focusing on its {@code toString()} method.
 */
public class JsonTypeInfoValueToStringTest {

    /**
     * Verifies that the toString() method of JsonTypeInfo.Value generates a correct
     * and comprehensive string representation of its state.
     */
    @Test
    public void toStringShouldGenerateCorrectStringRepresentation() {
        // Arrange: Define the configuration for a JsonTypeInfo.Value instance.
        final JsonTypeInfo.Id idType = JsonTypeInfo.Id.MINIMAL_CLASS;
        final JsonTypeInfo.As inclusion = JsonTypeInfo.As.WRAPPER_OBJECT;
        final String propertyName = "@2LLQRbW9{J2*\"1GY";
        final Class<?> defaultImpl = Object.class;
        final boolean idVisible = false;
        
        // The original test used Boolean.valueOf(propertyName), which results in 'false'.
        // Using Boolean.FALSE directly is much clearer and less error-prone.
        final Boolean requireTypeIdForSubtypes = Boolean.FALSE;

        // Act: Construct the Value object and get its string representation.
        JsonTypeInfo.Value value = JsonTypeInfo.Value.construct(
                idType,
                inclusion,
                propertyName,
                defaultImpl,
                idVisible,
                requireTypeIdForSubtypes
        );
        String actualToString = value.toString();

        // Assert: Verify the string representation matches the expected format.
        String expectedToString = "JsonTypeInfo.Value(idType=MINIMAL_CLASS,"
                + "includeAs=WRAPPER_OBJECT,"
                + "propertyName=@2LLQRbW9{J2*\"1GY,"
                + "defaultImpl=java.lang.Object,"
                + "idVisible=false,"
                + "requireTypeIdForSubtypes=false)";

        assertEquals(expectedToString, actualToString);
    }
}