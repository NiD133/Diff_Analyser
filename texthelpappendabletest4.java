package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class TextHelpAppendableTestTest4 {

    private StringBuilder sb;

    private TextHelpAppendable underTest;

    @BeforeEach
    public void setUp() {
        sb = new StringBuilder();
        underTest = new TextHelpAppendable(sb);
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.cli.help.UtilTest#charArgs")
    void testindexOfWrapPosWithWhitespace(final Character c, final boolean isWhitespace) {
        final String text = String.format("Hello%cWorld", c);
        assertEquals(isWhitespace ? 5 : 6, TextHelpAppendable.indexOfWrap(text, 7, 0));
    }

    @Test
    void testAppendHeader() throws IOException {
        final String[] expected = { " Hello World", " ===========", "" };
        sb.setLength(0);
        underTest.appendHeader(1, "Hello World");
        List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(Arrays.asList(expected), actual, "header 1 failed");
        sb.setLength(0);
        underTest.appendHeader(2, "Hello World");
        actual = IOUtils.readLines(new StringReader(sb.toString()));
        expected[1] = " %%%%%%%%%%%";
        assertEquals(Arrays.asList(expected), actual, "header 2 failed");
        sb.setLength(0);
        underTest.appendHeader(3, "Hello World");
        actual = IOUtils.readLines(new StringReader(sb.toString()));
        expected[1] = " +++++++++++";
        assertEquals(Arrays.asList(expected), actual, "header 3 failed");
        sb.setLength(0);
        underTest.appendHeader(4, "Hello World");
        actual = IOUtils.readLines(new StringReader(sb.toString()));
        expected[1] = " ___________";
        assertEquals(Arrays.asList(expected), actual, "header 4 failed");
        sb.setLength(0);
        underTest.appendHeader(5, "Hello World");
        actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(Arrays.asList(expected), actual, "header 5 failed");
        sb.setLength(0);
        assertThrows(IllegalArgumentException.class, () -> underTest.appendHeader(0, "Hello World"));
        sb.setLength(0);
        underTest.appendHeader(5, "");
        assertEquals(0, sb.length(), "empty string test failed");
        sb.setLength(0);
        underTest.appendHeader(5, null);
        assertEquals(0, sb.length(), "null test failed");
    }
}
