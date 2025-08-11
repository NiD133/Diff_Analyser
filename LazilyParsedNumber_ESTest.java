package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.gson.internal.LazilyParsedNumber;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
)
public class LazilyParsedNumber_ESTest extends LazilyParsedNumber_ESTest_scaffolding {

    // Test toString method with various inputs
    @Test(timeout = 4000)
    public void testToStringWithValidString() {
        LazilyParsedNumber number = new LazilyParsedNumber("A_>sS8(ab<");
        assertEquals("A_>sS8(ab<", number.toString());
    }

    @Test(timeout = 4000)
    public void testToStringWithEmptyString() {
        LazilyParsedNumber number = new LazilyParsedNumber("");
        assertEquals("", number.toString());
    }

    @Test(timeout = 4000)
    public void testToStringWithNullString() {
        LazilyParsedNumber number = new LazilyParsedNumber((String) null);
        assertNull(number.toString());
    }

    // Test hashCode method
    @Test(timeout = 4000)
    public void testHashCodeWithValidString() {
        LazilyParsedNumber number = new LazilyParsedNumber("Deserialization is unsupported");
        number.hashCode(); // No exception expected
    }

    @Test(timeout = 4000)
    public void testHashCodeWithNullString() {
        LazilyParsedNumber number = new LazilyParsedNumber((String) null);
        try {
            number.hashCode();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.internal.LazilyParsedNumber", e);
        }
    }

    // Test longValue method
    @Test(timeout = 4000)
    public void testLongValueWithZero() {
        LazilyParsedNumber number = new LazilyParsedNumber("0");
        assertEquals(0L, number.longValue());
    }

    @Test(timeout = 4000)
    public void testLongValueWithPositiveNumber() {
        LazilyParsedNumber number = new LazilyParsedNumber("3");
        assertEquals(3L, number.longValue());
    }

    @Test(timeout = 4000)
    public void testLongValueWithNegativeNumber() {
        LazilyParsedNumber number = new LazilyParsedNumber("-6");
        assertEquals(-6L, number.longValue());
    }

    @Test(timeout = 4000)
    public void testLongValueWithInvalidString() {
        LazilyParsedNumber number = new LazilyParsedNumber("Deserialization is unsupported");
        try {
            number.longValue();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            verifyException("java.math.BigDecimal", e);
        }
    }

    @Test(timeout = 4000)
    public void testLongValueWithNullString() {
        LazilyParsedNumber number = new LazilyParsedNumber((String) null);
        try {
            number.longValue();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.internal.NumberLimits", e);
        }
    }

    // Test intValue method
    @Test(timeout = 4000)
    public void testIntValueWithZero() {
        LazilyParsedNumber number = new LazilyParsedNumber("0");
        assertEquals(0, number.intValue());
    }

    @Test(timeout = 4000)
    public void testIntValueWithPositiveNumber() {
        LazilyParsedNumber number = new LazilyParsedNumber("5");
        assertEquals(5, number.intValue());
    }

    @Test(timeout = 4000)
    public void testIntValueWithNegativeNumber() {
        LazilyParsedNumber number = new LazilyParsedNumber("-6");
        assertEquals(-6, number.intValue());
    }

    @Test(timeout = 4000)
    public void testIntValueWithInvalidString() {
        LazilyParsedNumber number = new LazilyParsedNumber("Deserialization is unsupported");
        try {
            number.intValue();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            verifyException("java.math.BigDecimal", e);
        }
    }

    @Test(timeout = 4000)
    public void testIntValueWithNullString() {
        LazilyParsedNumber number = new LazilyParsedNumber((String) null);
        try {
            number.intValue();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.internal.NumberLimits", e);
        }
    }

    // Test floatValue method
    @Test(timeout = 4000)
    public void testFloatValueWithZero() {
        LazilyParsedNumber number = new LazilyParsedNumber("0");
        assertEquals(0.0F, number.floatValue(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testFloatValueWithPositiveNumber() {
        LazilyParsedNumber number = new LazilyParsedNumber("7");
        assertEquals(7.0F, number.floatValue(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testFloatValueWithNegativeNumber() {
        LazilyParsedNumber number = new LazilyParsedNumber("-6");
        assertEquals(-6.0F, number.floatValue(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testFloatValueWithInvalidString() {
        LazilyParsedNumber number = new LazilyParsedNumber("Deserialization is unsupported");
        try {
            number.floatValue();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testFloatValueWithNullString() {
        LazilyParsedNumber number = new LazilyParsedNumber((String) null);
        try {
            number.floatValue();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Test doubleValue method
    @Test(timeout = 4000)
    public void testDoubleValueWithZero() {
        LazilyParsedNumber number = new LazilyParsedNumber("0");
        assertEquals(0.0, number.doubleValue(), 0.01);
    }

    @Test(timeout = 4000)
    public void testDoubleValueWithPositiveNumber() {
        LazilyParsedNumber number = new LazilyParsedNumber("3");
        assertEquals(3.0, number.doubleValue(), 0.01);
    }

    @Test(timeout = 4000)
    public void testDoubleValueWithNegativeNumber() {
        LazilyParsedNumber number = new LazilyParsedNumber("-6");
        assertEquals(-6.0, number.doubleValue(), 0.01);
    }

    @Test(timeout = 4000)
    public void testDoubleValueWithInvalidString() {
        LazilyParsedNumber number = new LazilyParsedNumber("...");
        try {
            number.doubleValue();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testDoubleValueWithNullString() {
        LazilyParsedNumber number = new LazilyParsedNumber((String) null);
        try {
            number.doubleValue();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Test equals method
    @Test(timeout = 4000)
    public void testEqualsWithSameObject() {
        LazilyParsedNumber number = new LazilyParsedNumber("...");
        assertTrue(number.equals(number));
    }

    @Test(timeout = 4000)
    public void testEqualsWithIdenticalContent() {
        LazilyParsedNumber number1 = new LazilyParsedNumber("Deserialization is pnsuported");
        LazilyParsedNumber number2 = new LazilyParsedNumber("Deserialization is pnsuported");
        assertTrue(number1.equals(number2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentContent() {
        LazilyParsedNumber number = new LazilyParsedNumber("d<Hh");
        assertFalse(number.equals("d<Hh"));
    }

    @Test(timeout = 4000)
    public void testEqualsWithNullString() {
        LazilyParsedNumber number1 = new LazilyParsedNumber((String) null);
        LazilyParsedNumber number2 = new LazilyParsedNumber("Deserialization is pnsuported");
        try {
            number1.equals(number2);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
}