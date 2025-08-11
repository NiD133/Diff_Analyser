package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.io.BigDecimalParser;
import java.math.BigDecimal;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class BigDecimalParser_ESTest extends BigDecimalParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testParseWithInvalidOffsetAndLengthThrowsNoClassDefFoundError() {
        char[] charArray = new char[1];
        try {
            BigDecimalParser.parse(charArray, -1747, 771);
            fail("Expecting exception: NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            verifyException("com.fasterxml.jackson.core.io.BigDecimalParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseSingleDigitCharArray() {
        char[] charArray = new char[9];
        charArray[1] = '2';
        BigDecimal result = BigDecimalParser.parse(charArray, 1, 1);
        assertEquals((short) 2, result.shortValue());
    }

    @Test(timeout = 4000)
    public void testParseNegativeZeroCharArray() {
        char[] charArray = new char[] {'-', '0'};
        BigDecimal result = BigDecimalParser.parse(charArray);
        assertEquals((short) 0, result.shortValue());
    }

    @Test(timeout = 4000)
    public void testParseSingleDigitCharArrayToByte() {
        char[] charArray = new char[] {'4'};
        BigDecimal result = BigDecimalParser.parse(charArray);
        assertEquals((byte) 4, result.byteValue());
    }

    @Test(timeout = 4000)
    public void testParseScientificNotationCharArray() {
        char[] charArray = new char[] {'2', 'E', '2'};
        BigDecimal result = BigDecimalParser.parse(charArray);
        assertEquals((byte) (-56), result.byteValue());
    }

    @Test(timeout = 4000)
    public void testParseSingleDigitString() {
        BigDecimal result = BigDecimalParser.parse("8");
        assertEquals((byte) 8, result.byteValue());
    }

    @Test(timeout = 4000)
    public void testParseScientificNotationString() {
        BigDecimal result = BigDecimalParser.parse("7e2");
        assertEquals((short) 700, result.shortValue());
    }

    @Test(timeout = 4000)
    public void testParseEmptyCharArrayThrowsNumberFormatException() {
        char[] charArray = new char[1];
        try {
            BigDecimalParser.parse(charArray, 0, 0);
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            verifyException("com.fasterxml.jackson.core.io.BigDecimalParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseNullCharArrayThrowsNullPointerException() {
        try {
            BigDecimalParser.parse((char[]) null, 486, 486);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.math.BigDecimal", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseNullCharArrayWithoutOffsetThrowsNullPointerException() {
        try {
            BigDecimalParser.parse((char[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.fasterxml.jackson.core.io.BigDecimalParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseNullStringThrowsNullPointerException() {
        try {
            BigDecimalParser.parse((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.fasterxml.jackson.core.io.BigDecimalParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseEmptyCharArrayWithInvalidOffsetThrowsStringIndexOutOfBoundsException() {
        char[] charArray = new char[0];
        try {
            BigDecimalParser.parse(charArray, 265, 265);
            fail("Expecting exception: StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testParseInvalidStringThrowsNumberFormatException() {
        try {
            BigDecimalParser.parse("eA8ojpN");
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            verifyException("com.fasterxml.jackson.core.io.BigDecimalParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseEmptyCharArrayWithLargeOffsetThrowsNoClassDefFoundError() {
        char[] charArray = new char[0];
        try {
            BigDecimalParser.parse(charArray, 500, 500);
            fail("Expecting exception: NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            verifyException("com.fasterxml.jackson.core.io.BigDecimalParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseDecimalString() {
        BigDecimal result = BigDecimalParser.parse(".1");
        assertEquals((byte) 0, result.byteValue());
    }

    @Test(timeout = 4000)
    public void testParseWithFastParserInvalidOffsetThrowsNoClassDefFoundError() {
        char[] charArray = new char[6];
        try {
            BigDecimalParser.parseWithFastParser(charArray, 1754, -2552);
            fail("Expecting exception: NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            verifyException("com.fasterxml.jackson.core.io.BigDecimalParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseWithFastParserEmptyStringThrowsNoClassDefFoundError() {
        try {
            BigDecimalParser.parseWithFastParser("");
            fail("Expecting exception: NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            verifyException("com.fasterxml.jackson.core.io.BigDecimalParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseEmptyCharArrayThrowsNumberFormatException() {
        char[] charArray = new char[3];
        try {
            BigDecimalParser.parse(charArray);
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            verifyException("com.fasterxml.jackson.core.io.BigDecimalParser", e);
        }
    }
}