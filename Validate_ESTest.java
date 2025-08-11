package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.FormatFlagsConversionMismatchException;
import java.util.IllegalFormatConversionException;
import java.util.IllegalFormatFlagsException;
import java.util.IllegalFormatWidthException;
import java.util.MissingFormatArgumentException;
import java.util.MissingFormatWidthException;
import java.util.UnknownFormatConversionException;
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
public class Validate_ESTest extends Validate_ESTest_scaffolding {

    // Tests for noNullElements()
    @Test(expected = NullPointerException.class, timeout = 4000)
    public void noNullElements_NullArrayWithMessage_ThrowsNPE() {
        Validate.noNullElements(null, "message");
    }

    @Test(expected = NullPointerException.class, timeout = 4000)
    public void noNullElements_NullArrayWithoutMessage_ThrowsNPE() {
        Validate.noNullElements(null);
    }

    @Test(timeout = 4000)
    public void noNullElements_EmptyArray_DoesNotThrow() {
        Object[] array = new Object[0];
        Validate.noNullElements(array);
    }

    @Test(timeout = 4000)
    public void noNullElements_ArrayWithNonNullElement_DoesNotThrow() {
        Object[] array = new Object[] { "valid" };
        Validate.noNullElements(array, "message");
    }

    @Test(expected = IllegalArgumentException.class, timeout = 4000)
    public void noNullElements_ArrayWithNullElement_ThrowsIAE() {
        Object[] array = new Object[] { null };
        Validate.noNullElements(array, "Object must not be null");
    }

    @Test(expected = IllegalArgumentException.class, timeout = 4000)
    public void noNullElements_ArrayWithNullElementWithoutMessage_ThrowsIAE() {
        Object[] array = new Object[] { null, "valid" };
        Validate.noNullElements(array);
    }

    // Tests for fail()
    @Test(expected = IllegalArgumentException.class, timeout = 4000)
    public void fail_SimpleMessage_ThrowsIAE() {
        Validate.fail("message");
    }

    @Test(expected = IllegalArgumentException.class, timeout = 4000)
    public void fail_WithFormatArguments_ThrowsIAE() {
        Validate.fail("a<QA~>32");
    }

    @Test(expected = UnknownFormatConversionException.class, timeout = 4000)
    public void fail_InvalidFormatConversion_ThrowsFormatException() {
        Validate.fail("w%(7ml:");
    }

    @Test(expected = MissingFormatWidthException.class, timeout = 4000)
    public void fail_MissingFormatWidth_ThrowsFormatException() {
        Validate.fail("FU?~WMWfE%-xniuY", new Object[2]);
    }

    @Test(expected = IllegalFormatWidthException.class, timeout = 4000)
    public void fail_IllegalFormatWidth_ThrowsFormatException() {
        Validate.fail("wz%c^4e%9no&N8", new Object[5]);
    }

    @Test(expected = IllegalFormatFlagsException.class, timeout = 4000)
    public void fail_IllegalFormatFlags_ThrowsFormatException() {
        Validate.fail("FU?~WMWfE%-niuY", new Object[2]);
    }

    @Test(expected = IllegalFormatConversionException.class, timeout = 4000)
    public void fail_IllegalFormatConversion_ThrowsFormatException() {
        Validate.fail("jzb<%EEu_", new Object[] { "jzb<%EEu_" });
    }

    @Test(expected = FormatFlagsConversionMismatchException.class, timeout = 4000)
    public void fail_FormatFlagsMismatch_ThrowsFormatException() {
        Validate.fail("gu?%+BkAQ5QsJ");
    }

    @Test(expected = NullPointerException.class, timeout = 4000)
    public void fail_NullMessage_ThrowsNPE() {
        Validate.fail(null, new Object[0]);
    }

    // Tests for ensureNotNull()
    @Test(expected = UnknownFormatConversionException.class, timeout = 4000)
    public void ensureNotNull_NullObjectWithFormatMessage_ThrowsFormatException() {
        Validate.ensureNotNull(null, "P>)7X)a:%'uj0", new Object[0]);
    }

    @Test(expected = MissingFormatArgumentException.class, timeout = 4000)
    public void ensureNotNull_NullObjectWithMissingFormatArgument_ThrowsFormatException() {
        Validate.ensureNotNull(null, "The '%s' parameter must not be empty.", new Object[0]);
    }

    @Test(expected = IllegalFormatFlagsException.class, timeout = 4000)
    public void ensureNotNull_NullObjectWithIllegalFormatFlags_ThrowsFormatException() {
        Validate.ensureNotNull(null, "x%,-n/sxs2[", new Object[8]);
    }

    @Test(expected = IllegalFormatConversionException.class, timeout = 4000)
    public void ensureNotNull_NullObjectWithIllegalFormatConversion_ThrowsFormatException() {
        Validate.ensureNotNull(null, "K)%x7cPKb^^;05", new Object[] { "K)%x7cPKb^^;05" });
    }

    @Test(expected = NullPointerException.class, timeout = 4000)
    public void ensureNotNull_NullObjectWithNullMessage_ThrowsNPE() {
        Validate.ensureNotNull(null, null, new Object[0]);
    }

    @Test(timeout = 4000)
    public void ensureNotNull_NonNullObject_ReturnsObject() {
        Object obj = new Object();
        Object result = Validate.ensureNotNull(obj, "", new Object[0]);
        assertSame(obj, result);
    }

