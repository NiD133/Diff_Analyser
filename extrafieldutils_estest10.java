package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

/**
 * Tests for {@link ExtraFieldUtils}.
 */
public class ExtraFieldUtilsTest {

    /**
     * Verifies that the register() method throws a ClassCastException when
     * provided with a class that does not implement the ZipExtraField interface.
     * The Javadoc for register() explicitly requires the class to implement
     * ZipExtraField.
     */
    @Test(expected = ClassCastException.class)
    public void registerShouldThrowClassCastExceptionForNonZipExtraFieldClass() {
        // The register method expects a class that implements ZipExtraField.
        // java.lang.Object is used here as a simple example of a class that does not.
        ExtraFieldUtils.register(Object.class);
    }
}