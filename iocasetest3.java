package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link IOCase#checkEndsWith(String, String)}.
 */
@DisplayName("IOCase.checkEndsWith")
class IOCaseCheckEndsWithTest {

    private static final boolean IS_WINDOWS = File.separatorChar == '\\';

    @Test
    @DisplayName("SENSITIVE should perform a case-sensitive check")
    void sensitiveCheckEndsWithIsCaseSensitive() {
        final String text = "Apache Commons IO";

        assertTrue(IOCase.SENSITIVE.checkEndsWith(text, "IO"), "Exact match should return true");
        assertFalse(IOCase.SENSITIVE.checkEndsWith(text, "io"), "Different case should return false");
        assertFalse(IOCase.SENSITIVE.checkEndsWith(text, "Apache"), "Should not match the start");
    }

    @Test
    @DisplayName("INSENSITIVE should perform a case-insensitive check")
    void insensitiveCheckEndsWithIsCaseInsensitive() {
        final String text = "Apache Commons IO";

        assertTrue(IOCase.INSENSITIVE.checkEndsWith(text, "IO"), "Exact match should return true");
        assertTrue(IOCase.INSENSITIVE.checkEndsWith(text, "io"), "Different case should also return true");
        assertFalse(IOCase.INSENSITIVE.checkEndsWith(text, "Apache"), "Should not match the start");
    }

    @Test
    @DisplayName("SYSTEM should use OS-dependent case sensitivity")
    void systemCheckEndsWithIsOsDependent() {
        final String text = "Apache Commons IO";

        // An exact match should always work, regardless of the OS.
        assertTrue(IOCase.SYSTEM.checkEndsWith(text, "IO"));

        // A case-different match should only work on Windows.
        // On Windows, IS_WINDOWS is true, and the check is case-insensitive (returns true).
        // On other OS, IS_WINDOWS is false, and the check is case-sensitive (returns false).
        assertEquals(IS_WINDOWS, IOCase.SYSTEM.checkEndsWith(text, "io"));
    }

    @Test
    @DisplayName("should return false for null inputs")
    void checkEndsWithReturnsFalseForNull() {
        // All IOCase types should behave identically for null inputs.
        final IOCase caseType = IOCase.SENSITIVE;

        assertFalse(caseType.checkEndsWith(null, "IO"), "Null string should return false");
        assertFalse(caseType.checkEndsWith("Apache Commons IO", null), "Null suffix should return false");
        assertFalse(caseType.checkEndsWith(null, null), "Both null should return false");
    }
}