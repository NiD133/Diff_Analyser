package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.internal.LazilyParsedNumber;

/**
 * Test suite for LazilyParsedNumber class.
 * LazilyParsedNumber holds a string representation of a number and converts it 
 * to specific numeric types on demand.
 */
public class LazilyParsedNumberTest {

    // ========== toString() Tests ==========
    
    @Test
    public void toString_withValidString_returnsOriginalString() {
        LazilyParsedNumber number = new LazilyParsedNumber("A_>sS8(ab<");
        
        String result = number.toString();
        
        assertEquals("A_>sS8(ab<", result);
    }

    @Test
    public void toString_withEmptyString_returnsEmptyString() {
        LazilyParsedNumber number = new LazilyParsedNumber("");
        
        String result = number.toString();
        
        assertEquals("", result);
    }

    @Test
    public void toString_withNullValue_returnsNull() {
        LazilyParsedNumber number = new LazilyParsedNumber(null);
        
        String result = number.toString();
        
        assertNull(result);
    }

    // ========== intValue() Tests ==========
    
    @Test
    public void intValue_withZero_returnsZero() {
        LazilyParsedNumber number = new LazilyParsedNumber("0");
        
        int result = number.intValue();
        
        assertEquals(0, result);
    }

    @Test
    public void intValue_withPositiveInteger_returnsCorrectValue() {
        LazilyParsedNumber number = new LazilyParsedNumber("5");
        
        int result = number.intValue();
        
        assertEquals(5, result);
    }

    @Test
    public void intValue_withNegativeInteger_returnsCorrectValue() {
        LazilyParsedNumber number = new LazilyParsedNumber("-6");
        
        int result = number.intValue();
        
        assertEquals(-6, result);
    }

    @Test(expected = NumberFormatException.class)
    public void intValue_withInvalidNumberString_throwsNumberFormatException() {
        LazilyParsedNumber number = new LazilyParsedNumber("Deserialization is unsupported");
        
        number.intValue();
    }

    @Test(expected = NullPointerException.class)
    public void intValue_withNullValue_throwsNullPointerException() {
        LazilyParsedNumber number = new LazilyParsedNumber(null);
        
        number.intValue();
    }

    // ========== longValue() Tests ==========
    
    @Test
    public void longValue_withZero_returnsZero() {
        LazilyParsedNumber number = new LazilyParsedNumber("0");
        
        long result = number.longValue();
        
        assertEquals(0L, result);
    }

    @Test
    public void longValue_withPositiveLong_returnsCorrectValue() {
        LazilyParsedNumber number = new LazilyParsedNumber("3");
        
        long result = number.longValue();
        
        assertEquals(3L, result);
    }

    @Test
    public void longValue_withNegativeLong_returnsCorrectValue() {
        LazilyParsedNumber number = new LazilyParsedNumber("-6");
        
        long result = number.longValue();
        
        assertEquals(-6L, result);
    }

    @Test(expected = NumberFormatException.class)
    public void longValue_withInvalidNumberString_throwsNumberFormatException() {
        LazilyParsedNumber number = new LazilyParsedNumber("Deserialization is unsupported");
        
        number.longValue();
    }

    @Test(expected = NullPointerException.class)
    public void longValue_withNullValue_throwsNullPointerException() {
        LazilyParsedNumber number = new LazilyParsedNumber(null);
        
        number.longValue();
    }

    // ========== floatValue() Tests ==========
    
    @Test
    public void floatValue_withZero_returnsZeroFloat() {
        LazilyParsedNumber number = new LazilyParsedNumber("0");
        
        float result = number.floatValue();
        
        assertEquals(0.0F, result, 0.01F);
    }

    @Test
    public void floatValue_withPositiveFloat_returnsCorrectValue() {
        LazilyParsedNumber number = new LazilyParsedNumber("7");
        
        float result = number.floatValue();
        
        assertEquals(7.0F, result, 0.01F);
    }

    @Test
    public void floatValue_withNegativeFloat_returnsCorrectValue() {
        LazilyParsedNumber number = new LazilyParsedNumber("-6");
        
        float result = number.floatValue();
        
        assertEquals(-6.0F, result, 0.01F);
    }

    @Test(expected = NumberFormatException.class)
    public void floatValue_withInvalidNumberString_throwsNumberFormatException() {
        LazilyParsedNumber number = new LazilyParsedNumber("Deserialization is unsupported");
        
        number.floatValue();
    }

    @Test(expected = NullPointerException.class)
    public void floatValue_withNullValue_throwsNullPointerException() {
        LazilyParsedNumber number = new LazilyParsedNumber(null);
        
        number.floatValue();
    }

    // ========== doubleValue() Tests ==========
    
    @Test
    public void doubleValue_withZero_returnsZeroDouble() {
        LazilyParsedNumber number = new LazilyParsedNumber("0");
        
        double result = number.doubleValue();
        
        assertEquals(0.0, result, 0.01);
    }

    @Test
    public void doubleValue_withPositiveDouble_returnsCorrectValue() {
        LazilyParsedNumber number = new LazilyParsedNumber("3");
        
        double result = number.doubleValue();
        
        assertEquals(3.0, result, 0.01);
    }

    @Test
    public void doubleValue_withNegativeDouble_returnsCorrectValue() {
        LazilyParsedNumber number = new LazilyParsedNumber("-6");
        
        double result = number.doubleValue();
        
        assertEquals(-6.0, result, 0.01);
    }

    @Test(expected = NumberFormatException.class)
    public void doubleValue_withInvalidNumberString_throwsNumberFormatException() {
        LazilyParsedNumber number = new LazilyParsedNumber("...");
        
        number.doubleValue();
    }

    @Test(expected = NullPointerException.class)
    public void doubleValue_withNullValue_throwsNullPointerException() {
        LazilyParsedNumber number = new LazilyParsedNumber(null);
        
        number.doubleValue();
    }

    // ========== equals() Tests ==========
    
    @Test
    public void equals_withSameStringValues_returnsTrue() {
        LazilyParsedNumber number1 = new LazilyParsedNumber("Deserialization is pnsuported");
        LazilyParsedNumber number2 = new LazilyParsedNumber("Deserialization is pnsuported");
        
        boolean result = number1.equals(number2);
        
        assertTrue(result);
    }

    @Test
    public void equals_withSameInstance_returnsTrue() {
        LazilyParsedNumber number = new LazilyParsedNumber("...");
        
        boolean result = number.equals(number);
        
        assertTrue(result);
    }

    @Test
    public void equals_withDifferentType_returnsFalse() {
        LazilyParsedNumber number = new LazilyParsedNumber("d<Hh");
        
        boolean result = number.equals("d<Hh");
        
        assertFalse(result);
    }

    @Test(expected = NullPointerException.class)
    public void equals_withNullValue_throwsNullPointerException() {
        LazilyParsedNumber nullNumber = new LazilyParsedNumber(null);
        LazilyParsedNumber validNumber = new LazilyParsedNumber("Deserialization is pnsuported");
        
        nullNumber.equals(validNumber);
    }

    // ========== hashCode() Tests ==========
    
    @Test
    public void hashCode_withValidString_executesWithoutException() {
        LazilyParsedNumber number = new LazilyParsedNumber("Deserialization is unsupported");
        
        // Should not throw any exception
        number.hashCode();
    }

    @Test(expected = NullPointerException.class)
    public void hashCode_withNullValue_throwsNullPointerException() {
        LazilyParsedNumber number = new LazilyParsedNumber(null);
        
        number.hashCode();
    }
}