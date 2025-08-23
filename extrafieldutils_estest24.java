package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

/**
 * Unit tests for the {@link ExtraFieldUtils} class.
 */
public class ExtraFieldUtilsTest {

    /**
     * Verifies that {@code createExtraField} throws a NullPointerException
     * when passed a null header ID. The underlying implementation uses this
     * ID as a key in a map that does not permit nulls.
     */
    @Test(expected = NullPointerException.class)
    public void createExtraFieldWithNullHeaderIdShouldThrowNullPointerException() {
        // Call the method under test with a null argument to trigger the exception.
        ExtraFieldUtils.createExtraField(null);
    }
}