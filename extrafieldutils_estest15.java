package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

/**
 * Unit tests for the {@link ExtraFieldUtils} class.
 */
public class ExtraFieldUtilsTest {

    /**
     * Verifies that calling the parse method with a null byte array
     * results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void parseWithNullDataShouldThrowNullPointerException() {
        // The 'local' parameter is set to false, but its value is not
        // relevant for this specific null-check scenario.
        ExtraFieldUtils.parse(null, false);
    }
}