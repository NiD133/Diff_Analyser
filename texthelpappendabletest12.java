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

public class TextHelpAppendableTestTest12 {

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
    void testPrintWrapped() throws IOException {
        String text = "The quick brown fox jumps over the lazy dog";
        final TextStyle.Builder styleBuilder = TextStyle.builder().setMaxWidth(10).setIndent(0).setLeftPad(0);
        final List<String> expected = new ArrayList<>();
        expected.add("The quick");
        expected.add("brown fox");
        expected.add("jumps over");
        expected.add("the lazy");
        expected.add("dog");
        underTest.printWrapped(text, styleBuilder.get());
        List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(expected, actual, "left aligned failed");
        sb.setLength(0);
        expected.clear();
        expected.add(" The quick");
        expected.add(" brown fox");
        expected.add("jumps over");
        expected.add("  the lazy");
        expected.add("       dog");
        styleBuilder.setAlignment(TextStyle.Alignment.RIGHT);
        underTest.printWrapped(text, styleBuilder.get());
        actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(expected, actual, "right aligned failed");
        sb.setLength(0);
        expected.clear();
        expected.add("The quick");
        expected.add("brown fox");
        expected.add("jumps over");
        expected.add(" the lazy");
        expected.add("   dog");
        styleBuilder.setAlignment(TextStyle.Alignment.CENTER);
        underTest.printWrapped(text, styleBuilder.get());
        actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(expected, actual, "center aligned failed");
        sb.setLength(0);
        expected.clear();
        expected.add(" The quick brown fox jumps over the lazy dog");
        assertEquals(1, underTest.getLeftPad(), "unexpected page left pad");
        assertEquals(3, underTest.getIndent(), "unexpected page indent");
        assertEquals(74, underTest.getMaxWidth(), "unexpected page width");
        underTest.printWrapped(text);
        actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(expected, actual, "default format aligned failed");
        sb.setLength(0);
        text += ".\nNow is the time for all good people to come to the aid of their country.";
        expected.clear();
        expected.add(" The quick brown fox jumps over the lazy dog.");
        expected.add("    Now is the time for all good people to come to the aid of their");
        expected.add("    country.");
        underTest.printWrapped(text);
        actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(expected, actual, "default format aligned failed");
    }
}
