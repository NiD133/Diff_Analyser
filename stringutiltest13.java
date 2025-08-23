package org.jsoup.internal;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import static org.jsoup.internal.StringUtil.normaliseWhitespace;
import static org.jsoup.internal.StringUtil.resolve;
import static org.junit.jupiter.api.Assertions.*;

public class StringUtilTestTest13 {

    @Test
    void isAsciiLetter() {
        assertTrue(StringUtil.isAsciiLetter('a'));
        assertTrue(StringUtil.isAsciiLetter('n'));
        assertTrue(StringUtil.isAsciiLetter('z'));
        assertTrue(StringUtil.isAsciiLetter('A'));
        assertTrue(StringUtil.isAsciiLetter('N'));
        assertTrue(StringUtil.isAsciiLetter('Z'));
        assertFalse(StringUtil.isAsciiLetter(' '));
        assertFalse(StringUtil.isAsciiLetter('-'));
        assertFalse(StringUtil.isAsciiLetter('0'));
        assertFalse(StringUtil.isAsciiLetter('ß'));
        assertFalse(StringUtil.isAsciiLetter('Ě'));
    }
}
