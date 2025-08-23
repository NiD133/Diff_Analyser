package org.apache.commons.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test case for the PosixParser.
 * <p>
 * This class inherits from {@link AbstractParserTestCase}, which provides a generic
 * suite of tests for command-line parsers. This class specializes the test suite
 * for the {@link PosixParser}.
 * <p>
 * Some tests from the parent class are overridden and disabled because the
 * PosixParser does not support certain features, such as long options with a
 * single dash.
 */
public class PosixParserTest extends AbstractParserTestCase {

    @Override
    @BeforeEach
    @SuppressWarnings("deprecation")
    public void setUp() {
        super.setUp();
        parser = new PosixParser();
    }

    /**
     * This test is inherited from {@link AbstractParserTestCase} and is disabled
     * because the {@code PosixParser} does not support long options with a single
     * dash (e.g., {@code -version}).
     * <p>
     * The POSIX standard treats an argument like {@code -version} as a cluster of
     * short options ({@code -v}, {@code -e}, {@code -r}, ...), not as a single
     * long option. Therefore, this behavior is not applicable to the
     * {@code PosixParser} and is explicitly marked as unsupported.
     */
    @Override
    @Test
    @Disabled("Long options with a single dash are not supported by PosixParser.")
    void testAmbiguousLongWithoutEqualSingleDash2() throws Exception {
        // This test is intentionally left empty because the feature is not supported.
        // The @Disabled annotation prevents it from running and documents the reason.
    }
}