package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link TypeHandler} class.
 */
public class TypeHandlerTest {

    /**
     * Verifies that createClass successfully returns the correct Class object
     * for a valid, fully qualified class name.
     */
    @Test
    public void createClass_shouldReturnCorrectClass_whenGivenValidClassName() throws ParseException {
        // Arrange
        String validClassName = "org.apache.commons.cli.ParseException";
        Class<ParseException> expectedClass = ParseException.class;

        // Act
        Class<?> actualClass = TypeHandler.createClass(validClassName);

        // Assert
        assertEquals("The returned class should match the expected class.", expectedClass, actualClass);
    }
}