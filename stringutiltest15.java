package org.jsoup.internal;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import static org.jsoup.internal.StringUtil.normaliseWhitespace;
import static org.jsoup.internal.StringUtil.resolve;
import static org.junit.jupiter.api.Assertions.*;

public class StringUtilTestTest15 {

    @Test
    void isHexDigit() {
        assertTrue(StringUtil.isHexDigit('0'));
        assertTrue(StringUtil.isHexDigit('1'));
        assertTrue(StringUtil.isHexDigit('2'));
        assertTrue(StringUtil.isHexDigit('3'));
        assertTrue(StringUtil.isHexDigit('4'));
        assertTrue(StringUtil.isHexDigit('5'));
        assertTrue(StringUtil.isHexDigit('6'));
        assertTrue(StringUtil.isHexDigit('7'));
        assertTrue(StringUtil.isHexDigit('8'));
        assertTrue(StringUtil.isHexDigit('9'));
        assertTrue(StringUtil.isHexDigit('a'));
        assertTrue(StringUtil.isHexDigit('b'));
        assertTrue(StringUtil.isHexDigit('c'));
        assertTrue(StringUtil.isHexDigit('d'));
        assertTrue(StringUtil.isHexDigit('e'));
        assertTrue(StringUtil.isHexDigit('f'));
        assertTrue(StringUtil.isHexDigit('A'));
        assertTrue(StringUtil.isHexDigit('B'));
        assertTrue(StringUtil.isHexDigit('C'));
        assertTrue(StringUtil.isHexDigit('D'));
        assertTrue(StringUtil.isHexDigit('E'));
        assertTrue(StringUtil.isHexDigit('F'));
        assertFalse(StringUtil.isHexDigit('g'));
        assertFalse(StringUtil.isHexDigit('G'));
        assertFalse(StringUtil.isHexDigit('ä'));
        assertFalse(StringUtil.isHexDigit('Ä'));
        assertFalse(StringUtil.isHexDigit('١'));
        assertFalse(StringUtil.isHexDigit('୳'));
    }
}
