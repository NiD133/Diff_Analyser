package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link LazilyParsedNumber}.
 * This class focuses on verifying the correct parsing of numeric strings
 * and the handling of equality, hashing, and edge cases like invalid formats.
 */
public class LazilyParsedNumberTest {

    // --- Conversion to Primitive Types ---

    @Test
    public void intValue_withPositiveIntegerString_returnsCorrectInt() {
        LazilyParsedNumber number = new LazilyParsedNumber("123");
        assertEquals(123, number.intValue());
    }

    @Test
    public void intValue_withNegativeIntegerString_returnsCorrectInt() {
        LazilyParsedNumber number = new LazilyParsedNumber("-456");
        assertEquals(-456, number.intValue());
    }

    @Test
    public void intValue_withZeroString_returnsZero() {
        LazilyParsedNumber number = new LazilyParsedNumber("0");
        assertEquals(0, number.intValue());
    }

    @Test
    public void intValue_withDecimalString_returnsTruncatedInteger() {
        LazilyParsedNumber number = new LazilyParsedNumber("123.789");
        assertEquals(123, number.intValue());
    }

    @Test(expected = NumberFormatException.class)
    public void intValue_withNonNumericString_throwsNumberFormatException() {
        new LazilyParsedNumber("not a number").intValue();
    }

    @Test
    public void longValue_withPositiveIntegerString_returnsCorrectLong() {
        LazilyParsedNumber number = new LazilyParsedNumber("1234567890123");
        assertEquals(1234567890123L, number.longValue());
    }

    @Test
    public void longValue_withNegativeIntegerString_returnsCorrectLong() {
        LazilyParsedNumber number = new LazilyParsedNumber("-1234567890123");
        assertEquals(-1234567890123L, number.longValue());
    }

    @Test
    public void longValue_withZeroString_returnsZero() {
        LazilyParsedNumber number = new LazilyParsedNumber("0");
        assertEquals(0L, number.longValue());
    }

    @Test
    public void longValue_withDecimalString_returnsTruncatedLong() {
        LazilyParsedNumber number = new LazilyParsedNumber("1234567890.987");
        assertEquals(1234567890L, number.longValue());
    }

    @Test(expected = NumberFormatException.class)
    public void longValue_withNonNumericString_throwsNumberFormatException() {
        new LazilyParsedNumber("not a number").longValue();
    }

    @Test
    public void floatValue_withPositiveNumberString_returnsCorrectFloat() {
        LazilyParsedNumber number = new LazilyParsedNumber("123.45");
        assertEquals(123.45f, number.floatValue(), 0.0f);
    }

    @Test
    public void floatValue_withNegativeNumberString_returnsCorrectFloat() {
        LazilyParsedNumber number = new LazilyParsedNumber("-123.45");
        assertEquals(-123.45f, number.floatValue(), 0.0f);
    }

    @Test
    public void floatValue_withZeroString_returnsZero() {
        LazilyParsedNumber number = new LazilyParsedNumber("0.0");
        assertEquals(0.0f, number.floatValue(), 0.0f);
    }

    @Test(expected = NumberFormatException.class)
    public void floatValue_withNonNumericString_throwsNumberFormatException() {
        new LazilyParsedNumber("not a number").floatValue();
    }

    @Test
    public void doubleValue_withPositiveNumberString_returnsCorrectDouble() {
        LazilyParsedNumber number = new LazilyParsedNumber("123.456789");
        assertEquals(123.456789, number.doubleValue(), 0.0);
    }

    @Test
    public void doubleValue_withNegativeNumberString_returnsCorrectDouble() {
        LazilyParsedNumber number = new LazilyParsedNumber("-123.456789");
        assertEquals(-123.456789, number.doubleValue(), 0.0);
    }

    @Test
    public void doubleValue_withZeroString_returnsZero() {
        LazilyParsedNumber number = new LazilyParsedNumber("0.0");
        assertEquals(0.0, number.doubleValue(), 0.0);
    }

