package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link org.joda.time.IllegalFieldValueException}.
 * The tests cover all constructors and public methods, ensuring that the
 * exception state and message are correctly handled for various scenarios.
 */
public class IllegalFieldValueExceptionTest {

    // --- Constructor Tests ---

    @Test
    public void testConstructor_withDateTimeFieldTypeAndNumberOutOfBounds() {
        // Arrange
        DateTimeFieldType fieldType = DateTimeFieldType.monthOfYear();
        Number illegalValue = 13;
        Number lowerBound = 1;
        Number upperBound = 12;
        String expectedMessage = "Value 13 for monthOfYear must be in the range [1,12]";

        // Act
        IllegalFieldValueException ex = new IllegalFieldValueException(fieldType, illegalValue, lowerBound, upperBound);

        // Assert
        assertEquals(expectedMessage, ex.getMessage());
        assertEquals(fieldType, ex.getDateTimeFieldType());
        assertEquals("monthOfYear", ex.getFieldName());
        assertEquals(illegalValue, ex.getIllegalNumberValue());
        assertEquals(lowerBound, ex.getLowerBound());
        assertEquals(upperBound, ex.getUpperBound());
        assertNull(ex.getDurationFieldType());
        assertNull(ex.getIllegalStringValue());
    }

    @Test
    public void testConstructor_withDateTimeFieldTypeAndStringValue() {
        // Arrange
        DateTimeFieldType fieldType = DateTimeFieldType.era();
        String illegalValue = "BC/AD"; // Example of an unsupported string value
        String expectedMessage = "Value \"BC/AD\" for era is not supported";

        // Act
        IllegalFieldValueException ex = new IllegalFieldValueException(fieldType, illegalValue);

        // Assert
        assertEquals(expectedMessage, ex.getMessage());
        assertEquals(fieldType, ex.getDateTimeFieldType());
        assertEquals("era", ex.getFieldName());
        assertEquals(illegalValue, ex.getIllegalStringValue());
        assertNull(ex.getIllegalNumberValue());
        assertNull(ex.getLowerBound());
        assertNull(ex.getUpperBound());
    }
    
    @Test
    public void testConstructor_withDurationFieldTypeAndNumberOutOfBounds() {
        // Arrange
        DurationFieldType fieldType = DurationFieldType.hours();
        Number illegalValue = -1;
        Number lowerBound = 0;
        Number upperBound = 23;
        String expectedMessage = "Value -1 for hours must be in the range [0,23]";

        // Act
        IllegalFieldValueException ex = new IllegalFieldValueException(fieldType, illegalValue, lowerBound, upperBound);

        // Assert
        assertEquals(expectedMessage, ex.getMessage());
        assertEquals(fieldType, ex.getDurationFieldType());
        assertEquals("hours", ex.getFieldName());
        assertEquals(illegalValue, ex.getIllegalNumberValue());
        assertEquals(lowerBound, ex.getLowerBound());
        assertEquals(upperBound, ex.getUpperBound());
        assertNull(ex.getDateTimeFieldType());
        assertNull(ex.getIllegalStringValue());
    }

    @Test
    public void testConstructor_withDurationFieldTypeAndStringValue() {
        // Arrange
        DurationFieldType fieldType = DurationFieldType.days();
        String illegalValue = "yesterday";
        String expectedMessage = "Value \"yesterday\" for days is not supported";

        // Act
        IllegalFieldValueException ex = new IllegalFieldValueException(fieldType, illegalValue);

        // Assert
        assertEquals(expectedMessage, ex.getMessage());
        assertEquals(fieldType, ex.getDurationFieldType());
        assertEquals("days", ex.getFieldName());
        assertEquals(illegalValue, ex.getIllegalStringValue());
        assertNull(ex.getIllegalNumberValue());
    }

    @Test
    public void testConstructor_withFieldNameAndNumberOutOfBounds() {
        // Arrange
        String fieldName = "customField";
        Number illegalValue = 101;
        Number lowerBound = 0;
        Number upperBound = 100;
        String expectedMessage = "Value 101 for customField must be in the range [0,100]";

        // Act
        IllegalFieldValueException ex = new IllegalFieldValueException(fieldName, illegalValue, lowerBound, upperBound);

        // Assert
        assertEquals(expectedMessage, ex.getMessage());
        assertEquals(fieldName, ex.getFieldName());
        assertEquals(illegalValue, ex.getIllegalNumberValue());
        assertEquals(lowerBound, ex.getLowerBound());
        assertEquals(upperBound, ex.getUpperBound());
        assertNull(ex.getDateTimeFieldType());
        assertNull(ex.getDurationFieldType());
    }

