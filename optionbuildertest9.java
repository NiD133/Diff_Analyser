package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class OptionBuilderTestTest9 {

    @Test
    void testTwoCompleteOptions() {
        //@formatter:off
        Option simple = OptionBuilder.withLongOpt("simple option").hasArg().isRequired().hasArgs().withType(Float.class).withDescription("this is a simple option").create('s');
        //@formatter:on
        assertEquals("s", simple.getOpt());
        assertEquals("simple option", simple.getLongOpt());
        assertEquals("this is a simple option", simple.getDescription());
        assertEquals(simple.getType(), Float.class);
        assertTrue(simple.hasArg());
        assertTrue(simple.isRequired());
        assertTrue(simple.hasArgs());
        //@formatter:off
        simple = OptionBuilder.withLongOpt("dimple option").hasArg().withDescription("this is a dimple option").create('d');
        //@formatter:on
        assertEquals("d", simple.getOpt());
        assertEquals("dimple option", simple.getLongOpt());
        assertEquals("this is a dimple option", simple.getDescription());
        assertEquals(String.class, simple.getType());
        assertTrue(simple.hasArg());
        assertFalse(simple.isRequired());
        assertFalse(simple.hasArgs());
    }
}