    @Test(expected = NumberFormatException.class)
    public void doubleValue_withNonNumericString_throwsNumberFormatException() {
        new LazilyParsedNumber("not a number").doubleValue();
    }

    // --- toString() Method ---

    @Test
    public void toString_returnsTheOriginalStringValue() {
        assertEquals("123.45", new LazilyParsedNumber("123.45").toString());
        assertEquals("", new LazilyParsedNumber("").toString());
        assertEquals("some text", new LazilyParsedNumber("some text").toString());
    }

    // --- equals() and hashCode() Methods ---

    @Test
    public void equals_withSameInstance_returnsTrue() {
        LazilyParsedNumber number = new LazilyParsedNumber("10");
        assertTrue(number.equals(number));
    }

    @Test
    public void equals_withDifferentInstancesButSameValue_returnsTrue() {
        LazilyParsedNumber number1 = new LazilyParsedNumber("10");
        LazilyParsedNumber number2 = new LazilyParsedNumber("10");
        assertTrue(number1.equals(number2));
    }

    @Test
    public void equals_withDifferentValues_returnsFalse() {
        LazilyParsedNumber number1 = new LazilyParsedNumber("10");
        LazilyParsedNumber number2 = new LazilyParsedNumber("20");
        assertFalse(number1.equals(number2));
    }

    @Test
    public void equals_withDifferentType_returnsFalse() {
        LazilyParsedNumber number = new LazilyParsedNumber("10");
        assertFalse(number.equals("10"));
    }

    @Test
    public void equals_withNullObject_returnsFalse() {
        LazilyParsedNumber number = new LazilyParsedNumber("10");
        assertFalse(number.equals(null));
    }

    @Test
    public void hashCode_isConsistentWithEquals() {
        LazilyParsedNumber number1 = new LazilyParsedNumber("10");
        LazilyParsedNumber number2 = new LazilyParsedNumber("10");
        assertEquals(number1.hashCode(), number2.hashCode());
    }

    @Test
    public void hashCode_returnsHashCodeOfStringValue() {
        String value = "123.45";
        LazilyParsedNumber number = new LazilyParsedNumber(value);
        assertEquals(value.hashCode(), number.hashCode());
    }

    // --- Edge Case: Construction with Null ---
    // The class contract states value must not be null, so these tests verify
    // the resulting (but potentially undefined) behavior of a contract violation.

    @Test(expected = NullPointerException.class)
    public void constructorWithNull_whenCallingIntValue_throwsNullPointerException() {
        new LazilyParsedNumber(null).intValue();
    }

    @Test(expected = NullPointerException.class)
    public void constructorWithNull_whenCallingLongValue_throwsNullPointerException() {
        new LazilyParsedNumber(null).longValue();
    }

    @Test(expected = NullPointerException.class)
    public void constructorWithNull_whenCallingFloatValue_throwsNullPointerException() {
        new LazilyParsedNumber(null).floatValue();
    }

    @Test(expected = NullPointerException.class)
    public void constructorWithNull_whenCallingDoubleValue_throwsNullPointerException() {
        new LazilyParsedNumber(null).doubleValue();
    }

    @Test(expected = NullPointerException.class)
    public void constructorWithNull_whenCallingHashCode_throwsNullPointerException() {
        new LazilyParsedNumber(null).hashCode();
    }

    @Test
    public void constructorWithNull_whenCallingToString_returnsNull() {
        // This behavior is inconsistent with other methods but is verified here.
        LazilyParsedNumber number = new LazilyParsedNumber(null);
        assertNull(number.toString());
    }

    @Test(expected = NullPointerException.class)
    public void constructorWithNull_whenCallingEquals_throwsNullPointerException() {
        LazilyParsedNumber numberWithNull = new LazilyParsedNumber(null);
        LazilyParsedNumber otherNumber = new LazilyParsedNumber("10");
        // The NPE is expected from `numberWithNull` because it tries to access its internal null value.
        numberWithNull.equals(otherNumber);
    }
}