package org.apache.commons.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests for the deprecated {@link PosixParser}.
 * <p>
 * This class inherits from {@link AbstractParserTestCase} and overrides tests for features
 * that {@code PosixParser} does not support, such as long options.
 * </p>
 */
public class PosixParserTest extends AbstractParserTestCase {

    @BeforeEach
    @Override
    @SuppressWarnings("deprecation")
    public void setUp() {
        super.setUp();
        // Use the PosixParser for the tests defined in the abstract superclass.
        parser = new PosixParser();
    }

    /**
     * This test is disabled because the {@link PosixParser} is designed for
     * single-character options (e.g., "-v") and does not support long options
     * (e.g., "--version"). Therefore, this inherited test for long options is not applicable.
     */
    @Override
    @Test
    @Disabled("PosixParser does not support long options")
    void testLongWithUnexpectedArgument1() {
        // This inherited test is intentionally left blank and disabled.
    }
}