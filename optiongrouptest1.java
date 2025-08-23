package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OptionGroupTestTest1 {

    private Options options;

    private final Parser parser = new PosixParser();

    @BeforeEach
    public void setUp() {
        final Option file = new Option("f", "file", false, "file to process");
        final Option dir = new Option("d", "directory", false, "directory to process");
        final OptionGroup optionGroup1 = new OptionGroup();
        optionGroup1.addOption(file);
        optionGroup1.addOption(dir);
        options = new Options().addOptionGroup(optionGroup1);
        final Option section = new Option("s", "section", false, "section to process");
        final Option chapter = new Option("c", "chapter", false, "chapter to process");
        final OptionGroup optionGroup2 = new OptionGroup();
        optionGroup2.addOption(section);
        optionGroup2.addOption(chapter);
        options.addOptionGroup(optionGroup2);
        final Option importOpt = new Option(null, "import", false, "section to process");
        final Option exportOpt = new Option(null, "export", false, "chapter to process");
        final OptionGroup optionGroup3 = new OptionGroup();
        optionGroup3.addOption(importOpt);
        optionGroup3.addOption(exportOpt);
        options.addOptionGroup(optionGroup3);
        options.addOption("r", "revision", false, "revision number");
    }

    @Test
    void testGetNames() {
        final OptionGroup optionGroup = new OptionGroup();
        assertFalse(optionGroup.isSelected());
        optionGroup.addOption(OptionBuilder.create('a'));
        optionGroup.addOption(OptionBuilder.create('b'));
        assertNotNull(optionGroup.getNames(), "null names");
        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
    }
}
