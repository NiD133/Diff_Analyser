package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

@SuppressWarnings("deprecation") // OptionBuilder is marked deprecated
class OptionBuilderTest {

    private static final String OPTION_DESCRIPTION = "option description";
    private static final String SIMPLE_OPTION = "simple option";
    private static final String SIMPLE_OPTION_DESCRIPTION = "this is a simple option";
    private static final String DIMPLE_OPTION = "dimple option";
    private static final String DIMPLE_OPTION_DESCRIPTION = "this is a dimple option";

    @Test
    void shouldCreateOptionWithCharOpt() {
        final Option option = OptionBuilder.withDescription(OPTION_DESCRIPTION).create('o');

        assertEquals("o", option.getOpt());
        assertEquals(OPTION_DESCRIPTION, option.getDescription());
        assertFalse(option.hasArg());
    }

    @Test
    void shouldCreateOptionWithStringOpt() {
        final Option option = OptionBuilder.withDescription(OPTION_DESCRIPTION).create("o");

        assertEquals("o", option.getOpt());
        assertEquals(OPTION_DESCRIPTION, option.getDescription());
        assertFalse(option.hasArg());
    }

    @Test
    void shouldResetBuilderAfterEachUse() {
        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.withDescription("JUnit").create('"'));
        assertNull(OptionBuilder.create('x').getDescription(), "Description should not be inherited");
        assertThrows(IllegalArgumentException.class, OptionBuilder::create);
        assertNull(OptionBuilder.create('x').getDescription(), "Description should not be inherited");
    }

    @Test
    void shouldCreateCompleteOption() {
        final Option option = OptionBuilder.withLongOpt(SIMPLE_OPTION)
                                           .hasArg()
                                           .isRequired()
                                           .hasArgs()
                                           .withType(Float.class)
                                           .withDescription(SIMPLE_OPTION_DESCRIPTION)
                                           .create('s');

        assertEquals("s", option.getOpt());
        assertEquals(SIMPLE_OPTION, option.getLongOpt());
        assertEquals(SIMPLE_OPTION_DESCRIPTION, option.getDescription());
        assertEquals(Float.class, option.getType());
        assertTrue(option.hasArg());
        assertTrue(option.isRequired());
        assertTrue(option.hasArgs());
    }

    @Test
    void shouldThrowExceptionForIncompleteOption() {
        assertThrows(IllegalArgumentException.class, OptionBuilder::create);
        // Reset the builder implicitly
        OptionBuilder.create("opt");
    }

    @Test
    void shouldThrowExceptionForIllegalOptions() {
        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.withDescription(OPTION_DESCRIPTION).create('"'));
        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.create("opt`"));
        // Valid option
        OptionBuilder.create("opt");
    }

    @Test
    void shouldSetOptionArgNumbers() {
        final Option option = OptionBuilder.withDescription(OPTION_DESCRIPTION)
                                           .hasArgs(2)
                                           .create('o');

        assertEquals(2, option.getArgs());
    }

    @Test
    void shouldHandleSpecialOptionCharacters() {
        final Option opt1 = OptionBuilder.withDescription("help options").create('?');
        assertEquals("?", opt1.getOpt());

        final Option opt2 = OptionBuilder.withDescription("read from stdin").create('@');
        assertEquals("@", opt2.getOpt());

        assertThrows(IllegalArgumentException.class, () -> OptionBuilder.create(' '));
    }

    @Test
    void shouldCreateTwoCompleteOptions() {
        Option option = OptionBuilder.withLongOpt(SIMPLE_OPTION)
                                     .hasArg()
                                     .isRequired()
                                     .hasArgs()
                                     .withType(Float.class)
                                     .withDescription(SIMPLE_OPTION_DESCRIPTION)
                                     .create('s');

        assertEquals("s", option.getOpt());
        assertEquals(SIMPLE_OPTION, option.getLongOpt());
        assertEquals(SIMPLE_OPTION_DESCRIPTION, option.getDescription());
        assertEquals(Float.class, option.getType());
        assertTrue(option.hasArg());
        assertTrue(option.isRequired());
        assertTrue(option.hasArgs());

        option = OptionBuilder.withLongOpt(DIMPLE_OPTION)
                              .hasArg()
                              .withDescription(DIMPLE_OPTION_DESCRIPTION)
                              .create('d');

        assertEquals("d", option.getOpt());
        assertEquals(DIMPLE_OPTION, option.getLongOpt());
        assertEquals(DIMPLE_OPTION_DESCRIPTION, option.getDescription());
        assertEquals(String.class, option.getType());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgs());
    }
}