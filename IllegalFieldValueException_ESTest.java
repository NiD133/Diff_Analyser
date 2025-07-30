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
public class IllegalFieldValueExceptionTest extends IllegalFieldValueException_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testIllegalFieldValueExceptionWithEmptyString() throws Throwable {
        // Test with empty string value
        IllegalFieldValueException exception = new IllegalFieldValueException("ZON+$0'' {[}Z#94 eu", "");
        assertEquals("Value \"\" for ZON+$0'' {[}Z#94 eu is not supported", exception.getMessage());
    }

    @Test(timeout = 4000)
    public void testIllegalFieldValueExceptionWithDurationFieldType() throws Throwable {
        // Test with DurationFieldType and string value
        DurationFieldType durationFieldType = DurationFieldType.CENTURIES_TYPE;
        IllegalFieldValueException exception = new IllegalFieldValueException(durationFieldType, "Value ");
        assertEquals("Value \"Value \" for centuries is not supported", exception.getMessage());
    }

    @Test(timeout = 4000)
    public void testIllegalFieldValueExceptionWithNullValues() throws Throwable {
        // Test with null values
        IllegalFieldValueException exception = new IllegalFieldValueException((String) null, (Number) null, (Number) null, (Number) null);
        assertEquals("Value null for null is not supported", exception.getMessage());
    }

    @Test(timeout = 4000)
    public void testIllegalFieldValueExceptionWithDateTimeFieldType() throws Throwable {
        // Test with DateTimeFieldType and float values
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Float floatValue = 1.0F;
        IllegalFieldValueException exception = new IllegalFieldValueException(dateTimeFieldType, floatValue, floatValue, floatValue);
        assertEquals("Value 1.0 for year must be in the range [1.0,1.0]", exception.getMessage());
    }

    @Test(timeout = 4000)
    public void testIllegalFieldValueExceptionWithSecondOfMinute() throws Throwable {
        // Test with DateTimeFieldType secondOfMinute and empty string
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.secondOfMinute();
        IllegalFieldValueException exception = new IllegalFieldValueException(dateTimeFieldType, "");
        assertEquals("Value \"\" for secondOfMinute is not supported", exception.getMessage());
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionWithNullDurationFieldType() throws Throwable {
        // Test for NullPointerException when DurationFieldType is null
        try {
            new IllegalFieldValueException((DurationFieldType) null, "");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.joda.time.IllegalFieldValueException", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionWithNullDateTimeFieldType() throws Throwable {
        // Test for NullPointerException when DateTimeFieldType is null
        try {
            new IllegalFieldValueException((DateTimeFieldType) null, "4s1_{!");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.joda.time.IllegalFieldValueException", e);
        }
    }

    @Test(timeout = 4000)
    public void testIllegalFieldValueExceptionWithNegativeLongValue() throws Throwable {
        // Test with negative long value
        Long longValue = -42521587200000L;
        IllegalFieldValueException exception = new IllegalFieldValueException("nrMiC", (Number) null, (Number) null, longValue);
        assertEquals("Value null for nrMiC must not be larger than -42521587200000", exception.getMessage());
    }

    @Test(timeout = 4000)
    public void testIllegalFieldValueExceptionWithWeekYear() throws Throwable {
        // Test with DateTimeFieldType weekyear and negative long value
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.weekyear();
        Long longValue = -42521587200000L;
        IllegalFieldValueException exception = new IllegalFieldValueException(dateTimeFieldType, longValue, longValue, longValue);
        assertEquals("Value -42521587200000 for weekyear must be in the range [-42521587200000,-42521587200000]", exception.getMessage());
    }

    @Test(timeout = 4000)
    public void testIllegalFieldValueExceptionWithEras() throws Throwable {
        // Test with DurationFieldType eras and empty string
        DurationFieldType durationFieldType = DurationFieldType.eras();
        IllegalFieldValueException exception = new IllegalFieldValueException(durationFieldType, "");
        assertEquals("Value \"\" for eras is not supported", exception.getMessage());
    }
}