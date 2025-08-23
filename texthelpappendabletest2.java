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

public class TextHelpAppendableTestTest2 {

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
    void testAdjustTableFormat() {
        // test width smaller than header
        // @formatter:off
        final TableDefinition tableDefinition = TableDefinition.from("Testing", Collections.singletonList(TextStyle.builder().setMaxWidth(3).get()), Collections.singletonList("header"), // "data" shorter than "header"
        Collections.singletonList(Collections.singletonList("data")));
        // @formatter:on
        final TableDefinition actual = underTest.adjustTableFormat(tableDefinition);
        assertEquals("header".length(), actual.columnTextStyles().get(0).getMaxWidth());
        assertEquals("header".length(), actual.columnTextStyles().get(0).getMinWidth());
    }
}
