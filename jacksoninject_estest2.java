package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JacksonInject.Value} class.
 */
public class JacksonInjectValueTest {

    @Test
    public void toString_shouldReflectOptionalProperty_whenSetToFalse() {
        // Arrange
        // Create a base JacksonInject.Value instance with a null ID.
        // By default, 'useInput' and 'optional' will also be null.
        JacksonInject.Value baseValue = JacksonInject.Value.forId(null);
        String expectedToString = "JacksonInject.Value(id=null,useInput=null,optional=false)";

        // Act
        // Create a new instance by setting the 'optional' property to false.
        JacksonInject.Value valueWithOptionalSet = baseValue.withOptional(false);
        String actualToString = valueWithOptionalSet.toString();

        // Assert
        // Verify that the string representation correctly includes the 'optional=false' part.
        assertEquals(expectedToString, actualToString);
    }
}