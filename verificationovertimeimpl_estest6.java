package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Tests for {@link VerificationOverTimeImpl}.
 */
public class VerificationOverTimeImplTest {

    /**
     * Verifies that copyWithVerificationMode() creates a new, distinct instance
     * rather than modifying or returning the original object.
     */
    @Test
    public void copyWithVerificationModeShouldReturnNewInstance() {
        // Arrange: Create an initial VerificationOverTimeImpl instance.
        // The specific constructor arguments are not critical for this test's purpose.
        VerificationMode delegateMode = null;
        VerificationOverTimeImpl originalMode = new VerificationOverTimeImpl(100L, 50L, delegateMode, true);

        // Act: Create a copy of the original instance.
        VerificationOverTimeImpl copiedMode = originalMode.copyWithVerificationMode(delegateMode);

        // Assert: The copied object should be a new instance and therefore not the same as the original.
        // We assert that the two references point to different objects in memory.
        assertNotSame(originalMode, copiedMode);

        // As a secondary check, since VerificationOverTimeImpl does not override equals(),
        // this assertion is equivalent to assertNotSame().
        assertNotEquals(originalMode, copiedMode);
    }
}