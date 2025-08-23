package org.jsoup.internal;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import static org.jsoup.internal.StringUtil.normaliseWhitespace;
import static org.jsoup.internal.StringUtil.resolve;
import static org.junit.jupiter.api.Assertions.*;

public class StringUtilTestTest14 {

    @Test
    void isDigit() {
        assertTrue(StringUtil.isDigit('0'));
        assertTrue(StringUtil.isDigit('1'));
        assertTrue(StringUtil.isDigit('2'));
        assertTrue(StringUtil.isDigit('3'));
        assertTrue(StringUtil.isDigit('4'));
        assertTrue(StringUtil.isDigit('5'));
        assertTrue(StringUtil.isDigit('6'));
        assertTrue(StringUtil.isDigit('7'));
        assertTrue(StringUtil.isDigit('8'));
        assertTrue(StringUtil.isDigit('9'));
        assertFalse(StringUtil.isDigit('a'));
        assertFalse(StringUtil.isDigit('A'));
        assertFalse(StringUtil.isDigit('ä'));
        assertFalse(StringUtil.isDigit('Ä'));
        assertFalse(StringUtil.isDigit('١'));
        assertFalse(StringUtil.isDigit('୳'));
    }
}
