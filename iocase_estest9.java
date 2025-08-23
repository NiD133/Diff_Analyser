package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that IOCase.SENSITIVE correctly identifies itself as case-sensitive.
     */
    @Test
    public void isCaseSensitive_shouldReturnTrue_whenCaseIsSensitive() {
        // The test verifies the fundamental property of the SENSITIVE enum constant.
        // An explicit message is added to the assertion for better failure diagnosis.
        assertTrue("IOCase.SENSITIVE is expected to be case-sensitive",
                   IOCase.SENSITIVE.isCaseSensitive());
    }
}