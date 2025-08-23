package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link IllegalFieldValueException}.
 */
public class IllegalFieldValueExceptionTest {

    @Test
    public void constructor_WithDurationFieldTypeAndStringValue_ShouldSetPropertiesCorrectly() {
        // Arrange
        final DurationFieldType fieldType = DurationFieldType.centuries();
        final String illegalValue = "InvalidTextValue";
        final String expectedMessage = "Value \"InvalidTextValue\" for centuries is not supported";

        // Act
        IllegalFieldValueException exception = new IllegalFieldValueException(fieldType, illegalValue);

        // Assert
        // Verify that the exception message is formatted as expected.
        assertEquals(expectedMessage, exception.getMessage());

        // Verify that the exception's properties are correctly initialized.
        assertEquals(fieldType, exception.getDurationFieldType());
        assertEquals("centuries", exception.getFieldName());
        assertEquals(illegalValue, exception.getIllegalStringValue());
        
        // Verify that properties related to other constructors are null.
        assertNull(exception.getDateTimeFieldType());
        assertNull(exception.getIllegalNumberValue());
        assertNull(exception.getLowerBound());
        assertNull(exception.getUpperBound());
    }
}