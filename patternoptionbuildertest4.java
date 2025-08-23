package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
class PatternOptionBuilderTest {

    @Test
    @DisplayName("The '<' pattern should return a null object when the specified file does not exist")
    void existingFilePatternShouldReturnNullForNonExistentFile() throws ParseException {
        // ARRANGE
        // The '<' pattern character creates an option that expects the value to be an existing file.
        // If the file exists, getOptionObject() would return a FileInputStream.
        final Options options = PatternOptionBuilder.parsePattern("f<");
        final CommandLineParser parser = new DefaultParser();
        final String[] args = {"-f", "non-existent.file"};

        // ACT
        // Parse the command line arguments.
        final CommandLine cmd = parser.parse(options, args);

        // ASSERT
        // When the file does not exist, the parser should not throw an exception.
        // Instead, the corresponding option object should be null.
        assertNull(cmd.getOptionObject("f"), "Expected a null object because the file does not exist.");
    }
}