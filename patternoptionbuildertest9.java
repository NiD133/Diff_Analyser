package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
class PatternOptionBuilderTest {

    /**
     * Verifies that a pattern string without type specifiers (e.g., "abc")
     * creates simple flag options that do not take any arguments.
     */
    @Test
    void parsePatternWithSimpleFlagsCreatesOptionsWithoutArguments() throws ParseException {
        // Arrange
        final String pattern = "abc";
        final Options options = PatternOptionBuilder.parsePattern(pattern);
        final CommandLineParser parser = new DefaultParser();
        final String[] args = {"-abc"};

        // Act
        final CommandLine commandLine = parser.parse(options, args);

        // Assert
        for (final char opt : pattern.toCharArray()) {
            assertTrue(commandLine.hasOption(opt), "Option '" + opt + "' should be present.");
            assertNull(commandLine.getOptionObject(opt), "Option '" + opt + "' should not have a value.");
        }
    }
}