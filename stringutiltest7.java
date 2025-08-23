package org.jsoup.internal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.jsoup.internal.StringUtil.normaliseWhitespace;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the StringUtil#normaliseWhitespace method.
 */
@DisplayName("StringUtil.normaliseWhitespace")
public class StringUtilTest {

    @ParameterizedTest(name = "[{index}] Input: \"{0}\" → Expected: \"{1}\"")
    @CsvSource(textBlock = """
        '    \r \n \r\n',              ' '
        '   hello   \r \n  there    \n', ' hello there '
        'hello',                       'hello'
        'hello\nthere',                'hello there'
    """)
    void shouldNormaliseWhitespaceCorrectly(String input, String expected) {
        String actual = normaliseWhitespace(input);
        assertEquals(expected, actual);
    }
}