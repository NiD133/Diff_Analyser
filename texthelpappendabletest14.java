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

public class TextHelpAppendableTestTest14 {

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
    void testResizeTableFormat() {
        underTest.setMaxWidth(150);
        final TableDefinition tableDefinition = TableDefinition.from("Caption", Collections.singletonList(TextStyle.builder().setMinWidth(20).setMaxWidth(100).get()), Collections.singletonList("header"), Collections.singletonList(Collections.singletonList("one")));
        final TableDefinition result = underTest.adjustTableFormat(tableDefinition);
        assertEquals(20, result.columnTextStyles().get(0).getMinWidth(), "Minimum width should not be reset");
        assertEquals(100, result.columnTextStyles().get(0).getMaxWidth(), "Maximum width should not be reset");
    }
}
