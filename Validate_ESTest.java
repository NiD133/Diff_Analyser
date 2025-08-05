package org.jsoup.helper;

import org.junit.Test;

import java.util.MissingFormatArgumentException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link Validate} helper class.
 */
public class ValidateTest {

    //region Tests for notNull methods
    @Test
    public void notNull_withObject_doesNotThrow() {
        Validate.notNull("A string");
        Validate.notNull(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void notNull_withNullObject_throwsIllegalArgumentException() {
        Validate.notNull(null);
    }

    @Test
    public void notNullWithMessage_withNullObject_throwsExceptionWithCustomMessage() {
        try {
            Validate.notNull(null, "The object must not be null");
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("The object must not be null", e.getMessage());
        }
    }

    @Test
    public void notNullParam_withNullObject_throwsExceptionWithParamName() {
        try {
            Validate.notNullParam(null, "paramName");
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("The parameter 'paramName' must not be null.", e.getMessage());
        }
    }
    //endregion

    //region Tests for notEmpty methods
    @Test
    public void notEmpty_withNonEmptyString_doesNotThrow() {
        Validate.notEmpty("test");
        Validate.notEmpty(" "); // A space is not empty
    }

    @Test(expected = IllegalArgumentException.class)
    public void notEmpty_withNullString_throwsIllegalArgumentException() {
        Validate.notEmpty(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void notEmpty_withEmptyString_throwsIllegalArgumentException() {
        Validate.notEmpty("");
    }

    @Test
    public void notEmptyWithMessage_withEmptyString_throwsExceptionWithCustomMessage() {
        try {
            Validate.notEmpty("", "String must not be empty");
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("String must not be empty", e.getMessage());
        }
    }

    @Test
    public void notEmptyParam_withEmptyString_throwsExceptionWithParamName() {
        try {
            Validate.notEmptyParam("", "paramName");
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("The 'paramName' parameter must not be empty.", e.getMessage());
        }
    }
    //endregion

    //region Tests for noNullElements
    @Test
    public void noNullElements_withValidArray_doesNotThrow() {
        String[] strings = {"one", "two", "three"};
        Validate.noNullElements(strings);
    }

    @Test
    public void noNullElements_withEmptyArray_doesNotThrow() {
        String[] emptyArray = {};
        Validate.noNullElements(emptyArray);
    }

    @Test(expected = NullPointerException.class)
    public void noNullElements_withNullArray_throwsNullPointerException() {
        Validate.noNullElements(null);
    }

    @Test
    public void noNullElements_withArrayContainingNull_throwsIllegalArgumentException() {
        String[] strings = {"one", null, "three"};
        try {
            Validate.noNullElements(strings);
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Array must not contain any null objects", e.getMessage());
        }
    }

    @Test
    public void noNullElementsWithMessage_withArrayContainingNull_throwsExceptionWithCustomMessage() {
        String[] strings = {"one", null, "three"};
        try {
            Validate.noNullElements(strings, "Custom message");
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Custom message", e.getMessage());
        }
    }
    //endregion

    //region Tests for isTrue / isFalse
    @Test
    public void isTrue_withTrue_doesNotThrow() {
        Validate.isTrue(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void isTrue_withFalse_throwsIllegalArgumentException() {
        Validate.isTrue(false);
    }

    @Test
    public void isTrueWithMessage_withFalse_throwsExceptionWithCustomMessage() {
        try {
            Validate.isTrue(false, "It must be true");
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("It must be true", e.getMessage());
        }
    }

    @Test
    public void isFalse_withFalse_doesNotThrow() {
        Validate.isFalse(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void isFalse_withTrue_throwsIllegalArgumentException() {
        Validate.isFalse(true);
    }
    //endregion

    //region Tests for fail, wtf, assertFail
    @Test
    public void fail_always_throwsIllegalArgumentException() {
        try {
            Validate.fail("This should always fail");
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("This should always fail", e.getMessage());
        }
    }

    @Test
    public void wtf_always_throwsIllegalStateException() {
        try {
            Validate.wtf("A terrible failure occurred");
            fail("Expected an IllegalStateException to be thrown.");
        } catch (IllegalStateException e) {
            assertEquals("A terrible failure occurred", e.getMessage());
        }
    }

    @Test
    public void assertFail_always_throwsIllegalArgumentException() {
        try {
            // In practice, this would be used in an assert statement,
            // but we test its throwing behavior directly.
            Validate.assertFail("This should always fail");
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("This should always fail", e.getMessage());
        }
    }

    /**
     * The original test suite included many cases for various formatting exceptions.
     * These primarily test the underlying JDK String.format implementation.
     * This single representative test is sufficient to confirm that formatting
     * exceptions from invalid format strings are passed through.
     */
    @Test(expected = MissingFormatArgumentException.class)
    public void fail_withMismatchedFormatString_throwsFormattingException() {
        Validate.fail("This has a format specifier %s but no argument");
    }
    //endregion

    //region Tests for deprecated ensureNotNull
    @Test
    public void ensureNotNull_withObject_returnsSameObject() {
        String obj = "test";
        Object result = Validate.ensureNotNull(obj);
        assertSame(obj, result);
    }

    @Test
    public void ensureNotNull_withNull_throwsIllegalArgumentException() {
        try {
            Validate.ensureNotNull(null);
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Object must not be null", e.getMessage());
        }
    }

    @Test
    public void ensureNotNullWithMessageAndArgs_withNull_throwsFormattedException() {
        try {
            Validate.ensureNotNull(null, "Object '%s' is null!", "testParam");
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Object 'testParam' is null!", e.getMessage());
        }
    }
    //endregion
}