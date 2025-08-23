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

public class TextHelpAppendableTestTest16 {

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
    void testWriteColumnQueues() throws IOException {
        final List<Queue<String>> queues = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();
        queue.add("The quick ");
        queue.add("brown fox ");
        queue.add("jumps over");
        queue.add("the lazy  ");
        queue.add("dog       ");
        queues.add(queue);
        queue = new LinkedList<>();
        queue.add("     Now is the");
        queue.add("     time for  ");
        queue.add("     all good  ");
        queue.add("     people to ");
        queue.add("     come to   ");
        queue.add("     the aid of");
        queue.add("     their     ");
        queue.add("     country   ");
        queues.add(queue);
        final TextStyle.Builder styleBuilder = TextStyle.builder().setMaxWidth(10).setIndent(0).setLeftPad(0);
        final List<TextStyle> columns = new ArrayList<>();
        columns.add(styleBuilder.get());
        columns.add(styleBuilder.setLeftPad(5).get());
        final List<String> expected = new ArrayList<>();
        expected.add(" The quick      Now is the");
        expected.add(" brown fox      time for  ");
        expected.add(" jumps over     all good  ");
        expected.add(" the lazy       people to ");
        expected.add(" dog            come to   ");
        expected.add("                the aid of");
        expected.add("                their     ");
        expected.add("                country   ");
        sb.setLength(0);
        underTest.writeColumnQueues(queues, columns);
        final List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(expected, actual);
    }
}
