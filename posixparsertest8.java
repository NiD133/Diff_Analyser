package org.apache.commons.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests for the deprecated {@link PosixParser}.
 * <p>
 * This class specifically handles test cases from the parent
 * {@link AbstractParserTestCase} where the PosixParser's behavior differs,
 * particularly for unsupported features.
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
     * The {@link PosixParser} does not support negative numbers as option arguments
     * (e.g., "-200"). This behavior is documented in JIRA issue CLI-184.
     * Therefore, the inherited test for this feature is disabled.
     */
    @Override
    @Test
    @Disabled("Not supported by the PosixParser (CLI-184)")
    void testNegativeOption() {
        // This test is intentionally overridden and left empty because this
        // feature is not supported by PosixParser. The @Disabled annotation
        // prevents this test from running and failing.
    }
}