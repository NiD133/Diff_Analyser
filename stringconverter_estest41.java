package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link StringConverter}.
 */
public class StringConverterTest {

    @Test
    public void getSupportedType_shouldReturnStringClass() {
        // Arrange
        StringConverter converter = StringConverter.INSTANCE;
        Class<?> expectedSupportedType = String.class;

        // Act
        Class<?> actualSupportedType = converter.getSupportedType();

        // Assert
        assertEquals(expectedSupportedType, actualSupportedType);
    }
}