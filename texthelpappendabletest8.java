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

public class TextHelpAppendableTestTest8 {

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
    void testAppendTable() throws IOException {
        final TextStyle.Builder styleBuilder = TextStyle.builder();
        final List<TextStyle> styles = new ArrayList<>();
        styles.add(styleBuilder.setIndent(2).get());
        styles.add(styleBuilder.setIndent(0).setLeftPad(5).setAlignment(TextStyle.Alignment.RIGHT).get());
        final String[] headers = { "fox", "time" };
        // @formatter:off
        final List<List<String>> rows = Arrays.asList(Arrays.asList("The quick brown fox jumps over the lazy dog", "Now is the time for all good people to come to the aid of their country"), Arrays.asList("Léimeann an sionnach donn gasta thar an madra leisciúil", "Anois an t-am do na daoine maithe go léir teacht i gcabhair ar a dtír"));
        // @formatter:on
        List<String> expected = new ArrayList<>();
        expected.add(" Common Phrases");
        expected.add("");
        expected.add("               fox                                       time                   ");
        expected.add(" The quick brown fox jumps over           Now is the time for all good people to");
        expected.add("   the lazy dog                                 come to the aid of their country");
        expected.add(" Léimeann an sionnach donn gasta       Anois an t-am do na daoine maithe go léir");
        expected.add("   thar an madra leisciúil                           teacht i gcabhair ar a dtír");
        expected.add("");
        TableDefinition table = TableDefinition.from("Common Phrases", styles, Arrays.asList(headers), rows);
        sb.setLength(0);
        underTest.setMaxWidth(80);
        underTest.appendTable(table);
        List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(expected, actual, "full table failed");
        table = TableDefinition.from(null, styles, Arrays.asList(headers), rows);
        expected.remove(1);
        expected.remove(0);
        sb.setLength(0);
        underTest.appendTable(table);
        actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(expected, actual);
        table = TableDefinition.from(null, styles, Arrays.asList(headers), Collections.emptyList());
        expected = new ArrayList<>();
        expected.add(" fox     time");
        expected.add("");
        sb.setLength(0);
        underTest.appendTable(table);
        actual = IOUtils.readLines(new StringReader(sb.toString()));
        assertEquals(expected, actual, "no rows test failed");
    }
}
