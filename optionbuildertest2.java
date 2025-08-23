package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class OptionBuilderTestTest2 {

    @Test
    void testBaseOptionStringOpt() {
        final Option base = OptionBuilder.withDescription("option description").create("o");
        assertEquals("o", base.getOpt());
        assertEquals("option description", base.getDescription());
        assertFalse(base.hasArg());
    }
}
