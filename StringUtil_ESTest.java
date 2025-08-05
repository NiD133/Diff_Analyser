package org.jsoup.internal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.net.MockURL;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.stream.Collector;

import static org.evosuite.shaded.org.mockito.Mockito.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class StringUtil_ESTest extends StringUtil_ESTest_scaffolding {

    // Hex Digit Tests
    @Test(timeout = 4000)
    public void testIsHexDigit_UpperCase() {
        Assert.assertTrue(StringUtil.isHexDigit('F'));
        Assert.assertTrue(StringUtil.isHexDigit('A'));
    }

    @Test(timeout = 4000)
    public void testIsHexDigit_LowerCase() {
        Assert.assertTrue(StringUtil.isHexDigit('f'));
        Assert.assertTrue(StringUtil.isHexDigit('a'));
    }

    @Test(timeout = 4000)
    public void testIsHexDigit_Numeric() {
        Assert.assertTrue(StringUtil.isHexDigit('9'));
    }

    @Test(timeout = 4000)
    public void testIsHexDigit_NonHex() {
        Assert.assertFalse(StringUtil.isHexDigit('\"'));
        Assert.assertFalse(StringUtil.isHexDigit('\u008F'));
    }

    // ASCII Letter Tests
    @Test(timeout = 4000)
    public void testIsAsciiLetter_UpperCase() {
        Assert.assertTrue(StringUtil.isAsciiLetter('Z'));
        Assert.assertTrue(StringUtil.isAsciiLetter('A'));
    }

    @Test(timeout = 4000)
    public void testIsAsciiLetter_LowerCase() {
        Assert.assertTrue(StringUtil.isAsciiLetter('z'));
        Assert.assertTrue(StringUtil.isAsciiLetter('a'));
    }

    @Test(timeout = 4000)
    public void testIsAsciiLetter_NonLetter() {
        Assert.assertFalse(StringUtil.isAsciiLetter('!'));
        Assert.assertFalse(StringUtil.isAsciiLetter('}'));
    }

    // Digit Tests
    @Test(timeout = 4000)
    public void testIsDigit_Valid() {
        Assert.assertTrue(StringUtil.isDigit('0'));
        Assert.assertTrue(StringUtil.isDigit('3'));
    }

    @Test(timeout = 4000)
    public void testIsDigit_Invalid() {
        Assert.assertFalse(StringUtil.isDigit('Y'));
        Assert.assertFalse(StringUtil.isDigit('%'));
    }

    // Whitespace Tests
    @Test(timeout = 4000)
    public void testIsWhitespace() {
        Assert.assertTrue(StringUtil.isWhitespace(32));
        Assert.assertTrue(StringUtil.isWhitespace(13));
        Assert.assertTrue(StringUtil.isWhitespace(12));
        Assert.assertTrue(StringUtil.isWhitespace(10));
        Assert.assertTrue(StringUtil.isWhitespace(9));
        Assert.assertFalse(StringUtil.isWhitespace(21));
        Assert.assertFalse(StringUtil.isWhitespace(0));
    }

    @Test(timeout = 4000)
    public void testIsActuallyWhitespace() {
        Assert.assertTrue(StringUtil.isActuallyWhitespace(32));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(13));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(12));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(10));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(9));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(160));
        Assert.assertFalse(StringUtil.isActuallyWhitespace(193));
        Assert.assertFalse(StringUtil.isActuallyWhitespace(-5106));
    }

    // Invisible Character Tests
    @Test(timeout = 4000)
    public void testIsInvisibleChar() {
        Assert.assertTrue(StringUtil.isInvisibleChar(173));
        Assert.assertTrue(StringUtil.isInvisibleChar(8203));
        Assert.assertFalse(StringUtil.isInvisibleChar(8222));
    }

    // Padding Tests
    @Test(timeout = 4000)
    public void testPadding() {
        Assert.assertEquals("                     ", StringUtil.padding(21, 21));
        Assert.assertEquals("", StringUtil.padding(0, -1));
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testPadding_InvalidWidth() {
        StringUtil.padding(-1);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testPadding_InvalidMaxWidth() {
        StringUtil.padding(166, -37);
    }

    // URL Resolve Tests
    @Test(timeout = 4000)
    public void testResolve_Valid() throws MalformedURLException {
        URL baseUrl = MockURL.getFtpExample();
        URL resolvedUrl = StringUtil.resolve(baseUrl, "=:,DeW^");
        Assert.assertEquals("ftp://ftp.someFakeButWellFormedURL.org/=:,DeW^", resolvedUrl.toString());
    }

    @Test(timeout = 4000, expected = MalformedURLException.class)
    public void testResolve_Invalid() throws MalformedURLException {
        StringUtil.resolve((URL) null, "B1s2U[+");
    }

    // Join Tests
    @Test(timeout = 4000)
    public void testJoin_EmptyCollection() {
        LinkedList<StringBuilder> emptyList = new LinkedList<>();
        Assert.assertEquals("", StringUtil.join((Collection<?>) emptyList, "String must not be empty"));
    }

    @Test(timeout = 4000)
    public void testJoin_EmptyArray() {
        String[] emptyArray = new String[0];
        Assert.assertEquals("", StringUtil.join(emptyArray, "c^\"k/FFrM6)OCGy9F03"));
    }

    @Test(timeout = 4000)
    public void testJoin_EmptyIterator() {
        LinkedList<Object> emptyList = new LinkedList<>();
        ListIterator<Object> iterator = emptyList.listIterator();
        Assert.assertEquals("", StringUtil.join((Iterator<?>) iterator, ")&*!.GRzo RF[l,9W"));
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testJoin_NullCollection() {
        StringUtil.join((Collection<?>) null, "M");
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testJoin_NullIterator() {
        StringUtil.join((Iterator<?>) null, (String) null);
    }

    // String Joiner Tests
    @Test(timeout = 4000)
    public void testStringJoiner_Complete() {
        StringUtil.StringJoiner joiner = new StringUtil.StringJoiner("H5OE=&4p F0oe@7Fl1");
        Assert.assertEquals("", joiner.complete());
    }

    @Test(timeout = 4000)
    public void testStringJoiner_Add() {
        StringUtil.StringJoiner joiner = new StringUtil.StringJoiner("H5OE=&4p F0oe@7Fl1");
        joiner.add("H5OE=&4p F0oe@7Fl1");
        joiner.add("H5OE=&4p F0oe@7Fl1");
        Assert.assertEquals("H5OE=&4p F0oe@7Fl1H5OE=&4p F0oe@7Fl1", joiner.complete());
    }

    // Other Tests
    @Test(timeout = 4000)
    public void testIsAscii() {
        Assert.assertTrue(StringUtil.isAscii("{3f\"nUAQw7TH,Y-"));
        Assert.assertFalse(StringUtil.isAscii("Yn)+vHQao!UlQ0jsv(O"));
    }

    @Test(timeout = 4000)
    public void testIsNumeric() {
        Assert.assertTrue(StringUtil.isNumeric("3"));
        Assert.assertFalse(StringUtil.isNumeric("Yn)+vHQao!UlQ0jsv(O"));
        Assert.assertFalse(StringUtil.isNumeric(""));
        Assert.assertFalse(StringUtil.isNumeric((String) null));
    }

    @Test(timeout = 4000)
    public void testIsBlank() {
        Assert.assertTrue(StringUtil.isBlank("                    "));
        Assert.assertTrue(StringUtil.isBlank(""));
        Assert.assertTrue(StringUtil.isBlank((String) null));
        Assert.assertFalse(StringUtil.isBlank("_5s]9,"));
    }

    @Test(timeout = 4000)
    public void testStartsWithNewline() {
        Assert.assertFalse(StringUtil.startsWithNewline(""));
        Assert.assertFalse(StringUtil.startsWithNewline("^[a-zA-Z][a-zA-Z0-9+-.]*:"));
        Assert.assertFalse(StringUtil.startsWithNewline((String) null));
    }

    // Exception Tests
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testReleaseBuilderVoid_Null() {
        StringUtil.releaseBuilderVoid((StringBuilder) null);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testReleaseBuilder_Null() {
        StringUtil.releaseBuilder((StringBuilder) null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testNormaliseWhitespace_Null() {
        StringUtil.normaliseWhitespace((String) null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testAppendNormalisedWhitespace_Null() {
        StringUtil.appendNormalisedWhitespace((StringBuilder) null, "@i:M]zJx[8", true);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testInSorted_NullArray() {
        StringUtil.inSorted("l~", (String[]) null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testIn_NullArray() {
        StringUtil.in("", (String[]) null);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testIsAscii_Null() {
        StringUtil.isAscii((String) null);
    }

    @Test(timeout = 4000, expected = ConcurrentModificationException.class)
    public void testJoin_ConcurrentModification() {
        LinkedList<Object> list = new LinkedList<>();
        ListIterator<Object> iterator = list.listIterator();
        list.add(iterator);
        StringUtil.join((Iterator<?>) iterator, "?4");
    }

    @Test(timeout = 4000, expected = StackOverflowError.class)
    public void testJoin_StackOverflow() {
        LinkedList<Object> list0 = new LinkedList<>();
        LinkedList<Object> list1 = new LinkedList<>();
        list0.add(list1);
        list1.add(list0);
        list0.addAll(list1);
        StringUtil.join((Collection<?>) list0, "?h");
    }
}