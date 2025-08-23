package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.internal.util.Timer;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link VerificationOverTimeImpl}.
 */
public class VerificationOverTimeImplTest {

    @Test
    public void shouldCorrectlyInitializeStateFromConstructor() {
        // Arrange
        long pollingPeriodMillis = 10L;
        long durationMillis = 100L;
        boolean returnOnSuccess = false;
        VerificationMode delegateMode = null; // Not relevant for this test

        // Act
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(
            pollingPeriodMillis,
            durationMillis,
            delegateMode,
            returnOnSuccess
        );

        // Assert
        // Verify that the internal Timer is created
        Timer timer = verificationOverTime.getTimer();
        assertNotNull("The internal timer should be initialized upon construction.", timer);

        // Verify that the returnOnSuccess flag is set correctly
        assertFalse(
            "The returnOnSuccess flag should reflect the value passed to the constructor.",
            verificationOverTime.isReturnOnSuccess()
        );
    }
}