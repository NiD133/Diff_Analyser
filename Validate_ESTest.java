package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class Validate_ESTest extends Validate_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testNoNullElementsWithNullArrayThrowsNullPointerException() {
        try {
            Validate.noNullElements((Object[]) null, "Array must not be null");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNoNullElementsWithoutMessageThrowsNullPointerException() {
        try {
            Validate.noNullElements((Object[]) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testFailWithIllegalArgumentException() {
        Object[] emptyArray = new Object[0];
        try {
            Validate.fail("Invalid argument", emptyArray);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testFailWithUnknownFormatConversionException() {
        Object[] emptyArray = new Object[0];
        try {
            Validate.fail("Invalid format: %m", emptyArray);
            fail("Expected UnknownFormatConversionException");
        } catch (UnknownFormatConversionException e) {
            verifyException("java.util.Formatter$FormatSpecifier", e);
        }
    }

    @Test(timeout = 4000)
    public void testFailWithMissingFormatWidthException() {
        Object[] arrayWithTwoElements = new Object[2];
        try {
            Validate.fail("Missing width: %-x", arrayWithTwoElements);
            fail("Expected MissingFormatWidthException");
        } catch (MissingFormatWidthException e) {
            verifyException("java.util.Formatter$FormatSpecifier", e);
        }
    }

    @Test(timeout = 4000)
    public void testFailWithIllegalFormatWidthException() {
        Object[] arrayWithFiveElements = new Object[5];
        try {
            Validate.fail("Illegal width: %9", arrayWithFiveElements);
            fail("Expected IllegalFormatWidthException");
        } catch (IllegalFormatWidthException e) {
            verifyException("java.util.Formatter$FormatSpecifier", e);
        }
    }

    @Test(timeout = 4000)
    public void testFailWithIllegalFormatFlagsException() {
        Object[] arrayWithTwoElements = new Object[2];
        try {
            Validate.fail("Illegal flags: %-", arrayWithTwoElements);
            fail("Expected IllegalFormatFlagsException");
        } catch (IllegalFormatFlagsException e) {
            verifyException("java.util.Formatter$FormatSpecifier", e);
        }
    }

    @Test(timeout = 4000)
    public void testFailWithIllegalFormatConversionException() {
        Object[] arrayWithOneElement = new Object[1];
        arrayWithOneElement[0] = "String";
        try {
            Validate.fail("Conversion error: %e", arrayWithOneElement);
            fail("Expected IllegalFormatConversionException");
        } catch (IllegalFormatConversionException e) {
            verifyException("java.util.Formatter$FormatSpecifier", e);
        }
    }

    @Test(timeout = 4000)
    public void testFailWithFormatFlagsConversionMismatchException() {
        Object[] emptyArray = new Object[0];
        try {
            Validate.fail("Mismatch: %+b", emptyArray);
            fail("Expected FormatFlagsConversionMismatchException");
        } catch (FormatFlagsConversionMismatchException e) {
            verifyException("java.util.Formatter$FormatSpecifier", e);
        }
    }

    @Test(timeout = 4000)
    public void testFailWithNullPointerExceptionForNullMessage() {
        Object[] emptyArray = new Object[0];
        try {
            Validate.fail(null, emptyArray);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // No message in exception
        }
    }

    @Test(timeout = 4000)
    public void testEnsureNotNullWithUnknownFormatConversionException() {
        Object[] emptyArray = new Object[0];
        try {
            Validate.ensureNotNull(null, "Invalid conversion: '%'", emptyArray);
            fail("Expected UnknownFormatConversionException");
        } catch (UnknownFormatConversionException e) {
            verifyException("java.util.Formatter", e);
        }
    }

    @Test(timeout = 4000)
    public void testEnsureNotNullWithMissingFormatArgumentException() {
        Object[] emptyArray = new Object[0];
        try {
            Validate.ensureNotNull(null, "Parameter '%s' must not be empty.", emptyArray);
            fail("Expected MissingFormatArgumentException");
        } catch (MissingFormatArgumentException e) {
            verifyException("java.util.Formatter", e);
        }
    }

    @Test(timeout = 4000)
    public void testEnsureNotNullWithIllegalFormatFlagsException() {
        Object[] arrayWithEightElements = new Object[8];
        try {
            Validate.ensureNotNull(null, "Illegal flags: %-,", arrayWithEightElements);
            fail("Expected IllegalFormatFlagsException");
        } catch (IllegalFormatFlagsException e) {
            verifyException("java.util.Formatter$FormatSpecifier", e);
        }
    }

    @Test(timeout = 4000)
    public void testEnsureNotNullWithIllegalFormatConversionException() {
        Object[] arrayWithSevenElements = new Object[7];
        arrayWithSevenElements[0] = "String";
        try {
            Validate.ensureNotNull(null, "Conversion error: %x", arrayWithSevenElements);
            fail("Expected IllegalFormatConversionException");
        } catch (IllegalFormatConversionException e) {
            verifyException("java.util.Formatter$FormatSpecifier", e);
        }
    }

    @Test(timeout = 4000)
    public void testEnsureNotNullWithNullPointerExceptionForNullMessage() {
        Object[] emptyArray = new Object[0];
        try {
            Validate.ensureNotNull(null, null, emptyArray);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // No message in exception
        }
    }

    @Test(timeout = 4000)
    public void testNoNullElementsWithEmptyString() {
        Object[] arrayWithOneEmptyString = new Object[1];
        arrayWithOneEmptyString[0] = "";
        Validate.noNullElements(arrayWithOneEmptyString, "Array must not contain null elements");
        assertEquals(1, arrayWithOneEmptyString.length);
    }

    @Test(timeout = 4000)
    public void testNoNullElementsThrowsIllegalArgumentException() {
        Object[] arrayWithOneElement = new Object[1];
        try {
            Validate.noNullElements(arrayWithOneElement, "Object must not be null");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testFailWithIllegalArgumentExceptionForValidationException() {
        try {
            Validate.fail("org.jsoup.helper.ValidationException");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNotEmptyThrowsIllegalArgumentException() {
        try {
            Validate.notEmpty("", "String must not be empty");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNotEmptyWithNonEmptyString() {
        Validate.notEmpty("Non-empty string", "String must not be empty");
    }

    @Test(timeout = 4000)
    public void testNotEmptyThrowsIllegalArgumentExceptionForNull() {
        try {
            Validate.notEmpty(null, "String must not be empty");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNotEmptyParamWithNonEmptyString() {
        Validate.notEmptyParam("Non-empty string", "Parameter must not be empty");
    }

    @Test(timeout = 4000)
    public void testNotEmptyParamThrowsIllegalArgumentException() {
        try {
            Validate.notEmptyParam("", "Parameter must not be empty");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNotEmptyParamThrowsIllegalArgumentExceptionForNull() {
        try {
            Validate.notEmptyParam(null, "Parameter must not be empty");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNotEmptyThrowsIllegalArgumentExceptionForEmptyString() {
        try {
            Validate.notEmpty("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNotEmptyWithNonEmptyStringWithoutMessage() {
        Validate.notEmpty("Non-empty string");
    }

    @Test(timeout = 4000)
    public void testNotEmptyThrowsIllegalArgumentExceptionForNullWithoutMessage() {
        try {
            Validate.notEmpty(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNoNullElementsThrowsIllegalArgumentExceptionForNullElement() {
        Object[] arrayWithNullElement = new Object[7];
        try {
            Validate.noNullElements(arrayWithNullElement);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsFalseThrowsIllegalArgumentException() {
        try {
            Validate.isFalse(true, "Must be false");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsFalseWithFalseValue() {
        Validate.isFalse(false, "Must be false");
    }

    @Test(timeout = 4000)
    public void testIsFalseThrowsIllegalArgumentExceptionWithoutMessage() {
        try {
            Validate.isFalse(true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsFalseWithFalseValueWithoutMessage() {
        Validate.isFalse(false);
    }

    @Test(timeout = 4000)
    public void testIsTrueThrowsIllegalArgumentException() {
        try {
            Validate.isTrue(false, "Must be true");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsTrueWithTrueValue() {
        Validate.isTrue(true, "Must be true");
    }

    @Test(timeout = 4000)
    public void testIsTrueThrowsIllegalArgumentExceptionWithoutMessage() {
        try {
            Validate.isTrue(false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsTrueWithTrueValueWithoutMessage() {
        Validate.isTrue(true);
    }

    @Test(timeout = 4000)
    public void testEnsureNotNullThrowsIllegalArgumentException() {
        try {
            Validate.ensureNotNull(null, "Object must not be null", (Object[]) null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testEnsureNotNullWithNonNullObject() {
        Object[] arrayWithSevenElements = new Object[7];
        Object result = Validate.ensureNotNull("", "Object must not be null", arrayWithSevenElements);
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testEnsureNotNullThrowsIllegalArgumentExceptionWithoutMessage() {
        try {
            Validate.ensureNotNull(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testEnsureNotNullWithNonNullObjectWithoutMessage() {
        Object object = new Object();
        Object result = Validate.ensureNotNull(object);
        assertSame(result, object);
    }

    @Test(timeout = 4000)
    public void testNotNullThrowsIllegalArgumentException() {
        try {
            Validate.notNull(null, "Object must not be null");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNotNullWithNonNullObject() {
        Object object = new Object();
        Validate.notNull(object, "Object must not be null");
    }

    @Test(timeout = 4000)
    public void testNotNullParamThrowsIllegalArgumentException() {
        try {
            Validate.notNullParam(null, "Parameter must not be null");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNotNullParamWithNonNullObject() {
        Object[] arrayWithTwoElements = new Object[2];
        arrayWithTwoElements[1] = "";
        Validate.notNullParam(arrayWithTwoElements[1], "Parameter must not be null");
        assertEquals(2, arrayWithTwoElements.length);
    }

    @Test(timeout = 4000)
    public void testNotNullThrowsIllegalArgumentExceptionWithoutMessage() {
        try {
            Validate.notNull(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNotNullWithNonNullObjectWithoutMessage() {
        Validate.notNull("Non-null object");
    }

    @Test(timeout = 4000)
    public void testFailWithMissingFormatArgumentException() {
        Object[] emptyArray = new Object[0];
        try {
            Validate.fail("The parameter '%s' must not be null.", emptyArray);
            fail("Expected MissingFormatArgumentException");
        } catch (MissingFormatArgumentException e) {
            verifyException("java.util.Formatter", e);
        }
    }

    @Test(timeout = 4000)
    public void testNoNullElementsWithEmptyArray() {
        Object[] emptyArray = new Object[0];
        Validate.noNullElements(emptyArray);
        assertEquals(0, emptyArray.length);
    }

    @Test(timeout = 4000)
    public void testAssertFailWithIllegalArgumentException() {
        try {
            Validate.assertFail("Assertion failed");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testWtfWithIllegalStateException() {
        try {
            Validate.wtf("Unexpected state");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }
}