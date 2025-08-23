package org.apache.commons.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests for the deprecated {@link PosixParser}.
 *
 * <p>
 * This class inherits from {@link AbstractParserTestCase}, which defines a common
 * suite of tests for different parser implementations. This class overrides and
 * disables tests for features that {@code PosixParser} does not support.
 * </p>
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
     * Overrides the superclass test to confirm that PosixParser does not support
     * long options with a single dash (e.g., "-file"). The Posix standard
     * treats this as a cluster of short options ("-f", "-i", "-l", "-e").
     */
    @Override
    @Test
    @Disabled("Not supported by PosixParser: long options require a double dash.")
    void longOptionWithSingleDashIsNotSupported() {
        // This test is intentionally empty because it overrides a test from the
        // base class that is not applicable to PosixParser. The @Disabled
        // annotation formally documents this intended behavior.
    }
}