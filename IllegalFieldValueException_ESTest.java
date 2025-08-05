package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.IllegalFieldValueException;

/**
 * Test suite for IllegalFieldValueException class.
 * Tests various constructor scenarios and getter methods for exceptions
 * thrown when field values are outside their supported ranges.
 */
public class IllegalFieldValueException_ESTest {

    // ========== Constructor Tests with DateTimeFieldType ==========
    
    @Test
    public void shouldCreateExceptionWithDateTimeFieldAndNumericRange() {
        DateTimeFieldType yearField = DateTimeFieldType.year();
        Number invalidValue = 1.0f;
        Number lowerBound = 1.0f;
        Number upperBound = 1.0f;
        
        IllegalFieldValueException exception = new IllegalFieldValueException(
            yearField, invalidValue, lowerBound, upperBound);
        
        assertEquals("Value 1.0 for year must be in the range [1.0,1.0]", exception.getMessage());
        assertEquals(yearField, exception.getDateTimeFieldType());
        assertEquals("year", exception.getFieldName());
        assertEquals(invalidValue, exception.getIllegalNumberValue());
        assertEquals(lowerBound, exception.getLowerBound());
        assertEquals(upperBound, exception.getUpperBound());
    }
    
    @Test
    public void shouldCreateExceptionWithDateTimeFieldAndStringValue() {
        DateTimeFieldType secondField = DateTimeFieldType.secondOfMinute();
        String invalidValue = "";
        
        IllegalFieldValueException exception = new IllegalFieldValueException(secondField, invalidValue);
        
        assertEquals("Value \"\" for secondOfMinute is not supported", exception.getMessage());
        assertEquals(secondField, exception.getDateTimeFieldType());
        assertEquals("secondOfMinute", exception.getFieldName());
        assertEquals(invalidValue, exception.getIllegalStringValue());
        assertEquals("", exception.getIllegalValueAsString());
    }
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerWhenDateTimeFieldTypeIsNull() {
        new IllegalFieldValueException((DateTimeFieldType) null, "invalid");
    }
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerWhenDateTimeFieldTypeIsNullWithNumericRange() {
        new IllegalFieldValueException((DateTimeFieldType) null, 1L, 0L, 10L);
    }

    // ========== Constructor Tests with DurationFieldType ==========
    
    @Test
    public void shouldCreateExceptionWithDurationFieldAndStringValue() {
        DurationFieldType centuriesField = DurationFieldType.CENTURIES_TYPE;
        String invalidValue = "Value ";
        
        IllegalFieldValueException exception = new IllegalFieldValueException(centuriesField, invalidValue);
        
        assertEquals("Value \"Value \" for centuries is not supported", exception.getMessage());
        assertEquals(centuriesField, exception.getDurationFieldType());
        assertEquals("centuries", exception.getFieldName());
        assertEquals(invalidValue, exception.getIllegalStringValue());
    }
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerWhenDurationFieldTypeIsNull() {
        new IllegalFieldValueException((DurationFieldType) null, "invalid");
    }
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerWhenDurationFieldTypeIsNullWithNumericRange() {
        new IllegalFieldValueException((DurationFieldType) null, 1L, 0L, 10L);
    }

    // ========== Constructor Tests with String Field Names ==========
    
    @Test
    public void shouldCreateExceptionWithStringFieldNameAndStringValue() {
        String fieldName = "customField";
        String invalidValue = "";
        
        IllegalFieldValueException exception = new IllegalFieldValueException(fieldName, invalidValue);
        
        assertEquals("Value \"\" for customField is not supported", exception.getMessage());
        assertEquals(fieldName, exception.getFieldName());
        assertEquals(invalidValue, exception.getIllegalStringValue());
    }
    
    @Test
    public void shouldCreateExceptionWithStringFieldNameAndNumericRange() {
        String fieldName = "";
        Number invalidValue = (byte) 0;
        Number lowerBound = (byte) 0;
        Number upperBound = (byte) 0;
        
        IllegalFieldValueException exception = new IllegalFieldValueException(
            fieldName, invalidValue, lowerBound, upperBound);
        
        assertEquals("Value 0 for  must be in the range [0,0]", exception.getMessage());
        assertEquals(fieldName, exception.getFieldName());
        assertEquals(invalidValue, exception.getIllegalNumberValue());
    }
    