    // Tests for notEmpty() variants
    @Test(expected = IllegalArgumentException.class, timeout = 4000)
    public void notEmpty_EmptyStringWithMessage_ThrowsIAE() {
        Validate.notEmpty("", "message");
    }

    @Test(timeout = 4000)
    public void notEmpty_NonEmptyString_DoesNotThrow() {
        Validate.notEmpty("valid", "");
    }

    @Test(expected = IllegalArgumentException.class, timeout = 4000)
    public void notEmpty_NullStringWithNullMessage_ThrowsIAE() {
        Validate.notEmpty(null, null);
    }

    @Test(timeout = 4000)
    public void notEmptyParam_NonEmptyString_DoesNotThrow() {
        Validate.notEmptyParam("valid", "param");
    }

    @Test(expected = IllegalArgumentException.class, timeout = 4000)
    public void notEmptyParam_EmptyString_ThrowsIAE() {
        Validate.notEmptyParam("", "paramName");
    }

    @Test(expected = IllegalArgumentException.class, timeout = 4000)
    public void notEmptyParam_NullString_ThrowsIAE() {
        Validate.notEmptyParam(null, "paramName");
    }

    @Test(expected = IllegalArgumentException.class, timeout = 4000)
    public void notEmpty_EmptyStringWithoutMessage_ThrowsIAE() {
        Validate.notEmpty("");
    }

    @Test(timeout = 4000)
    public void notEmpty_NonEmptyStringWithoutMessage_DoesNotThrow() {
        Validate.notEmpty("valid");
    }

    @Test(expected = IllegalArgumentException.class, timeout = 4000)
    public void notEmpty_NullStringWithoutMessage_ThrowsIAE() {
        Validate.notEmpty(null);
    }

    // Tests for isFalse()
    @Test(expected = IllegalArgumentException.class, timeout = 4000)
    public void isFalse_TrueValueWithMessage_ThrowsIAE() {
        Validate.isFalse(true, "message");
    }

    @Test(timeout = 4000)
    public void isFalse_FalseValueWithMessage_DoesNotThrow() {
        Validate.isFalse(false, "message");
    }

    @Test(expected = IllegalArgumentException.class, timeout = 4000)
    public void isFalse_TrueValueWithoutMessage_ThrowsIAE() {
        Validate.isFalse(true);
    }

    @Test(timeout = 4000)
    public void isFalse_FalseValueWithoutMessage_DoesNotThrow() {
        Validate.isFalse(false);
    }

    // Tests for isTrue()
    @Test(expected = IllegalArgumentException.class, timeout = 4000)
    public void isTrue_FalseValueWithMessage_ThrowsIAE() {
        Validate.isTrue(false, "message");
    }

    @Test(timeout = 4000)
    public void isTrue_TrueValueWithMessage_DoesNotThrow() {
        Validate.isTrue(true, "message");
    }

    @Test(expected = IllegalArgumentException.class, timeout = 4000)
    public void isTrue_FalseValueWithoutMessage_ThrowsIAE() {
        Validate.isTrue(false);
    }

    @Test(timeout = 4000)
    public void isTrue_TrueValueWithoutMessage_DoesNotThrow() {
        Validate.isTrue(true);
    }

    // Tests for ensureNotNull() without message
    @Test(expected = IllegalArgumentException.class, timeout = 4000)
    public void ensureNotNull_NullObjectWithoutMessage_ThrowsIAE() {
        Validate.ensureNotNull(null);
    }

    @Test(timeout = 4000)
    public void ensureNotNull_NonNullObjectWithoutMessage_ReturnsObject() {
        Object obj = new Object();
        Object result = Validate.ensureNotNull(obj);
        assertSame(obj, result);
    }

    // Tests for notNull() variants
    @Test(expected = IllegalArgumentException.class, timeout = 4000)
    public void notNull_NullObjectWithMessage_ThrowsIAE() {
        Validate.notNull(null, "message");
    }

    @Test(timeout = 4000)
    public void notNull_NonNullObjectWithMessage_DoesNotThrow() {
        Validate.notNull(new Object(), "message");
    }

    @Test(expected = IllegalArgumentException.class, timeout = 4000)
    public void notNullParam_NullObject_ThrowsIAE() {
        Validate.notNullParam(null, "paramName");
    }

    @Test(timeout = 4000)
    public void notNullParam_NonNullObject_DoesNotThrow() {
        Validate.notNullParam(new Object(), "paramName");
    }

    @Test(expected = IllegalArgumentException.class, timeout = 4000)
    public void notNull_NullObjectWithoutMessage_ThrowsIAE() {
        Validate.notNull(null);
    }

    @Test(timeout = 4000)
    public void notNull_NonNullObjectWithoutMessage_DoesNotThrow() {
        Validate.notNull(new Object());
    }

    // Tests for fail() with formatting
    @Test(expected = MissingFormatArgumentException.class, timeout = 4000)
    public void fail_FormatStringWithoutArguments_ThrowsFormatException() {
        Validate.fail("The '%s' parameter must not be null.", new Object[0]);
    }

    // Tests for assertFail()
    @Test(expected = IllegalArgumentException.class, timeout = 4000)
    public void assertFail_WithMessage_ThrowsIAE() {
        Validate.assertFail("message");
    }

    // Tests for wtf()
    @Test(expected = IllegalStateException.class, timeout = 4000)
    public void wtf_WithMessage_ThrowsIllegalStateException() {
        Validate.wtf("message");
    }
}