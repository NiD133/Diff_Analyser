package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OptionGroupTestTest3 {

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
    void testSingleLongOption() throws Exception {
        final String[] args = { "--file" };
        final CommandLine cl = parser.parse(options, args);
        assertFalse(cl.hasOption("r"), "Confirm -r is NOT set");
        assertTrue(cl.hasOption("f"), "Confirm -f is set");
        assertFalse(cl.hasOption("d"), "Confirm -d is NOT set");
        assertFalse(cl.hasOption("s"), "Confirm -s is NOT set");
        assertFalse(cl.hasOption("c"), "Confirm -c is NOT set");
        assertTrue(cl.getArgList().isEmpty(), "Confirm no extra args");
    }
}
