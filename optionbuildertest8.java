package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class OptionBuilderTestTest8 {

    @Test
    void testSpecialOptChars() throws Exception {
        // '?'
        final Option opt1 = OptionBuilder.withDescription("help options").create('?');
        assertEquals("?", opt1.getOpt());
        // '@'
        final Option opt2 = OptionBuilder.withDescription("read from stdin").create('@');
        assertEquals("@", opt2.getOpt());
        // ' '
        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.create(' '));
    }
}
