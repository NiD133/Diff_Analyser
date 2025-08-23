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

public class TextHelpAppendableTestTest5 {

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
    void testAppendList() throws IOException {
        final List<String> expected = new ArrayList<>();
        final String[] entries = { "one", "two", "three" };
        for (int i = 0; i < entries.length; i++) {
            expected.add(String.format("  %s. %s", i + 1, entries[i]));
        }
        expected.add("");
        sb.setLength(0);
        underTest.appendList(true, Arrays.asList(entries));
        List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(expected, actual, "ordered list failed");
        sb.setLength(0);
        expected.clear();
        for (final String entry : entries) {
            expected.add(String.format("  * %s", entry));
        }
        expected.add("");
        underTest.appendList(false, Arrays.asList(entries));
        actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(expected, actual, "unordered list failed");
        sb.setLength(0);
        expected.clear();
        underTest.appendList(false, Collections.emptyList());
        actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(expected, actual, "empty list failed");
        sb.setLength(0);
        expected.clear();
        underTest.appendList(false, null);
        actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(expected, actual, "null list failed");
    }
}
