package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.io.FileInputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PatternOptionBuilder} focusing on file type patterns.
 */
public class PatternOptionBuilderTestTest3 {

    private static final String EXISTING_FILE_PATH = "src/test/resources/org/apache/commons/cli/existing-readable.file";

    @Test
    @DisplayName("Pattern with '<' should create an option that returns a FileInputStream for an existing file")
    void parsePattern_shouldReturnFileInputStream_whenUsingExistingFilePattern() throws Exception {
        // ARRANGE
        // The '<' character in a pattern string configures an option to expect a path
        // to an existing file and to create a FileInputStream from it.
        final Options options = PatternOptionBuilder.parsePattern("g<");
        final CommandLineParser parser = new PosixParser();
        final String[] args = {"-g", EXISTING_FILE_PATH};
        Object optionValue = null;

        try {
            // ACT
            final CommandLine commandLine = parser.parse(options, args);
            optionValue = commandLine.getOptionObject("g");

            // ASSERT
            // The returned object should be a valid, non-null FileInputStream.
            // assertInstanceOf performs a null check implicitly.
            assertInstanceOf(FileInputStream.class, optionValue,
                "The option object should be a FileInputStream for the specified file.");
        } finally {
            // CLEANUP: Ensure the opened resource is closed to prevent leaks.
            if (optionValue instanceof FileInputStream) {
                ((FileInputStream) optionValue).close();
            }
        }
    }
}