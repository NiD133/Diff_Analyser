package com.google.common.reflect;

import static org.junit.Assert.assertEquals;

import java.lang.annotation.Annotation;
import org.junit.Test;

/**
 * Tests for {@link Parameter#toString()}.
 */
public class ParameterToStringTest {

    @Test
    public void toString_withNullType_returnsStringWithNullAndArgumentIndex() {
        // Arrange
        final int parameterPosition = 11;
        
        // This test case simulates a scenario where the Parameter's type is unknown (null).
        // The Parameter constructor is package-private, so this test is structured as if
        // it were in the same package, focusing on the unit behavior of toString().
        Parameter parameter = new Parameter(
                null,              // invokable
                parameterPosition,
                null,              // typeToken
                new Annotation[0], // annotations
                null               // annotatedType
        );

        String expectedToString = "null arg11";

        // Act
        String actualToString = parameter.toString();

        // Assert
        assertEquals("The toString() output should correctly format a parameter with a null type.",
                expectedToString, actualToString);
    }
}