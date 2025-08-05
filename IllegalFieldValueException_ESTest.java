/*
 * Refactored test suite for IllegalFieldValueException for better readability
 * Tests cover constructor behaviors, getter methods, and exception cases
 */
package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class IllegalFieldValueException_ESTest extends IllegalFieldValueException_ESTest_scaffolding {

    // ===========================================
    // Tests for Constructors with String Parameters
    // ===========================================
    
    @Test(timeout = 4000)
    public void testConstructorWithTwoStrings_CheckUpperBoundIsNull() {
        IllegalFieldValueException ex = 
            new IllegalFieldValueException("ZON+$0'' {[}Z#94 eu", "");
        
        assertNull("Upper bound should be null for string-value constructor", ex.getUpperBound());
        assertEquals("Value \"\" for ZON+$0'' {[}Z#94 eu is not supported", ex.getMessage());
    }
    
    @Test(timeout = 4000)
    public void testConstructorWithTwoStrings_CheckIllegalStringValue() {
        IllegalFieldValueException ex = 
            new IllegalFieldValueException("ZON+$0'' {[}Z#94 eu", "");
        
        assertEquals("Illegal string value should match input", "", ex.getIllegalStringValue());
        assertEquals("Value \"\" for ZON+$0'' {[}Z#94 eu is not supported", ex.getMessage());
    }

    // =================================================
    // Tests for Constructors with DurationFieldType
    // =================================================
    
    @Test(timeout = 4000)
    public void testConstructorWithDurationFieldTypeAndString_CheckIllegalStringValue() {
        IllegalFieldValueException ex = 
            new IllegalFieldValueException(DurationFieldType.CENTURIES_TYPE, "Value ");
        
        assertEquals("Illegal string value should match input", "Value ", ex.getIllegalStringValue());
        assertEquals("Value \"Value \" for centuries is not supported", ex.getMessage());
    }
    
    @Test(timeout = 4000)
    public void testConstructorWithDurationFieldTypeAndString_CheckFieldType() {
        IllegalFieldValueException ex = 
            new IllegalFieldValueException(DurationFieldType.eras(), "");
        
        assertEquals(DurationFieldType.eras(), ex.getDurationFieldType());
        assertEquals("Value \"\" for eras is not supported", ex.getMessage());
    }
    
    @Test(timeout = 4000)
    public void testConstructorWithNullDurationFieldTypeAndString_ThrowsNPE() {
        try {
            new IllegalFieldValueException((DurationFieldType) null, "");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
    
    @Test(timeout = 4000)
    public void testConstructorWithNullDurationFieldTypeAndNumbers_ThrowsNPE() {
        try {
            new IllegalFieldValueException((DurationFieldType) null, null, null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // =================================================
    // Tests for Constructors with DateTimeFieldType
    // =================================================
    
    @Test(timeout = 4000)
    public void testConstructorWithDateTimeFieldTypeAndString_CheckFieldType() {
        IllegalFieldValueException ex = 
            new IllegalFieldValueException(DateTimeFieldType.secondOfMinute(), "");
        
        assertEquals(DateTimeFieldType.secondOfMinute(), ex.getDateTimeFieldType());
        assertEquals("Value \"\" for secondOfMinute is not supported", ex.getMessage());
    }
    
    @Test(timeout = 4000)
    public void testConstructorWithDateTimeFieldTypeAndNumbers_CheckFieldName() {
        Float value = 1.0F;
        IllegalFieldValueException ex = 
            new IllegalFieldValueException(DateTimeFieldType.year(), value, value, value);
        
        assertEquals("year", ex.getFieldName());
        assertEquals("Value 1.0 for year must be in the range [1.0,1.0]", ex.getMessage());
    }
    
    @Test(timeout = 4000)
    public void testConstructorWithDateTimeFieldTypeAndNumbers_CheckMessage() {
        Long value = -42521587200000L;
        IllegalFieldValueException ex = 
            new IllegalFieldValueException(DateTimeFieldType.weekyear(), value, value, value);
        
        assertEquals("Value -42521587200000 for weekyear must be in the range [-42521587200000,-42521587200000]", 
                     ex.getMessage());
    }
    
    @Test(timeout = 4000)
    public void testConstructorWithNullDateTimeFieldTypeAndString_ThrowsNPE() {
        try {
            new IllegalFieldValueException((DateTimeFieldType) null, "4s1_{!");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
    
    @Test(timeout = 4000)
    public void testConstructorWithNullDateTimeFieldTypeAndNumbers_ThrowsNPE() {
        try {
            new IllegalFieldValueException((DateTimeFieldType) null, (Number) null, (Number) null, (Number) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // ===========================================
    // Tests for Generic Constructors (Name + Values)
    // ===========================================
    
    @Test(timeout = 4000)
    public void testConstructorWithFieldNameAndNullNumbers_CheckIllegalValueAsString() {
        IllegalFieldValueException ex = 
            new IllegalFieldValueException("", null, null, null);
        
        assertEquals("null", ex.getIllegalValueAsString());
        assertEquals("Value null for  is not supported", ex.getMessage());
    }
    
    @Test(timeout = 4000)
    public void testConstructorWithFieldNameAndNumbers_CheckBounds() {
        Byte value = (byte) 0;
        IllegalFieldValueException ex = 
            new IllegalFieldValueException("", value, value, value);
        
        assertEquals(value, ex.getLowerBound());
        assertEquals(value, ex.getUpperBound());
        assertEquals("Value 0 for  must be in the range [0,0]", ex.getMessage());
    }
    
    @Test(timeout = 4000)
    public void testConstructorWithFieldNameAndNumbers_CheckFieldName() {
        Byte value = (byte) 0;
        IllegalFieldValueException ex = 
            new IllegalFieldValueException("testField", value, value, value);
        
        assertEquals("testField", ex.getFieldName());
    }
    
    @Test(timeout = 4000)
    public void testConstructorWithFieldNameAndNumbers_CheckIllegalNumberValue() {
        Long illegalValue = -42521587200000L;
        IllegalFieldValueException ex = 
            new IllegalFieldValueException("nrMiC", null, null, illegalValue);
        
        assertNull(ex.getIllegalNumberValue());
        assertEquals("Value null for nrMiC must not be larger than -42521587200000", ex.getMessage());
    }

    // ==============================
    // Tests for Message Modification
    // ==============================
    
    @Test(timeout = 4000)
    public void testPrependMessage_WithEmptyString() {
        IllegalFieldValueException ex = 
            new IllegalFieldValueException("", null, null, null);
        
        ex.prependMessage("");
        assertEquals(": Value null for  is not supported", ex.getMessage());
    }
    
    @Test(timeout = 4000)
    public void testPrependMessage_WithNull() {
        Byte value = (byte) 0;
        IllegalFieldValueException ex = 
            new IllegalFieldValueException("", value, value, value);
        
        ex.prependMessage(null);
        assertEquals("Value 0 for  must be in the range [0,0]", ex.getMessage());
    }

    // =================================
    // Tests for Edge Case Constructions
    // =================================
    
    @Test(timeout = 4000)
    public void testConstructorWithTwoNulls() {
        // Should not throw exception
        new IllegalFieldValueException(null, null);
    }
    
    @Test(timeout = 4000)
    public void testConstructorWithMixedNullsAndNumbers() {
        // Should not throw exception
        new IllegalFieldValueException("", null, new Float(0.0F), null);
    }
    
    @Test(timeout = 4000)
    public void testConstructorWithExplanationMessage() {
        // Should not throw exception
        new IllegalFieldValueException(
            DateTimeFieldType.clockhourOfDay(), 
            null, 
            ".#Q:=KY]1ld]Nf>u"
        );
    }
}