package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link TaiInstant#equals(Object)} method.
 */
public class TaiInstant_ESTestTest44 extends TaiInstant_ESTest_scaffolding {

    /**
     * Tests that TaiInstant.equals() returns false when compared with an object of a different type.
     */
    @Test
    public void equals_returnsFalse_whenComparedWithDifferentType() {
        // Arrange: Create a TaiInstant and an object of an incompatible type.
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(19L, 19L);
        Object nonTaiInstantObject = new Object();

        // Act & Assert: Verify that the equals method returns false.
        assertFalse("TaiInstant should not be equal to an object of a different type.", taiInstant.equals(nonTaiInstantObject));
    }
}