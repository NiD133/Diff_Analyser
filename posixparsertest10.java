package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test case for the {@link PosixParser}.
 * <p>
 * This class overrides tests from {@link AbstractParserTestCase} to assert the
 * specific behavior of the PosixParser, particularly for features it does not
 * support, such as long options.
 * </p>
 */
public class PosixParserTest extends AbstractParserTestCase {

    @Override
    @BeforeEach
    @SuppressWarnings("deprecation") // The PosixParser class is deprecated.
    public void setUp() {
        super.setUp();
        parser = new PosixParser();
    }

    /**
     * Overrides the inherited test for unambiguous partial long options.
     * <p>
     * The {@link PosixParser} does not support long options, so it cannot recognize
     * partial long options. The original test in {@link AbstractParserTestCase} is
     * designed for parsers that do support this feature and would otherwise fail.
     * <p>
     * This overridden test asserts that the PosixParser correctly throws an
     * {@link UnrecognizedOptionException} when it encounters an argument that looks
     * like a long option. This turns a disabled test into an active verification of
     * the parser's expected behavior.
     */
    @Override
    @Test
    public void testUnambiguousPartialLongOption4() {
        // These are example arguments the superclass test might use, e.g.,
        // expecting "--ver" to be recognized as a partial match for "--version".
        final String[] args = {"--ver"};

        // Assert that parsing throws an exception. PosixParser does not handle long
        // options and will fail to parse this argument.
        final UnrecognizedOptionException e = assertThrows(UnrecognizedOptionException.class, () -> {
            parser.parse(options, args);
        }, "PosixParser should throw an exception for unsupported long options.");

        // Verify that the exception correctly identifies the problematic option token.
        assertEquals("--ver", e.getOption());
    }
}