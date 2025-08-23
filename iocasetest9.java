package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link IOCase}, focusing on the {@code checkRegionMatches} method.
 */
public class IOCaseTest {

    private static final boolean IS_WINDOWS = File.separatorChar == '\\';

    @Test
    @DisplayName("SENSITIVE should perform an exact, case-sensitive region match")
    void checkRegionMatchesWithSensitiveIsCaseSensitive() {
        assertTrue(IOCase.SENSITIVE.checkRegionMatches("ABC", 0, "AB"), "Exact match should return true");
        assertFalse(IOCase.SENSITIVE.checkRegionMatches("ABC", 0, "Ab"), "Different case should return false");
    }

    @Test
    @DisplayName("INSENSITIVE should perform a case-insensitive region match")
    void checkRegionMatchesWithInsensitiveIsCaseInsensitive() {
        assertTrue(IOCase.INSENSITIVE.checkRegionMatches("ABC", 0, "AB"), "Exact match should return true");
        assertTrue(IOCase.INSENSITIVE.checkRegionMatches("ABC", 0, "Ab"), "Different case should also return true");
    }

    @Test
    @DisplayName("SYSTEM should use the operating system's case sensitivity for region matching")
    void checkRegionMatchesWithSystemFollowsOsSensitivity() {
        // An exact match should always work, regardless of the OS.
        assertTrue(IOCase.SYSTEM.checkRegionMatches("ABC", 0, "AB"), "Exact match should always return true");

        // The behavior for mixed-case matching depends on the OS.
        if (IS_WINDOWS) {
            assertTrue(IOCase.SYSTEM.checkRegionMatches("ABC", 0, "Ab"),
                "On Windows, region match should be case-insensitive");
        } else {
            assertFalse(IOCase.SYSTEM.checkRegionMatches("ABC", 0, "Ab"),
                "On non-Windows, region match should be case-sensitive");
        }
    }
}