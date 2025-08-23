package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link OptionGroup}s containing options that only have a long name (no short name).
 */
class OptionGroupWithLongOnlyOptionsTest {

    private final Parser parser = new PosixParser();
    private Options options;

    @BeforeEach
    void setUp() {
        // Define an option group where members only have long names.
        // This is the specific scenario under test.
        final Option importOpt = new Option(null, "import", false, "Import from a file");
        final Option exportOpt = new Option(null, "export", false, "Export to a file");

        final OptionGroup longOnlyGroup = new OptionGroup();
        longOnlyGroup.addOption(importOpt);
        longOnlyGroup.addOption(exportOpt);

        options = new Options().addOptionGroup(longOnlyGroup);
    }

    @ParameterizedTest
    @ValueSource(strings = {"import", "export"})
    void shouldRecognizeSelectedLongOnlyOptionInGroup(String longOptionName) throws ParseException {
        // Arrange: Create command line arguments, e.g., {"--import"}
        final String[] args = {"--" + longOptionName};

        // Act: Parse the command line
        final CommandLine cmd = parser.parse(options, args);

        // Assert: Verify that the specified long option is present
        assertTrue(cmd.hasOption(longOptionName),
            () -> "Expected command line to have option: --" + longOptionName);
    }
}