    @Test
    public void shouldHandleNullFieldNameAndValue() {
        IllegalFieldValueException exception = new IllegalFieldValueException(
            (String) null, (Number) null, (Number) null, (Number) null);
        
        assertEquals("Value null for null is not supported", exception.getMessage());
        assertNull(exception.getFieldName());
        assertNull(exception.getIllegalNumberValue());
    }

    // ========== Boundary Condition Tests ==========
    
    @Test
    public void shouldCreateExceptionWithOnlyUpperBound() {
        String fieldName = "testField";
        Number invalidValue = null;
        Number upperBound = -42521587200000L;
        
        IllegalFieldValueException exception = new IllegalFieldValueException(
            fieldName, invalidValue, null, upperBound);
        
        assertEquals("Value null for testField must not be larger than -42521587200000", 
            exception.getMessage());
        assertNull(exception.getLowerBound());
        assertEquals(upperBound, exception.getUpperBound());
    }
    
    @Test
    public void shouldCreateExceptionWithEqualBounds() {
        DateTimeFieldType weekyearField = DateTimeFieldType.weekyear();
        Long value = -42521587200000L;
        
        IllegalFieldValueException exception = new IllegalFieldValueException(
            weekyearField, value, value, value);
        
        assertEquals("Value -42521587200000 for weekyear must be in the range [-42521587200000,-42521587200000]", 
            exception.getMessage());
    }

    // ========== Getter Method Tests ==========
    
    @Test
    public void shouldReturnCorrectIllegalValueAsString() {
        IllegalFieldValueException exceptionWithNull = new IllegalFieldValueException(
            "", (Number) null, (Number) null, (Number) null);
        
        assertNotNull(exceptionWithNull.getIllegalValueAsString());
        
        DateTimeFieldType field = DateTimeFieldType.secondOfMinute();
        IllegalFieldValueException exceptionWithString = new IllegalFieldValueException(field, "");
        
        assertEquals("", exceptionWithString.getIllegalValueAsString());
    }
    
    @Test
    public void shouldReturnNullForIrrelevantFieldTypes() {
        IllegalFieldValueException stringFieldException = new IllegalFieldValueException("field", "value");
        
        assertNull(stringFieldException.getDateTimeFieldType());
        assertNull(stringFieldException.getDurationFieldType());
        
        DurationFieldType erasField = DurationFieldType.eras();
        IllegalFieldValueException durationFieldException = new IllegalFieldValueException(erasField, "");
        
        assertNull(durationFieldException.getDateTimeFieldType());
        assertEquals(erasField, durationFieldException.getDurationFieldType());
    }

    // ========== Message Manipulation Tests ==========
    
    @Test
    public void shouldPrependMessageWithColon() {
        IllegalFieldValueException exception = new IllegalFieldValueException(
            "", (Number) null, (Number) null, (Number) null);
        String originalMessage = exception.getMessage();
        
        exception.prependMessage("");
        
        assertEquals(": " + originalMessage, exception.getMessage());
    }
    
    @Test
    public void shouldHandleNullPrependMessage() {
        Number zeroByte = (byte) 0;
        IllegalFieldValueException exception = new IllegalFieldValueException(
            "", zeroByte, zeroByte, zeroByte);
        String originalMessage = exception.getMessage();
        
        exception.prependMessage(null);
        
        assertEquals(originalMessage, exception.getMessage());
    }

    // ========== Edge Case Tests ==========
    
    @Test
    public void shouldCreateExceptionWithNullValues() {
        // Test that constructor accepts null values without throwing
        new IllegalFieldValueException((String) null, (String) null);
        
        Float zeroFloat = 0.0f;
        new IllegalFieldValueException("", null, zeroFloat, null);
        
        DateTimeFieldType clockhourField = DateTimeFieldType.clockhourOfDay();
        new IllegalFieldValueException(clockhourField, null, ".#Q:=KY]1ld]Nf>u");
        
        DurationFieldType secondsField = DurationFieldType.SECONDS_TYPE;
        new IllegalFieldValueException(secondsField, null, null, null);
        
        DateTimeFieldType yearField = DateTimeFieldType.year();
        new IllegalFieldValueException(yearField, null, null, null, "explanation");
    }
}