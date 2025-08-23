package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ExtraFieldUtils} class, focusing on the
 * {@link ExtraFieldUtils.UnparseableExtraField} inner class.
 */
public class ExtraFieldUtilsTest {

    /**
     * Verifies that the THROW constant of the UnparseableExtraField "enum"
     * returns the correct key value from its getKey() method.
     */
    @Test
    public void unparseableExtraFieldThrowShouldHaveCorrectKey() {
        // Arrange
        // The expected key for the THROW action is defined by the public THROW_KEY constant.
        final int expectedKey = ExtraFieldUtils.UnparseableExtraField.THROW_KEY;
        final ExtraFieldUtils.UnparseableExtraField throwBehavior = ExtraFieldUtils.UnparseableExtraField.THROW;

        // Act
        final int actualKey = throwBehavior.getKey();

        // Assert
        // The key from the THROW instance should match the public constant.
        assertEquals("The key for THROW should match its constant.", expectedKey, actualKey);
    }
}