package org.joda.time.convert;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for the {@link StringConverter} class.
 */
public class StringConverterTest {

    /**
     * Tests that the converter correctly identifies its supported type.
     */
    @Test
    public void getSupportedType_shouldReturnStringClass() {
        // Arrange: The StringConverter is a singleton, accessed via INSTANCE.
        StringConverter converter = StringConverter.INSTANCE;

        // Act: Call the method under test.
        Class<?> supportedType = converter.getSupportedType();

        // Assert: Verify that the returned type is String.class.
        assertEquals("The converter should support String.class", String.class, supportedType);
    }
}