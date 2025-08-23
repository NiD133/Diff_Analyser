package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that the static method {@code IOCase.isCaseSensitive(IOCase)} correctly
     * handles a null input by returning false.
     */
    @Test
    public void isCaseSensitive_withNullInput_shouldReturnFalse() {
        // The isCaseSensitive method is designed to be null-safe.
        // According to its Javadoc, it should return false if the input is null.
        boolean result = IOCase.isCaseSensitive(null);

        assertFalse("isCaseSensitive(null) should return false", result);
    }
}