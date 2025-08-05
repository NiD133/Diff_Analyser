package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
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
public class CaseFormat_ESTest extends CaseFormat_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testConvertUpperUnderscoreToLowerCamel() throws Throwable {
        CaseFormat sourceFormat = CaseFormat.UPPER_UNDERSCORE;
        CaseFormat targetFormat = CaseFormat.LOWER_CAMEL;
        String input = "Y3:E]9bSR@H%B/_?";
        String result = sourceFormat.to(targetFormat, input);
        assertEquals("y3:e]9bsr@h%b/?", result);
    }

    @Test(timeout = 4000)
    public void testConvertLowerCamelToLowerUnderscore() throws Throwable {
        CaseFormat sourceFormat = CaseFormat.LOWER_CAMEL;
        CaseFormat targetFormat = CaseFormat.LOWER_UNDERSCORE;
        String input = "0P|{HG$S{ax$v|r_ ";
        String result = sourceFormat.to(targetFormat, input);
        assertEquals("0_p|{_h_g$_s{ax$v|r_ ", result);
    }

    @Test(timeout = 4000)
    public void testConvertUpperUnderscoreToSameFormat() throws Throwable {
        CaseFormat format = CaseFormat.UPPER_UNDERSCORE;
        String input = " ALUEZ";
        String result = format.convert(format, input);
        assertEquals(" ALUEZ", result);
    }

    @Test(timeout = 4000)
    public void testConvertLowerUnderscoreToUpperCamel() throws Throwable {
        CaseFormat sourceFormat = CaseFormat.LOWER_UNDERSCORE;
        CaseFormat targetFormat = CaseFormat.UPPER_CAMEL;
        String input = "bDU\"5";
        String result = sourceFormat.to(targetFormat, input);
        assertEquals("Bdu\"5", result);
    }

    @Test(timeout = 4000)
    public void testCaseFormatEnumValues() throws Throwable {
        CaseFormat[] caseFormats = CaseFormat.values();
        assertEquals(5, caseFormats.length);
    }

    @Test(timeout = 4000)
    public void testValueOfLowerCamel() throws Throwable {
        assertNotNull(CaseFormat.valueOf("LOWER_CAMEL"));
    }

    @Test(timeout = 4000)
    public void testNormalizeFirstWordEmptyString() throws Throwable {
        CaseFormat format = CaseFormat.UPPER_CAMEL;
        String result = format.normalizeFirstWord("");
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testConvertUpperCamelToSameFormatEmptyString() throws Throwable {
        CaseFormat format = CaseFormat.UPPER_CAMEL;
        String result = format.convert(format, "");
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testConvertLowerUnderscoreToSameFormatNullString() throws Throwable {
        CaseFormat format = CaseFormat.LOWER_UNDERSCORE;
        try {
            format.to(format, (String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testNormalizeFirstWordNullString() throws Throwable {
        CaseFormat format = CaseFormat.LOWER_CAMEL;
        try {
            format.normalizeFirstWord((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testConverterToNullFormat() throws Throwable {
        CaseFormat format = CaseFormat.LOWER_CAMEL;
        try {
            format.converterTo((CaseFormat) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testConvertLowerHyphenToUpperCamelEmptyString() throws Throwable {
        CaseFormat sourceFormat = CaseFormat.LOWER_HYPHEN;
        CaseFormat targetFormat = CaseFormat.UPPER_CAMEL;
        String result = sourceFormat.to(targetFormat, "");
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testConvertLowerCamelToSameFormat() throws Throwable {
        CaseFormat format = CaseFormat.LOWER_CAMEL;
        String input = "UPPER_UNDERSCORE";
        String result = format.convert(format, input);
        assertEquals("uPPER_UNDERSCORE", result);
    }

    @Test(timeout = 4000)
    public void testConvertLowerCamelToSameFormatNoChange() throws Throwable {
        CaseFormat format = CaseFormat.LOWER_CAMEL;
        String input = "0P|{HG$S{ax$v|r_ ";
        String result = format.to(format, input);
        assertEquals("0P|{HG$S{ax$v|r_ ", result);
    }

    @Test(timeout = 4000)
    public void testConvertLowerUnderscoreToUpperUnderscoreNoChange() throws Throwable {
        CaseFormat sourceFormat = CaseFormat.LOWER_UNDERSCORE;
        CaseFormat targetFormat = CaseFormat.UPPER_UNDERSCORE;
        String input = "2$ddmbvc\" rj0j %>[";
        String result = sourceFormat.convert(targetFormat, input);
        assertEquals("2$ddmbvc\" rj0j %>[", result);
    }

    @Test(timeout = 4000)
    public void testConvertUpperUnderscoreToLowerHyphenNoChange() throws Throwable {
        CaseFormat sourceFormat = CaseFormat.UPPER_UNDERSCORE;
        CaseFormat targetFormat = CaseFormat.LOWER_HYPHEN;
        String input = "-";
        String result = sourceFormat.convert(targetFormat, input);
        assertEquals("-", result);
    }

    @Test(timeout = 4000)
    public void testConvertLowerCamelToUpperUnderscoreNullString() throws Throwable {
        CaseFormat sourceFormat = CaseFormat.LOWER_CAMEL;
        CaseFormat targetFormat = CaseFormat.UPPER_UNDERSCORE;
        try {
            targetFormat.convert(sourceFormat, (String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.CharMatcher", e);
        }
    }

    @Test(timeout = 4000)
    public void testConvertLowerUnderscoreToUpperUnderscore() throws Throwable {
        CaseFormat sourceFormat = CaseFormat.LOWER_UNDERSCORE;
        CaseFormat targetFormat = CaseFormat.UPPER_UNDERSCORE;
        String input = "Q#&'.zTN&p_";
        String result = sourceFormat.to(targetFormat, input);
        assertEquals("Q#&'.ZTN&P_", result);
    }

    @Test(timeout = 4000)
    public void testConvertLowerUnderscoreToLowerHyphenNullString() throws Throwable {
        CaseFormat sourceFormat = CaseFormat.LOWER_UNDERSCORE;
        CaseFormat targetFormat = CaseFormat.LOWER_HYPHEN;
        try {
            sourceFormat.convert(targetFormat, (String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.CaseFormat$2", e);
        }
    }

    @Test(timeout = 4000)
    public void testConvertLowerUnderscoreToSameFormat() throws Throwable {
        CaseFormat format = CaseFormat.LOWER_UNDERSCORE;
        String input = "83pvzR?h!";
        String result = format.convert(format, input);
        assertEquals("83pvzr?h!", result);
    }

    @Test(timeout = 4000)
    public void testConvertLowerHyphenToUpperUnderscoreNullString() throws Throwable {
        CaseFormat sourceFormat = CaseFormat.LOWER_HYPHEN;
        CaseFormat targetFormat = CaseFormat.UPPER_UNDERSCORE;
        try {
            sourceFormat.convert(targetFormat, (String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.CaseFormat$1", e);
        }
    }

    @Test(timeout = 4000)
    public void testConvertLowerHyphenToLowerUnderscore() throws Throwable {
        CaseFormat sourceFormat = CaseFormat.LOWER_HYPHEN;
        CaseFormat targetFormat = CaseFormat.LOWER_UNDERSCORE;
        String input = "S:^5jO-|]r";
        String result = sourceFormat.to(targetFormat, input);
        assertEquals("S:^5jO_|]r", result);
    }

    @Test(timeout = 4000)
    public void testConvertLowerHyphenToSameFormat() throws Throwable {
        CaseFormat format = CaseFormat.LOWER_HYPHEN;
        String input = "83pvzR?h!";
        String result = format.convert(format, input);
        assertEquals("83pvzr?h!", result);
    }

    @Test(timeout = 4000)
    public void testNormalizeFirstWordUpperCamel() throws Throwable {
        CaseFormat format = CaseFormat.UPPER_CAMEL;
        String input = "&/>Ql\"@^2R";
        String result = format.normalizeFirstWord(input);
        assertEquals("&/>ql\"@^2r", result);
    }

    @Test(timeout = 4000)
    public void testConverterToSameFormat() throws Throwable {
        CaseFormat format = CaseFormat.LOWER_CAMEL;
        Converter<String, String> converter = format.converterTo(format);
        assertNotNull(converter);
    }
}