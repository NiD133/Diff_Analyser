package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

/**
 * Unit tests for the {@link ExtraFieldUtils} class.
 */
public class ExtraFieldUtilsTest {

    /**
     * Tests that calling {@code createExtraFieldNoDefault} with a null header ID
     * correctly throws a {@code NullPointerException}.
     */
    @Test(expected = NullPointerException.class)
    public void createExtraFieldNoDefaultWithNullHeaderIdThrowsNullPointerException() {
        // This call should fail with a NullPointerException because the argument is null.
        ExtraFieldUtils.createExtraFieldNoDefault(null);
    }
}