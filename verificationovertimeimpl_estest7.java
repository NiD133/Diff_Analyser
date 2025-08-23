package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link VerificationOverTimeImpl}.
 */
public class VerificationOverTimeImplTest {

    /**
     * This test verifies that the copyWithVerificationMode() method creates a new
     * instance of VerificationOverTimeImpl, preserving the original's configuration
     * such as polling period and duration.
     */
    @Test
    public void copyWithVerificationMode_shouldCreateNewInstanceWithCopiedConfiguration() {
        // Arrange
        final long pollingPeriod = 50L;
        final long duration = 100L;
        final boolean returnOnSuccess = false;
        final VerificationMode originalDelegate = new NoMoreInteractions();

        VerificationOverTimeImpl originalVerification = new VerificationOverTimeImpl(
            pollingPeriod,
            duration,
            originalDelegate,
            returnOnSuccess
        );

        // A new delegate mode to be used in the copied instance.
        VerificationMode newDelegate = mock(VerificationMode.class);

        // Act
        VerificationOverTimeImpl copiedVerification = originalVerification.copyWithVerificationMode(newDelegate);

        // Assert
        // 1. Ensure it's a new object instance.
        assertNotSame("A new instance should be created", originalVerification, copiedVerification);

        // 2. Verify that the configuration was copied from the original.
        assertEquals("Polling period should be copied", pollingPeriod, copiedVerification.getPollingPeriodMillis());
        assertEquals("Duration should be copied", duration, copiedVerification.getTimer().getDuration());
        assertEquals("returnOnSuccess flag should be copied", returnOnSuccess, copiedVerification.isReturnOnSuccess());
        
        // 3. Confirm the new instance uses the new delegate mode.
        assertEquals("The new delegate mode should be used", newDelegate, copiedVerification.getDelegate());
    }
}