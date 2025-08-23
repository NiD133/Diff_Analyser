package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

/**
 * Tests for {@link TypeHandler}.
 */
public class TypeHandlerTest {

    /**
     * Verifies that the default TypeHandler provides a non-null converter for the built-in Date type.
     */
    @Test
    public void getConverterShouldReturnConverterForBuiltInDateType() {
        // Arrange
        TypeHandler typeHandler = TypeHandler.getDefault();

        // Act
        Converter<Date, ?> dateConverter = typeHandler.getConverter(Date.class);

        // Assert
        assertNotNull("The default TypeHandler should have a registered converter for java.util.Date", dateConverter);
    }
}