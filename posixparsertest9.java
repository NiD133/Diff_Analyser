package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link PosixParser} to verify its specific behaviors,
 * especially where it differs from other parser implementations.
 */
public class PosixParserTest extends AbstractParserTestCase {

    @Override
    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setUp() {
        super.setUp();
        parser = new PosixParser();
    }

    /**
     * Overrides the base test case to assert the specific behavior of the PosixParser.
     * <p>
     * The PosixParser does not support the {@code -o=value} syntax for short options in the
     * same way as other parsers (e.g., GnuParser). It does not strip the equals sign
     * from the value. Instead of parsing {@code -b=foo} to option {@code b} with value
     * {@code "foo"}, it results in option {@code b} with value {@code "=foo"}.
     * </p>
     * <p>
     * This test verifies this behavior, effectively confirming that the syntax is "not supported"
     * in the conventional sense.
     * </p>
     */
    @Override
    @Test
    public void testShortWithEqual() throws Exception {
        // The "b" option from the base test case requires an argument.
        final String[] args = {"-b=foo"};

        final CommandLine cmd = parser.parse(options, args);

        // The parser should recognize the 'b' option.
        assertTrue(cmd.hasOption("b"), "The 'b' option should be recognized.");

        // Verify that the PosixParser includes the equals sign in the option's value.
        // This is different from GnuParser, which would yield "foo".
        assertEquals("=foo", cmd.getOptionValue("b"), "The value of option 'b' should include the equals sign.");

        // Ensure no other arguments were parsed.
        assertTrue(cmd.getArgList().isEmpty(), "There should be no leftover arguments.");
    }
}