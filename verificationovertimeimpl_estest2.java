package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.internal.util.Timer;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link VerificationOverTimeImpl}.
 */
public class VerificationOverTimeImplTest {

    /**
     * Verifies that the constructor correctly initializes the object's properties
     * when a pre-configured Timer instance is provided.
     */
    @Test
    public void constructorWithTimerShouldSetPropertiesCorrectly() {
        // Arrange
        long expectedPollingPeriod = 500L;
        long timerDuration = 100L;
        boolean shouldReturnOnSuccess = true;

        VerificationMode delegateMode = new NoMoreInteractions();
        Timer timer = new Timer(timerDuration);

        // Act
        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(
            expectedPollingPeriod,
            delegateMode,
            shouldReturnOnSuccess,
            timer
        );

        // Assert
        // Verify that all properties were set as expected by the constructor.
        assertTrue(
            "Should be configured to return on success",
            verificationOverTime.isReturnOnSuccess()
        );
        assertEquals(
            "Polling period should match the constructor argument",
            expectedPollingPeriod,
            verificationOverTime.getPollingPeriodMillis()
        );
        assertSame(
            "Delegate verification mode should be the one passed to the constructor",
            delegateMode,
            verificationOverTime.getDelegate()
        );
        assertSame(
            "Timer instance should be the one passed to the constructor",
            timer,
            verificationOverTime.getTimer()
        );
    }
}