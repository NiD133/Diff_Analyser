package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link IOCase} enum.
 */
class IOCaseTest {

    /**
     * Tests that {@link IOCase#getName()} returns the correct, human-readable
     * name for each of the enum constants.
     */
    @Test
    @DisplayName("getName() should return the correct name for each IOCase constant")
    void testGetName() {
        assertAll("Verify names for all IOCase constants",
            () -> assertEquals("Sensitive", IOCase.SENSITIVE.getName()),
            () -> assertEquals("Insensitive", IOCase.INSENSITIVE.getName()),
            () -> assertEquals("System", IOCase.SYSTEM.getName())
        );
    }
}