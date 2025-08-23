package org.mockito.internal.verification;

import org.junit.Test;
import org.mockito.internal.util.Timer;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link VerificationOverTimeImpl}.
 *
 * This test class focuses on the behavior of the canRecoverFromFailure method.
 */
public class VerificationOverTimeImplTest {

    @Test
    public void canRecoverFromFailureShouldReturnFalseForAtMostMode() {
        // --- Arrange ---
        // The AtMost verification mode is a "terminal" condition. Once the number of
        // invocations exceeds the specified maximum, it's impossible to recover from
        // that failure (i.e., you cannot "un-invoke" a method).
        VerificationMode atMostMode = new AtMost(0);

        // The following parameters are required to construct VerificationOverTimeImpl,
        // but their specific values do not affect the outcome of this test.
        long pollingPeriodMillis = 100L;
        boolean returnOnSuccess = true;
        Timer timer = new Timer(0L);

        VerificationOverTimeImpl verificationOverTime = new VerificationOverTimeImpl(
            pollingPeriodMillis,
            atMostMode,
            returnOnSuccess,
            timer
        );

        // --- Act ---
        // We check if the system correctly identifies that AtMost mode cannot recover from failure.
        boolean canRecover = verificationOverTime.canRecoverFromFailure(atMostMode);

        // --- Assert ---
        // The result must be false, as a failure of AtMost is final.
        assertFalse("An AtMost verification failure should not be recoverable", canRecover);
    }
}