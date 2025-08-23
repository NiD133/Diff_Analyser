package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PatternOptionBuilder} focusing on the number type pattern ('%').
 */
// The original class name 'PatternOptionBuilderTestTest5' was redundant.
// Renamed to follow standard Java testing conventions.
public class PatternOptionBuilderTest {

    @Test
    @DisplayName("The '%' pattern should parse valid numbers and ignore invalid formats")
    void testNumberPattern() throws ParseException {
        // ARRANGE
        // The '%' pattern character specifies that an option's argument should be parsed as a number.
        // - 'n' will be tested with an integer value ("1").
        // - 'd' will be tested with a floating-point value ("2.1").
        // - 'x' will be tested with an invalid number format ("3,5").
        final String pattern = "n%d%x%";
        final Options options = PatternOptionBuilder.parsePattern(pattern);
        final CommandLineParser parser = new PosixParser();
        final String[] args = {"-n", "1", "-d", "2.1", "-x", "3,5"};

        // ACT
        final CommandLine commandLine = parser.parse(options, args);

        // ASSERT

        // 1. Verify that an integer string is correctly converted to a Long.
        final Object nValue = commandLine.getOptionObject("n");
        assertInstanceOf(Long.class, nValue, "Option 'n' with value '1' should be parsed as a Long.");
        assertEquals(1L, nValue, "The value for option 'n' should be 1.");

        // 2. Verify that a floating-point string is correctly converted to a Double.
        final Object dValue = commandLine.getOptionObject("d");
        assertInstanceOf(Double.class, dValue, "Option 'd' with value '2.1' should be parsed as a Double.");
        assertEquals(2.1, dValue, "The value for option 'd' should be 2.1.");

        // 3. Verify that an invalid number format is ignored.
        // The value "3,5" is not a valid number format because it uses a comma.
        // The parser fails to convert it, so the corresponding option object should be null.
        assertNull(commandLine.getOptionObject("x"),
            "Option 'x' with an invalid number format ('3,5') should result in a null object.");
    }
}