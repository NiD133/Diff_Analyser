package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    /**
     * Tests that getFieldType() returns the correct field type, which is 'years'.
     */
    @Test
    public void getFieldType_shouldReturnYearsType() {
        // Arrange
        Years yearsInstance = Years.years(20);
        DurationFieldType expectedType = DurationFieldType.years();

        // Act
        DurationFieldType actualType = yearsInstance.getFieldType();

        // Assert
        assertEquals(expectedType, actualType);
    }
}