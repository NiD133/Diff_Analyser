package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PatternOptionBuilder} focusing on required options.
 */
// The original class name 'PatternOptionBuilderTestTest7' is kept as requested.
public class PatternOptionBuilderTestTest7 {

    @Test
    @DisplayName("Parsing should throw MissingOptionException when a required option is not provided.")
    void parse_shouldThrowMissingOptionException_forMissingRequiredOption() {
        // Arrange
        // Define a pattern where 'n' is a required option (indicated by '!')
        // and both 'n' and 'm' expect a Number argument (indicated by '%').
        final String pattern = "!n%m%";
        final Options options = PatternOptionBuilder.parsePattern(pattern);
        final CommandLineParser parser = new PosixParser();
        final String[] emptyArgs = {};

        // Act & Assert
        // Verify that parsing with no arguments throws MissingOptionException because 'n' is required.
        final MissingOptionException thrown = assertThrows(MissingOptionException.class, () -> {
            parser.parse(options, emptyArgs);
        });

        // Further Assertions to confirm the exception details
        assertEquals(1, thrown.getMissingOptions().size(), "There should be exactly one missing option.");
        assertTrue(thrown.getMissingOptions().contains("n"), "The list of missing options should contain 'n'.");
    }
}