    @Test
    public void testConstructor_withFieldNameAndStringValue() {
        // Arrange
        String fieldName = "customField";
        String illegalValue = "invalid";
        String expectedMessage = "Value \"invalid\" for customField is not supported";

        // Act
        IllegalFieldValueException ex = new IllegalFieldValueException(fieldName, illegalValue);

        // Assert
        assertEquals(expectedMessage, ex.getMessage());
        assertEquals(fieldName, ex.getFieldName());
        assertEquals(illegalValue, ex.getIllegalStringValue());
        assertNull(ex.getIllegalNumberValue());
    }
    
    @Test
    public void testConstructor_withExplanation() {
        // Arrange
        DateTimeFieldType fieldType = DateTimeFieldType.year();
        Number illegalValue = 2000;
        String explanation = "leap year not allowed";
        String expectedMessage = "Value 2000 for year is not supported: leap year not allowed";

        // Act
        IllegalFieldValueException ex = new IllegalFieldValueException(fieldType, illegalValue, explanation);

        // Assert
        assertEquals(expectedMessage, ex.getMessage());
        assertEquals(fieldType, ex.getDateTimeFieldType());
        assertEquals(illegalValue, ex.getIllegalNumberValue());
    }

    // --- Message Formatting for Different Bounds ---

    @Test
    public void testMessage_withOnlyLowerBound() {
        // Arrange
        IllegalFieldValueException ex = new IllegalFieldValueException("field", -5, 0, null);
        
        // Assert
        assertEquals("Value -5 for field must not be smaller than 0", ex.getMessage());
    }

    @Test
    public void testMessage_withOnlyUpperBound() {
        // Arrange
        IllegalFieldValueException ex = new IllegalFieldValueException("field", 100, null, 99);

        // Assert
        assertEquals("Value 100 for field must not be larger than 99", ex.getMessage());
    }

    // --- Null-Handling in Constructors ---

    @Test(expected = NullPointerException.class)
    public void testConstructor_withNullDateTimeFieldType_throwsNPE() {
        new IllegalFieldValueException((DateTimeFieldType) null, 1, 0, 10);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_withNullDateTimeFieldTypeAndString_throwsNPE() {
        new IllegalFieldValueException((DateTimeFieldType) null, "invalid");
    }

-
    @Test(expected = NullPointerException.class)
    public void testConstructor_withNullDurationFieldType_throwsNPE() {
        new IllegalFieldValueException((DurationFieldType) null, 1, 0, 10);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_withNullDurationFieldTypeAndString_throwsNPE() {
        new IllegalFieldValueException((DurationFieldType) null, "invalid");
    }

    // --- Method Tests ---

    @Test
    public void testPrependMessage() {
        // Arrange
        IllegalFieldValueException ex = new IllegalFieldValueException("monthOfYear", 13, 1, 12);
        String originalMessage = ex.getMessage();
        String prefix = "Cannot parse date";

        // Act
        ex.prependMessage(prefix);

        // Assert
        assertEquals(prefix + ": " + originalMessage, ex.getMessage());
    }

    @Test
    public void testPrependMessage_withNullPrefix_doesNotChangeMessage() {
        // Arrange
        IllegalFieldValueException ex = new IllegalFieldValueException("monthOfYear", 13, 1, 12);
        String originalMessage = ex.getMessage();

        // Act
        ex.prependMessage(null);

        // Assert
        assertEquals(originalMessage, ex.getMessage());
    }

    @Test
    public void testPrependMessage_withEmptyPrefix_addsColonSeparator() {
        // Arrange
        IllegalFieldValueException ex = new IllegalFieldValueException("monthOfYear", 13, 1, 12);
        String originalMessage = ex.getMessage();

        // Act
        ex.prependMessage("");

        // Assert
        assertEquals(": " + originalMessage, ex.getMessage());
    }

    @Test
    public void testGetIllegalValueAsString() {
        // Arrange
        IllegalFieldValueException exWithNumber = new IllegalFieldValueException("field", 123);
        IllegalFieldValueException exWithString = new IllegalFieldValueException("field", "abc");
        IllegalFieldValueException exWithNullNumber = new IllegalFieldValueException("field", (Number) null, 0, 100);

        // Act & Assert
        assertEquals("123", exWithNumber.getIllegalValueAsString());
        assertEquals("abc", exWithString.getIllegalValueAsString());
        assertEquals("null", exWithNullNumber.getIllegalValueAsString());
    }
}