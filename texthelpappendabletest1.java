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

public class TextHelpAppendableTestTest1 {

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
    void tesstMakeColumnQueue() {
        final String text = "The quick brown fox jumps over the lazy dog";
        final TextStyle.Builder styleBuilder = TextStyle.builder().setMaxWidth(10).setIndent(0).setLeftPad(0);
        Queue<String> expected = new LinkedList<>();
        expected.add("The quick ");
        expected.add("brown fox ");
        expected.add("jumps over");
        expected.add("the lazy  ");
        expected.add("dog       ");
        Queue<String> result = underTest.makeColumnQueue(text, styleBuilder.get());
        assertEquals(expected, result, "left aligned failed");
        expected.clear();
        expected.add(" The quick");
        expected.add(" brown fox");
        expected.add("jumps over");
        expected.add("  the lazy");
        expected.add("       dog");
        styleBuilder.setAlignment(TextStyle.Alignment.RIGHT);
        result = underTest.makeColumnQueue(text, styleBuilder.get());
        assertEquals(expected, result, "right aligned failed");
        expected.clear();
        expected.add("The quick ");
        expected.add("brown fox ");
        expected.add("jumps over");
        expected.add(" the lazy ");
        expected.add("   dog    ");
        styleBuilder.setAlignment(TextStyle.Alignment.CENTER);
        result = underTest.makeColumnQueue(text, styleBuilder.get());
        assertEquals(expected, result, "center aligned failed");
        expected = new LinkedList<>();
        expected.add("      The quick");
        expected.add("          brown");
        expected.add("            fox");
        expected.add("          jumps");
        expected.add("       over the");
        expected.add("       lazy dog");
        styleBuilder.setAlignment(TextStyle.Alignment.RIGHT).setLeftPad(5).setIndent(2);
        result = underTest.makeColumnQueue(text, styleBuilder.get());
        assertEquals(expected, result, "right aligned failed");
    }
}
