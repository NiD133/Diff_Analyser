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

public class TextHelpAppendableTestTest11 {

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
    void testindexOfWrapPos() {
        final String testString = "The quick brown fox jumps over\tthe lazy dog";
        assertEquals(9, TextHelpAppendable.indexOfWrap(testString, 10, 0), "did not find end of word");
        assertEquals(9, TextHelpAppendable.indexOfWrap(testString, 14, 0), "did not backup to end of word");
        assertEquals(15, TextHelpAppendable.indexOfWrap(testString, 15, 0), "did not find word at 15");
        assertEquals(15, TextHelpAppendable.indexOfWrap(testString, 16, 0));
        assertEquals(30, TextHelpAppendable.indexOfWrap(testString, 15, 20), "did not find break character");
        assertEquals(30, TextHelpAppendable.indexOfWrap(testString, 150, 0), "did not handle text shorter than width");
        assertThrows(IllegalArgumentException.class, () -> TextHelpAppendable.indexOfWrap("", 0, 0));
        assertEquals(3, TextHelpAppendable.indexOfWrap("Hello", 4, 0));
    }
}
