package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link BigDecimalParser} class, focusing on its dependency handling.
 */
public class BigDecimalParserTest {

    /**
     * Verifies that calling {@code parseWithFastParser} throws a {@code NoClassDefFoundError}
     * when its required dependency (the 'fastdoubleparser' library) is not available on the classpath.
     * <p>
     * This test simulates a specific runtime environment to ensure the code fails predictably
     * when its dependencies are not met.
     */
    @Test(timeout = 4000, expected = NoClassDefFoundError.class)
    public void parseWithFastParserShouldThrowErrorWhenDependencyIsMissing() {
        // This call is expected to fail because the test is configured to run
        // without the 'ch.randelshofer.fastdoubleparser.JavaBigDecimalParser' class.
        BigDecimalParser.parseWithFastParser("");
    }
}