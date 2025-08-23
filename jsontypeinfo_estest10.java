package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonTypeInfo.Value} class.
 */
public class JsonTypeInfoValueTest {

    /**
     * Verifies that the toString() method of the default {@code JsonTypeInfo.Value.EMPTY}
     * instance produces a correct and comprehensive string representation of its state.
     */
    @Test
    public void toString_onEmptyValue_shouldReturnCorrectStringRepresentation() {
        // Arrange
        JsonTypeInfo.Value emptyValue = JsonTypeInfo.Value.EMPTY;
        String expectedString = "JsonTypeInfo.Value(idType=NONE,includeAs=PROPERTY,propertyName=null,defaultImpl=NULL,idVisible=false,requireTypeIdForSubtypes=null)";

        // Act
        String actualString = emptyValue.toString();

        // Assert
        assertEquals(expectedString, actualString);
    }
}