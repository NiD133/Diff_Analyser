package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Properties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the precedence rules for options within an OptionGroup when provided
 * from multiple sources (e.g., command-line arguments and properties).
 */
class OptionGroupPrecedenceTest {

    private final Parser parser = new PosixParser();

    @Test
    @DisplayName("CLI arguments should have precedence over properties for options in the same group")
    void shouldPrioritizeCliArgumentOverPropertyWhenBothInSameOptionGroup() throws Exception {
        // Arrange
        // Define a mutually exclusive group for a "file" option and a "directory" option.
        final Option fileOption = new Option("f", "file", false, "file to process");
        final Option dirOption = new Option("d", "directory", false, "directory to process");
        final OptionGroup fileOrDirGroup = new OptionGroup();
        fileOrDirGroup.addOption(fileOption);
        fileOrDirGroup.addOption(dirOption);

        final Options options = new Options().addOptionGroup(fileOrDirGroup);

        // Provide the '-f' option via command-line arguments.
        final String[] args = {"-f"};

        // Provide the conflicting '-d' option via properties.
        final Properties properties = new Properties();
        properties.setProperty("d", "true");

        // Act
        // Parse the inputs. The parser must resolve the conflict based on precedence rules.
        final CommandLine cl = parser.parse(options, args, properties);

        // Assert
        // The command-line argument ('-f') should be selected, and the property ('d') should be ignored.
        assertTrue(cl.hasOption("f"), "The option from command-line arguments should be selected.");
        assertFalse(cl.hasOption("d"), "The option from properties should be ignored due to CLI argument precedence.");
    }
}