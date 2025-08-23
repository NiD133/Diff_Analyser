package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

/**
 * Unit tests for the {@link ExtraFieldUtils} class.
 */
public class ExtraFieldUtilsTest {

    /**
     * Verifies that calling the register method with a null class argument
     * results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void registerShouldThrowNullPointerExceptionWhenClassIsNull() {
        // Attempt to register a null class, which should trigger the exception.
        ExtraFieldUtils.register(null);
    }
}