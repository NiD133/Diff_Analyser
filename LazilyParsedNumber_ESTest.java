package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class LazilyParsedNumber_ESTest extends LazilyParsedNumber_ESTest_scaffolding {

    // ===========================================================
    // Tests for constructor and toString()
    // ===========================================================
    
    @Test(timeout = 4000)
    public void testConstructorWithNonEmptyString() {
        LazilyParsedNumber number = new LazilyParsedNumber("A_>sS8(ab<");
        assertEquals("A_>sS8(ab<", number.toString());
    }

    @Test(timeout = 4000)
    public void testConstructorWithEmptyString() {
        LazilyParsedNumber number = new LazilyParsedNumber("");
        assertEquals("", number.toString());
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullString() {
        LazilyParsedNumber number = new LazilyParsedNumber(null);
        assertNull(number.toString());
    }

    // ===========================================================
    // Tests for intValue()
    // ===========================================================
    
    @Test(timeout = 4000)
    public void testIntValueWithZero() {
        LazilyParsedNumber number = new LazilyParsedNumber("0");
        assertEquals(0, number.intValue());
    }

    @Test(timeout = 4000)
    public void testIntValueWithPositiveInteger() {
        LazilyParsedNumber number = new LazilyParsedNumber("5");
        assertEquals(5, number.intValue());
    }

    @Test(timeout = 4000)
    public void testIntValueWithNegativeInteger() {
        LazilyParsedNumber number = new LazilyParsedNumber("-6");
        assertEquals(-6, number.intValue());
    }

    @Test(timeout = 4000)
    public void testIntValueWithNonNumericStringThrowsNumberFormatException() {
        LazilyParsedNumber number = new LazilyParsedNumber("Deserialization is unsupported");
        try {
            number.intValue();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            verifyException("java.math.BigDecimal", e);
        }
    }

    @Test(timeout = 4000)
    public void testIntValueWithNullThrowsNullPointerException() {
        LazilyParsedNumber number = new LazilyParsedNumber(null);
        try {
            number.intValue();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.internal.NumberLimits", e);
        }
    }

    // ===========================================================
    // Tests for longValue()
    // ===========================================================
    
    @Test(timeout = 4000)
    public void testLongValueWithZero() {
        LazilyParsedNumber number = new LazilyParsedNumber("0");
        assertEquals(0L, number.longValue());
    }

    @Test(timeout = 4000)
    public void testLongValueWithPositiveInteger() {
        LazilyParsedNumber number = new LazilyParsedNumber("3");
        assertEquals(3L, number.longValue());
    }

    @Test(timeout = 4000)
    public void testLongValueWithNegativeInteger() {
        LazilyParsedNumber number = new LazilyParsedNumber("-6");
        assertEquals(-6L, number.longValue());
    }

    @Test(timeout = 4000)
    public void testLongValueWithNonNumericStringThrowsNumberFormatException() {
        LazilyParsedNumber number = new LazilyParsedNumber("Deserialization is unsupported");
        try {
            number.longValue();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            verifyException("java.math.BigDecimal", e);
        }
    }

    @Test(timeout = 4000)
    public void testLongValueWithNullThrowsNullPointerException() {
        LazilyParsedNumber number = new LazilyParsedNumber(null);
        try {
            number.longValue();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.internal.NumberLimits", e);
        }
    }

    // ===========================================================
    // Tests for floatValue()
    // ===========================================================
    
    @Test(timeout = 4000)
    public void testFloatValueWithZero() {
        LazilyParsedNumber number = new LazilyParsedNumber("0");
        assertEquals(0.0F, number.floatValue(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testFloatValueWithPositiveInteger() {
        LazilyParsedNumber number = new LazilyParsedNumber("7");
        assertEquals(7.0F, number.floatValue(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testFloatValueWithNegativeInteger() {
        LazilyParsedNumber number = new LazilyParsedNumber("-6");
        assertEquals(-6.0F, number.floatValue(), 0.01F);
    }

    @Test(timeout = 4000, expected = NumberFormatException.class)
    public void testFloatValueWithNonNumericStringThrowsNumberFormatException() {
        LazilyParsedNumber number = new LazilyParsedNumber("Deserialization is unsupported");
        number.floatValue();
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testFloatValueWithNullThrowsNullPointerException() {
        LazilyParsedNumber number = new LazilyParsedNumber(null);
        number.floatValue();
    }

    // ===========================================================
    // Tests for doubleValue()
    // ===========================================================
    
    @Test(timeout = 4000)
    public void testDoubleValueWithZero() {
        LazilyParsedNumber number = new LazilyParsedNumber("0");
        assertEquals(0.0, number.doubleValue(), 0.01);
    }

    @Test(timeout = 4000)
    public void testDoubleValueWithPositiveInteger() {
        LazilyParsedNumber number = new LazilyParsedNumber("3");
        assertEquals(3.0, number.doubleValue(), 0.01);
    }

    @Test(timeout = 4000)
    public void testDoubleValueWithNegativeInteger() {
        LazilyParsedNumber number = new LazilyParsedNumber("-6");
        assertEquals(-6.0, number.doubleValue(), 0.01);
    }

    @Test(timeout = 4000, expected = NumberFormatException.class)
    public void testDoubleValueWithNonNumericStringThrowsNumberFormatException() {
        LazilyParsedNumber number = new LazilyParsedNumber("...");
        number.doubleValue();
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testDoubleValueWithNullThrowsNullPointerException() {
        LazilyParsedNumber number = new LazilyParsedNumber(null);
        number.doubleValue();
    }

    // ===========================================================
    // Tests for equals()
    // ===========================================================
    
    @Test(timeout = 4000)
    public void testEqualsWithSameStringValueReturnsTrue() {
        LazilyParsedNumber number1 = new LazilyParsedNumber("Deserialization is pnsuported");
        LazilyParsedNumber number2 = new LazilyParsedNumber("Deserialization is pnsuported");
        assertTrue(number1.equals(number2));
    }

    @Test(timeout = 4000)
    public void testEqualsSameObjectReturnsTrue() {
        LazilyParsedNumber number = new LazilyParsedNumber("...");
        assertTrue(number.equals(number));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentTypeReturnsFalse() {
        LazilyParsedNumber number = new LazilyParsedNumber("d<Hh");
        assertFalse(number.equals("d<Hh"));
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testEqualsWithNullValueThrowsNullPointerException() {
        LazilyParsedNumber numberWithNull = new LazilyParsedNumber(null);
        LazilyParsedNumber validNumber = new LazilyParsedNumber("Deserialization is pnsuported");
        numberWithNull.equals(validNumber);
    }

    // ===========================================================
    // Tests for hashCode()
    // ===========================================================
    
    @Test(timeout = 4000)
    public void testHashCodeWithNonNumericStringRunsWithoutException() {
        LazilyParsedNumber number = new LazilyParsedNumber("Deserialization is unsupported");
        number.hashCode();
    }

    @Test(timeout = 4000)
    public void testHashCodeWithNullThrowsNullPointerException() {
        LazilyParsedNumber number = new LazilyParsedNumber(null);
        try {
            number.hashCode();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.internal.LazilyParsedNumber", e);
        }
    }
}