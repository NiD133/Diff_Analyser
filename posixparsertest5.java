package org.apache.commons.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Confirms that features unsupported by the {@link PosixParser} are handled correctly.
 * <p>
 * This class inherits from {@link AbstractParserTestCase} and intentionally disables
 * tests for features that the {@code PosixParser} does not implement. This ensures
 * the test suite accurately reflects the parser's specific capabilities.
 * </p>
 */
public class PosixParserUnsupportedFeaturesTest extends AbstractParserTestCase {

    @Override
    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setUp() {
        super.setUp();
        parser = new PosixParser();
    }

    /**
     * Overrides and disables the superclass test for long options with a single dash
     * and an equals sign (e.g., {@code -long-opt=value}).
     * <p>
     * This format is not supported by the {@link PosixParser}, so the inherited
     * test is disabled.
     * </p>
     */
    @Override
    @Test
    @Disabled("Long options with a single dash are not supported by PosixParser.")
    void testLongWithEqualSingleDash() {
        // This test is intentionally left empty because its purpose is to
        // disable an inherited test from AbstractParserTestCase that does
        // not apply to PosixParser.
    }
}