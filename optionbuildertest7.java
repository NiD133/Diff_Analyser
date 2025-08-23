package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class OptionBuilderTestTest7 {

    @Test
    void testOptionArgNumbers() {
        //@formatter:off
        final Option opt = OptionBuilder.withDescription("option description").hasArgs(2).create('o');
        //@formatter:on
        assertEquals(2, opt.getArgs());
    }
}
